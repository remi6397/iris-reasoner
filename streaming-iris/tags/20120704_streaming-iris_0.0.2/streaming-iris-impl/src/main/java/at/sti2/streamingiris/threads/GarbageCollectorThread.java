package at.sti2.streamingiris.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.streamingiris.KnowledgeBase;

public class GarbageCollectorThread extends Thread {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private KnowledgeBase knowledgeBase;

	public GarbageCollectorThread(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	public void run() {
		logger.debug("Garbage collector ...");
		knowledgeBase.cleanKnowledgeBase();
	}
}
