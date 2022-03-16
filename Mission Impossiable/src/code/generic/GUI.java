package code.generic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import code.mission.Agent;
import code.mission.Ethan;
import code.mission.MissionImpossible;
import code.mission.Node;
import code.mission.Submarine;
import code.mission.Unit;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	MIMap map;
	Unit[][] grid = MissionImpossible.getGraphicalGrid().getGrid();

	// int m, int n, Point ethanPosition, Point submarinePos, ArrayList<Agent>
	// agentsArrayList
	public GUI(int m, int n) {
		this.setTitle("AI Team 45");
		this.setSize((m + 1) * 63 + 50, (n + 1) * 63 + 120);
		this.setLocation(500, 20);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		MIMap map = new MIMap(m + 1, n + 1);
		this.map = map;
		this.setContentPane(map);
	}

	public void expand(Node n) {
		this.map.expand(n);

	}

	public void showPath(String string) {
		// System.out.println("HELLO " + string);
		this.map.showPath(string);
	}

	public class MIMap extends JPanel {
		int m;
		int n;
		JLabel[][] labels;
		boolean[][] expanded;
		boolean[][] isPath;
		boolean[][] carry;
		int ethanPosX;
		int ethanPosY;
		// public Unit[][] grid = MissionImpossible.getGraphicalGrid().getGrid();

		public MIMap(int m, int n) {
			this.ethanPosX = MissionImpossible.ethan.getX();
			this.ethanPosY = MissionImpossible.ethan.getY();
			this.expanded = new boolean[m][n];
			this.isPath = new boolean[m][n];
			this.carry = new boolean[m][n];

			this.m = m;
			this.n = n;
			this.labels = new JLabel[m][n];
			for (int i = 0; i < m; i++) {
				for (int j = 0; j < n; j++) {
					labels[i][j] = new JLabel();
					this.add(labels[i][j]);
					this.expanded[i][j] = false;
					this.isPath[i][j] = false;
					this.carry[i][j] = false;
				}
			}
		}

		public void expand(Node n) {
			int x = n.getEthanX();
			int y = n.getEthanY();
			this.expanded[y][x] = true;
		}

		public void showPath(String string) {
			String[] path = string.split(",");
			int spacing = 3;
			int width = 60;

			for (String action : path) {
				switch (action) {
				case "down":
					ethanPosX++;
					this.isPath[ethanPosY][ethanPosX] = true;
					break;
				case "up":
					ethanPosX--;
					this.isPath[ethanPosY][ethanPosX] = true;
					break;
				case "left":
					ethanPosY--;
					this.isPath[ethanPosY][ethanPosX] = true;
					break;
				case "right":
					ethanPosY++;
					this.isPath[ethanPosY][ethanPosX] = true;
					break;
				case "carry":
					this.carry[ethanPosY][ethanPosX] = true;
					grid[ethanPosY][ethanPosX] = null;
					break;
				default:
					break;
				}

				revalidate();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// zabat el health
				for (int i = 0; i < this.m; i++) {
					for (int j = 0; j < this.n; j++) {
						String textString = labels[i][j].getText();
						if (textString.length() > 0 && !textString.equals("Ethan") && !textString.equals("Sub")) {
							int damage = Integer.parseInt(textString);
							// System.out.println(health);
							damage += 2;
							if (damage > 100)
								damage = 100;

							if (damage < 10)
								textString = "0" + damage;
							else
								textString = damage + "";

							if (grid[j][i] != null) {
								((Agent) (grid[j][i])).takeDamage();
								labels[i][j].setBounds(spacing + i * width + width - 9, spacing + j * width + width,
										width - spacing, width - spacing);
								if (damage > 10)
									labels[i][j].setText(damage + "");
								else
									labels[i][j].setText("0" + damage);
								labels[i][j].setVisible(true);
							} else {
								labels[i][j].setVisible(false);
							}

						}
					}
				}
				repaint();
				revalidate();
			}
		}

		public void paintComponent(Graphics g) {
			int spacing = 3;
			int width = 60;

			// boolean[][] carry = new boolean[m][n];

			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, 3000, 3000);
			g.setColor(Color.gray);

			// Create grid

			for (int i = 0; i < this.m; i++) {
				for (int j = 0; j < this.n; j++) {
					g.setColor(Color.gray);
					if (i != ethanPosY || j != ethanPosX)
						g.setColor(Color.gray);
					else
						g.setColor(Color.cyan);
					// g.setColor(Color.gray);
					g.fillRect(spacing + i * width + width / 2, spacing + j * width + width, width - spacing,
							width - spacing);
				}
			}

			for (int i = 0; i < this.m; i++) {
				for (int j = 0; j < this.n; j++) {
					if (grid[j][i] instanceof Agent && !carry[i][j]) {
						g.setColor(Color.magenta);
						g.fillRect(spacing + i * width + width / 2, spacing + j * width + width, width - spacing,
								width - spacing);
						int damage = ((Agent) (grid[j][i])).getDamage();
						JLabel label = labels[i][j];
						label.setBounds(spacing + i * width + width - 9, spacing + j * width + width, width - spacing,
								width - spacing);
						if (damage > 10)
							label.setText(damage + "");
						else
							label.setText("0" + damage);
						label.setVisible(true);
					}else {
						labels[i][j].setVisible(false);
					}
//					if (grid[j][i] instanceof Ethan) {
//						g.setColor(Color.cyan);
//						g.fillRect(spacing + i * width + width / 2, spacing + j * width + width, width - spacing,
//								width - spacing);
//
//						JLabel label = labels[i][j];
//						label.setBounds(spacing + i * width + width - 16, spacing + j * width + width, width - spacing,
//								width - spacing);
//						label.setText("Ethan");
//						label.setVisible(true);
//					}
					if (grid[j][i] instanceof Submarine) {
						g.setColor(Color.GREEN);
						g.fillRect(spacing + i * width + width / 2, spacing + j * width + width, width - spacing,
								width - spacing);
						JLabel label = labels[i][j];
						label.setBounds(spacing + i * width + width - 13, spacing + j * width + width, width - spacing,
								width - spacing);
						label.setText("Sub");
						label.setVisible(true);
					}

					if (!(i != ethanPosY || j != ethanPosX)) {

						g.setColor(Color.cyan);
						// g.setColor(Color.gray);
						g.fillRect(spacing + i * width + width / 2, spacing + j * width + width, width - spacing,
								width - spacing);
					}
				}
			}

		}
	}

}
