package com.JayPi4c.Entity;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

public class MediPack extends Item {

// -------------------------ATTRIBUTES--------------------------//

	double healthValue;

// -------------------------CONSTRUCTOR-------------------------//

	public MediPack(double x, double y) {
		super(x, y);
		this.c = new Color(20, 255, 20);
		this.diameter = 3;
		this.healthValue = ThreadLocalRandom.current().nextDouble(5);
	}

// ---------------------------HELPER----------------------------//

	public double getHealthValue() {
		return this.healthValue;
	}
}
