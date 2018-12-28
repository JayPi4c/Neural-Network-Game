package com.JayPi4c;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JPanel;

import com.JayPi4c.Entity.Agent;
import com.JayPi4c.Entity.Ammunition;
import com.JayPi4c.Entity.Asteroid;
import com.JayPi4c.Entity.Fireball;
import com.JayPi4c.Entity.MediPack;
import com.JayPi4c.util.Vector;

public class Game extends JPanel {
	int cycles = 0;
	public static double WIDTH = 640, HEIGHT = 480;

	private static final long serialVersionUID = -5544732485569865162L;

	private boolean play = true;
	private int keyPressed = 0;

	public Agent a;

	public static ArrayList<Fireball> fireballs;
	ArrayList<Ammunition> ammunitions;
	ArrayList<Asteroid> asteroids;
	ArrayList<MediPack> mediPacks;

	public Game() {

		addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {

			}

			@Override
			public void componentResized(ComponentEvent e) {
				WIDTH = e.getComponent().getWidth();
				HEIGHT = e.getComponent().getHeight();
				// System.out.println(e.getComponent().getWidth() + " | " +
				// e.getComponent().getHeight());
			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentHidden(ComponentEvent e) {

			}
		});

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				play = !play;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyPressed = 0;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				keyPressed = 1;
				Fireball f = a.shoot();
				if (f != null)
					fireballs.add(f);
				// System.out.println("shoot");
			}
		});
		this.setFocusable(true);

		a = new Agent(WIDTH / 2, HEIGHT / 2);

		fireballs = new ArrayList<Fireball>();
		ammunitions = new ArrayList<Ammunition>();
		asteroids = new ArrayList<Asteroid>();
		for (int i = 0; i < 7; i++) {
			asteroids.add(createAsteroid());
		}

		mediPacks = new ArrayList<MediPack>();

	}

	public void update() {
		cycles++;
		if (cycles % (5 * 60) == 0) {
			System.out.println(cycles + ": cycles has passed!");
			// a.printTrainingStatus(getInputs());
		}

		if (play) {

			Point p = this.getMousePosition();// MouseInfo.getPointerInfo().getLocation();
			// System.out.println(p.x + " " + p.y);
			if (p != null) {

				double[][] inputs = getInputs();

				Vector pos = a.getPosition();
				Vector mPos = new Vector(p.x, p.y);
				mPos.sub(pos);
				mPos.limit(5);
				double outputs[][] = { { keyPressed, 0, 0, 0, 0, 0, 0, 0, 0 } };
				int index = getIndex(mPos);
				outputs[0][index] = 1;
				a.applyForce(mPos);
				a.train(inputs, outputs);
			}
		} else {

			double[][] inputs = getInputs();
			a.move(inputs);

			// Do some awesome AI stuff :D
		}
		a.update();
		this.repaint();
	}

	@Override
	public void paint(Graphics g) {
		BufferedImage bImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) bImage.getGraphics();
		g2.setColor(new Color(51, 51, 51));
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		medPacks(g2);
		asteroid(g2);
		fireball(g2);
		ammo(g2);

		a.checkMedPacks(mediPacks);
		a.checkAmmunition(ammunitions);
		a.checkAsteroid(asteroids);
		a.show(g2);
		g2.drawString("Ammunition: " + a.getAmmunition(), 10, (int) HEIGHT - 10);
		g2.drawString(play ? "HUMAN" : "AI", 10, 20);
		showLifeBar(g2);
		g.drawImage(bImage, 0, 0, null);

	}

	void showLifeBar(Graphics2D g) {
		g.setColor(new Color(255, 0, 0));
		g.drawLine((int) WIDTH / 2, (int) HEIGHT - 10, (int) WIDTH - 10, (int) HEIGHT - 10);

		double proportion = a.getHealth() / a.getMaxHealth();
		double len = (((int) WIDTH - 10) - ((int) WIDTH / 2)) * proportion;
		g.setColor(new Color(0, 255, 0));
		g.drawLine((int) WIDTH / 2, (int) HEIGHT - 10, (int) (WIDTH / 2 + len), (int) HEIGHT - 10);
	}

	Ammunition createAmmunition() {
		return new Ammunition(random(WIDTH), random(HEIGHT));
	}

	void ammo(Graphics2D g) {
		if (ammunitions.size() < 15) {
			if (ammunitions.size() < 3)
				ammunitions.add(createAmmunition());
			else if (random(1) < 0.4)
				ammunitions.add(createAmmunition());
		}

		for (int i = 0; i < ammunitions.size(); i++) {
			ammunitions.get(i).show(g);
		}
	}

	void medPacks(Graphics2D g) {
		if (mediPacks.size() < 3 && random(1) < 0.01)
			mediPacks.add(new MediPack(random(WIDTH), random(HEIGHT)));

		for (int i = 0; i < mediPacks.size(); i++) {
			mediPacks.get(i).show(g);
		}
	}

	void fireball(Graphics2D g) {
		for (int i = fireballs.size() - 1; i >= 0; i--) {
			Fireball f = fireballs.get(i);
			if (f.isOffscreen())
				fireballs.remove(f);
			else {
				f.update();
				f.show(g);
			}
		}
	}

	Asteroid createAsteroid() {
		return new Asteroid(ThreadLocalRandom.current().nextDouble(WIDTH),
				ThreadLocalRandom.current().nextDouble(HEIGHT), Vector.random2D(), 40, 2);
	}

	void asteroid(Graphics2D g) {
		if (asteroids.size() < 15) {
			if (asteroids.size() < 3)
				asteroids.add(createAsteroid());
			else if (random(1) < 0.4)
				asteroids.add(createAsteroid());
		}
		for (int i = 0; i < asteroids.size(); i++) {
			Asteroid as = asteroids.get(i);
			if (as.collidesFireball(fireballs)) {
				if (!(as.getStage() == 0)) {
					Vector asPos = as.getPosition();
					for (int j = 0; j < max(1, (int) random(4)); j++) {
						asteroids.add(new Asteroid(asPos.x, asPos.y, Vector.random2D(), as.getDiameter() * 0.5,
								as.getStage() - 1));
					}
				}
				asteroids.remove(as);
			} else {
				as.update();
				as.show(g);
			}
		}
	}

	double random() {
		return Math.random();
	}

	double random(double max) {
		return random(0, max);
	}

	double random(double min, double max) {
		return min + (Math.random() * (max - min));
	}

	int max(int a, int b) {
		return (a >= b ? a : b);
	}

	int min(int a, int b) {
		return (a < b ? a : b);
	}

	int getIndex(Vector v) {
		double angle = v.heading();
		if (angle < -0.875 * Math.PI || angle >= 0.875 * Math.PI)
			return 1;
		else if (angle < -0.625 * Math.PI)
			return 2;
		else if (angle < -0.375 * Math.PI)
			return 3;
		else if (angle < -0.125 * Math.PI)
			return 4;
		else if (angle < 0.125 * Math.PI)
			return 5;
		else if (angle < 0.375 * Math.PI)
			return 6;
		else if (angle < 0.625 * Math.PI)
			return 7;
		else if (angle < 0.875 * Math.PI)
			return 8;
		return -1;
	}

	double[][] getInputs() {

		double[][] inputs = new double[1][35];
		for (int i = 0; i < 35; i++) {
			inputs[0][i] = 0;
		}

		Vector aPos = a.getPosition();

		ArrayList<ScoreObj<Ammunition>> ammo = new ArrayList<ScoreObj<Ammunition>>();
		for (Ammunition am : ammunitions) {
			double dist = aPos.dist(am.getPosition());
			ammo.add(new ScoreObj<Ammunition>(dist, am));
		}
		ammo.sort(null);

		ArrayList<ScoreObj<MediPack>> medPacks = new ArrayList<ScoreObj<MediPack>>();
		for (MediPack medPack : mediPacks) {
			double dist = aPos.dist(medPack.getPosition());
			medPacks.add(new ScoreObj<MediPack>(dist, medPack));
		}
		medPacks.sort(null);

		ArrayList<ScoreObj<Asteroid>> astros = new ArrayList<ScoreObj<Asteroid>>();
		for (Asteroid astro : asteroids) {
			double dist = aPos.dist(astro.getPosition());
			astros.add(new ScoreObj<Asteroid>(dist, astro));
		}
		astros.sort(null);

		inputs[0][0] = a.getPosition().x;
		inputs[0][1] = a.getPosition().y;
		inputs[0][2] = a.getVelocity().x;
		inputs[0][3] = a.getVelocity().y;
		for (int i = 0; i < min(5, ammo.size()); i++) {
			int index = i * 2;
			inputs[0][4 + index] = ammo.get(i).type.getPosition().x;
			inputs[0][4 + index + 1] = ammo.get(i).type.getPosition().y;
		}

		for (int i = 0; i < min(2, medPacks.size()); i++) {
			int index = i * 2;
			inputs[0][14 + index] = medPacks.get(i).type.getPosition().x;
			inputs[0][14 + index + 1] = medPacks.get(i).type.getPosition().y;
		}
		for (int i = 0; i < min(5, astros.size()); i++) {
			int index = i * 3;
			inputs[0][18 + index] = astros.get(i).type.getPosition().x;
			inputs[0][18 + index + 1] = astros.get(i).type.getPosition().y;
			inputs[0][18 + index + 2] = astros.get(i).type.getDiameter();
		}
		inputs[0][33] = a.getAmmunition();
		inputs[0][34] = a.getHealth();

		/*
		 * for (int i = 0; i < inputs[0].length; i++) {
		 * System.out.println(inputs[0][i]); } System.out.println('\n');
		 */

		return inputs;
	}

	class ScoreObj<Type> implements Comparable<ScoreObj<Type>> {
		public double distance;
		public Type type;

		public ScoreObj(double distance, Type obj) {
			this.distance = distance;
			type = obj;
		}

		@Override
		public int compareTo(ScoreObj<Type> o) {
			return distance < o.distance ? -1 : 1;
		}
	}

}
