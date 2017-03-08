package com.stephengware.java.ai.planning.astarsearch;

	
import java.util.HashMap;
import java.util.HashSet;

import com.stephengware.java.ai.planning.State;
import com.stephengware.java.ai.planning.Step;
import com.stephengware.java.ai.planning.logic.Conjunction;
import com.stephengware.java.ai.planning.logic.Expression;
import com.stephengware.java.ai.planning.logic.Literal;
import com.stephengware.java.ai.planning.ss.StateSpaceNode;
import com.stephengware.java.ai.planning.util.ImmutableArray;
	

public class HSPHeuristics extends State{	
	
	HashMap<Literal, Double> literals_map = new HashMap<Literal, Double>();
				
	public double evaluate(StateSpaceNode curr_node, Expression goal, HashSet<Literal> all_literals, ImmutableArray<Step> steps){
		
		boolean hasChanged = true;
		Double cost_to_reach_goal = 0.0;
		
		for(Literal l : all_literals){
			
			if(l.isTrue(curr_node.state)){
				
				literals_map.put(l, 0.0);
				
			}else{
				
				literals_map.put(l, Double.MAX_VALUE);
				
			}
			
			
		}
		
		
		while(hasChanged){
			
			hasChanged = false;
			
			for(int i = 0 ; i < steps.length; i++){
				
				Expression precond_exp = steps.get(i).precondition;
				Expression effect_exp = steps.get(i).effect;
				
				
				Double cost_of_pre_cond = 0.0;
				
				// condition for calculating cost of precondition
				
				if(precond_exp instanceof Literal){
					
					Literal key = (Literal)precond_exp;
					cost_of_pre_cond = literals_map.get(key);
					
				}else {
					
					Conjunction cnj = (Conjunction) precond_exp;
					for(Expression conjunct : cnj.arguments){
						
						Literal key = (Literal)conjunct;
						Double literal_cost = literals_map.get(key);
						if(literal_cost.equals(Double.MAX_VALUE)){
							
							cost_of_pre_cond = Double.MAX_VALUE;
							break;    // because if any one of the literal has infinite value then the whole expression will have infinite value
														
						}else{
							
							cost_of_pre_cond += literal_cost;
							
						}
											
						
					}
					
					
				}
				
				
				// conditions for calculating cost of each literals of effect of current step
				
				if(effect_exp instanceof Literal){
					
					Literal key = (Literal)effect_exp;
					Double current_cost = literals_map.get(key);
					Double updated_cost = Math.min(current_cost, cost_of_pre_cond+1);
					
					if(Double.compare(updated_cost, current_cost) != 0){
						
						hasChanged = true;
						
					}
					
					literals_map.put(key, updated_cost);
					
					
				}else {
					
					Conjunction cnj = (Conjunction) effect_exp;
					for(Expression conjunct : cnj.arguments){
						
						Literal key = (Literal)conjunct;
						Double current_cost = literals_map.get(key);
						Double updated_cost = Math.min(current_cost, cost_of_pre_cond+1);
						
						if(Double.compare(updated_cost, current_cost) != 0){
							
							hasChanged = true;
							
						}
						
						literals_map.put(key, updated_cost);
						
						
					}
					
					
				}
				
							
				
			}
			
			
		}
		
		
		// finally return the cost of goal state
		
		if(goal instanceof Literal){
			
			Literal key = (Literal)goal;
			cost_to_reach_goal = literals_map.get(key); 
			
		}else {
			
			Conjunction cnj = (Conjunction) goal;
			for(Expression conjunct : cnj.arguments){
				
				Literal key = (Literal)conjunct;
				
				Double literal_cost = literals_map.get(key);
				if(literal_cost.equals(Double.MAX_VALUE)){
					
					cost_to_reach_goal = Double.MAX_VALUE;
					break;    // because if any one of the literal has infinite value then the whole expression will have infinite value
												
				}else{
					
					cost_to_reach_goal += literal_cost;
					
				}
							
				
			}
			
			
		}
		
		
		return cost_to_reach_goal;
		
	}

	
	
}
