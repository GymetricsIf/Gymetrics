package br.com.gymetrics.tela;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import br.com.gymetrics.GymSingleton;

public class TelaDebug extends JPanel {
	public TelaDebug() {
		BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(boxLayout);
		
		this.add(genGtBtn("Tela check-in", "check-in"));
		this.add(genGtBtn("Tela gerenciamento alunos", "genAlunos"));
		this.add(genGtBtn("Tela gerenciamento financeiro", "genFinanceiro"));
		this.add(genGtBtn("Tela gerenciamento planos", "genPlanos"));
		this.add(genGtBtn("Tela login administrativo", "login"));
		this.add(genGtBtn("Tela Principal", "principal"));
	}
	
	public JButton genGtBtn(String texto, String nomeTela) {
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
