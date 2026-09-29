package br.com.gymetrics.tela;

import java.awt.*;
import javax.swing.*;

import br.com.gymetrics.GymSingleton;

public class TelaGerenciamentoAlunos extends JPanel {
	public TelaGerenciamentoAlunos() {
		this.setLayout(new BorderLayout(10, 10));

		JLabel titulo = new JLabel("Gerenciamento de Alunos", SwingConstants.CENTER);
		this.add(titulo, BorderLayout.NORTH);

		JPanel conteudo = new JPanel(new BorderLayout(5, 5));
		JPanel busca = new JPanel(new FlowLayout());
		busca.add(new JLabel("Nome ou CPF:"));
		busca.add(new JTextField(20));
		busca.add(new JButton("Buscar"));
		conteudo.add(busca, BorderLayout.NORTH);

		String[] colunas = { "Nome", "CPF", "Telefone", "Plano", "Status" };
		JTable tabela = new JTable(new Object[0][5], colunas);
		JPanel painelTabela = new JPanel(new BorderLayout());
		painelTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
		painelTabela.add(tabela, BorderLayout.CENTER);
		conteudo.add(painelTabela, BorderLayout.CENTER);
		this.add(conteudo, BorderLayout.CENTER);

		JPanel botoes = new JPanel(new FlowLayout());
		botoes.add(new JButton("Cadastrar"));
		botoes.add(new JButton("Editar"));
		botoes.add(new JButton("Bloquear"));
		JButton voltar = new JButton("Voltar");
		voltar.addActionListener(e -> GymSingleton.getInstance().showTela("principal"));
		botoes.add(voltar);
		this.add(botoes, BorderLayout.SOUTH);
	}
}
