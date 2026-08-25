package br.com.gymetrics.tela;

import java.awt.*;
import javax.swing.*;

public class TelaPrincipal extends JPanel {
	public TelaPrincipal() {
		this.setLayout(new BorderLayout(10, 10));

		JLabel titulo = new JLabel("GyMetrics - Tela Principal", SwingConstants.CENTER);
		this.add(titulo, BorderLayout.NORTH);

		JPanel resumo = new JPanel(new GridLayout(1, 4, 10, 10));
		resumo.add(new JLabel("Presentes: 0", SwingConstants.CENTER));
		resumo.add(new JLabel("Entradas hoje: 0", SwingConstants.CENTER));
		resumo.add(new JLabel("Bloqueados: 0", SwingConstants.CENTER));
		resumo.add(new JLabel("Vencimentos: 0", SwingConstants.CENTER));

		this.add(resumo, BorderLayout.CENTER);

		JPanel menu = new JPanel();
		menu.add(new JButton("Check-in"));
		menu.add(new JButton("Alunos"));
		menu.add(new JButton("Planos"));
		menu.add(new JButton("Financeiro"));
		menu.add(new JButton("Sair"));

		this.add(menu, BorderLayout.SOUTH);
	}
}