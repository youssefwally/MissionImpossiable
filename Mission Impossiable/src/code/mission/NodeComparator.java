package code.mission;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

	String operation = "";

	public NodeComparator(String operation) {
		this.operation = operation;
	}

	public int compare(Node node1, Node node2) {
		float n1;
		float n2;
		switch (operation) {
		case "ucs": {
			if (node1.getCost() > node2.getCost()) {
				return 1;
			} else {
				if (node1.getCost() < node2.getCost()) {
					return -1;
				}
				return 0;
			}
		}
		case "greedy1": {
			n1 = !(MissionImpossible.isGoalState(node1)) ? node1.heuristic1() : 0;
			n2 = !(MissionImpossible.isGoalState(node2)) ? node2.heuristic1() : 0;
			if (n1 > n2) {
				return 1;
			} else {
				if (n1 < n2) {
					return -1;
				}
			}

			return 0;
		}
		case "greedy2": {
			n1 = MissionImpossible.isGoalState(node1) ? 0 : node1.heuristic2();
			n2 = MissionImpossible.isGoalState(node2) ? 0 : node2.heuristic2();
			if (n1 > n2) {
				return 1;
			} else {
				if (n1 < n2) {
					return -1;
				}
			}

			return 0;
		}
		case "astar1": {
			n1 = MissionImpossible.isGoalState(node1) ? node1.getCost() : node1.heuristic1() + node1.getCost();
			n2 = MissionImpossible.isGoalState(node2) ? node2.getCost() : node2.heuristic1() + node2.getCost();
			if (n1 > n2) {
				return 1;
			} else {
				if (n1 < n2) {
					return -1;
				}
			}

			return 0;
		}
		case "astar2": {
			n1 = MissionImpossible.isGoalState(node1) ? node1.getCost() : node1.heuristic2() + node1.getCost();
			n2 = MissionImpossible.isGoalState(node2) ? node2.getCost() : node2.heuristic2() + node2.getCost();
			if (n1 > n2) {
				return 1;
			} else {
				if (n1 < n2) {
					return -1;
				}
			}
			return 0;
		}
		default: {
			return 0;
		}

		}
	}

}
