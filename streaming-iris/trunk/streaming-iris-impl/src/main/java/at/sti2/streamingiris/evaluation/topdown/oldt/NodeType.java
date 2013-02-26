package at.sti2.streamingiris.evaluation.topdown.oldt;

/**
 * Used to classify nodes.
 * 
 * LINK A link node which is a special form of a memo node. A link node is not
 * evaluated by resolution, but previously computed answers are used to expand
 * the node. By trying to expand the link node using resolution, the node gets
 * paused.
 * 
 * ANSWER A answer node which is a special form of a memo node. A answer node is
 * evaluated by resolution. The answers are stored in the corresponding entry in
 * the memo table.
 * 
 * NORMAL A ordinary node (not a memo node).
 * 
 * @author gigi
 * 
 */
public enum NodeType {
	LINK, ANSWER, NORMAL
}
