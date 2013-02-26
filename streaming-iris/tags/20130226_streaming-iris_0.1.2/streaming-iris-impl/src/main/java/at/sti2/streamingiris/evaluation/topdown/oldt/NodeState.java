package at.sti2.streamingiris.evaluation.topdown.oldt;

/**
 * Represents the state of a node in a top-down proof tree.
 * 
 * This enum could be reduced to PAUSED_N, PAUSED_C and DONE, but for the sake
 * of better debugging output it includes all possible node states.
 * 
 * INITIALIZED EVALUATING PAUSED_N this is a paused node PAUSED_C node contains
 * at least one paused child SUCCESS FAILURE DONE node does not contain paused
 * nodes
 * 
 * @author gigi
 * 
 */
public enum NodeState {
	INITIALIZED, EVALUATING, PAUSED_N, PAUSED_C, SUCCESS, FAILURE, DONE
}