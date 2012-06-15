package org.deri.iris;

import org.deri.iris.api.IKnowledgeBase;
import org.junit.Test;

public class KnowledgeBaseShutdownTest {

	@Test
	public void startAndShutdownTest() {
		try {
			// get the default configuration for the knowledge base
			Configuration configuration = KnowledgeBaseFactory
					.getDefaultConfiguration();

			// start the knowledge base
			IKnowledgeBase knowledgeBase = KnowledgeBaseFactory
					.createKnowledgeBase(null, null, configuration);

			// shut the knowledge base down
			knowledgeBase.shutdown();

		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void startAndShutdownWithSleepTest() {
		try {
			// get the default configuration for the knowledge base
			Configuration configuration = KnowledgeBaseFactory
					.getDefaultConfiguration();

			// start the knowledge base
			IKnowledgeBase knowledgeBase = KnowledgeBaseFactory
					.createKnowledgeBase(null, null, configuration);

			Thread.sleep(6000);

			// shut the knowledge base down
			knowledgeBase.shutdown();
		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
