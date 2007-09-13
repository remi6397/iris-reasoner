package org.deri.iris.dbstorage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.factory.IBasicFactory;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.terms.concrete.GMonthDay;
import org.deri.iris.terms.concrete.GYearMonth;

import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;

import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.TERM;
import static org.deri.iris.factory.Factory.CONCRETE;

public class DbStorageManager {

	private static IBasicFactory bFactory=BasicFactory.getInstance();
	
	private static DatatypeFactory xmlFactory;
	
	private static String REFERENCETABLENAME = "referenceTable";

	private Connection con = null;

	public DbStorageManager(Map conf)
			throws DbStorageManagerException {
		try {
			con = createConnection(conf);
			xmlFactory=DatatypeFactory.newInstance();
			if (!existsTable(REFERENCETABLENAME))
				createPredicateReferenceTable();
		} catch (Exception e) {
			throw new DbStorageManagerException(e);
		}
	}

	public void destroy(boolean cleanDatabaseFlag)
			throws DbStorageManagerException {
		try {
			if (cleanDatabaseFlag)
				clear();

		} finally {
			try {
				if(con!=null) con.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}
	}

	/**
	 * Generates a hash of a predicate only based on it's symbol and arity. This
	 * method was written to enable access to the same relations for ordinary
	 * predicates and adorned ones.
	 * 
	 * @param p
	 *            the predicate for which to create the hash
	 * @return the hash
	 * @throws NullPointerException
	 *             if the predicate was <code>null</code>
	 */
	private static int plainPredicateHash(final IPredicate p) {
		if (p == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		int res = 17;
		res = res * 37 + p.getPredicateSymbol().hashCode();
		res = res * 37 + p.getArity();
		return res;
	}

	private String getTableName(IPredicate p) {
		String tableName = "table" + plainPredicateHash(p);
		if (tableName.indexOf("-") > 0)
			tableName = tableName.replaceAll("-", "_");
		return tableName;
	}

	private void createPredicateReferenceTable() throws SQLException {
		Statement stmt = null;
		StringBuilder  referenceTable = new StringBuilder("CREATE TABLE " + REFERENCETABLENAME + " (\n"
				+ " predicateSymbol VARCHAR(255) NOT NULL,\n"
				+ " predicateTable VARCHAR(255),\n"
				+ " predicateArity INT  NOT NULL,\n"
				+ " predicateHash INT  NOT NULL,\n"
				+ " PRIMARY KEY (predicateSymbol,predicateArity)\n" + ")");
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(referenceTable.toString());
		} finally {
			if(stmt!=null) stmt.close();
		}
	}
	
	/**
	 * Return the table name for the given predicateSymbol and arity
	 * 
	 * @param predicateSymbol
	 *             the predicate symbol for which we are searching the table name
	 * @param arity
	 *             the predicate airty for which we are searching the table name
	 * @return the table name or null if a table for the given predicateSymbol and arity
	 *         does not exist
	 * @throws DbStorageManagerException      
	 */
	public String getTable(String predicateSymbol, int predicateArity) throws DbStorageManagerException{
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder query =  new StringBuilder("SELECT predicateTable FROM " + REFERENCETABLENAME
				+ " WHERE predicateSymbol='" + predicateSymbol+"' AND predicateArity="+predicateArity);
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			if (rs.next()){
				return rs.getString("predicateTable");
			}
			return null;
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		} finally {
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}
	}

	private boolean addPredicateToReferenceTable(IPredicate p)
			throws SQLException {
		PreparedStatement stmt = null;
		String sqlStatement = "INSERT INTO " + REFERENCETABLENAME
				+ " VALUES (?, ?, ?, ?)";
		stmt = con.prepareStatement(sqlStatement);
		stmt.setString(1, p.getPredicateSymbol());
		if (p.getArity() > 0)
			stmt.setString(2, this.getTableName(p));
		else
			stmt.setString(2, null);
		stmt.setInt(3, p.getArity());
		stmt.setInt(4, plainPredicateHash(p));
		try {
			stmt.execute();
			return true;
		} catch (SQLException e) {
			return false;
		} finally {			
				if(stmt!=null) stmt.close();
		}
	}

