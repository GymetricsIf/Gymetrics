package br.com.gymetrics.tela;

import java.awt.*;
import javax.swing.*;

import br.com.gymetrics.GymSingleton;

public class TelaGerenciamentoFinanceiro extends JPanel {
	public TelaGerenciamentoFinanceiro() {
		this.setLayout(new BorderLayout(10, 10));

		JLabel titulo = new JLabel("Gerenciamento Financeiro", SwingConstants.CENTER);
		this.add(titulo, BorderLayout.NORTH);

		JPanel conteudo = new JPanel(new BorderLayout(5, 5));
		JPanel filtros = new JPanel(new FlowLayout());
		filtros.add(new JLabel("Aluno:"));
		filtros.add(new JTextField(20));
		filtros.add(new JLabel("Situação:"));
		String[] situacoes = { "Todos", "Pendente", "Vencida", "Paga" };
		filtros.add(new JComboBox<String>(situacoes));
		conteudo.add(filtros, BorderLayout.NORTH);

		String[] colunas = { "Aluno", "Plano", "Vencimento", "Valor", "Situação" };
		JTable tabela = new JTable(new Object[0][5], colunas);
		JPanel painelTabela = new JPanel(new BorderLayout());
		painelTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
		painelTabela.add(tabela, BorderLayout.CENTER);
		conteudo.add(painelTabela, BorderLayout.CENTER);
		this.add(conteudo, BorderLayout.CENTER);

		JPanel botoes = new JPanel(new FlowLayout());
		botoes.add(new JButton("Registrar pagamento"));
		JButton voltar = new JButton("Voltar");
		voltar.addActionListener(e -> GymSingleton.getInstance().showTela("principal"));
		botoes.add(voltar);
		this.add(botoes, BorderLayout.SOUTH);
	}
}
