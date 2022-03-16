package code.mission;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import code.generic.GUI;
import code.generic.SearchProblem;

public class MissionImpossible extends SearchProblem {
	
	private static String stringGrid;
	private static Grid graphicalGrid;
	public static Point submarinePosition;
	public static int initialCapacity;
	private static int width;
	private static int height;
	public static Ethan ethan;
	public static Submarine sub;
	public static GUI gui;

	public static int getNumberOfAgents() {
		return numberOfAgents;
	}


	public static String getSearchType() {
		return searchType;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static Point getSubmarinePosition() {
		return new Point(sub.getX(), sub.getY());
	}

	public static Grid getGraphicalGrid() {
		return graphicalGrid;
	}

	private static String genGrid() {

		// Grid Size (m,n)
		int m = (int) (Math.random() * 10 + 5);
		int n = (int) (Math.random() * 10 + 5);
		graphicalGrid = new Grid(m, n);

		int agents = (int) (Math.random() * 5 + 5);
		int c = (int) (Math.random() * agents + 1);

		// Ethan Generation
		int eX = (int) (Math.random() * m);
		int eY = (int) (Math.random() * n);
		Ethan ethan = new Ethan(eX, eY, c);
		graphicalGrid.addToGrid(ethan);

		// Submarine Generation
		int sX, sY;
		do {
			sX = (int) (Math.random() * m);
			sY = (int) (Math.random() * n);
		} while (sX == eX && sY == eX);
		Submarine sub = new Submarine(sX, sY);
		graphicalGrid.addToGrid(sub);

		String str = m + "," + n + ";" + eX + "," + eY + ";" + sX + "," + sY + ";";

		// IMF Agent Generation
		int[] agentDamage = new int[agents];
		for (int i = 0; i < agents; i++) {
			int x = (int) (Math.random() * m);
			int y = (int) (Math.random() * n);
			if (graphicalGrid.getGrid()[x][y] == null) {
				Agent agent = new Agent(x, y);
				graphicalGrid.addToGrid(agent);
				agentDamage[i] = agent.getDamage();
				if (i == agents - 1)
					str += agent.getX() + "," + agent.getY() + ";";
				else
					str += agent.getX() + "," + agent.getY() + ",";
			} else
				i--;
		}
		for (int i = 0; i < agents; i++) {
			if (i == agents - 1)
				str += agentDamage[i] + ";";
			else
				str += agentDamage[i] + ",";
		}
		str += c;

		stringGrid = str;
		System.out.println(graphicalGrid);
		return stringGrid;
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();

		String xString = solve("17,7;1,6;5,4;2,2,1,4,0,3,2,3,0,1,4,5;6,44,82,49,24,54;4", "BF", true);
		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;
		System.out.println("total nodes expanded: " + totalExpandedNodes);
		System.out.println("Execution time in seconds : " + timeElapsed / 1000000000);
		System.out.println(xString);

	}

	public static String solve(String grid, String strategy, boolean visualize) {
		states = new HashMap<String, Boolean>();
		stack = new Stack<>();
		q = new LinkedList<>();
		String[] str = grid.split(";");
		String[] widthAndHeight = str[0].split(",");
		String[] ethanPos = str[1].split(",");
		String[] submarinePos = str[2].split(",");
		String[] agentsPos = str[3].split(",");
		String[] health = str[4].split(",");
		//////////////////////////////////////////
		stringGrid = grid;
		graphicalGrid = new Grid(Integer.parseInt(widthAndHeight[0]), Integer.parseInt(widthAndHeight[1]));

		ethan = new Ethan(Integer.parseInt(ethanPos[0]), Integer.parseInt(ethanPos[1]), Integer.parseInt(str[5]));
		graphicalGrid.addToGrid(ethan);

		sub = new Submarine(Integer.parseInt(submarinePos[0]), Integer.parseInt(submarinePos[1]));
		graphicalGrid.addToGrid(sub);
		numberOfAgents = agentsPos.length / 2;

		int j = 0;
		for (int i = 0; i < agentsPos.length; i += 2) {
			int x = Integer.parseInt(agentsPos[i]);
			int y = Integer.parseInt(agentsPos[i + 1]);
			if (graphicalGrid.getGrid()[x][y] == null) {
				Agent agent = new Agent(x, y, Integer.parseInt(health[j]));
				graphicalGrid.addToGrid(agent);
			}
			j++;
		}
		// System.out.println(graphicalGrid);
		/////////////////////////////////////////
		searchType = strategy;

		initialCapacity = Integer.parseInt(str[5]);
		width = Integer.parseInt(widthAndHeight[0]) - 1;
		height = Integer.parseInt(widthAndHeight[1]) - 1;

		// create the isCarried Array for state object
		String[] isCarriedArray = new String[numberOfAgents];
		for (int i = 0; i < numberOfAgents; i++) {
			isCarriedArray[i] = "0";
		}
		String initialState = Node.generateStateString(ethanPos, 0, 0, 0, agentsPos, health, isCarriedArray);
		Node initial = new Node(initialState, 0, null, 0, Action.INITIAL);
		// System.out.println("the initial state string is equal "+initial);
		// System.out.println("the initial state string is equal "+initial.getState());
		String result = "";
		if (visualize) {
			gui = new GUI(height, width);
		}
		switch (strategy) {
		case "BF": {
			Node node;
			if(visualize)
				node= bfs(initial, true);
			else 
				node= bfs(initial);
			if (node != null) {

				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				if(visualize) {
					gui.showPath(result);
				}
				agentHealths = agentHealths.substring(0, agentHealths.length() - 1);
				result += node.getAgentsDead() + ";" + agentHealths + ";" + totalExpandedNodes;

			} else {
				System.out.println("Not Found");
			}
			break;
		}
		case "DF": {
			Node node;
			if(visualize)
				node = dfs(initial, true);
			else	
				node= dfs(initial);
			if (node != null) {
				// System.out.println("Found");

				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				if(visualize) {
					gui.showPath(result);
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
			Node node;
			if(visualize)
				node = ids(initial, depth, true);
			else	
				node= ids(initial, depth);
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
			if(visualize) {
				gui.showPath(result);
			}
			agentHealths = agentHealths.substring(0, agentHealths.length() - 1);
			result += node.getAgentsDead() + ";" + agentHealths + ";" + totalExpandedNodes;
			break;
		}
		case "UC": {
			pq = new PriorityQueue<Node>(new NodeComparator("ucs"));
			Node node;
			if(visualize)
				node = ucs(initial, true);
			else	
				node= ucs(initial);
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
				if(visualize) {
					gui.showPath(result);
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
			Node node;
			if(visualize)
				node = greedy1(initial, true);
			else	
				node= greedy1(initial);
			if (node != null) {
				// System.out.println("Found");
				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				if(visualize) {
					gui.showPath(result);
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
			Node node;
			if(visualize)
				node = greedy2(initial, true);
			else	
				node= greedy2(initial);
			if (node != null) {
				// System.out.println("Found");
				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				if(visualize) {
					gui.showPath(result);
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
			Node node;
			if (visualize)
				node = astar1(initial, true);
			else
				node = astar1(initial);
			if (node != null) {
//				System.out.println(node.getAgentsDead());
//				System.out.println(node.getCost());
				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				if(visualize) {
					gui.showPath(result);
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
			Node node;
			if (visualize)
				node = astar2(initial, true);
			else
				node = astar2(initial);
			if (node != null) {
				// System.out.println(node.getAgentsDead());
				// System.out.println("Found");
				result = printSolution(node, "").substring(1) + ";";
				int[] totalHealth = node.getHealthArray();
				String agentHealths = "";
				for (int agentHealth : totalHealth) {
					agentHealths += agentHealth + ",";
				}
				if(visualize) {
					gui.showPath(result);
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

	
	private static Node ids(Node initial, int depth, boolean visualize) {
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
					gui.expand(currentNode);
					gui.repaint();
					
					

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

	
	
	public static Node dfs(Node node, boolean visualize) {
		stack.push(node);
		while (!stack.isEmpty()) {
			Node currentNode = stack.pop();
			totalExpandedNodes++;
			if (isGoalState(currentNode))
				return currentNode;

			if (!currentNode.visitedBefore()) {
				gui.expand(currentNode);
				gui.repaint();
				
				// System.out.println(currentNode.getState());
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					stack.push(newNode);
				}
			}
		}
		return null;
	}
	
	public static Node bfs(Node node, boolean Visualize) {
		q.add(node);
		Node currentNode;
		while (!q.isEmpty()) {
			currentNode = q.remove();

			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				gui.expand(currentNode);
				gui.repaint();
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

	public static Node ucs(Node node) {
		pq.add(node);

		Node currentNode;
		while (!pq.isEmpty()) {

			currentNode = pq.remove();
			// System.out.println(currentNode.getCost());
			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					pq.add(newNode);
				}
			}
		}
		return null;
	}
	
	public static Node ucs(Node node, boolean visualize) {
		pq.add(node);

		Node currentNode;
		while (!pq.isEmpty()) {

			currentNode = pq.remove();
			// System.out.println(currentNode.getCost());
			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				gui.expand(currentNode);
				gui.repaint();
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					pq.add(newNode);
				}
			}
		}
		return null;
	}

	public static Node greedy1(Node node) {
		pq.add(node);
		//System.out.println(node);
		Node currentNode;
		while (!pq.isEmpty()) {
			currentNode = pq.remove();

			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					pq.add(newNode);
				}
			}
		}
		return null;
	}
	
	public static Node greedy1(Node node, boolean visualize) {
		pq.add(node);
		//System.out.println(node);
		Node currentNode;
		while (!pq.isEmpty()) {
			currentNode = pq.remove();

			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				gui.expand(currentNode);
				gui.repaint();
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					pq.add(newNode);
				}
			}
		}
		return null;
	}

	public static Node greedy2(Node node) {
		pq.add(node);
		Node currentNode;
		while (!pq.isEmpty()) {
			currentNode = pq.remove();

			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					pq.add(newNode);
				}
			}
		}
		return null;
	}
	
	public static Node greedy2(Node node, boolean visualize) {
		pq.add(node);
		Node currentNode;
		while (!pq.isEmpty()) {
			currentNode = pq.remove();

			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				gui.expand(currentNode);
				gui.repaint();
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					pq.add(newNode);
				}
			}
		}
		return null;
	}

	public static Node astar1(Node node) {
		pq.add(node);
		Node currentNode;
		while (!pq.isEmpty()) {
			currentNode = pq.remove();
			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					pq.add(newNode);
				}
			}
		}
		return null;
	}

	public static Node astar1(Node node, boolean visualize) {
		pq.add(node);
		Node currentNode;
		while (!pq.isEmpty()) {
			currentNode = pq.remove();
			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				ArrayList<Node> expandedNodes = currentNode.expand();
				gui.expand(currentNode);
				gui.repaint();
				
			
				for (Node newNode : expandedNodes) {
					pq.add(newNode);
				}
			}
		}
		return null;
	}

	public static Node astar2(Node node) {
		pq.add(node);
		Node currentNode;
		while (!pq.isEmpty()) {
			currentNode = pq.remove();

			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					pq.add(newNode);
				}
			}
		}
		return null;
	}

	public static Node astar2(Node node, boolean visualize) {
		pq.add(node);
		Node currentNode;
		while (!pq.isEmpty()) {
			currentNode = pq.remove();

			totalExpandedNodes++;
			if (isGoalState(currentNode)) {
				return currentNode;
			}

			if (!currentNode.visitedBefore()) {
				gui.expand(currentNode);
				gui.repaint();
				
				ArrayList<Node> expandedNodes = currentNode.expand();
				for (Node newNode : expandedNodes) {
					pq.add(newNode);
				}
			}
		}
		return null;
	}

	public static boolean isGoalState(Node node) {
		if (node != null) {
			int onSubmarine = node.getAgentsOnSubmarine();
			if (onSubmarine == MissionImpossible.numberOfAgents && node.getEthanX() == sub.getX()
					&& node.getEthanY() == sub.getY())
				return true;
			return false;
		}
		return false;

	}

}