	private boolean removePredicateFromReferenceTable(IPredicate p)
			throws SQLException {
		PreparedStatement stmt = null;
		String sqlStatement = "DELETE FROM " + REFERENCETABLENAME
				+ " WHERE predicateTable=?";
		stmt = con.prepareStatement(sqlStatement);
		stmt.setString(1, this.getTableName(p));
		try {
			return stmt.execute();
		} catch (SQLException e) {
			return false;
		} finally {			
				if(stmt!=null) stmt.close();
		}
	}

	private boolean existsTable(String tableName) throws SQLException {
		Statement stmt = con.createStatement();
		try {
			stmt.executeQuery("SELECT count(*) FROM " + tableName);
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			if(stmt!=null) stmt.close();
		}
	}

	public void clear() throws DbStorageManagerException {
		Statement stmt = null;
		Statement deleteStmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			deleteStmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + REFERENCETABLENAME);
			con.setAutoCommit(false);
			while (rs.next()) {
				String tableName = rs.getString("predicateTable");
				int hashValue = rs.getInt("predicateHash");
				int arityValue = rs.getInt("predicateArity");
				if (arityValue > 0)
					deleteStmt.executeUpdate("DROP TABLE " + tableName);
				deleteStmt.executeUpdate("DELETE FROM " + REFERENCETABLENAME
						+ " WHERE predicateHash=" + hashValue);
			}
			con.commit();
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		} finally {
			try {
				con.setAutoCommit(true);
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}
	}

	public IMixedDatatypeRelation computeIMixedDatatypeRelation(IPredicate p,String SqlQuery) throws DbStorageManagerException {
		IMixedDatatypeRelation rel=RELATION.getMixedRelation(p.getArity());
		Statement stmt=null;
		ResultSet rs=null;
		if(p.getArity()==0)
			return rel;
		try {
			stmt=con.createStatement();
			rs=stmt.executeQuery(SqlQuery);
			while(rs.next()){
				List<ITerm> terms=new ArrayList<ITerm>();
				for(int i=0; i<p.getArity(); i++){
					int valueColumn=2 * i + 1;
					int typeColumn=2 * i + 2;
					String termValue=rs.getString(valueColumn);
					String termType=rs.getString(typeColumn);
					ITerm term=null;
					term=serializedStringToTerm(termType, termValue);
                    if(term!=null) terms.add(term);
					else throw new DbStorageManagerException("term cannot be null");					
				}
				rel.add(bFactory.createTuple(terms));
			}
			return rel;
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		} finally{
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}

	}

    public boolean isPredicateRegistered(IPredicate p)
			throws DbStorageManagerException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT * FROM " + REFERENCETABLENAME
				+ " WHERE predicateHash=" + plainPredicateHash(p);
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			return rs.next();
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		} finally {
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}
	}
	
	public boolean isEmptyPredicate(IPredicate p) throws DbStorageManagerException{
		if(p.getArity()==0) return false;
		if(!isPredicateRegistered(p)) return true;
		Statement stmt = null;
		String query="SELECT count(*) FROM "+getTableName(p);
		try {
			stmt=con.createStatement();
			return !stmt.executeQuery(query).next();
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		} finally {
			try {			
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}
	}

	/**
	 * Generates a datatabase table for a predicate based on its hash.
	 * 
	 * @param p
	 *            the predicate for which to create the table
	 * @return <code>true</code> if the table is succesfully created
	 * @throws DbStorageManagerException
	 *             if the were errors during the table creation
	 */

	public boolean registerPredicate(IPredicate p)
			throws DbStorageManagerException {
		Statement stmt = null;
		
		try {
			if (!isPredicateRegistered(p))
				addPredicateToReferenceTable(p);
			stmt = con.createStatement();
			if (p.getArity() == 0)
				return true;
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		}

		String tableName = getTableName(p);
		try {
			if (existsTable(tableName)) {
				return false;
			} else {
				con.setAutoCommit(false);
				StringBuilder tableSQL=new StringBuilder();
				tableSQL.append("CREATE TABLE " + tableName + " (\n");
				for (int i = 0; i < p.getArity(); i++) {
					int termPosition=i+1;
					tableSQL.append(" term" + termPosition + " VARCHAR(255) NOT NULL,\n");
					tableSQL.append(" termType" + termPosition
							+ " VARCHAR(255) NOT NULL,\n");
				}
				tableSQL.append(" PRIMARY KEY (");
				for (int i = 0; i < p.getArity(); i++) {
					int termPosition=i+1;
					tableSQL.append("term" + termPosition+", termType" + termPosition);
					if (i < p.getArity() - 1)
						tableSQL.append(", ");
				}
				tableSQL.append(")\n)");
				stmt.executeUpdate(tableSQL.toString());
				for (int i = 0; i < p.getArity(); i++) {
					int termPosition=i+1;
					stmt.executeUpdate("CREATE INDEX term" + termPosition + "_"
							+ tableName + "_idx ON " + tableName + " (term"
							+ termPosition + ")");
				}
				con.commit();
				return true;
			}
		} catch (SQLException sqle) {
			throw new DbStorageManagerException(sqle);
		} finally {
			try {
				con.setAutoCommit(true);
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}
	}

	/**
	 * Delete the datatabase table for a predicate.
	 * 
	 * @param p
	 *            the predicate for which to delete the table
	 * @return <code>true</code> if the table is succesfully deleted
	 * @throws DbStorageManagerException
	 *             if the were errors during the table deletion
	 */

	public boolean unRegisterPredicate(IPredicate p)
			throws DbStorageManagerException {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		}
		String tableName = getTableName(p);
		try {
			con.setAutoCommit(false);
			stmt.executeUpdate("DROP TABLE " + tableName);
			removePredicateFromReferenceTable(p);
			con.commit();
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			try {
				con.setAutoCommit(true);
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}
	}

	private Connection createConnection(Map conf)
			throws ClassNotFoundException, FileNotFoundException, IOException,
			SQLException {
		String dburl = null;
		String dbuser = null;
		String dbpassword = null;
		String dbclass = null;
		
		if (conf.containsKey("DB_URL"))
			dburl = (String)conf.get("DB_URL");
		if (conf.containsKey("DB_USER"))
			dbuser = (String)conf.get("DB_USER");
		if (conf.containsKey("DB_PASSWORD"))
			dbpassword = (String)conf.get("DB_PASSWORD");
		if (conf.containsKey("DB_CLASS"))
			dbclass = (String)conf.get("DB_CLASS");
		Class.forName(dbclass);

		return DriverManager.getConnection(dburl, dbuser, dbpassword);
	}

	public Connection getConnection() {
		return con;
	}

	public boolean addFact(IAtom a) throws DbStorageManagerException {

		return this.addTuple(a.getTuple(), a.getPredicate());

	}
	
	public boolean addTuple(ITuple t, IPredicate p) throws DbStorageManagerException {
		String tableName = this.getTableName(p);
		if (t.getArity() == 0)
			return true;
		if (t.getTerms().isEmpty())
			return true;
		if(hasFact(t, p)) return true;
		PreparedStatement stmt = null;
		StringBuilder sqlStatement =  new StringBuilder("INSERT INTO " + tableName + " VALUES (");
		for (int i = 0; i < t.getArity(); i++) {
			sqlStatement.append("?, ?");
			if (i < t.getArity() - 1)
				sqlStatement.append(", ");
		}
		sqlStatement.append(")");
		try {
			stmt = con.prepareStatement(sqlStatement.toString());

			for (int i = 0; i < t.getArity(); i++) {
				int valueColumn=2 * i + 1;
				int typeColumn=2 * i + 2;
				stmt.setString(valueColumn,serializeTermToString(t.getTerm(i)));
				stmt.setString(typeColumn,t.getTerm(i).getClass().getCanonicalName());
			}
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		}
		try {
			stmt.execute();
			return true;
		} catch (SQLException e) {
			return true;
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}

	}
	
	public boolean removeFact(IAtom a) throws DbStorageManagerException {

		String tableName = this.getTableName(a.getPredicate());
		if (a.getTuple().getArity() == 0)
			return true;
		if (a.getTuple().getTerms().isEmpty())
			return true;
		PreparedStatement stmt = null;
		StringBuilder sqlStatement = new StringBuilder("DELETE FROM " + tableName + " WHERE ");
		for (int i = 0; i < a.getTuple().getArity(); i++) {
			int termPosition=i+1;
			sqlStatement.append("term"+termPosition+"= ? AND termType"+termPosition+"= ?");
			if (i < a.getTuple().getArity() - 1)
				sqlStatement.append("AND ");
		}
		try {
			stmt = con.prepareStatement(sqlStatement.toString());

			for (int i = 0; i < a.getTuple().getArity(); i++) {
				int valueColumn=2 * i + 1;
				int typeColumn=2 * i + 2;
				stmt.setString(valueColumn,serializeTermToString(a.getTuple().getTerm(i)));
				stmt.setString(typeColumn, a.getTuple().getTerm(i).getValue().getClass().getCanonicalName()); //TODO improve this
			}
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		}
		try {
			stmt.execute();
			return true;
		} catch (SQLException e) {
			if (e.getMessage().indexOf("constraint") > 0
					&& e.getMessage().indexOf(" violated") > 0)
				return false;
			else
				throw new DbStorageManagerException(e);
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}

	}
	
	public boolean hasFact(IAtom a) throws DbStorageManagerException {
		return this.hasFact(a.getTuple(), a.getPredicate());
	}
	
	public boolean hasFact(ITuple t, IPredicate p) throws DbStorageManagerException {

		String tableName = this.getTableName(p);
		if (p.getArity() == 0)
			return true;
		if (t.getTerms().isEmpty())
			return true;
		PreparedStatement stmt = null;
		StringBuilder sqlStatement = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
		for (int i = 0; i < t.getArity(); i++) {
			int termPosition=i+1;
			sqlStatement.append("term"+termPosition+"= ? AND termType"+termPosition+"= ?");
			if (i < t.getArity() - 1)
				sqlStatement.append(" AND ");
		}
		try {
			stmt = con.prepareStatement(sqlStatement.toString());

			for (int i = 0; i < t.getArity(); i++) {
				int valueColumn=2 * i + 1;
				int typeColumn=2 * i + 2;
				stmt.setString(valueColumn,serializeTermToString(t.getTerm(i)));
				stmt.setString(typeColumn, t.getTerm(i).getValue().getClass().getCanonicalName());
			}
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		}
		try {
			return stmt.executeQuery().next();
		} catch (SQLException e) {			
				throw new DbStorageManagerException(e);
		} finally {
			try {
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}

	}
	
	public IMixedDatatypeRelation getFacts(IPredicate p) throws DbStorageManagerException {
		IMixedDatatypeRelation rel=RELATION.getMixedRelation(p.getArity());
		String sqlStatement = "SELECT * FROM " + this.getTableName(p);
		Statement stmt=null;
		ResultSet rs=null;
		if(p.getArity()==0)
			return rel;
		try {
			stmt=con.createStatement();
			rs=stmt.executeQuery(sqlStatement);
			while(rs.next()){
				List<ITerm> terms=new ArrayList<ITerm>();
				for(int i=0; i<p.getArity(); i++){
					int termPosition=i+1;
					String termValue=rs.getString("term"+termPosition);
					String termType=rs.getString("termType"+termPosition);
					ITerm term=null;
					term=serializedStringToTerm(termType, termValue);
                    if(term!=null) terms.add(term);
					else throw new DbStorageManagerException("term cannot be null");
				}
				rel.add(bFactory.createTuple(terms));
			}
			return rel;
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		} finally{
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
			} catch (SQLException e) {
				throw new DbStorageManagerException(e);
			}
		}

	}
	
	private String serializeTermToString(ITerm t){
		if(t instanceof org.deri.iris.terms.concrete.DateTerm){
			GregorianCalendar cal=(GregorianCalendar)t.getValue();
			XMLGregorianCalendar xmlCal=xmlFactory.newXMLGregorianCalendarDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.ZONE_OFFSET)/(60*60*1000));
			return xmlCal.toXMLFormat();
		}
		else if(t instanceof org.deri.iris.terms.concrete.DateTime){
			GregorianCalendar cal=(GregorianCalendar)t.getValue();
			XMLGregorianCalendar xmlCal=xmlFactory.newXMLGregorianCalendar(cal.get(Calendar.YEAR), 
                    cal.get(Calendar.MONTH), 
                    cal.get(Calendar.DAY_OF_MONTH), 
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    cal.get(Calendar.SECOND),
                    cal.get(Calendar.MILLISECOND),
                    cal.get(Calendar.ZONE_OFFSET)/(60*60*1000));
			return xmlCal.toXMLFormat();
		}
		else if(t instanceof org.deri.iris.terms.concrete.Time){
			GregorianCalendar cal=(GregorianCalendar)t.getValue();
			XMLGregorianCalendar xmlCal=xmlFactory.newXMLGregorianCalendarTime(cal.get(Calendar.HOUR_OF_DAY), 
                    cal.get(Calendar.MINUTE), 
                    cal.get(Calendar.SECOND),
                    cal.get(Calendar.MILLISECOND),
                    cal.get(Calendar.ZONE_OFFSET)/(60*60*1000));
			return xmlCal.toXMLFormat();
		}
		else if(t instanceof org.deri.iris.terms.concrete.GMonthDay){
			return ((GMonthDay)t).getMonth()+";"+((GMonthDay)t).getDay();
        }
		else if(t instanceof org.deri.iris.terms.concrete.GYearMonth){
			return ((GYearMonth)t).getYear()+";"+((GYearMonth)t).getMonth();
        }
		else {
			return t.getValue().toString();
		}
	}

    private ITerm serializedStringToTerm(String termType, String termValue) throws DbStorageManagerException {
        ITerm term=null;
        if ((termValue==null)||(termType=="")) return (ITerm)null;
        if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.Iri")){
            term=CONCRETE.createIri(termValue);
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.IntegerTerm")){
            term=CONCRETE.createInteger(Integer.parseInt(termValue));
        }
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.StringTerm")){
            term=TERM.createString(termValue);
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.Base64Binary")){
            term=CONCRETE.createBase64Binary(termValue);
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.BooleanTerm")){
            term=CONCRETE.createBoolean(Boolean.parseBoolean(termValue));
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.DateTerm")){
            XMLGregorianCalendar cal=xmlFactory.newXMLGregorianCalendar(termValue);
            term=CONCRETE.createDate(cal.getYear(), cal.getMonth(), cal.getDay());
        }
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.DateTime")){
            XMLGregorianCalendar cal=xmlFactory.newXMLGregorianCalendar(termValue);
            term=CONCRETE.createDateTime(cal.getYear(), 
                    cal.getMonth(), 
                    cal.getDay(), 
                    cal.getHour(), 
                    cal.getMinute(), 
                    cal.getSecond(), 
		    cal.getMillisecond(), 
                    cal.getTimezone()/60, 
                    cal.getTimezone()%60);
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.DecimalTerm")){
            term=CONCRETE.createDecimal(Double.parseDouble(termValue));
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.DoubleTerm")){
            term=CONCRETE.createDouble(Double.parseDouble(termValue));
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.Duration")){
            term=CONCRETE.createDuration(Long.parseLong(termValue));
        }
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.FloatTerm")){
            term=CONCRETE.createFloat(Float.parseFloat(termValue));
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.GDay")){
            term=CONCRETE.createGDay(Integer.parseInt(termValue));
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.GMonthDay")){
            String[] month_day=termValue.split(";", 0);
            term=CONCRETE.createGMonthDay(Integer.parseInt(month_day[0]),Integer.parseInt(month_day[1]));
        }
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.GMonth")){
            term=CONCRETE.createGMonth(Integer.parseInt(termValue));
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.GYear")){
            term=CONCRETE.createGYear(Integer.parseInt(termValue));
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.GYearMonth")){
            String[] year_month=termValue.split(";", 0);
            term=CONCRETE.createGMonthDay(Integer.parseInt(year_month[0]),Integer.parseInt(year_month[1]));
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.HexBinary")){
            term=CONCRETE.createHexBinary(termValue);
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.IntegerTerm")){
            term=CONCRETE.createInteger(Integer.parseInt(termValue));
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.SqName")){
            term=CONCRETE.createSqName(termValue);
        } 
        else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.Time")){
            XMLGregorianCalendar cal=xmlFactory.newXMLGregorianCalendar(termValue);
            term=CONCRETE.createTime(cal.getHour(), 
                    cal.getMinute(), 
                    cal.getSecond(), 
		    cal.getMillisecond(), 
                    cal.getTimezone()/60, 
                    cal.getTimezone()%60);
        }
        else throw new DbStorageManagerException("unsupported term");
        return term;
    }
    

}
