package org.deri.iris.threads;

import org.deri.iris.KnowledgeBase;

public class ExecutionThread extends Thread {

	private KnowledgeBase knowledgeBase;

	public ExecutionThread(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	public void run() {
		knowledgeBase.execute();
	}
}
