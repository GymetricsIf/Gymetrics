package br.com.gymetrics.tela;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import br.com.gymetrics.GymSingleton;

public class TelaCheckin extends JPanel {
	private final JTextField txtCode = new JTextField();

	public TelaCheckin() {
		setLayout(new BorderLayout(15, 15)); setBackground(Color.WHITE); setBorder(new EmptyBorder(25, 35, 25, 35));

		JPanel topo = new JPanel(new GridLayout(2, 1)); topo.setBackground(Color.WHITE);
		JLabel titulo = new JLabel("CHECK-IN DE ALUNOS", SwingConstants.CENTER); titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
		JLabel subtitulo = new JLabel("DIGITE SEU CÓDIGO OU CPF PARA REGISTRAR A ENTRADA", SwingConstants.CENTER); subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 10)); subtitulo.setForeground(Color.GRAY);
		topo.add(titulo); topo.add(subtitulo); add(topo, BorderLayout.NORTH);

		JPanel centro = new JPanel(new BorderLayout(15, 15)); centro.setBackground(Color.WHITE);
		txtCode.setFont(new Font("SansSerif", Font.BOLD, 28)); txtCode.setHorizontalAlignment(JTextField.CENTER); txtCode.setEditable(false);
		txtCode.setPreferredSize(new Dimension(0, 50)); txtCode.setBackground(new Color(250, 250, 250));
		txtCode.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
		centro.add(txtCode, BorderLayout.NORTH);

		JPanel teclado = new JPanel(new GridLayout(4, 3, 10, 10)); teclado.setBackground(Color.WHITE);
		for (String k : new String[]{"1","2","3","4","5","6","7","8","9","C","0","←"}) {
			JButton b = new JButton(k); b.setFont(new Font("SansSerif", Font.BOLD, 18)); b.setFocusable(false);
			b.setBackground(new Color(245, 245, 245)); b.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
			b.addActionListener(e -> {
				if (k.equals("C")) txtCode.setText("");
				else if (k.equals("←")) { String t = txtCode.getText(); if (!t.isEmpty()) txtCode.setText(t.substring(0, t.length() - 1)); }
				else txtCode.setText(txtCode.getText() + k);
			});
			teclado.add(b);
		}
		centro.add(teclado, BorderLayout.CENTER); add(centro, BorderLayout.CENTER);

		JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); botoes.setBackground(Color.WHITE);
		JButton btnConfirmar = new JButton("Confirmar Check-in"); btnConfirmar.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnConfirmar.setBackground(new Color(40, 40, 40)); btnConfirmar.setForeground(Color.WHITE);
		btnConfirmar.addActionListener(e -> {
			if (txtCode.getText().isBlank()) { GymSingleton.getInstance().showMsg("Digite o código!"); return; }
			GymSingleton.getInstance().showMsg("Check-in realizado! Código: " + txtCode.getText()); txtCode.setText("");
		});
		JButton btnVoltar = new JButton("Voltar"); btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnVoltar.addActionListener(e -> GymSingleton.getInstance().showTela("principal"));
		botoes.add(btnConfirmar); botoes.add(btnVoltar); add(botoes, BorderLayout.SOUTH);
	}
}