package code.mission;

import java.awt.Point;
import java.util.ArrayList;



public class Node {
	private String state;
	private float cost;
	private Node parent;
	private int depth;
	private Action dir;

	public Node(String state, float cost, Node parent, int depth, Action dir) {
		this.state = state;
		this.cost = cost;
		this.parent = parent;
		this.depth = depth;
		this.dir = dir;
	}

//	public float heuristic1() {
//		int xn = this.getEthanX();
//		int yn = this.getEthanY();
//		int xr = MissionImpossible.getSubmarinePosition().x;
//		int yr = MissionImpossible.getSubmarinePosition().y;
//		return Math.abs(xr - xn) + Math.abs(yr - yn);
//	}
	public float heuristic1() {
		int xn = this.getEthanX();
		int yn = this.getEthanY();

		float cost = 0;
		Point[] positions = this.getPositionsArray();
		int[] carried = this.getIsCarriedArray();
		int[] health = this.getHealthArray();
		for (int i = 0; i < health.length; i++) {
			int moves = 0;
			if (carried[i] == 0 && health[i] < 100) {
				moves = (Math.abs(positions[i].x - xn) + Math.abs(positions[i].y - yn));
				if (health[i] + (moves * 2) > 100) {
					cost += 2000;
				}
			}
		}
		return cost;
	}
	
