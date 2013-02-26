package at.sti2.streamingiris.performance;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.CountProfiler;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.KnowledgeBaseFactory;
import at.sti2.streamingiris.api.IKnowledgeBase;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;

public class AnswersTest {

	private static final String PROGRAM = "program";
	private static final String DELAY = "delay";
	private static final String RUNS = "runs";

	private Parser parser;
	private String program;
	private IKnowledgeBase knowledgeBase;
	private PerformanceTestListener performanceTestListener;

	public AnswersTest() {
	}

	public void start() throws IOException, ParserException,
			EvaluationException {
		try {
			parser = new Parser();

			InputStream inputStream = new FileInputStream(
					datalogProgramFileName);

			Reader r = new InputStreamReader(inputStream);

			StringBuilder builder = new StringBuilder();

			int ch = -1;
			while ((ch = r.read()) >= 0) {
				builder.append((char) ch);
			}
			program = builder.toString();

			parser.parse(program);

			Configuration configuration = KnowledgeBaseFactory
					.getDefaultConfiguration();
			configuration.timeWindowMilliseconds = timeWindow;

			knowledgeBase = KnowledgeBaseFactory.createKnowledgeBase(
					parser.getFacts(), parser.getRules(), configuration);

			List<IQuery> queries = parser.getQueries();
			ServerSocket server;
			for (IQuery query : queries) {
				server = new ServerSocket(0);
				performanceTestListener = new PerformanceTestListener(server);
				performanceTestListener.start();
				knowledgeBase.registerQueryListener(query, "localhost",
						server.getLocalPort());
			}

			// create multiple input streamer
			PerformanceTestEndlessInputStreamer inputStreamer1 = new PerformanceTestEndlessInputStreamer(
					8080, delay, streamTime, 0);

			// stream the facts
			inputStreamer1.start();

			Thread.sleep(streamTime + 5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		knowledgeBase.shutdown();
		performanceTestListener.shutdown();
	}

	private static boolean startsWith(String argument, String token) {
		if (argument.length() < token.length())
			return false;

		String start = argument.substring(0, token.length());

		return start.equalsIgnoreCase(token);
	}

	private static String getParameter(String argument) {
		int equals = argument.indexOf('=');

		if (equals >= 0) {
			return argument.substring(equals + 1);
		}

		return null;
	}

	// Configuration
	private static long delay = 2500;
	private static String datalogProgramFileName = "D:\\workspaces\\workspace_iris\\streaming-iris\\streaming-iris-impl\\src\\test\\resources\\performance_simple.txt";
	private static int timeWindow = 25000;
	private static int runs = 1;
	private static long streamTime = 600000;

	/**
	 * Entry point.
	 * 
	 * @throws EvaluationException
	 * @throws ParserException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, ParserException,
			EvaluationException {
		for (String argument : args) {
			if (startsWith(argument, PROGRAM))
				datalogProgramFileName = getParameter(argument);
			else if (startsWith(argument, DELAY))
				delay = Integer.parseInt(getParameter(argument));
			else if (startsWith(argument, RUNS))
				runs = Integer.parseInt(getParameter(argument));
			else
				usage();
		}

		CountProfiler.createFile(datalogProgramFileName, "", delay, timeWindow);

		try {
			for (int i = 0; i < runs; i++) {
				AnswersTest test = new AnswersTest();
				test.start();

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				CountProfiler.nextRun();

				test.shutdown();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void usage() {
		String space = "    ";

		System.out.println();
		System.out
				.println("Usage: at.sti2.streamingiris.performance.PerformanceTest <ARGUMENTS>");
		System.out.println();
		System.out.println("where <ARGUMENTS> is made up of:");
		System.out.println(space + PROGRAM + "=<datalog program>");
		System.out.println(space + DELAY
				+ "=<delay of streamed facts in milliseconds>");
		System.out.println(space + RUNS + "=<number of runs>");

		System.exit(1);
	}
}
