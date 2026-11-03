package br.com.gymetrics.tela;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import br.com.gymetrics.GymSingleton;

public class TelaGerenciamentoPlanos extends JPanel {
	public TelaGerenciamentoPlanos() {
		setLayout(new BorderLayout(10, 10)); setBackground(Color.WHITE); setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

		JPanel topo = new JPanel(new GridLayout(2, 1)); topo.setBackground(Color.WHITE);
		JLabel titulo = new JLabel("GERENCIAMENTO DE PLANOS", SwingConstants.CENTER); titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
		JLabel subtitulo = new JLabel("CADASTRE E GERENCIE OS PLANOS DA ACADEMIA", SwingConstants.CENTER); subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 10)); subtitulo.setForeground(Color.GRAY);
		topo.add(titulo); topo.add(subtitulo); add(topo, BorderLayout.NORTH);

		JPanel centro = new JPanel(new BorderLayout(10, 10)); centro.setBackground(Color.WHITE);
		JPanel campos = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0)); campos.setBackground(Color.WHITE);
		campos.add(new JLabel("Nome:")); campos.add(new JTextField(12));
		campos.add(new JLabel("Valor:")); campos.add(new JTextField(8));
		centro.add(campos, BorderLayout.NORTH);

		JTable tabela = new JTable(new Object[0][3], new String[] { "Nome", "Valor", "Status" });
		tabela.setRowHeight(32); tabela.setShowVerticalLines(false); tabela.setGridColor(new Color(230, 230, 230));
		tabela.getTableHeader().setBackground(new Color(225, 225, 225)); tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		DefaultTableCellRenderer center = new DefaultTableCellRenderer(); center.setHorizontalAlignment(SwingConstants.CENTER);
		tabela.setDefaultRenderer(Object.class, center);
		centro.add(new JScrollPane(tabela), BorderLayout.CENTER); add(centro, BorderLayout.CENTER);

		JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT)); botoes.setBackground(Color.WHITE);
		for (String txt : new String[]{"Cadastrar", "Editar", "Desativar"}) botoes.add(new JButton(txt));
		JButton voltar = new JButton("Voltar"); voltar.addActionListener(e -> GymSingleton.getInstance().showTela("principal"));
		botoes.add(voltar); add(botoes, BorderLayout.SOUTH);
	}
}