	public float heuristic2() {
		int xn = this.getEthanX();
		int yn = this.getEthanY();

		float cost = 0;
		Point[] positions = this.getPositionsArray();
		int[] carried = this.getIsCarriedArray();
		int[] health = this.getHealthArray();
		for (int i = 0; i < health.length; i++) {
			int moves = 0;
			if (carried[i] == 0 && health[i] < 100) {
				int xSquared = (int) Math.pow(positions[i].x - xn, 2);
				int ySquared = (int) Math.pow(positions[i].y - yn, 2);
				moves = (int) Math.sqrt(xSquared + ySquared);
				if (health[i] + ((moves - 1) * 2) > 100) {
					cost += 2000;
				}
			}

		}
		// cost= count+(count*cost);
//		System.out.println(cost);
		return cost;
	}

//	public float heuristic2() {
//		int xn = this.getEthanX();
//		int yn = this.getEthanY();
//		int xr = MissionImpossible.getSubmarinePosition().x;
//		int yr = MissionImpossible.getSubmarinePosition().y;
//		int xSquared = (int) Math.pow(xr - xn, 2);
//		int ySquared = (int) Math.pow(yr - yn, 2);
//		return (float) Math.sqrt(xSquared + ySquared);
//	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Action getDir() {
		return dir;
	}

	public void setDir(Action dir) {
		this.dir = dir;
	}

	public Node generateNewNode(Action action, float cost, int depth) {
		String newState = createState(this.state, action);
		//int deathCost = 0;
		//int damageCost = 0;
		int death = 0;
		int health = 0;
		String[] carried = newState.split(";")[6].split(",");
		String searchType = MissionImpossible.getSearchType();
		if ((searchType != "BF") && (searchType != "DF") && (searchType != "ID")) {
			String healthFromNewState = newState.split(";")[5];
			String[] personalHealthFromNewState = healthFromNewState.split(",");
			for (int i = 0; i < personalHealthFromNewState.length; i++) {
				if (carried[i].equals("0") && Integer.parseInt(personalHealthFromNewState[i]) < 100) {
					health += 2;

				}
			}
			death = Integer.parseInt((newState.split(";")[3])) - this.getAgentsDead();
		}

		Node newNode = new Node(newState, (cost + (death * 2000) + health), this, depth, action);

//		System.out.println(this.getState());		
//		System.out.println(newNode.getState());
//		System.out.println(newNode.getCost());
		return newNode;
	}

	public String createState(String previousState, Action action) {
		String state = "";
		Point point;
		String[] string;
		switch (action) {
		case UP:
			point = new Point(this.getEthanX()-1, this.getEthanY());
			string = this.updateHealth().split(";");
			state = generateStateString(point, string[1], string[2], string[3], string[4], string[5], string[6]);
			break;
		case DOWN:
			point = new Point(this.getEthanX()+1, this.getEthanY() );
			string = this.updateHealth().split(";");
			state = generateStateString(point, string[1], string[2], string[3], string[4], string[5], string[6]);
			break;


		case LEFT:
			point = new Point(this.getEthanX() , this.getEthanY()-1);
			string = this.updateHealth().split(";");
			state = generateStateString(point, string[1], string[2], string[3], string[4], string[5], string[6]);
			break;

		case RIGHT:
			point = new Point(this.getEthanX() , this.getEthanY()+1);
			string = this.updateHealth().split(";");
			state = generateStateString(point, string[1], string[2], string[3], string[4], string[5], string[6]);
			break;

		case CARRY:
			// System.out.println("I Should carry");
			Point[] positions = this.getPositionsArray();
			int i = 0;
			int ethanX = this.getEthanX();
			int ethanY = this.getEthanY();
			int agentsOnTruck = this.getAgentsOnTruck();
			String isCarriedString = Node.stringfy(this.getIsCarriedArray());
			isCarriedString.substring(0, isCarriedString.length() - 1);
			String[] carried = isCarriedString.split(",");

			for (Point agentPosition : positions) {
				if (agentPosition.x == ethanX && agentPosition.y == ethanY && carried[i].equals("0") ) {
					carried[i] = "1";
					agentsOnTruck++;
					// System.out.println("HMM got EM");
				}
				i++;
			}

			String carriedString = Node.stringfy(carried);
			// System.out.println("El stringfy_____: " + isCarriedString );
			carriedString.substring(0, carriedString.length() - 1);

			string = this.updateHealth(carriedString).split(";");
			state = generateStateString(new Point(ethanX, ethanY), string[1], agentsOnTruck + "", string[3], string[4],
					string[5], carriedString);
			

			break;

		case DROP:
			int onSub = this.getAgentsOnSubmarine();
			int onTruck = this.getAgentsOnTruck();
			string = this.updateHealth().split(";");
			string[1] = onSub + onTruck + "";
			string[2] = "0";
			state = generateStateString(new Point(this.getEthanX(), this.getEthanY()), string[1], string[2], string[3],
					string[4], string[5], string[6]);
			break;
		default:

		}

		return state;
	}



	public ArrayList<Node> expand() {
		ArrayList<Node> possibleNodes = new ArrayList<Node>();
		Node newNode;
		int depth = this.getDepth();
		float cost = this.getCost();

		
		int row = MissionImpossible.getWidth();
		int column = MissionImpossible.getHeight();
		int ethanX = this.getEthanX();
		int ethanY = this.getEthanY();
		// DROP
		if (this.canDrop()) {
			if (this.getParent() != null) {
				newNode = generateNewNode(Action.DROP, (cost) , depth + 1);
				// System.out.println(newNode.getState());
				// newNode = new Node(this.state, 0, this, this.depth++, Action.DROP);
				possibleNodes.add(newNode);
				// System.out.println(newNode.getState().state);
			} else {
				newNode = generateNewNode(Action.DROP, 0, depth + 1);
				// System.out.println(newNode.getState());
				// newNode = new Node(this.state, 0, this, this.depth++, Action.DROP);
				possibleNodes.add(newNode);
				// System.out.println(newNode.getState().state);
			}

		}

		// CARRY
		if (this.canCarry()) {
			if (this.getParent() != null) {
				newNode = generateNewNode(Action.CARRY, (cost), depth + 1);
				possibleNodes.add(newNode);
				// System.out.println(newNode.getState().state);
			} else {
				newNode = generateNewNode(Action.CARRY, 0, depth + 1);
				possibleNodes.add(newNode);
				// System.out.println(newNode.getState().state);
			}
		}

		// UP
		if (ethanX >= 0 && ethanX < row) {
			if (this.getParent() != null) {
				newNode = generateNewNode(Action.DOWN, (cost) , depth + 1);
				// newNode = new Node(this.state, 0, this, this.depth++, Action.UP);
				possibleNodes.add(newNode);
				// System.out.println(newNode);
			} else {
				newNode = generateNewNode(Action.DOWN, 0, depth + 1);
				// newNode = new Node(this.state, 0, this, this.depth++, Action.UP);
				possibleNodes.add(newNode);
				// System.out.println(newNode);
			}

		}

		// DOWN
		if (ethanX <= row && ethanX> 0) {
			if (this.getParent() != null) {
				// System.out.println("ethan Y location: "+this.getEthanY());
				newNode = generateNewNode(Action.UP, (cost) , depth + 1);
				// newNode = new Node(this.state, 0, this, this.depth++, Action.DOWN);
				possibleNodes.add(newNode);
				// System.out.println(newNode.getState().state);
			} else {
				// System.out.println("ethan Y location: "+this.getEthanY());
				newNode = generateNewNode(Action.UP, 0, depth + 1);
				// newNode = new Node(this.state, 0, this, this.depth++, Action.DOWN);
				possibleNodes.add(newNode);
				// System.out.println(newNode.getState().state);
			}
		}

		// LEFT
		if (ethanY > 0 && ethanY<= column) {
			if (this.getParent() != null) {
				newNode = generateNewNode(Action.LEFT, (cost) , depth + 1);
				// newNode = new Node(this.state, 0, this, this.depth++, Action.LEFT);
				possibleNodes.add(newNode);
				// System.out.println(newNode.getState().state);
			} else {
				newNode = generateNewNode(Action.LEFT, 0, depth + 1);
				// newNode = new Node(this.state, 0, this, this.depth++, Action.LEFT);
				possibleNodes.add(newNode);
				// System.out.println(newNode.getState().state);
			}
		}

		// RIGHT
		if (ethanY < column && ethanY >= 0) {
			if (this.getParent() != null) {
				newNode = generateNewNode(Action.RIGHT, (cost) , depth + 1);
				// newNode = new Node(this.state, 0, this, this.depth++, Action.RIGHT);
				possibleNodes.add(newNode);
				// System.out.println(newNode.getState().state);
			} else {
				newNode = generateNewNode(Action.RIGHT, 0, depth + 1);
				// newNode = new Node(this.state, 0, this, this.depth++, Action.RIGHT);
				possibleNodes.add(newNode);
				// System.out.println(newNode.getState().state);
			}
		}

		return possibleNodes;
	}

	public boolean visitedBefore() {
		String[] state = this.state.split(";");
		String key = "";
		
		for (int i = 0; i < state.length; i++) {
			if (!(i == 3 || i == 4 || i == 5)) {
				String[] content = state[i].split(",");
				for (int j = 0; j < content.length; j++) {
					if (j == 0)
						key += content[j];
					else
						key += "," + content[j];
				}
				key += ";";
			}

		}
		key = key.substring(0, key.length() - 1);
		
		// System.out.println(key);
		if (MissionImpossible.states.get(key) == null) {
			MissionImpossible.states.put(key, true);
			return false;
		}
		return true;
	}

	public boolean canDrop() {
		Point subPosition = MissionImpossible.getSubmarinePosition();
		boolean flag = false;
		if (this.getEthanX() == subPosition.x && this.getEthanY() == subPosition.y && this.getAgentsOnTruck() > 0)
			flag = true;
		return flag;
	}

	public boolean canCarry() {
		Point[] positions = this.getPositionsArray();
		int i = 0;
		for (Point agentPosition : positions) {
			if (agentPosition.x == getEthanX() && agentPosition.y == getEthanY() && this.getIsCarriedArray()[i] == 0
					&& MissionImpossible.initialCapacity > this.getAgentsOnTruck())
				return true;
			i++;
		}
		return false;
	}

	public int getEthanX() {
		// System.out.println("Getting Ethan X : " + this.state);
		String ethanPos = this.state.split(";")[0];
		return Integer.parseInt(ethanPos.split(",")[0]);
	}

	public int getEthanY() {
		String ethanPos = this.state.split(";")[0];
		return Integer.parseInt(ethanPos.split(",")[1]);
	}

	public int getAgentsOnSubmarine() {
		String agentsOnSubmarine = this.state.split(";")[1];
		return Integer.parseInt(agentsOnSubmarine);
	}

	public int getAgentsOnTruck() {
		String agentsOnTruck = this.state.split(";")[2];
		return Integer.parseInt(agentsOnTruck);
	}

	public int getAgentsDead() {
		String agentsDead = this.state.split(";")[3];
		return Integer.parseInt(agentsDead);
	}

	public String incrementAgentsDead() {
		Point ethanPos = new Point(getEthanX(), getEthanY());
		int deadAgents = this.getAgentsDead() + 1;
		String state = generateStateString(ethanPos, getAgentsOnSubmarine(), getAgentsOnTruck(), deadAgents,
				getPositionsArray(), getHealthArray(), getIsCarriedArray());
		return state;
	}

	public Point[] getPositionsArray() {
		String positionsFromState = this.state.split(";")[4];
		String[] individualPositionString = positionsFromState.split(",");
		String res = "";
		Point[] positions = new Point[individualPositionString.length / 2];
		int j = 0;
		for (int i = 0; i < positions.length; i++) {
			int x = Integer.parseInt(individualPositionString[j]);
			int y = Integer.parseInt(individualPositionString[j + 1]);
			positions[i] = new Point(x, y);
			j += 2;
		}
		return positions;
	}

	public int[] getHealthArray() {
		String healthFromState = this.state.split(";")[5];
		String[] individualHealthString = healthFromState.split(",");
		int[] healths = new int[individualHealthString.length];
		for (int i = 0; i < healths.length; i++) {
			healths[i] = Integer.parseInt(individualHealthString[i]);
		}
		return healths;

	}

	// 1 for carried, 0 for not
	public int[] getIsCarriedArray() {
		String isCarriedFromState = this.state.split(";")[6];
		String[] individualIsCarriedStrings = isCarriedFromState.split(",");
		int[] areCarried = new int[individualIsCarriedStrings.length];
		for (int i = 0; i < areCarried.length; i++) {
			areCarried[i] = Integer.parseInt(individualIsCarriedStrings[i]);
		}
		return areCarried;
	}

	public static String generateStateString(Point ethanPos, int onSubmarine, int onTruck, int deaths,
			Point[] positionArray, int[] healthArray, int[] isCarriedArray) {
		String res = ethanPos.x + "," + ethanPos.y + ";";
		res += onSubmarine + ";" + onTruck + ";" + deaths + ";";

		// handle position array
		res += stringfy(positionArray) + ";";

		// handle health array
		res += stringfy(healthArray) + ";";

		// handle carried array
		res += stringfy(isCarriedArray) + ";";

		return res;
	}

	// overloaded for string arrays
	public static String generateStateString(String[] ethanPos, int onSubmarine, int onTruck, int deaths,
			String[] positionArray, String[] healthArray, String[] isCarriedArray) {
		String res = ethanPos[0] + "," + ethanPos[1] + ";";
		res += onSubmarine + ";" + onTruck + ";" + deaths + ";";

		// handle position array
		res += stringfy(positionArray) + ";";

		// handle health array
		res += stringfy(healthArray) + ";";

		// handle carried array
		res += stringfy(isCarriedArray);

		return res;

	}

	public static String generateStateString(Point ethanPos, String onSubmarine, String onTruck, String deaths,
			String positionArray, String healthArray, String isCarriedArray) {
		String res = ethanPos.x + "," + ethanPos.y + ";";
		res += onSubmarine + ";" + onTruck + ";" + deaths + ";" + positionArray + ";" + healthArray + ";"
				+ isCarriedArray;
		return res;
	}

	public static String stringfy(String[] stringArray) {
		String res = "";
		for (String string : stringArray) {
			res += string + ",";
		}
		res = res.substring(0, res.length() - 1);
		return res;
	}

	public static String stringfy(Point[] pointArray) {
		String res = "";
		for (Point point : pointArray) {
			res += point.x + "," + point.y + ",";
		}
		res = res.substring(0, res.length() - 1);
		return res;
	}

	public static String stringfy(int[] intArray) {
		String res = "";
		for (int number : intArray) {
			res += number + ",";
		}
		res = res.substring(0, res.length() - 1);
		return res;
	}

	// Must be done after carry if carrying is to take place;
	public String updateHealth() {
		int[] healths = this.getHealthArray();
		int[] isCarried = this.getIsCarriedArray();
		int diedThisStep = 0;
		for (int i = 0; i < healths.length; i++) {
			if (healths[i] < 100 && isCarried[i] < 1) {
				healths[i] += 2;
				if (healths[i] >= 100) {
					healths[i] = 100;
					diedThisStep++;
				}
			}
		}
		return generateStateString(new Point(this.getEthanX(), this.getEthanY()), this.getAgentsOnSubmarine(),
				this.getAgentsOnTruck(), this.getAgentsDead() + diedThisStep, this.getPositionsArray(), healths,
				isCarried);
	}
	private String updateHealth(String carriedString) {
		int[] healths = this.getHealthArray();
		String[] isCarried = carriedString.split(",");
		int [] carriedAgent = new int[isCarried.length];
		for(int i = 0; i<healths.length;i++) {
			if(isCarried[i].equals("1") )
				carriedAgent[i] = 1;
			else {
				carriedAgent[i]= 0; 
			}
		}
		int diedThisStep = 0;
		for (int i = 0; i < healths.length; i++) {
			if (healths[i] < 100 && carriedAgent[i] < 1) {
				healths[i] += 2;
				if (healths[i] >= 100) {
					healths[i] = 100;
					diedThisStep++;
				}
			}
		}
		return generateStateString(new Point(this.getEthanX(), this.getEthanY()), this.getAgentsOnSubmarine(),
				this.getAgentsOnTruck(), this.getAgentsDead() + diedThisStep, this.getPositionsArray(), healths,
				carriedAgent);
}
	public String toString() {
		return this.cost+"";
	}


}
