package at.sti2.streamingiris;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.streamingiris.api.IKnowledgeBase;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;
import at.sti2.streamingiris.storage.IRelation;

public class KnowledgeBaseTest {

	private IKnowledgeBase knowledgeBase = null;
	private Parser parser;
	private ServerSocket server;

	@Before
	public void setUp() {
		try {
			Configuration configuration = KnowledgeBaseFactory
					.getDefaultConfiguration();

			BufferedInputStream in = new BufferedInputStream(getClass()
					.getResourceAsStream("/simpsons.txt"));
			StringBuilder builder = new StringBuilder();
			int ch = -1;
			while ((ch = in.read()) >= 0) {
				builder.append((char) ch);
			}
			String program = builder.toString();

			parser = new Parser();
			parser.parse(program);
			Map<IPredicate, IRelation> facts = parser.getFacts();
			List<IRule> rules = parser.getRules();

			server = new ServerSocket(0);

			knowledgeBase = KnowledgeBaseFactory.createKnowledgeBase(facts,
					rules, configuration);

			for (IQuery query : parser.getQueries()) {
				knowledgeBase.registerQueryListener(query, "localhost",
						server.getLocalPort());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (EvaluationException e) {
			e.printStackTrace();
		}
	}

	@After
	public void shutdown() {
		if (knowledgeBase != null) {
			knowledgeBase.shutdown();
		}
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRegisterQuery() throws ParserException,
			ProgramNotStratifiedException, RuleUnsafeException,
			EvaluationException {
		parser = new Parser();
		parser.parse("?- isFemale(?x).");
		List<IQuery> queries = parser.getQueries();

		Assert.assertEquals(1, queries.size());

		try {
			ServerSocket serverSocket = new ServerSocket(0);

			for (IQuery query : queries) {
				knowledgeBase.registerQueryListener(query, "localhost",
						serverSocket.getLocalPort());
			}

			// Socket socket = serverSocket.accept();
			// BufferedReader bufferedReader = new BufferedReader(
			// new InputStreamReader(socket.getInputStream()));
			// String factLine = bufferedReader.readLine();
			// Assert.assertNotNull(factLine);
			// Assert.assertEquals("isFemale('marge simpson').", factLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeregisterQuery() {
		parser = new Parser();
		try {
			parser.parse("?- isMale(?x).");
			List<IQuery> queries = parser.getQueries();

			Assert.assertEquals(1, queries.size());

			for (IQuery query : queries) {
				knowledgeBase.deregisterQueryListener(query, "localhost",
						server.getLocalPort());
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

}
