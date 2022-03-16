package code.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import code.mission.Node;
import code.mission.NodeComparator;

public abstract class SearchProblem {
	public static Queue<Node> q = new LinkedList<>();
	public static Stack<Node> stack = new Stack<>();
	public static PriorityQueue<Node> pq;
	public static HashMap<String, Boolean> states = new HashMap<String, Boolean>();
	public static int numberOfAgents;
	public static int totalExpandedNodes = 0;
	public static String searchType = "";

	public static String solve(String grid, String strategy, boolean visualize) {
		String result = "";
		Node initial = new Node(null, 0, null, 0, null);
		Node node = new Node(null, 0, null, 0, null);
		switch (strategy) {
		case "BF": {
			if (node != null) {

				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				agentHealths = agentHealths.substring(0, agentHealths.length() - 1);
				result += node.getAgentsDead() + ";" + agentHealths + ";" + totalExpandedNodes;

			} else {
				System.out.println("Not Found");
			}
			break;
		}
		case "DF": {
			if (node != null) {
				// System.out.println("Found");

				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				agentHealths = agentHealths.substring(0, agentHealths.length() - 1);
				result += node.getAgentsDead() + ";" + agentHealths + ";" + totalExpandedNodes;

			} else {
				System.out.println("Not Found");
			}
			break;
		}
		case "ID": {
			int depth = 0;
			while (node == null) {

				depth += 10;
				// System.out.println(depth);
				SearchProblem.states = new HashMap<String, Boolean>();
				node = ids(initial, depth);

				// System.out.println(depth);

			}
			// System.out.println(node.getDepth());
			result = printSolution(node, "").substring(1) + ";";
			int[] totalHealth = node.getHealthArray();
			String agentHealths = "";
			for (int agentHealth : totalHealth) {
				agentHealths += agentHealth + ",";
			}
			agentHealths = agentHealths.substring(0, agentHealths.length() - 1);
			result += node.getAgentsDead() + ";" + agentHealths + ";" + totalExpandedNodes;
			break;
		}
		case "UC": {
			pq = new PriorityQueue<Node>(new NodeComparator("ucs"));
			if (node != null) {
//				System.out.println(node.getAgentsDead());
//				System.out.println(node.getCost());
//				System.out.println(node.getDepth());
//				System.out.println(node.getState());
				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				agentHealths = agentHealths.substring(0, agentHealths.length() - 1);
				result += node.getAgentsDead() + ";" + agentHealths + ";" + totalExpandedNodes;

			} else {
				System.out.println("Not Found");
			}
			break;
		}
		case "GR1": {

			pq = new PriorityQueue<Node>(new NodeComparator("greedy1"));
			if (node != null) {
				// System.out.println("Found");
				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				agentHealths = agentHealths.substring(0, agentHealths.length() - 1);
				result += node.getAgentsDead() + ";" + agentHealths + ";" + totalExpandedNodes;

			} else {
				System.out.println("Not Found");
			}
			break;
		}
		case "GR2": {
			pq = new PriorityQueue<Node>(new NodeComparator("greedy2"));
			if (node != null) {
				// System.out.println("Found");
				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				agentHealths = agentHealths.substring(0, agentHealths.length() - 1);
				result += node.getAgentsDead() + ";" + agentHealths + ";" + totalExpandedNodes;

			} else {
				System.out.println("Not Found");
			}
			break;

		}
		case "AS1": {
			pq = new PriorityQueue<Node>(new NodeComparator("astar1"));
			if (node != null) {
//				System.out.println(node.getAgentsDead());
//				System.out.println(node.getCost());
				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				agentHealths = agentHealths.substring(0, agentHealths.length() - 1);
				result += node.getAgentsDead() + ";" + agentHealths + ";" + totalExpandedNodes;

			} else {
				System.out.println("Not Found");
			}
			break;
		}
		case "AS2": {
			pq = new PriorityQueue<Node>(new NodeComparator("astar2"));
			if (node != null) {
				// System.out.println(node.getAgentsDead());
				// System.out.println("Found");
				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				agentHealths = agentHealths.substring(0, agentHealths.length() - 1);
				result += node.getAgentsDead() + ";" + agentHealths + ";" + totalExpandedNodes;

			} else {
				System.out.println("Not Found");
			}
			break;
		}
		default:
			break;
		}
		return result;
	}

	public static Node ids(Node initial, int depth) {
		stack.push(initial);
		Node currentNode;
		while (!stack.isEmpty()) {
			currentNode = stack.pop();
			if (currentNode.getDepth() <= depth) {
				totalExpandedNodes++;
				if (isGoalState(currentNode)) {
					return currentNode;
				}

				if (!currentNode.visitedBefore()) {

					ArrayList<Node> expandedNodes = currentNode.expand();
					for (Node newNode : expandedNodes) {
						if (newNode.getDepth() <= depth)
							stack.push(newNode);
					}
				}
			} else {
				return null;
			}
		}
		return null;
	}
	
	public static Node dfs(Node node) {
		stack.push(node);
		while (!stack.isEmpty()) {
			Node currentNode = stack.pop();
			totalExpandedNodes++;
			if (isGoalState(currentNode))
				return currentNode;

			if (!currentNode.visitedBefore()) {
				// System.out.println(currentNode.getState());
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					stack.push(newNode);
				}
			}
		}
		return null;
	}
	
	public static Node bfs(Node node) {
		q.add(node);
		Node currentNode;
		while (!q.isEmpty()) {
			currentNode = q.remove();

			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				// System.out.println(currentNode.getState());
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					q.add(newNode);
				}
			}
			// System.out.println(q.size());
		}
		return null;
	}

	
	public static boolean isGoalState(Node currentNode) {
		return false;
	}

	public static String printSolution(Node node, String sol) {

		if (node.getParent() == null)
			return "";
		return (sol += printSolution(node.getParent(), sol) + "," + node.getDir().toString().toLowerCase());

	}

	
	
}