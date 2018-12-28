package com.JayPi4c.Entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.JayPi4c.util.Vector;

abstract class Item {

	// -------------------------ATTRIBUTES--------------------------//

	Vector pos;
	double diameter = 0;
	Color c = new Color(255, 192, 203);

	// -------------------------CONSTRUCTOR-------------------------//

	public Item(double x, double y) {
		this.pos = new Vector(x, y);
	}

	// -------------------------GAME-ENGINE-------------------------//

	// -----------------------PHYSICS-ENGINE------------------------//

	// -----------------------DESIGN-FUNCTIONS----------------------//

	public void show(Graphics2D g) {
		g.setColor(c);
		g.fillOval((int) (this.pos.x - this.diameter * 0.5), (int) (this.pos.y - this.diameter * 0.5),
				(int) this.diameter, (int) this.diameter);
	}

	// ---------------------------HELPER----------------------------//

	public Vector getPosition() {
		return this.pos.copy();
	}
}
