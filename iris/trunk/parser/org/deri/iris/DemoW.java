/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2007 Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.compiler.Parser;
import org.deri.iris.evaluation.bottomup.compiledrules.naive.NaiveEvaluatorFactory;
import org.deri.iris.evaluation.bottomup.compiledrules.seminaive.SemiNaiveEvaluatorFactory;
import org.deri.iris.storage.IRelation;

/**
 * A GUI version of the Demo application.
 */
public class DemoW
{
	public static final int FONT_SIZE = 12;
	public static String NEW_LINE = System.getProperty( "line.separator" );
	public static final boolean SHOW_VARIABLE_BINDINGS = true;
	public static final boolean SHOW_QUERY_TIME = true;
	public static final boolean SHOW_ROW_COUNT = true;

	/**
	 * Application entry point.
	 * @param args
	 */
	public static void main( String[] args )
	{
		// Set up the native look and feel
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
		}

		// Create the main window and show it.
		MainFrame mainFrame = new MainFrame();
		mainFrame.setSize( 800, 600 );
		mainFrame.setVisible( true );
	}
	
	/**
	 * The main application window
	 */
	public static class MainFrame extends JFrame implements ActionListener
	{
		/** The serialisation ID. */
        private static final long serialVersionUID = 1L;

        /**
         * Constructor
         */
		public MainFrame()
		{
			super( "IRIS - new" );

			setup();
		}

		/**
		 * Create all the widgets, lay them out and create listeners.
		 */
		private void setup()
		{
			setLayout( new BorderLayout() );
			
			mProgram.setText( 
				"man('homer').\r\n" +
				"woman('marge').\r\n" +
				"hasSon('homer','bart').\r\n" +
				"isMale(?x) :- man(?x).\r\n" +
				"isFemale(?x) :- woman(?x).\r\n" +
				"isMale(?y) :- hasSon(?x,?y).\r\n" +
				"\r\n" +
				"?-isMale(?x)."
				);
			
			mProgram.setText( "p(h(?X)) :- q(?X)." );
			
			mProgram.setText(	"p(succ(?X), ?Y) :- p(?X,?Z), ?Z+1=?Y, ?Y < 7.\r\n" +
								"p(?X,?Y) :- ?X=1, ?Y=1.\r\n" +
								"?-p(?X,?Y).\r\n" );
			
			mProgram.setText(	
							"p('a'):-.\r\n" +
							"p(?X) :- r(?X), not s(?Y).\r\n" +
							"?- p(?X)." );


			mProgram.setText(
							"p(?X) :- a(?X), d(?X, ?Y)." + NEW_LINE +
							"d(?X, ?Y) :- not s(?X, ?Y), UNIVERSE(?X), UNIVERSE(?Y)." + NEW_LINE +
							"s(?U, ?U) :- UNIVERSE(?U)." + NEW_LINE +
							"a(1)." + NEW_LINE +
							NEW_LINE +
							"?- p(?X)." + NEW_LINE +
							NEW_LINE +
							"UNIVERSE(1)." + NEW_LINE +
							"UNIVERSE(2)." + NEW_LINE
							);
			
			mProgram.setText(
							"triple(0,0,0,1)." + NEW_LINE +
							"triple(?n, ?x, ?y, ?z) :- triple(?n1, ?x1, ?y1, ?z1), ?n1 + 1 = ?n, ?n/100=?x, ?n%100=?y2, ?y2/10=?y, ?n%10=?zz, ?zz+1=?z, ?n < 1000." + NEW_LINE +
							NEW_LINE +
							"// get all those triples where a % n = b % n (definition of congruent)" + NEW_LINE +
							"congruent( ?a, ?b, ?n ) :- triple(?k, ?a, ?b, ?n ), ?a % ?n = ?amodn, ?b % ?n = ?bmodn, ?amodn = ?bmodn." + NEW_LINE +
							NEW_LINE +
							"// Proove that if a1 congruent a2 mod n and b1 congruent b2 mod n, then a1b1 congruent a2b2 mod n" + NEW_LINE +
							"mul( ?a1b1, ?a2b2, ?n ) :- congruent( ?a1, ?a2, ?n ), congruent( ?b1, ?b2, ?n ), ?a1*?b1=?a1b1, ?a2*?b2=?a2b2." + NEW_LINE +
							NEW_LINE +
							"// The multiplied triples where the congruency rule does not hold." + NEW_LINE +
							"exceptions_to_rule( ?x,?y,?n ) :- mul( ?x,?y,?n), ?x % ?n = ?xmodn, ?y % ?n = ?ymodn, ?xmodn != ?ymodn." + NEW_LINE +
							NEW_LINE +
							"// Ths should be empty if the congruency rule is correct and the reasoner behaves correctly." + NEW_LINE +
							"?-exceptions_to_rule( ?a1b1,?a2b2,?n )." + NEW_LINE
											);
			mRun.addActionListener( this );

			mAbort.addActionListener( this );
			mAbort.setEnabled( false );

			JScrollPane programScroller = new JScrollPane( mProgram );
			JScrollPane outputScroller = new JScrollPane( mOutput );
			
			Font f = new Font( "courier", Font.PLAIN, FONT_SIZE );
			mProgram.setFont( f );
			mOutput.setFont( f );

			JSplitPane mainSplitter = new JSplitPane( JSplitPane.VERTICAL_SPLIT, false, programScroller, outputScroller );

			getContentPane().add( mainSplitter, BorderLayout.CENTER );
			
			mEvaluationStrategy.setSelectedIndex( 1 );
			
			JPanel panel = new JPanel();
			panel.add( mEvaluationStrategy );
			panel.add( mRun );
			panel.add( mAbort );

			getContentPane().add( panel, BorderLayout.SOUTH );

			// Can't seem to make this happen before showinG, even with:
//			 mainSplitter.putClientProperty( JSplitPane.RESIZE_WEIGHT_PROPERTY, "0.5" );
//			mainSplitter.setDividerLocation( 0.5 );
			
			addWindowListener(
							new WindowAdapter()
							{
								public void windowClosing( WindowEvent e )
								{
									System.exit( 0 );
								}
							}
						);
			
		}

		private final JTextArea mProgram = new JTextArea();
		private final JTextArea mOutput = new JTextArea();
		
		private final JButton mRun = new JButton( "Evaluate" );
		private final JButton mAbort = new JButton( "Abort" );
		
		private final JComboBox mEvaluationStrategy = new JComboBox( new String[] { "Naive", "Semi-naive" } );	//, "Magic Sets" } );
		
		Thread mExecutionThread;
		
		public void actionPerformed( ActionEvent e )
        {
	        if( e.getSource() == mRun )
	        {
	        	run();
	        }
	        else if( e.getSource() == mAbort )
	        {
	        	abort();
	        }
        }

		/**
		 * Called when evaluation has finished.
		 * @param output The evaluation output 
		 */
		synchronized void setOutput( String output )
		{
			mRun.setEnabled( true );
			mAbort.setEnabled( false );

			mOutput.setText( output );
		}
		
		/**
		 * Notifier class that 'hops' the output from the evaluation thread to the UI thread.
		 */
		class NotifyOutput implements Runnable
		{
			NotifyOutput( String output )
			{
				mOutput = output;
			}
			
			public void run()
            {
	            setOutput( mOutput );
            }
			
			final String mOutput;
		}
		
		/**
		 * Starts the evaluation.
		 */
		synchronized void run()
		{
			mOutput.setText( "" );

			mRun.setEnabled( false );
			mAbort.setEnabled( true );
			
			String program = mProgram.getText();
			int strategy = mEvaluationStrategy.getSelectedIndex();

			mExecutionThread = new Thread( new ExecutionTask( program, strategy ), "Evaluation task" );

			mExecutionThread.setPriority( Thread.MIN_PRIORITY );
			mExecutionThread.start();
		}
		
		/**
		 * Aborts the evaluation.
		 */
		synchronized void abort()
		{
			mRun.setEnabled( true );
			mAbort.setEnabled( false );

			// Not very nice, but hey, that's life.
			mExecutionThread.stop();
		}
		
		/**
		 * Runnable task for performing the evaluation.
		 */
		class ExecutionTask implements Runnable
		{
			ExecutionTask( String program, int evaluationStrategy )
			{
				this.program = program;
				this.evaluationStrategy = evaluationStrategy;
			}
			
//			@Override
	        public void run()
	        {
				try
				{
					Parser parser = new Parser();
					parser.parse( program );
					Map<IPredicate,IRelation> facts = parser.getFacts();
					List<IRule> rules = parser.getRules();
					List<IQuery> queries = parser.getQueries();
					
					if( queries.size() > 1 )
					{
						SwingUtilities.invokeLater( new NotifyOutput( "Only one query at a time" ) );
						return;
					}
					IQuery query = queries.size() == 1 ? queries.iterator().next() : null;
					
					StringBuilder output = new StringBuilder();
					
					Configuration config = KnowledgeBaseFactory.getDefaultConfiguration();
					
					switch( evaluationStrategy )
					{
					case 0:
						output.append( "Naive evaluation" ).append( NEW_LINE );
						config.evaluationTechnique = new NaiveEvaluatorFactory();
						break;
					
					default:
					case 1:
						output.append( "Semi-naive evaluation" ).append( NEW_LINE );
						config.evaluationTechnique = new SemiNaiveEvaluatorFactory();
						break;
					
					}

					IKnowledgeBase knowledgeBase = KnowledgeBaseFactory.createKnowledgeBase( facts, rules, config );
					
					List<IVariable> variableBindings = new ArrayList<IVariable>();

					// Execute the query
					long queryDuration = -System.currentTimeMillis();
					IRelation results = knowledgeBase.execute( query, variableBindings );
					queryDuration += System.currentTimeMillis();

					if( SHOW_VARIABLE_BINDINGS )
					{
						boolean first = true;
						for( IVariable variable : variableBindings )
						{
							if( first )
								first = false;
							else
								output.append( ", " );
							output.append( variable );
						}
						output.append( NEW_LINE );
					}
					
					formatResults( output, results );

					if( SHOW_ROW_COUNT || SHOW_QUERY_TIME )
						output.append( "-----------------" ).append( NEW_LINE );
					if( SHOW_ROW_COUNT )
						output.append( "Rows: " ).append( results.size() ).append( NEW_LINE );
					if( SHOW_QUERY_TIME )
						output.append( "Time: " ).append( queryDuration ).append( "ms" ).append( NEW_LINE );
			
					SwingUtilities.invokeLater( new NotifyOutput( output.toString() ) );
				}
				catch( Exception e )
				{
					SwingUtilities.invokeLater( new NotifyOutput( e.getMessage() ) );
				}
	        }
			
			private final String program;
			private final int evaluationStrategy;
		}
	}

	public static void formatResults( StringBuilder builder, IRelation m )
	{
		for(int t = 0; t < m.size(); ++t )
		{
			ITuple tuple = m.get( t );
			builder.append( tuple.toString() ).append( NEW_LINE );
		}
    }
}
