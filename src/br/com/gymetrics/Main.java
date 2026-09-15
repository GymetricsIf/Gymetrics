package br.com.gymetrics;



import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		System.out.println("Vamos lá!");

		JFrame frame = new JFrame("Gymetrics");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		GymSingleton gym = GymSingleton.getInstance();
		
		gym.showTela("debug");
		
		frame.addWindowListener(new WindowAdapter() {
		    @Override
            public void windowClosing(WindowEvent e) {
		    	System.out.println("> fechamento requisitado");
		    	gym.shutdown();
		    }
		});

		frame.add(gym.takeFrame());
		frame.setVisible(true);
	}
}
