package br.com.gymetrics.tela;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import br.com.gymetrics.GymSingleton;

public class TelaDebug extends JPanel {
	public TelaDebug() {
		setLayout(new BorderLayout(20, 20)); setBackground(Color.WHITE); setBorder(new EmptyBorder(25, 35, 25, 35));

		JPanel topo = new JPanel(new GridLayout(2, 1)); topo.setBackground(Color.WHITE);
		JLabel titulo = new JLabel("PAINEL DE DEBUG", SwingConstants.CENTER); titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
		JLabel subtitulo = new JLabel("ATALHOS DE NAVEGAÇÃO PARA DESENVOLVIMENTO E TESTES", SwingConstants.CENTER); subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 10)); subtitulo.setForeground(Color.GRAY);
		topo.add(titulo); topo.add(subtitulo); add(topo, BorderLayout.NORTH);

		JPanel centro = new JPanel(new GridLayout(3, 2, 12, 12)); centro.setBackground(Color.WHITE);
		String[][] telas = {
			{"Tela Check-in", "check-in"}, {"Gerenciamento Alunos", "genAlunos"},
			{"Gerenciamento Financeiro", "genFinanceiro"}, {"Gerenciamento Planos", "genPlanos"},
			{"Login Administrativo", "login"}, {"Tela Principal", "principal"}
		};

		for (String[] t : telas) centro.add(genGtBtn(t[0], t[1]));
		add(centro, BorderLayout.CENTER);
	}

	public JButton genGtBtn(String texto, String nomeTela) {
		JButton bt = new JButton(texto); bt.setFont(new Font("SansSerif", Font.BOLD, 12));
		bt.setBackground(new Color(245, 245, 245)); bt.setForeground(Color.BLACK); bt.setFocusable(false);
		bt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)), new EmptyBorder(10, 15, 10, 15)));
		bt.setCursor(new Cursor(Cursor.HAND_CURSOR));
		bt.addActionListener(e -> GymSingleton.getInstance().showTela(nomeTela));
		return bt;
	}
}