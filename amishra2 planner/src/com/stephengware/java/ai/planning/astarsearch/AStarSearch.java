package com.stephengware.java.ai.planning.astarsearch;


import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import com.stephengware.java.ai.planning.Plan;
import com.stephengware.java.ai.planning.Step;
import com.stephengware.java.ai.planning.logic.Conjunction;
import com.stephengware.java.ai.planning.logic.Expression;
import com.stephengware.java.ai.planning.logic.Literal;
import com.stephengware.java.ai.planning.ss.StateSpaceNode;
import com.stephengware.java.ai.planning.ss.StateSpaceProblem;
import com.stephengware.java.ai.planning.ss.StateSpaceSearch;
import com.stephengware.java.ai.planning.util.ImmutableArray;

/**
 * Searches for a solution using A* Search.
 * 
 * @author Your Name
 */
public class AStarSearch extends StateSpaceSearch {

		
	/*
	 * Priority queue which holds the StateSpaceNode .... node with lowest heuristic value remains at the top
	 */

	Comparator<QNode> comparator = new QNode();
	protected final PriorityQueue<QNode> pQueue = new PriorityQueue<QNode>(10, comparator);
	
	/*
	 * Holds all the literals for a given problem
	 */
	
	java.util.HashSet<Literal> literals = new java.util.HashSet<>();
	
	/*
	 * Holds set of possible steps for the given problem
	 */
	
	ImmutableArray<Step> steps = null;
	
	/**
	 * Constructs a new A* search planner.
	 * 
	 * @param problem the state space search problem to be solved
	 */
	public AStarSearch(StateSpaceProblem problem) {
		super(problem);
		QNode rootNode = new QNode();
		rootNode.ssn = root;
		rootNode.value = 0.0;
		pQueue.offer(rootNode);
		
	}

	@Override
	public Plan findNextSolution() {
		
		HashSet<Literal> all_literals = getLiterals(problem);
				
		while(!pQueue.isEmpty()) {
			QNode qn = pQueue.poll();
			StateSpaceNode node = qn.ssn;
			node.expand();
			
			if(problem.goal.isTrue(node.state))
				return node.plan;
			for(StateSpaceNode child : node.children){
				HSPHeuristics hsp = new HSPHeuristics();
				int dist_from_root_to_current = child.plan.size();
				QNode subNode = new QNode();
				subNode.ssn = child;
				subNode.value = dist_from_root_to_current+hsp.evaluate(subNode.ssn, problem.goal, all_literals, steps);
				pQueue.offer(subNode);
				
			}
					
			
		}
				
		return null;
	}
	
	
	/*
	 * We can obtain literals from goal state, root state and from step of steps precondition and effects
	 */
	
	public HashSet<Literal> getLiterals(StateSpaceProblem problem){
		
		steps = problem.steps;
		Expression goal_exp = problem.goal;
		Expression root_exp = root.state.toExpression();
		
		if(goal_exp instanceof Literal){
			
			literals.add((Literal) goal_exp);
						
		}else {
			
			Conjunction conj = (Conjunction) goal_exp;
		    for(Expression conjunct : conj.arguments)
		        literals.add((Literal) conjunct);
			
		}
		
		if(root_exp instanceof Literal){
			
			literals.add((Literal) root_exp);
						
		}else {
			
			Conjunction conj = (Conjunction) root_exp;
		    for(Expression conjunct : conj.arguments)
		        literals.add((Literal) conjunct);
			
		}
		
		
		for(int i = 0 ; i < steps.length; i++){
			
			Expression precond_exp = steps.get(i).precondition;
			Expression effect_exp = steps.get(i).effect;
			
			if(precond_exp instanceof Literal){
				
				literals.add((Literal) precond_exp);
							
			}else {
				
				Conjunction conj = (Conjunction) precond_exp;
			    for(Expression conjunct : conj.arguments)
			        literals.add((Literal) conjunct);
				
			}
			
			if(effect_exp instanceof Literal){
				
				literals.add((Literal) effect_exp);
							
			}else {
				
				Conjunction conj = (Conjunction) effect_exp;
			    for(Expression conjunct : conj.arguments)
			        literals.add((Literal) conjunct);
				
			}
						
			
		}
		
		
		return literals;
		
	}
	
}
