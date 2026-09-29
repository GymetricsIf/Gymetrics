package br.com.gymetrics;

import java.awt.CardLayout;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.com.gymetrics.tela.*;

public class GymSingleton {
	private static GymSingleton instance = null;
	
	private CardLayout telasLayout = null;
	private boolean mp = false;
	private JPanel telas = null;
	private Data data = new Data();
	
	public GymSingleton() {
		System.out.println("Gym Core!");
		
		try {
			data.open("gymetrics.db");
			data.criarTabelas();
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível abrir o banco de dados", e);
		}
		
		this.telasLayout = new CardLayout();
		this.telas = new JPanel();
		telas.setLayout(telasLayout);
		
		telas.add(new TelaDebug(), "debug");
		telas.add(new TelaPrincipal(), "principal");
		telas.add(new TelaCheckin(), "check-in");
		telas.add(new TelaGerenciamentoPlanos(), "genPlanos");
		telas.add(new TelaGerenciamentoAlunos(), "genAlunos");
		telas.add(new TelaGerenciamentoFinanceiro(), "genFinanceiro");
		telas.add(new TelaLogin(), "login");

	}
	
	public void showTela(String nome) {
		System.out.printf("> mostrando %s!\n", nome);
        ((CardLayout) telas.getLayout()).show(telas, nome);
	}
	
	public Data getData() {
		return data;
	}
	
	public void showMsg(String text) {
		JOptionPane.showMessageDialog(telas, text);
		
	}
	
	public JPanel takeFrame() {
		if (mp) throw new Error("BUG: Alguem ja tem o frame das telas owned");
		mp = true;
		return this.telas;
	}
	
	
	public static GymSingleton getInstance() {
		if (instance == null) {
			instance = new GymSingleton();
		}
		
		return instance;
	}
}
