package br.com.gymetrics;

import java.awt.Dimension;

import javax.swing.*;

import br.com.gymetrics.telas.Estilo;

public class Main {
	public static void main(String[] args) {
		System.out.println("Vamos lá!");

		// Look and feel multiplataforma: garante as mesmas cores do protótipo no Windows, macOS e Linux.
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception ignored) {}

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Gymetrics");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setIconImage(Estilo.iconeJanela());

			frame.setMinimumSize(new Dimension(1024, 680));
			frame.setSize(1280, 800);
			frame.setLocationRelativeTo(null);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

			GymSingleton gym = GymSingleton.getInstance();
			frame.add(gym.takeFrame());
			gym.showTela("login");

			frame.setVisible(true);
		});
	}
}
