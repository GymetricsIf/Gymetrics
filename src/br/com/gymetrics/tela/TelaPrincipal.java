package br.com.gymetrics.tela;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import br.com.gymetrics.GymSingleton;

public class TelaPrincipal extends JPanel {
	public TelaPrincipal() {
		setLayout(new BorderLayout(20, 20)); setBackground(Color.WHITE); setBorder(new EmptyBorder(25, 35, 25, 35));

		JPanel topo = new JPanel(new GridLayout(2, 1)); topo.setBackground(Color.WHITE);
		JLabel titulo = new JLabel("GYMETRICS", SwingConstants.CENTER); titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		JLabel subtitulo = new JLabel("PAINEL GENERAL E VISÃO GERAL DAS ATIVIDADES", SwingConstants.CENTER); subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 10)); subtitulo.setForeground(Color.GRAY);
		topo.add(titulo); topo.add(subtitulo); add(topo, BorderLayout.NORTH);

		JPanel resumo = new JPanel(new GridLayout(1, 4, 15, 0)); resumo.setBackground(Color.WHITE);
		String[] kpis = {"Presentes: 0", "Entradas Hoje: 0", "Bloqueados: 0", "Vencimentos: 0"};
		for (String kpi : kpis) {
			JLabel lbl = new JLabel("<html><center>" + kpi.replace(": ", "<br><b>") + "</b></center></html>", SwingConstants.CENTER);
			lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
			lbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)), new EmptyBorder(15, 5, 15, 5)));
			lbl.setOpaque(true); lbl.setBackground(new Color(250, 250, 250));
			resumo.add(lbl);
		}
		add(resumo, BorderLayout.CENTER);

		JPanel acoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); acoes.setBackground(Color.WHITE);
		String[][] btns = {{"Check-in", "check-in"}, {"Alunos", "genAlunos"}, {"Planos", "genPlanos"}, {"Financeiro", "genFinanceiro"}, {"Sair", "login"}};
		for (String[] b : btns) acoes.add(genGtBtn(b[0], b[1]));
		add(acoes, BorderLayout.SOUTH);
	}

	private JButton genGtBtn(String texto, String nomeTela) {
		JButton bt = new JButton(texto); bt.setFont(new Font("SansSerif", Font.BOLD, 12));
		bt.setBackground(texto.equals("Sair") ? new Color(240, 240, 240) : new Color(40, 40, 40));
		bt.setForeground(texto.equals("Sair") ? Color.BLACK : Color.WHITE); bt.setFocusable(false);
		bt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), new EmptyBorder(8, 16, 8, 16)));
		bt.addActionListener(e -> GymSingleton.getInstance().showTela(nomeTela));
		return bt;
	}
}