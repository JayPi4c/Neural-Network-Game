package com.JayPi4c.Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.JayPi4c.Game;
import com.JayPi4c.Matrix.Matrix;
import com.JayPi4c.NeuralNetwork.NeuralNetwork;
import com.JayPi4c.util.Vector;

public class Agent {
//-------------------------ATTRIBUTES--------------------------//

	Vector pos, vel, acc;
	double maxspeed;
	double diameter;
	int ammunition = 25;
	double health, maxHealth;
	NeuralNetwork brain;

//-------------------------CONSTRUCTOR-------------------------//

	public Agent(double x, double y) {
		this.pos = new Vector(x, y);
		this.vel = new Vector(0, 0);
		this.acc = new Vector(0, 0);
		this.maxspeed = 2;
		this.diameter = 30;
		this.health = maxHealth = 50;
		// inputs:
		// 0: eigene x-pos
		// 1: eigene y-pos
		// 2: eigene x-vel
		// 3: eigene y-vel
		// 4-13 5 dichteste Ammo koords
		// 14 -17 2 dichteste MedPacks-koords
		// 18 - 32 5 dichteste Asteroiden koords + diameter
		// 33: ammozahl
		// 34: healthpunkte
		//
		// ouputs:
		// 0: shoot
		// (Bewegungsrichtung)
		// 1: WEST
		// 2: NORTH-WEST
		// 3: NORTH
		// 4: NORTH-EAST
		// 5: EAST
		// 6: SOUTH-EAST
		// 7: SOUTH
		// 8: SOUTH-WEST
		this.brain = new NeuralNetwork(35, 25, 9, 0.01);

	}

//-------------------------GAME-ENGINE-------------------------//

	public Fireball shoot() {
		if (this.ammunition > 0) {
			this.ammunition--;
			return new Fireball(this.pos.x, this.pos.y, this.vel.copy().mult(1.5));
		}
		return null;
	}

	public void checkAmmunition(ArrayList<Ammunition> ammos) {
		for (int i = ammos.size() - 1; i >= 0; i--) {
			Ammunition ammo = ammos.get(i);
			if (ammo.getPosition().dist(this.pos) < this.diameter * 0.5) {
				this.ammunition++;
				ammos.remove(ammo);
			}
		}
	}

	public void checkMedPacks(ArrayList<MediPack> medis) {
		for (int i = medis.size() - 1; i >= 0; i--) {
			MediPack medi = medis.get(i);
			if (medi.getPosition().dist(this.pos) < this.diameter * 0.5) {
				if (this.health + medi.getHealthValue() <= this.maxHealth) {
					this.health += medi.getHealthValue();
					medis.remove(medi);
				}
			}
		}
	}

	public void checkAsteroid(ArrayList<Asteroid> astros) {
		for (int i = astros.size() - 1; i >= 0; i--) {
			Asteroid astro = astros.get(i);
			if (astro.getPosition().dist(this.pos) < this.diameter * 0.5 + astro.getDiameter() * 0.5) {
				this.health -= astro.getStage();
				astros.remove(astro);
			}
		}
	}

//-----------------------Neural-Network------------------------//

	public void train(double[][] inputs, double[][] targets) {
		brain.train(inputs, targets);
	}

	public void move(double[][] inputs) {
		Matrix m = brain.query(inputs);
		m.print();
		double[][] outputs = m.toArray();
		if (outputs[0][0] > 0.5) {
			Fireball f = shoot();
			if (f != null)
				Game.fireballs.add(f);
		}
		double angle = getAngle(outputs);
		Vector v = Vector.fromAngle(angle);
		v.setMag(5);
		applyForce(v);
	}

	public void printTrainingStatus(double[][] inputs) {
		Matrix m = brain.query(inputs);
		m.print();
	}

//-----------------------PHYSICS-ENGINE------------------------//

	public void applyForce(Vector force) {
		this.acc.add(force);
	}

	public void update() {
		this.vel.add(acc);
		this.vel.limit(maxspeed);
		this.pos.add(vel);
		this.acc.mult(0);
		this.edges();
	}

//-----------------------DESIGN-FUNCTIONS----------------------// 

	public void show(Graphics2D g) {
		g.setColor(new Color(200, 200, 200, 200));
		g.fillOval((int) (this.pos.x - this.diameter * 0.5), (int) (this.pos.y - this.diameter * 0.5),
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

//---------------------------HELPER----------------------------//

	public Vector getPosition() {
		return this.pos.copy();
	}

	public Vector getVelocity() {
		return this.vel.copy();
	}

	public int getAmmunition() {
		return this.ammunition;
	}

	public double getHealth() {
		return this.health;
	}

	public double getMaxHealth() {
		return this.maxHealth;
	}

	double getAngle(double[][] arr) {

		int index = -1;
		double max = -1;
		for (int i = 1; i < arr.length; i++) {
			if (arr[i][0] > max) {
				index = i;
				max = arr[i][0];
			}
		}
		System.out.println(index);

		switch (index) {
		case 1:
			return Math.PI;
		case 2:
			return -0.75 * Math.PI;
		case 3:
			return -0.5 * Math.PI;
		case 4:
			return -0.25 * Math.PI;
		case 5:
			return 0;
		case 6:
			return 0.25 * Math.PI;
		case 7:
			return 0.5 * Math.PI;
		case 8:
			return 0.75 * Math.PI;
		default:
			return 0;
		}

	}
}
