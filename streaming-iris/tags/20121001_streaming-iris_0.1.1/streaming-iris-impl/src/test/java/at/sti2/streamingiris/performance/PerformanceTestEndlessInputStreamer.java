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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.Logger;

public class PerformanceTestEndlessInputStreamer extends Thread {

	static Logger logger = Logger
			.getLogger(PerformanceTestEndlessInputStreamer.class);

	private int port = 0;
	private Socket sock = null;
	private long delay;
	private PerformanceTest program;
	private long time;
	private String[] facts;

	/**
	 * Constructor.
	 * 
	 * @param port
	 *            The port to which the streamer sends the data.
	 * @param fileName
	 *            The file name of the datalog program.
	 */
	public PerformanceTestEndlessInputStreamer(int port, long delay, long time,
			PerformanceTest program, ArrayList<String> factExamples) {
		this.port = port;
		this.delay = delay;
		this.time = time;
		this.program = program;

		this.facts = new String[factExamples.size()];
		this.facts = factExamples.toArray(this.facts);
	}

	public void run() {

		int i = 0;

		try {
			long endtime = System.currentTimeMillis() + time;
			int randomInt;
			String factString;
			int count = 0;

			logger.info("Beginning of streaming.");

			while (System.currentTimeMillis() < endtime) {
				i++;
				sock = new Socket("localhost", port);
				PrintWriter streamWriter = new PrintWriter(
						sock.getOutputStream());

				// generate new fact
				Random randomGenerator = new Random();
				randomInt = randomGenerator.nextInt(facts.length);
				factString = facts[randomInt];

				while (factString.contains("?")) {
					factString = factString.replaceFirst("\\?+", "" + count);
					count++;
				}

				streamWriter.println(factString);

				streamWriter.flush();
				program.addStartTime(i, System.currentTimeMillis());
				streamWriter.close();
				sock.close();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					break;
				}
			}

			logger.info("End of streaming.");
			logger.info("Streamed " + i + " facts.");

			logger.info("Disconnected.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
