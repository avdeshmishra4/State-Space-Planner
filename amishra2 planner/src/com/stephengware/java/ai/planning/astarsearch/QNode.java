package com.stephengware.java.ai.planning.astarsearch;

import java.util.Comparator;

import com.stephengware.java.ai.planning.ss.StateSpaceNode;

public class QNode implements Comparator<QNode> {
	
	StateSpaceNode ssn;
	Double value;
	
	@Override
	public int compare(QNode Q1, QNode Q2) {
		
		return Double.compare(Q1.value, Q2.value);				 
		
	}

}
