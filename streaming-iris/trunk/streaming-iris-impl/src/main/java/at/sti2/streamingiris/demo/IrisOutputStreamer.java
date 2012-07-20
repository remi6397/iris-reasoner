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
package at.sti2.streamingiris.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;

import org.apache.log4j.Logger;

public class IrisOutputStreamer implements IIrisOutputStreamer {

	static Logger logger = Logger.getLogger(IrisOutputStreamer.class);

	private String host = null;
	private int port = 0;
	private Socket socket = null;

	private PrintWriter streamWriter;

	/**
	 * Constructor.
	 * 
	 * @param host
	 *            The host where the results are sent.
	 * @param port
	 *            The port where the results are sent.
	 */
	public IrisOutputStreamer(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.deri.iris.demo.IIrisOutputStreamer#connect()
	 */
	@Override
	public void connect() {
		try {
			socket = new Socket(host, port);
			logger.info("Connected.");
		} catch (IOException e) {
			logger.debug("Cannot connect to server.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.deri.iris.demo.IIrisOutputStreamer#stream(java.lang.String)
	 */
	@Override
	public void stream(String output) {
		long factCounter = 0;

		try {
			streamWriter = new PrintWriter(socket.getOutputStream());
			BufferedReader bufferedReader = new BufferedReader(
					new StringReader(output));
			String factLine = null;
			logger.info("Beginning of streaming.");

			while ((factLine = bufferedReader.readLine()) != null) {
				streamWriter.println(factLine);
				logger.debug(factLine);
				factCounter++;
			}

			streamWriter.flush();

			logger.info("End of streaming.");
			logger.info("Streamed " + factCounter + " fact(s) to " + host + ":"
					+ port + ".");

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.deri.iris.demo.IIrisOutputStreamer#shutdown()
	 */
	@Override
	public boolean shutdown() {
		boolean result;
		try {
			if (streamWriter != null)
				streamWriter.close();
			if (socket != null)
				socket.close();
			logger.info("Disconnected.");
		} catch (IOException e) {
			logger.error("IO exception occured!", e);
			e.printStackTrace();
		} finally {
			result = true;
		}
		return result;
	}
}
