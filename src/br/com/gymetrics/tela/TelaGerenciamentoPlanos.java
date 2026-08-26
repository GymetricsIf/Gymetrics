package br.com.gymetrics.tela;

import java.awt.*;
import javax.swing.*;

import br.com.gymetrics.GymSingleton;

public class TelaGerenciamentoPlanos extends JPanel {
	public TelaGerenciamentoPlanos() {
		this.setLayout(new BorderLayout(10, 10));

		JLabel titulo = new JLabel("Gerenciamento de Planos", SwingConstants.CENTER);
		this.add(titulo, BorderLayout.NORTH);

		JPanel conteudo = new JPanel(new BorderLayout(5, 5));
		JPanel campos = new JPanel(new GridLayout(2, 2, 5, 5));
		campos.add(new JLabel("Nome do plano:"));
		campos.add(new JTextField(15));
		campos.add(new JLabel("Valor:"));
		campos.add(new JTextField(10));
		conteudo.add(campos, BorderLayout.NORTH);

		String[] colunas = { "Nome", "Valor", "Status" };
		JTable tabela = new JTable(new Object[0][3], colunas);
		JPanel painelTabela = new JPanel(new BorderLayout());
		painelTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
		painelTabela.add(tabela, BorderLayout.CENTER);
		conteudo.add(painelTabela, BorderLayout.CENTER);
		this.add(conteudo, BorderLayout.CENTER);

		JPanel botoes = new JPanel(new FlowLayout());
		botoes.add(new JButton("Cadastrar"));
		botoes.add(new JButton("Editar"));
		botoes.add(new JButton("Desativar"));
		JButton voltar = new JButton("Voltar");
		voltar.addActionListener(e -> GymSingleton.getInstance().showTela("principal"));
		botoes.add(voltar);
		this.add(botoes, BorderLayout.SOUTH);
	}
}
