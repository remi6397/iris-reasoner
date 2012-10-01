package at.sti2.streamingiris.threads;

import at.sti2.streamingiris.KnowledgeBase;

public class ExecutionThread extends Thread {

	private KnowledgeBase knowledgeBase;

	public ExecutionThread(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	public void run() {
		knowledgeBase.execute();
	}
}
