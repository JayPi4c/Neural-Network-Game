package com.JayPi4c.Entity;

import java.awt.Color;

public class Ammunition extends Item {

	// -------------------------CONSTRUCTOR-------------------------//

	public Ammunition(double d, double e) {
		super(d, e);
		this.c = new Color(100, 100, 100);
		this.diameter = 3;
	}
}
