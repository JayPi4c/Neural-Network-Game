package com.JayPi4c;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {
	static JFrame frame;

	public static void main(String args[]) {
		frame = new JFrame("Neural Network Game");
		frame.setSize((int) Game.WIDTH, (int) Game.HEIGHT);
		Game g = new Game();
		frame.setLayout(new BorderLayout());
		frame.add(g, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		while (true) {
			try {
				Thread.sleep(1000 / 60);
				g.update();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
