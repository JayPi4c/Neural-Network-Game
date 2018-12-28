
package com.JayPi4c.util;

public class Vector {

	public double x;
	public double y;
	public double z;

	public Vector() {
	}

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector set(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0;
		return this;
	}

	public Vector set(Vector v) {
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}

	static public Vector random2D() {
		return fromAngle((Math.random() * Math.PI * 2));

	}

	static public Vector fromAngle(double angle) {
		return new Vector(Math.cos(angle), Math.sin(angle), 0);
	}

	public Vector copy() {
		return new Vector(x, y, z);
	}

	@Deprecated
	public Vector get() {
		return copy();
	}

	public double mag() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double magSq() {
		return (x * x + y * y + z * z);
	}

	public Vector add(Vector v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}

	public Vector add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Vector add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	static public Vector add(Vector v1, Vector v2) {
		return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}

	public Vector sub(Vector v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

	public Vector sub(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	public Vector sub(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	static public Vector sub(Vector v1, Vector v2) {
		return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}

	public Vector mult(double n) {
		x *= n;
		y *= n;
		z *= n;
		return this;
	}

	static public Vector mult(Vector v, float n) {
		return new Vector(v.x * n, v.y * n, v.z * n);
	}

	public Vector div(double n) {
		x /= n;
		y /= n;
		z /= n;
		return this;
	}

	static public Vector div(Vector v, float n) {
		return new Vector(v.x / n, v.y / n, v.z / n);
	}

	public double dist(Vector v) {
		double dx = x - v.x;
		double dy = y - v.y;
		double dz = z - v.z;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	static public double dist(Vector v1, Vector v2) {
		double dx = v1.x - v2.x;
		double dy = v1.y - v2.y;
		double dz = v1.z - v2.z;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public double dot(Vector v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public double dot(double x, double y, double z) {
		return this.x * x + this.y * y + this.z * z;
	}

	static public double dot(Vector v1, Vector v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}

	public Vector cross(Vector v) {
		double crossX = y * v.z - v.y * z;
		double crossY = z * v.x - v.z * x;
		double crossZ = x * v.y - v.x * y;
		return new Vector(crossX, crossY, crossZ);
	}

	public Vector normalize() {
		double m = mag();
		if (m != 0 && m != 1) {
			div(m);
		}
		return this;
	}

	public Vector normalize(Vector target) {
		if (target == null) {
			target = new Vector();
		}
		double m = mag();
		if (m > 0) {
			target.set(x / m, y / m, z / m);
		} else {
			target.set(x, y, z);
		}
		return target;
	}

	public Vector limit(double max) {
		if (magSq() > max * max) {
			normalize();
			mult(max);
		}
		return this;
	}

	public Vector setMag(double len) {
		normalize();
		mult(len);
		return this;
	}

	public Vector setMag(Vector target, double len) {
		target = normalize(target);
		target.mult(len);
		return target;
	}

	public double heading() {
		double angle = Math.atan2(y, x);
		return angle;
	}

	@Deprecated
	public double heading2D() {
		return heading();
	}

	public Vector rotate(double theta) {
		double temp = x;
		// Might need to check for rounding errors like with angleBetween function?
		x = x * Math.cos(theta) - y * Math.sin(theta);
		y = temp * Math.sin(theta) + y * Math.cos(theta);
		return this;
	}

}
