package code.mission;



public class Grid {
	private Unit[][] grid;
	private int rows;
	private int columns;

	public Grid(int x, int y) {
		grid = new Unit[x][y];
		rows = x;
		columns = y;
	}

	public Unit[][] getGrid() {
		return this.grid;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public int getColumns() {
		return this.columns;
	}
	
	public void addToGrid(Unit unit) {
		int x = unit.getX();
		int y = unit.getY();

		this.grid[x][y] = unit;
	}

	public String toString() {
		String str = "";
		int i  = 0;
		for (Unit[] row : this.grid) {

			for (Unit cell : row) {

				if (cell == null)
					str += "       |";
				else if (cell instanceof Agent) {
					if (((Agent) (cell)).getDamage() < 10)
						str += " F(0" + ((Agent) (cell)).getDamage() + ") |";
					else
						str += " F(" + ((Agent) (cell)).getDamage() + ") |";
				} else if (cell instanceof Ethan) {
					str += "   E   |";
				} else if(cell instanceof Submarine) {
					str += "   S   |";
				}
			}
			str += "    :" + i++ +":\n";
		}
		return str;
	}
}
