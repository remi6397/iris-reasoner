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
package org.deri.iris.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

public class IrisInputStreamer {

	static Logger logger = Logger.getLogger(IrisInputStreamer.class);

	private String fileName = null;

	private int port = 0;
	private Socket sock = null;

	/**
	 * Constructor.
	 * 
	 * @param port
	 *            The port to which the streamer sends the data.
	 * @param fileName
	 *            The file name of the datalog program.
	 */
	public IrisInputStreamer(String port, String fileName) {
		this.port = Integer.parseInt(port);
		this.fileName = fileName;

		// Connect to the socket
		connect();

		// Stream file
		stream();
	}

	private void connect() {

		try {
			sock = new Socket("localhost", port);
		} catch (IOException e) {
			logger.debug("Cannot connect to server.");
		}
		logger.info("Connected.");
	}

	private void stream() {

		long factCounter = 0;

		try {
			PrintWriter streamWriter = new PrintWriter(sock.getOutputStream());
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					fileName));
			String factLine = null;
			logger.info("Beginning of streaming.");

			while ((factLine = bufferedReader.readLine()) != null) {
				streamWriter.println(factLine);
				factCounter++;
			}

			streamWriter.flush();

			logger.info("End of streaming.");
			logger.info("Streamed " + factCounter + " facts.");

			bufferedReader.close();
			streamWriter.close();
			sock.close();

			logger.info("Disconnected.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		if (args.length != 2) {
			System.out
					.println("IrisInputStreamer sends a stream of datalog facts to a designated port. The streamer expects to receive following arguments:");
			System.out
					.println(" <port> - the local port at which KnowledgeBase instance listens for upcoming facts.");
			System.out
					.println(" <file_name> - name of the file in datalog format holding facts to be streamed.");
			System.exit(0);
		}

		logger.debug("Started IrisInputStreamer on port: " + args[0] + "("
				+ args[1] + ")");

		new IrisInputStreamer(args[0], args[1]);
	}

}
