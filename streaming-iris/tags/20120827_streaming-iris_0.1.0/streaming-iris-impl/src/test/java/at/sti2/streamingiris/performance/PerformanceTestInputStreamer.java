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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

public class PerformanceTestInputStreamer {

	static Logger logger = Logger.getLogger(PerformanceTestInputStreamer.class);

	private String fileName = null;

	private int port = 0;
	private Socket sock = null;

	private long delay;

	private PerformanceTest program;

	/**
	 * Constructor.
	 * 
	 * @param port
	 *            The port to which the streamer sends the data.
	 * @param fileName
	 *            The file name of the datalog program.
	 */
	public PerformanceTestInputStreamer(int port, String fileName, long delay,
			PerformanceTest program) {
		this.port = port;
		this.fileName = fileName;
		this.delay = delay;
		this.program = program;
	}

	public boolean stream() {

		int i = 0;

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					fileName));
			logger.info("Beginning of streaming.");

			String factLine = bufferedReader.readLine();

			while (factLine != null) {
				i++;
				sock = new Socket("localhost", port);
				PrintWriter streamWriter = new PrintWriter(
						sock.getOutputStream());
				while (factLine != null && !factLine.isEmpty()) {
					streamWriter.println(factLine);
					factLine = bufferedReader.readLine();
				}
				streamWriter.flush();
				program.addStartTime(i, System.currentTimeMillis());
				streamWriter.close();
				sock.close();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					break;
				}
				factLine = bufferedReader.readLine();
			}

			logger.info("End of streaming.");
			logger.info("Streamed " + i + " facts.");

			bufferedReader.close();

			logger.info("Disconnected.");

			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}
}
