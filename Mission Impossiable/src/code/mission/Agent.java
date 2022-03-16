package code.mission;

import java.awt.Point;




public class Agent extends Unit {
	private int damage;
	private CurrentStatus currentStatus;
	private boolean isDead;

	// Constructor
	// Might be removed
	
	public Agent(int x, int y) {
		super(x, y);
		damage = (int) (Math.random() * 98 + 1);
		this.currentStatus = CurrentStatus.NOTFOUND;
		this.isDead = false;
	}

	public Agent(int x, int y, int damage) {
		super(x, y);
		this.damage = damage;
		this.currentStatus = CurrentStatus.NOTFOUND;
		if(damage >= 100)
			isDead = true;
		else 
			isDead = false;
	}

	public Agent(int x, int y, int damage, CurrentStatus currentStatus) {
		super(x, y);
		this.damage = damage;
		this.currentStatus = currentStatus;
		if(damage >= 100)
			this.isDead = true;
		else 
			this.isDead = false;
	}
	
	



	public CurrentStatus getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentState(CurrentStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	public int getDamage() {
		return this.damage;
	}

	public void takeDamage() {
		if (this.damage < 100)
			this.handleTakeDamage();
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	private void handleTakeDamage() {
		if (this.currentStatus == CurrentStatus.NOTFOUND) {
			this.damage += 2;
			if (this.damage >= 100) {
				this.damage = 100;
				//MissionImpossible.incrementDeaths(); // Would need to rewrite
				this.isDead = true;
			}
		}
	}

	public Agent cloneAgent() {
		Agent newAgent = new Agent(this.getX(), this.getY(), this.getDamage(), this.getCurrentStatus());
		return newAgent;
	}
	
	public String toString() {
		String str = "";
		str += "	Location: " +  this.getX() + ", " + this.getY() +"\n"
				+ "	Damage: " + this.damage +"\n"
				+ "	Current Status: " + this.currentStatus+"\n"
				+ "	Dead: " + this.isDead +"\n";
		return str;
	}
	
	public Point getLocation() {
		return new Point(getX(), getY());
	}

}
