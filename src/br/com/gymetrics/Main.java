package br.com.gymetrics;



import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		System.out.println("Vamos lá!");

		JFrame frame = new JFrame("Gymetrics");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 200);

		GymSingleton gym = GymSingleton.getInstance();
		
		gym.showTela("debug");

		frame.add(gym.takeFrame());
		frame.setVisible(true);
	}
}
