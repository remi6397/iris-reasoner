package org.deri.iris.dbstorage;

import java.io.FileInputStream;
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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.factory.IBasicFactory;
import org.deri.iris.basics.BasicFactory;

import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.TERM;
import static org.deri.iris.factory.Factory.CONCRETE;

public class DbStorageManager {

	private static IBasicFactory bFactory=BasicFactory.getInstance();
	
	private static DatatypeFactory xmlFactory;
	
	private static String REFERENCETABLENAME = "referenceTable";

	private Connection con = null;

	private String conf = null;

	/*private static Logger logger = Logger
			.getLogger(org.deri.iris.dbstorage.DbStorageManager.class.getName());*/

	public DbStorageManager(String confilePath)
			throws DbStorageManagerException {
		conf = confilePath;
		try {
			con = createConnection(conf);
			xmlFactory=DatatypeFactory.newInstance();
			if (!existsTable(REFERENCETABLENAME))
				createPredicateReferenceTable();
		} catch (Exception e) {
			throw new DbStorageManagerException(e);
		}
	}

	public void destroy(Boolean cleanDatabaseFlag)
			throws DbStorageManagerException {
		try {
			if (cleanDatabaseFlag)
				clear();

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				/* silent exception */
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
			tableName = tableName.replaceAll("-", "");
		return tableName;
	}

	/*
	 * do we need to store references between predicates and tables names?! for
	 * now we don't since table names are computed using the predicate hash
	 */

	private void createPredicateReferenceTable() throws SQLException {
		Statement stmt = null;
		String referenceTable = "CREATE TABLE " + REFERENCETABLENAME + " (\n"
				+ " predicateSymbol VARCHAR(255) NOT NULL,\n"
				+ " predicateTable VARCHAR(255),\n"
				+ " predicateArity INT  NOT NULL,\n"
				+ " predicateHash INT  NOT NULL,\n"
				+ " PRIMARY KEY (predicateSymbol,predicateArity)\n" + ")";
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(referenceTable);
		} finally {
			stmt.close();
		}
	}
	
