package at.sti2.streamingiris.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.streamingiris.InputBuffer;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;
import at.sti2.streamingiris.storage.IRelation;

/**
 * This thread reads the input from a socket, parses it and hands it over to the
 * Knowledge Base.
 * 
 * @author norlan
 * 
 */
public class KnowledgeBaseServerThread extends Thread {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private InputBuffer inputBuffer = null;
	private Socket socket = null;

	/**
	 * Constructor.
	 * 
	 * @param inputBuffer
	 *            The Knowledge Base where to give the new data.
	 * @param socket
	 *            The socket where the new data is read from.
	 */
	public KnowledgeBaseServerThread(InputBuffer inputBuffer, Socket socket) {
		this.inputBuffer = inputBuffer;
		this.socket = socket;
	}

	public void run() {

		try {
			BufferedReader streamReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

			Parser parser = new Parser();

			StringBuffer buffer = new StringBuffer();

			String factLine = streamReader.readLine();
			while (factLine != null) {
				buffer.append(factLine);
				factLine = streamReader.readLine();
			}

			parser.parse(buffer.toString());
			Map<IPredicate, IRelation> newFacts = parser.getFacts();
			if (newFacts != null && newFacts.size() != 0) {
				inputBuffer.addFacts(newFacts);
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
