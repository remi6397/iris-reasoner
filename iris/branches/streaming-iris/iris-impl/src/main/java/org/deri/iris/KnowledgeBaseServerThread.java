package org.deri.iris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;
import org.deri.iris.storage.IRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This thread reads the input from a socket, parses it and hands it over to the
 * Knowledge Base.
 * 
 * @author norlan
 * 
 */
public class KnowledgeBaseServerThread extends Thread {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private KnowledgeBase knowledgeBase = null;
	private Socket socket = null;

	/**
	 * Constructor.
	 * 
	 * @param knowledgeBase
	 *            The Knowledge Base where to give the new data.
	 * @param socket
	 *            The socket where the new data is read from.
	 */
	public KnowledgeBaseServerThread(KnowledgeBase knowledgeBase, Socket socket) {
		this.knowledgeBase = knowledgeBase;
		this.socket = socket;
	}

	public void run() {

		try {
			BufferedReader streamReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

			String factLine = null;

			StringBuilder sb = new StringBuilder();
			Parser parser = new Parser();
			while (!Thread.interrupted()
					&& (factLine = streamReader.readLine()) != null) {
				// TODO maybe limit it to read max number of lines and then
				// start again
				sb.append(factLine);
			}

			if (!Thread.interrupted()) {
				parser.parse(sb.toString());
				Map<IPredicate, IRelation> newFacts = parser.getFacts();
				knowledgeBase.addFacts(newFacts);
			}

			streamReader.close();
			socket.close();
		} catch (IOException e) {
			logger.error("IO exception occured!", e);
			e.printStackTrace();
		} catch (ParserException e) {
			logger.error("Parse exception occured!", e);
			e.printStackTrace();
		}
	}
}