	public String getTable(String predicateSymbol, int predicateArity) throws DbStorageManagerException{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT predicateTable FROM " + REFERENCETABLENAME
				+ " WHERE predicateSymbol='" + predicateSymbol+"' AND predicateArity="+predicateArity+";";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()){
				return rs.getString("predicateTable");
			}
			return null;
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		} finally {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				// silent exception
			}
		}
	}

	private boolean addPredicateToReferenceTable(IPredicate p)
			throws SQLException {
		PreparedStatement stmt = null;
		String sqlStatement = "INSERT INTO " + REFERENCETABLENAME
				+ " VALUES (?, ?, ?, ?);";
		stmt = con.prepareStatement(sqlStatement);
		stmt.setString(1, p.getPredicateSymbol());
		if (p.getArity() > 0)
			stmt.setString(2, this.getTableName(p));
		else
			stmt.setString(2, "");
		stmt.setInt(3, p.getArity());
		stmt.setInt(4, plainPredicateHash(p));
		try {
			stmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				/* silent exception */
			}
		}
	}

	private boolean removePredicateFromReferenceTable(IPredicate p)
			throws SQLException {
		PreparedStatement stmt = null;
		String sqlStatement = "DELETE FROM " + REFERENCETABLENAME
				+ " WHERE predicateTable=?;";
		stmt = con.prepareStatement(sqlStatement);
		stmt.setString(1, this.getTableName(p));
		try {
			return stmt.execute();
		} catch (SQLException e) {
			return false;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				/* silent exception */
			}
		}
	}

	private boolean existsTable(String tableName) throws SQLException {
		Statement stmt = con.createStatement();
		try {
			stmt.executeQuery("SELECT * FROM " + tableName);
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			stmt.close();
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
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		} finally {
			try {
				con.setAutoCommit(true);
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				/* silent exception */
			}
		}
	}

	public IMixedDatatypeRelation computeIMixedDatatypeRelation(IPredicate p,String SqlQuery) throws DbStorageManagerException {
		IMixedDatatypeRelation rel=RELATION.getMixedRelation(p.getArity());
		Statement stmt=null;
		ResultSet rs=null;
		if(p.getArity()==0) return rel;
		try {
			stmt=con.createStatement();
			rs=stmt.executeQuery(SqlQuery);
			while(rs.next()){
				List<ITerm> terms=new ArrayList<ITerm>();
				for(int i=0; i<p.getArity(); i++){
					String termValue=rs.getString(2*i+1);
					String termType=rs.getString(2*i+2);
					ITerm term=null;
					if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.Iri")){
						term=CONCRETE.createIri(termValue);
					} else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.IntegerTerm")){
						term=CONCRETE.createInteger(Integer.parseInt(termValue));
					}
					else if(termType.equalsIgnoreCase("org.deri.iris.terms.StringTerm")){
						term=TERM.createString(termValue);
					} else throw new DbStorageManagerException("unsupported term");
					if(term!=null) terms.add(term);
					else throw new DbStorageManagerException("term cannot be null");
					// TODO add missing term types
				}
				rel.add(bFactory.createTuple(terms));
			}
			return rel;
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		} finally{
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				// silent exception
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
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				// silent exception
			}
		}
	}
	
	public boolean emptyPredicate(IPredicate p) throws DbStorageManagerException{
		if(p.getArity()==0) return false;
		if(!isPredicateRegistered(p)) return true;
		Statement stmt = null;
		String query="SELECT * FROM "+getTableName(p);
		try {
			stmt=con.createStatement();
			return stmt.executeQuery(query).next();
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// silent exception
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
				/*logger.log(Level.WARNING, "The table " + tableName
						+ " already exists");
				*/return false;
			} else {

				con.setAutoCommit(false);
				String tableSQL = "CREATE TABLE " + tableName + " (\n";
				for (int i = 0; i < p.getArity(); i++) {
					tableSQL += " term" + (i + 1) + " VARCHAR(255) NOT NULL,\n";
					tableSQL += " termType" + (i + 1)
							+ " VARCHAR(255) NOT NULL,\n";
				}
				tableSQL += " PRIMARY KEY (";
				for (int i = 0; i < p.getArity(); i++) {
					tableSQL += "term" + (i + 1)+", termType" + (i + 1);
					if (i < p.getArity() - 1)
						tableSQL += ", ";
				}
				tableSQL += ")\n);";
				stmt.executeUpdate(tableSQL);
				for (int i = 0; i < p.getArity(); i++) {
					stmt.executeUpdate("CREATE INDEX term" + (i + 1) + "_"
							+ tableName + "_idx ON " + tableName + " (term"
							+ (i + 1) + ")");
				}
//				logger.log(Level.INFO, "SQL Statement the table " + tableName
//						+ ":\n" + tableSQL);

				con.commit();
//				logger.log(Level.INFO, "The table " + tableName
//						+ " for predicate " + p.getPredicateSymbol()
//						+ " with arity " + p.getArity()
//						+ " was created and registered");
				return true;
			}
		} catch (SQLException sqle) {
			throw new DbStorageManagerException(sqle);
		} finally {
			try {
				con.setAutoCommit(true);
				stmt.close();
			} catch (SQLException e) {
				/* silent exception */
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
			stmt.executeUpdate("DROP TABLE " + tableName);
			removePredicateFromReferenceTable(p);
//			logger.log(Level.INFO, "The table " + tableName + " was removed");
			return true;
		} catch (SQLException e) {
//			logger.log(Level.SEVERE, "The table " + tableName
//					+ " doesn't exist");
			return false;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				/* silent exception */
			}
		}
	}

	private Connection createConnection(String confile)
			throws ClassNotFoundException, FileNotFoundException, IOException,
			SQLException {
		Properties conf = new Properties();
		String dburl = null;
		String dbuser = null;
		String dbpassword = null;
		String dbclass = null;
		conf.load(new FileInputStream(confile));
		if (conf.containsKey("dburl"))
			dburl = conf.getProperty("dburl");
		if (conf.containsKey("dbuser"))
			dbuser = conf.getProperty("dbuser");
		if (conf.containsKey("dbpassword"))
			dbpassword = conf.getProperty("dbpassword");
		if (conf.containsKey("dbclass"))
			dbclass = conf.getProperty("dbclass");
		Class.forName(dbclass);

		return DriverManager.getConnection(dburl, dbuser, dbpassword);
	}

	public Connection getConnection() {
		return con;
	}

	/* TODO agree with darko on what to store for the datatype */

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
		String sqlStatement = "INSERT INTO " + tableName + " VALUES (";
		for (int i = 0; i < t.getArity(); i++) {
			sqlStatement += "?, ?";
			if (i < t.getArity() - 1)
				sqlStatement += ", ";
		}
		sqlStatement += ");";
		try {
			stmt = con.prepareStatement(sqlStatement);

			for (int i = 0; i < t.getArity(); i++) {
				if(t.getTerm(i) instanceof org.deri.iris.terms.concrete.DateTerm){
					GregorianCalendar cal=(GregorianCalendar)t.getTerm(i).getValue();
					XMLGregorianCalendar xmlCal=xmlFactory.newXMLGregorianCalendarDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.ZONE_OFFSET)/(60*60*1000));
					String date=xmlCal.toXMLFormat();
					stmt.setString(2 * i + 1, date);
				}
				else {
				stmt.setString(2 * i + 1, t.getTerm(i).getValue()
						.toString());
				}
				stmt.setString(2 * i + 2,t.getTerm(i).getClass().getCanonicalName()); /* TODO improve this */
			}
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		}
		try {
			stmt.execute();
			return true;
		} catch (SQLException e) {
			return true;
			// TODO check why there are dublicate unique constraint violation
			//throw new DbStorageManagerException(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				/* silent exception */
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
		String sqlStatement = "DELETE FROM " + tableName + " WHERE ";
		for (int i = 0; i < a.getTuple().getArity(); i++) {
			sqlStatement +="term"+(i+1)+"= ? AND termType"+(i+1)+"= ?";
			if (i < a.getTuple().getArity() - 1)
				sqlStatement += "AND ";
		}
		sqlStatement += ";";
		try {
			stmt = con.prepareStatement(sqlStatement);

			for (int i = 0; i < a.getTuple().getArity(); i++) {
				stmt.setString(2*i + 1, a.getTuple().getTerm(i).getValue()						
						.toString());
				stmt.setString(2*i + 2, a.getTuple().getTerm(i).getValue().getClass().getCanonicalName()); //TODO improve this
			}
		} catch (SQLException e) {
			throw new DbStorageManagerException(e);
		}
		try {
			boolean test = stmt.execute();
			return true;
		} catch (SQLException e) {
			if (e.getMessage().indexOf("constraint") > 0
					&& e.getMessage().indexOf(" violated") > 0)
				return false;
			else
				throw new DbStorageManagerException(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				/* silent exception */
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
		String sqlStatement = "SELECT * FROM " + tableName + " WHERE ";
		for (int i = 0; i < t.getArity(); i++) {
			sqlStatement +="term"+(i+1)+"= ? AND termType"+(i+1)+"= ?";
			if (i < t.getArity() - 1)
				sqlStatement += " AND ";
		}
		sqlStatement += ";";
		try {
			stmt = con.prepareStatement(sqlStatement);

			for (int i = 0; i < t.getArity(); i++) {
				stmt.setString(2*i + 1, t.getTerm(i).getValue()
						.toString());
				stmt.setString(2*i + 2, t.getTerm(i).getValue().getClass().getCanonicalName());//TODO improve this
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
				stmt.close();
			} catch (SQLException e) {
				/* silent exception */
			}
		}

	}
	
	public IMixedDatatypeRelation getFacts(IPredicate p) throws DbStorageManagerException {
		IMixedDatatypeRelation rel=RELATION.getMixedRelation(p.getArity());
		String sqlStatement = "SELECT * FROM " + this.getTableName(p);
		Statement stmt=null;
		ResultSet rs=null;
		if(p.getArity()==0) return rel;
		try {
			stmt=con.createStatement();
			rs=stmt.executeQuery(sqlStatement);
			while(rs.next()){
				List<ITerm> terms=new ArrayList<ITerm>();
				for(int i=0; i<p.getArity(); i++){
					String termValue=rs.getString("term"+(i+1));
					String termType=rs.getString("termType"+(i+1));
					ITerm term=null;
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
					/*else if(termType.equalsIgnoreCase("org.deri.iris.terms.DateTime")){
                        DateTime is written using the toString method of a Calendar object!!!
						term=CONCRETE.createDateTime(termValue);
					} */
					else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.DecimalTerm")){
						term=CONCRETE.createDecimal(Double.parseDouble(termValue));
					} 
					else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.DoubleTerm")){
						term=CONCRETE.createDouble(Double.parseDouble(termValue));
					} 
					/*else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.Duration")){
                        Duration is written using the toString method of a Calendar object!!!
						term=CONCRETE.create(termValue);
					} */
					else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.FloatTerm")){
						term=CONCRETE.createFloat(Float.parseFloat(termValue));
					} 
					else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.GDay")){
						term=CONCRETE.createGDay(Integer.parseInt(termValue));
					} 
					/*else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.GMonthDay")){
                        GMonthDay is written using the toString method of an Integer[], the result is unreadable
						term=CONCRETE.create(termValue);
					} */
					else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.GMonth")){
						term=CONCRETE.createGMonth(Integer.parseInt(termValue));
					} 
					else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.GYear")){
						term=CONCRETE.createGYear(Integer.parseInt(termValue));
					} 
					/*else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.GYearMonth")){
                        GYearMonth is written using the toString method of an Integer[], the result is unreadable
						term=CONCRETE.create(termValue);
					} */
					else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.HexBinary")){
						term=CONCRETE.createHexBinary(termValue);
					} 
					else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.IntegerTerm")){
						term=CONCRETE.createInteger(Integer.parseInt(termValue));
					} 
					else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.SqName")){
						term=CONCRETE.createSqName(termValue);
					} 
					/*else if(termType.equalsIgnoreCase("org.deri.iris.terms.concrete.Time")){
                        // Time is written using the toString method of a Calendar object!!!
						// term=CONCRETE.create(termValue);
					} */

                    else throw new DbStorageManagerException("unsupported term");
					
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
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				// silent exception
			}
		}

	}
}
