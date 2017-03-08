package com.stephengware.java.ai.planning.astarsearch;

import java.io.File;

import com.stephengware.java.ai.planning.Domain;
import com.stephengware.java.ai.planning.Planner;
import com.stephengware.java.ai.planning.Problem;
import com.stephengware.java.ai.planning.Result;
import com.stephengware.java.ai.planning.Step;
import com.stephengware.java.ai.planning.io.Parser;

/**
 * A script for running all test cases from the Eclipse IDE.
 * 
 * @author Stephen G. Ware
 */
public class Test {
	
	/** An individual test problem */
	private static final class TestCase {
		
		/** The name of the problem's domain */
		public final String domain;
		
		/** The name of the problem */
		public final String problem;
		
		/** The number of nodes to visit before giving up */
		public final int limit;
		
		/**
		 * Constructs a new test case.
		 * 
		 * @param domain the name of the domain
		 * @param problem the name of the problem
		 */
		public TestCase(String domain, String problem, int limit) {
			this.domain = domain;
			this.problem = problem;
			this.limit = limit;
		}
	}
	
	/** An array of all test cases. */
	public static final TestCase[] TESTS = new TestCase[]{
		new TestCase("blocks", "do_nothing", 10),
		new TestCase("blocks", "easy_stack", 10),
		new TestCase("blocks", "easy_unstack", 10),
		new TestCase("blocks", "sussman", 50),
		new TestCase("blocks", "reverse_2", 10),
		new TestCase("blocks", "reverse_4", 50),
		new TestCase("blocks", "reverse_6", 50),
		new TestCase("blocks", "reverse_8", 50),
		new TestCase("blocks", "reverse_10", 100),
		new TestCase("blocks", "reverse_12", 500),
		new TestCase("cargo", "deliver_1", 10),
		new TestCase("cargo", "deliver_2", 50),		
		new TestCase("cargo", "deliver_3", 100000),
		new TestCase("cargo", "deliver_4", 100000), 
		new TestCase("cargo", "deliver_return_1", 50),
		new TestCase("cargo", "deliver_return_2", 500),
		new TestCase("cargo", "deliver_return_3", 100000), 
		new TestCase("cargo", "deliver_return_4", 100000), 
		new TestCase("wumpus", "easy_wumpus", 10),
		new TestCase("wumpus", "medium_wumpus", 50),  // search limit reached
		new TestCase("wumpus", "hard_wumpus", 1000), // search limit reached
	};
	
	/**
	 * Run all test cases and output the results to the console.
	 * 
	 * @param args ignored
	 * @throws Exception if an exception occurs
	 */
	public static void main(String[] args) throws Exception {
		Planner<?> planner = new Amishra2Planner();
		Parser parser = new Parser();
		for(TestCase test : TESTS) {
			parser.parse(new File("tests/" + test.domain + ".pddl"), Domain.class);
			Problem problem = parser.parse(new File("tests/" + test.problem + ".pddl"), Problem.class);
			Result result = planner.findSolutuion(problem, test.limit, Planner.NO_TIME_LIMIT);
			System.out.println(result);
			if(result.solution != null)
				for(Step step : result.solution)
					System.out.println("  " + step);
		}
	}
}
