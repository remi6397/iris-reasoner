package org.deri.iris.threads;

import org.deri.iris.KnowledgeBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionThread extends Thread {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private KnowledgeBase knowledgeBase;
	private long executionIntervall;

	public ExecutionThread(KnowledgeBase knowledgeBase,
			long executionIntervallMilliseconds) {
		this.knowledgeBase = knowledgeBase;
		this.executionIntervall = executionIntervallMilliseconds;
	}

	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(executionIntervall);
			} catch (InterruptedException e) {
				break;
			}
			knowledgeBase.execute();
		}
		logger.debug("Execution thread shut down!");
	}
}
