package br.com.gymetrics.tela;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import br.com.gymetrics.GymSingleton;

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

		JPanel acoesRapidas = new JPanel();
		acoesRapidas.add(genGtBtn("Check-in", "check-in"));
		acoesRapidas.add(genGtBtn("Alunos", "genAlunos"));
		acoesRapidas.add(genGtBtn("Planos", "genPlanos"));
		acoesRapidas.add(genGtBtn("Financeiro", "genFinanceiro"));
		acoesRapidas.add(genGtBtn("Sair", "login"));
		this.add(acoesRapidas, BorderLayout.SOUTH);
	}

	private JButton genGtBtn(String texto, String nomeTela) {
		JButton bt = new JButton(texto);
		bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GymSingleton.getInstance().showTela(nomeTela);
			}
		});
		return bt;
	}
}
