package org.deri.iris.threads;

import org.deri.iris.KnowledgeBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GarbageCollectorThread extends Thread {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private KnowledgeBase knowledgeBase;
	private long garbageCollectionIntervall;

	public GarbageCollectorThread(KnowledgeBase knowledgeBase,
			long executionIntervallMilliseconds) {
		this.knowledgeBase = knowledgeBase;
		this.garbageCollectionIntervall = executionIntervallMilliseconds;
	}

	public void run() {
		try {
			while (true) {
				Thread.sleep(garbageCollectionIntervall);
				logger.debug("Garbage collector ...");
				knowledgeBase.cleanKnowledgeBase();
			}
		} catch (InterruptedException e) {
			logger.debug("Garbage collector thread shut down!");
		}
	}
}
