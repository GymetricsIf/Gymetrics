package br.com.gymetrics.telas;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import br.com.gymetrics.GymSingleton;
import br.com.gymetrics.negocio.Mensalidade;
import br.com.gymetrics.negocio.Pagamento;

/** CSU04 / RF17: listagem financeira do mês e registro de pagamento. */
public class TelaGerenciamentoFinanceiro extends JPanel implements Atualizavel {
	private static final DateTimeFormatter MES = DateTimeFormatter.ofPattern("MM/yyyy");

	private final Estilo.Campo txtAluno = new Estilo.Campo("Buscar aluno", 280);
	private final JComboBox<String> cbSituacao = Estilo.combo(new String[]{"Todas", "Pendente", "Vencida", "Paga"});
	private final JTable tabela = Estilo.tabela("Aluno", "CPF", "Plano", "Vencimento", "Valor", "Situação");
	private final JLabel lblTotais = new JLabel(" ", SwingConstants.CENTER);
	private final JLabel lblMes = new JLabel();
	private List<Mensalidade> todas = new ArrayList<>();
	private final List<Mensalidade> visiveis = new ArrayList<>();

	public TelaGerenciamentoFinanceiro() {
		setLayout(new BorderLayout());
		setBackground(Estilo.FUNDO);

		JPanel pagina = Estilo.pagina();
		pagina.add(Estilo.cabecalho("GERENCIAMENTO\nFINANCEIRO", "Consulte e registre os pagamentos dos alunos"), BorderLayout.NORTH);

		lblMes.setFont(Estilo.fonte(Font.BOLD, 13));
		lblMes.setForeground(Estilo.TEXTO);
		lblTotais.setFont(Estilo.fonte(Font.PLAIN, 13));
		lblTotais.setForeground(Estilo.TEXTO_SUAVE);

		JPanel centro = new JPanel(new BorderLayout(0, 14));
		centro.setOpaque(false);
		centro.add(Estilo.linha(FlowLayout.CENTER, lblMes, txtAluno, cbSituacao), BorderLayout.NORTH);
		centro.add(Estilo.rolagem(tabela), BorderLayout.CENTER);
		centro.add(lblTotais, BorderLayout.SOUTH);
		pagina.add(centro, BorderLayout.CENTER);

		pagina.add(Estilo.linha(FlowLayout.RIGHT,
			Estilo.botao("Registrar Pagamento", this::registrarPagamento),
			Estilo.botaoSecundario("Voltar", () -> GymSingleton.getInstance().showTela("principal"))
		), BorderLayout.SOUTH);
		add(pagina);

		txtAluno.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void insertUpdate(DocumentEvent e) { filtrar(); }
			@Override public void removeUpdate(DocumentEvent e) { filtrar(); }
			@Override public void changedUpdate(DocumentEvent e) { filtrar(); }
		});
		cbSituacao.addActionListener(e -> filtrar());
	}

	@Override
	public void atualizar() {
		GymSingleton gym = GymSingleton.getInstance();
		YearMonth mes = YearMonth.now();
		lblMes.setText("Mês " + MES.format(mes) + "   ");
		gym.executar(() -> {
			gym.mensalidades().prepararMes(LocalDate.now());
			todas = gym.mensalidades().listarDoMes(mes);
		});
		filtrar();
	}

	private void filtrar() {
		String termo = txtAluno.getText().trim().toLowerCase();
		String situacao = (String) cbSituacao.getSelectedItem();
		visiveis.clear();
		List<Object[]> linhas = new ArrayList<>();
		BigDecimal recebido = BigDecimal.ZERO, aReceber = BigDecimal.ZERO;

		for (Mensalidade m : todas) {
			if (m.getStatus() == Mensalidade.Status.PAGA) recebido = recebido.add(m.getValor());
			else aReceber = aReceber.add(m.getValor());

			boolean bateTermo = termo.isEmpty() || m.getAluno().getNome().toLowerCase().contains(termo)
					|| m.getAluno().getCpf().contains(termo);
			boolean bateSituacao = "Todas".equals(situacao) || Estilo.nome(m.getStatus()).equals(situacao);
			if (!bateTermo || !bateSituacao) continue;

			visiveis.add(m);
			linhas.add(new Object[]{m.getAluno().getNome(), m.getAluno().getCpf(),
					m.getAluno().getPlano() == null ? "" : m.getAluno().getPlano().getNome(),
					Estilo.data(m.getVencimento()), Estilo.moeda(m.getValor()), Estilo.nome(m.getStatus())});
		}
		Estilo.preencher(tabela, linhas);
		lblTotais.setText("Recebido no mês: " + Estilo.moeda(recebido) + "     ·     A receber: " + Estilo.moeda(aReceber));
	}

	private void registrarPagamento() {
		GymSingleton gym = GymSingleton.getInstance();
		int i = tabela.getSelectedRow();
		if (i < 0) { gym.showMsg("Selecione uma mensalidade na tabela."); return; }
		Mensalidade m = visiveis.get(i);
		if (m.getStatus() == Mensalidade.Status.PAGA) { gym.showMsg("Esta mensalidade já está paga."); return; }

		Estilo.Campo mes = new Estilo.Campo("mm/aaaa", 220);
		Estilo.Campo valor = new Estilo.Campo(null, 220);
		mes.setText(MES.format(YearMonth.from(m.getVencimento())));
		valor.setText(m.getValor().toPlainString().replace('.', ','));

		String titulo = "Registrar Pagamento - " + m.getAluno().getNome();
		while (Estilo.formulario(this, titulo, new String[]{"Mês de referência", "Valor pago"}, new JComponent[]{mes, valor})) {
			boolean ok = gym.executar(() -> {
				Pagamento p = new Pagamento();
				p.setMensalidade(m);
				try {
					p.setMesReferencia(YearMonth.parse(mes.getText().trim(), MES));
				} catch (DateTimeParseException e) {
					throw new IllegalArgumentException("Mês de referência deve estar no formato mm/aaaa");
				}
				p.setValorPago(Estilo.lerValor(valor.getText(), "Valor pago"));
				gym.pagamentos().salvar(p);
			});
			if (ok) {
				gym.showMsg("Pagamento registrado. Situação do aluno: " + Estilo.nome(m.getAluno().getStatus()));
				atualizar();
				return;
			}
		}
	}
}
