package br.com.gymetrics;

import java.awt.CardLayout;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.com.gymetrics.tela.*;

public class GymSingleton {
	private static GymSingleton instance = null;
	
	private CardLayout telasLayout = null;
	private Data data = null;
	private boolean mp = false;
	private JPanel telas = null;
	
	public GymSingleton() {
		System.out.println("Gym Core!");
		
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
		
		
		var data = new Data();
		
		try {
			Class.forName("org.sqlite.JDBC");
			data.open("Gym.db");
			this.data = data;
			this.data.run("SELECT 1");
		} catch (SQLException e) {
			this.showMsg("falha no boot: " + e);
			System.exit(0);
		} catch (ClassNotFoundException e) {
			this.showMsg("falha no boot [driver sqlite ñ encontrado]: " + e);
			System.exit(1);
		}
	}
	
	public void showTela(String nome) {
		System.out.printf("> mostrando %s!\n", nome);
        ((CardLayout) telas.getLayout()).show(telas, nome);
	}
	
	public void showMsg(String text) {
		JOptionPane.showMessageDialog(telas, text);
		
	}
	
	public JPanel takeFrame() {
		if (mp) throw new Error("BUG: Alguem ja tem o frame das telas owned");
		mp = true;
		
		
		return this.telas;
	}
	
	public void shutdown() {
		if (!mp)
			throw new Error("BUG: o App não está com o frame inicializado.");
		
		try {
			data.suicide();
			System.out.println("> consegui fechar o BD!");
		} catch (SQLException e) {
			System.out.println("> ñ foi possivel fechar o BD.");
			e.printStackTrace();
		}
	}
	
	public static GymSingleton getInstance() {
		if (instance == null) {
			instance = new GymSingleton();
		}
		
		return instance;
	}
}
