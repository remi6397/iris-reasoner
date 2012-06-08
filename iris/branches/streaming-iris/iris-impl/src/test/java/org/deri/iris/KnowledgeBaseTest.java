package org.deri.iris;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;
import org.deri.iris.storage.IRelation;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class KnowledgeBaseTest {

	private IKnowledgeBase knowledgeBase = null;
	private Parser parser;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Before
	public void setUp() {
		try {
			Configuration configuration = KnowledgeBaseFactory
					.getDefaultConfiguration();

			String filename = "D:\\workspaces\\workspace_iris\\iris\\iris-impl\\src\\test\\resources\\simpsons.txt";
			FileReader r;
			r = new FileReader(filename);
			StringBuilder builder = new StringBuilder();
			int ch = -1;
			while ((ch = r.read()) >= 0) {
				builder.append((char) ch);
			}
			String program = builder.toString();

			parser = new Parser();
			parser.parse(program);
			Map<IPredicate, IRelation> facts = parser.getFacts();
			List<IRule> rules = parser.getRules();

			knowledgeBase = KnowledgeBaseFactory.createKnowledgeBase(facts,
					rules, configuration);

			for (IQuery query : parser.getQueries()) {
				knowledgeBase.registerQuery(query);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void shutdown() {
		if (knowledgeBase != null) {
			knowledgeBase.shutdown();
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

		for (IQuery query : queries) {
			knowledgeBase.registerQuery(query);
		}
	}

	@Test
	public void testAddListener() throws IOException,
			ProgramNotStratifiedException, RuleUnsafeException,
			ParserException, EvaluationException, InterruptedException {
		int port = 9456;

		ServerSocket server = new ServerSocket(port);
		logger.info("Server: " + server);

		ListenerThread listenerThread = new ListenerThread(server);

		knowledgeBase.addListener("localhost", server.getLocalPort());

		testRegisterQuery();

		listenerThread.interrupt();
	}

	@Test
	public void testStreamFacts() {
		// initialize IrisInputStreamer
		// check results
	}
}
