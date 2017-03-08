package com.stephengware.java.ai.planning.astarsearch;

import com.stephengware.java.ai.planning.ss.StateSpacePlanner;
import com.stephengware.java.ai.planning.ss.StateSpaceProblem;
import com.stephengware.java.ai.planning.ss.StateSpaceSearch;

/**
 * A simple planner which uses breadth first search to find a solution.
 * 
 * @author Your Name
 */
public class Amishra2Planner extends StateSpacePlanner {

	/**
	 * Constructs a new breadth first search planner.
	 */
	public Amishra2Planner() {
		super("amishra2");
	}

	@Override
	protected StateSpaceSearch makeStateSpaceSearch(StateSpaceProblem problem) {
		return new AStarSearch(problem);
	}
}
