package com.JayPi4c.Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.JayPi4c.Game;
import com.JayPi4c.util.Vector;

public class Asteroid {

	// -------------------------ATTRIBUTES--------------------------//

	Vector pos, vel;
	double diameter;
	int stage;

	// -------------------------CONSTRUCTOR-------------------------//

	public Asteroid(double x, double y, Vector vel, double diameter, int stage) {
		this.pos = new Vector(x, y);
		this.vel = vel;
		this.diameter = diameter;
		this.stage = stage;
	}

	// -------------------------GAME-ENGINE-------------------------//

	public boolean collidesFireball(ArrayList<Fireball> fireballs) {
		for (int i = 0; i < fireballs.size(); i++) {
			Vector ballPos = fireballs.get(i).getPosition();
			if (ballPos.dist(this.pos) < this.diameter * 0.5) {
				fireballs.remove(i);
				return true;
			}
		}
		return false;
	}

	// -----------------------PHYSICS-ENGINE------------------------//

	public void update() {
		this.pos.add(this.vel);
		edges();
	}

	// -----------------------DESIGN-FUNCTIONS----------------------//

	public void show(Graphics2D g) {
		g.setColor(new Color(0, 0, 0));
		g.drawOval((int) (this.pos.x - this.diameter * 0.5), (int) (this.pos.y - this.diameter * 0.5),
				(int) this.diameter, (int) this.diameter);
	}

	private void edges() {
		if (this.pos.x - this.diameter * 0.5 > Game.WIDTH)
			this.pos.x = -this.diameter * 0.5;
		if (this.pos.x + this.diameter * 0.5 < 0)
			this.pos.x = Game.WIDTH + this.diameter * 0.5;
		if (this.pos.y - this.diameter * 0.5 > Game.HEIGHT)
			this.pos.y = -this.diameter * 0.5;
		if (this.pos.y + this.diameter * 0.5 < 0)
			this.pos.y = Game.HEIGHT + this.diameter * 0.5;
	}

	// ---------------------------HELPER----------------------------//

	public int getStage() {
		return this.stage;
	}

	public Vector getPosition() {
		return this.pos.copy();
	}

	public double getDiameter() {
		return this.diameter;
	}
}