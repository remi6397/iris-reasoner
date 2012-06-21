package org.deri.iris;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KnowledgeBaseTest {

	private IKnowledgeBase knowledgeBase = null;
	private Parser parser;
	private Logger logger = LoggerFactory.getLogger(getClass());
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
				knowledgeBase.registerQuery(query, "localhost",
						server.getLocalPort());
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

		for (IQuery query : queries) {
			knowledgeBase.registerQuery(query, "localhost",
					server.getLocalPort());
		}
	}
}
