/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
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
package at.sti2.streamingiris.performance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.KnowledgeBaseFactory;
import at.sti2.streamingiris.api.IKnowledgeBase;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;

public class TimeTest implements PerformanceTest {

	private static final String PROGRAM = "program";
	private static final String DELAY = "delay";
	private static final String STREAM = "stream";
	private static final String RUNS = "runs";

	private Parser parser;
	private String program;
	private IKnowledgeBase knowledgeBase;
	private Map<Integer, Long> times;
	private PerformanceTestListener performanceTestListener;

	public TimeTest() {
	}

	public Map<Integer, Long> start(String datalogProgramFileName)
			throws IOException, ParserException, EvaluationException {
		times = new HashMap<Integer, Long>();
		parser = new Parser();

		InputStream inputStream = new FileInputStream(datalogProgramFileName);

		Reader r = new InputStreamReader(inputStream);

		StringBuilder builder = new StringBuilder();

		int ch = -1;
		while ((ch = r.read()) >= 0) {
			builder.append((char) ch);
		}
		program = builder.toString();

		parser.parse(program);

		Configuration confiuration = KnowledgeBaseFactory
				.getDefaultConfiguration();
		confiuration.timeWindowMilliseconds = timeWindow;

		knowledgeBase = KnowledgeBaseFactory.createKnowledgeBase(
				parser.getFacts(), parser.getRules(), confiuration);

		List<IQuery> queries = parser.getQueries();
		ServerSocket server;
		for (IQuery query : queries) {
			server = new ServerSocket(0);
			performanceTestListener = new PerformanceTestListener(server, this);
			performanceTestListener.start();
			knowledgeBase.registerQueryListener(query, "localhost",
					server.getLocalPort());
		}

		// create an input streamer
		PerformanceTestFileInputStreamer inputStreamer = new PerformanceTestFileInputStreamer(
				8080, inputStreamFileName, delay, this);

		// stream the facts
		inputStreamer.stream();

		try {
			Thread.sleep(delay * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return times;
	}

	public void shutdown() {
		knowledgeBase.shutdown();
		performanceTestListener.shutdown();
	}

	public void addStartTime(int count, long currentTimeMillis) {
		synchronized (times) {
			times.put(new Integer(count), new Long(-currentTimeMillis));
		}
	}

	public void addEndTime(int count, long currentTimeMillis) {
		Integer i = new Integer(count);
		synchronized (times) {
			Long startTime = times.get(i);
			if (startTime != null) {
				long time = startTime + currentTimeMillis;
				times.put(i, new Long(time));
			}
		}
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
	private static long delay = 20;
	private static String datalogProgramFileName = "D:\\workspaces\\workspace_iris\\streaming-iris\\streaming-iris-impl\\src\\test\\resources\\performance.txt";
	private static String inputStreamFileName = "D:\\workspaces\\workspace_iris\\streaming-iris\\streaming-iris-impl\\src\\test\\resources\\performance_stream.txt";
	// private static String datalogProgramFileName =
	// "D:\\workspaces\\workspace_iris\\streaming-iris\\streaming-iris-impl\\src\\test\\resources\\simpsons.txt";
	// private static String inputStreamFileName =
	// "D:\\workspaces\\workspace_iris\\streaming-iris\\streaming-iris-impl\\src\\test\\resources\\simpsons_stream_big.txt";
	private static int timeWindow = 50;
	private static String resultsPath = "D:\\workspaces\\workspace_iris\\streaming-iris\\streaming-iris-impl\\src\\test\\resources\\";
	private static int runs = 1;

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
			else if (startsWith(argument, STREAM))
				inputStreamFileName = getParameter(argument);
			else if (startsWith(argument, RUNS))
				runs = Integer.parseInt(getParameter(argument));
			else
				usage();
		}

		try {
			String fileName = resultsPath + "results_"
					+ System.currentTimeMillis() + ".csv";
			File outputFile = new File(fileName);

			FileWriter fileWriter = new FileWriter(outputFile);
			outputFile.createNewFile();

			fileWriter.append("Ontology," + datalogProgramFileName + ",\n");
			fileWriter.append("Stream," + inputStreamFileName + ",\n\n");

			fileWriter.append("Execution,");

			for (int i = 0; i < runs; i++) {
				fileWriter.append("Time [ms],");
			}

			fileWriter.append("Agerage Time [ms]\n");

			List<Map<Integer, Long>> timesList = new ArrayList<Map<Integer, Long>>();

			for (int i = 0; i < runs; i++) {
				TimeTest test = new TimeTest();
				Map<Integer, Long> times = test.start(datalogProgramFileName);
				timesList.add(times);

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				test.shutdown();
			}

			double avgTime = 0.0;
			Long time;
			for (int i = 1; i < timesList.get(0).size(); i++) {
				fileWriter.append(i + ",");
				for (Map<Integer, Long> entry : timesList) {
					time = entry.get(i);
					avgTime += time;
					fileWriter.append(time + ",");
				}
				avgTime = avgTime / runs;
				fileWriter.append(avgTime + ",\n");
			}

			fileWriter.flush();
			fileWriter.close();
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
		System.out.println(space + STREAM + "=<file with facts to stream>");
		System.out.println(space + RUNS + "=<number of runs>");

		System.exit(1);
	}
}
