package br.com.gymetrics.telas;

import java.awt.*;
import javax.swing.*;

import br.com.gymetrics.GymSingleton;

/** Atalhos de navegação para desenvolvimento (não aparece no fluxo normal). */
public class TelaDebug extends JPanel {
	public TelaDebug() {
		setLayout(new BorderLayout());
		setBackground(Estilo.FUNDO);

		JPanel pagina = Estilo.pagina();
		pagina.add(Estilo.cabecalho("PAINEL DE DEBUG", "Atalhos de navegação para desenvolvimento e testes"), BorderLayout.NORTH);

		JPanel centro = new JPanel(new GridLayout(4, 2, 12, 12));
		centro.setOpaque(false);
		String[][] telas = {
			{"Login", "login"}, {"Tela Principal", "principal"},
			{"Check-in", "check-in"}, {"Alunos", "genAlunos"},
			{"Financeiro", "genFinanceiro"}, {"Planos", "genPlanos"},
			{"Frequências", "relatorios"}
		};
		for (String[] t : telas)
			centro.add(Estilo.botao(t[0], () -> GymSingleton.getInstance().showTela(t[1])));
		pagina.add(centro, BorderLayout.CENTER);
		add(pagina);
	}
}
