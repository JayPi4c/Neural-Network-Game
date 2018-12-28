package com.JayPi4c.Entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.JayPi4c.Game;
import com.JayPi4c.util.Vector;

public class Fireball {

	// -------------------------ATTRIBUTES--------------------------//

	Vector pos, vel;
	double diameter;

	// -------------------------CONSTRUCTOR-------------------------//

	public Fireball(double x, double y, Vector vel) {
		this.pos = new Vector(x, y);
		this.vel = vel;
		this.diameter = 6;
	}

	// -------------------------GAME-ENGINE-------------------------//

	// -----------------------PHYSICS-ENGINE------------------------//

	public void update() {
		this.pos.add(this.vel);
	}

	// -----------------------DESIGN-FUNCTIONS----------------------//

	public void show(Graphics2D g) {
		g.setColor(new Color(255, 127, 80));
		g.fillOval((int) (this.pos.x - this.diameter * 0.5), (int) (this.pos.y - this.diameter * 0.5),
				(int) this.diameter, (int) this.diameter);
	}

	public boolean isOffscreen() {
		return this.pos.x + this.diameter * 0.5 < 0 || this.pos.x - this.diameter * 0.5 > Game.WIDTH
				|| this.pos.y + this.diameter * 0.5 < 0 || this.pos.y - this.diameter * 0.5 > Game.HEIGHT;
	}

	// ---------------------------HELPER----------------------------//

	public Vector getPosition() {
		return this.pos.copy();
	}
}