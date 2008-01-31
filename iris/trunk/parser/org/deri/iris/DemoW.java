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
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;

/**
 * A GUI version of the Demo application.
 */
public class DemoW
{
	public static final int FONT_SIZE = 12;
	public static String NEW_LINE = "\r\n";
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
			super( "IRIS - Old" );

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
			
			mRun.addActionListener( this );

			mAbort.addActionListener( this );
			mAbort.setEnabled( false );

			JScrollPane programScroller = new JScrollPane( mProgram );
			JScrollPane outputScroller = new JScrollPane( mOutput );
			
			Font f = new Font( "courier", Font.BOLD, FONT_SIZE );
			mProgram.setFont( f );
			mOutput.setFont( f );

			JSplitPane mainSplitter = new JSplitPane( JSplitPane.VERTICAL_SPLIT, false, programScroller, outputScroller );

			getContentPane().add( mainSplitter, BorderLayout.CENTER );
			
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
		
		private final JComboBox mEvaluationStrategy = new JComboBox( new String[] { "Naive", "Semi-naive", "Magic Sets" } );
		
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
					Map<IPredicate, IMixedDatatypeRelation> results;
					
					long queryDuration = -System.currentTimeMillis();
					
					IProgram p = ExecutionHelper.parseProgram( program );
			
					StringBuilder output = new StringBuilder();;
					switch( evaluationStrategy )
					{
					case 0:
						output.append( "Naive evaluation\r\n" );
						results = ExecutionHelper.evaluateNaive( p );
						break;
					case 1:
					default:
						output.append( "Semi-naive evaluation\r\n" );
						results = ExecutionHelper.evaluateSeminaive( p );
						break;
					case 2:
						output.append( "Semi-naive evaluation with magic sets\r\n" );
						results = ExecutionHelper.evaluateSeminaiveWithMagicSets( p );
						break;
					}

					queryDuration += System.currentTimeMillis();

					output.append( ExecutionHelper.resultsTostring( results ) );

//					if( SHOW_QUERY_TIME )
//					{
//						output += "-----------------\r\n";
//						output += ( "Time: " + queryDuration + "ms" );
//					}
					if( SHOW_ROW_COUNT || SHOW_QUERY_TIME )
						output.append( "-----------------" ).append( NEW_LINE );
					if( SHOW_ROW_COUNT )
						output.append( "Rows: " ).append( countRows( results ) ).append( NEW_LINE );
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
	public static int countRows( Map<IPredicate, IMixedDatatypeRelation> m )
	{
		int count = 0;
		for( IPredicate pr : m.keySet() )
		{
			count += m.get( pr ).size();
    	}

		return count;
    }

}
