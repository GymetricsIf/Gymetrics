package br.com.gymetrics.telas;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.gymetrics.GymSingleton;
import br.com.gymetrics.negocio.RegistroAcesso;
import br.com.gymetrics.util.ExportadorCSV;

/** CSU09 / CSU10 / RF07, RF09, RF15: frequência por período e exportação em CSV. */
public class TelaRelatorios extends JPanel implements Atualizavel {
	private final Estilo.Campo txtInicio = new Estilo.Campo("Data inicial", 160);
	private final Estilo.Campo txtFim = new Estilo.Campo("Data final", 160);
	private final Estilo.Campo txtAluno = new Estilo.Campo("Aluno (opcional)", 240);
	private final JTable tabela = Estilo.tabela("Data / Hora", "Aluno", "CPF", "Tipo", "Observação");
	private final JLabel lblTotal = new JLabel(" ", SwingConstants.CENTER);
	private final List<RegistroAcesso> registros = new ArrayList<>();

	public TelaRelatorios() {
		setLayout(new BorderLayout());
		setBackground(Estilo.FUNDO);

		JPanel pagina = Estilo.pagina();
		pagina.add(Estilo.cabecalho("RELATÓRIO DE\nFREQUÊNCIA", "Escolha o período e gere a lista de acessos pela catraca"), BorderLayout.NORTH);

		lblTotal.setFont(Estilo.fonte(Font.PLAIN, 13));
		lblTotal.setForeground(Estilo.TEXTO_SUAVE);

		JPanel centro = new JPanel(new BorderLayout(0, 14));
		centro.setOpaque(false);
		centro.add(Estilo.linha(FlowLayout.CENTER, txtInicio, txtFim, txtAluno,
				Estilo.botao("Gerar Relatório", this::gerar)), BorderLayout.NORTH);
		centro.add(Estilo.rolagem(tabela), BorderLayout.CENTER);
		centro.add(lblTotal, BorderLayout.SOUTH);
		pagina.add(centro, BorderLayout.CENTER);

		pagina.add(Estilo.linha(FlowLayout.RIGHT,
			Estilo.botao("Exportar CSV", this::exportar),
			Estilo.botaoSecundario("Voltar", () -> GymSingleton.getInstance().showTela("principal"))
		), BorderLayout.SOUTH);
		add(pagina);
	}

	/** Abre já com as frequências do mês atual (CSU10, passo 2). */
	@Override
	public void atualizar() {
		LocalDate hoje = LocalDate.now();
		txtInicio.setText(Estilo.data(hoje.withDayOfMonth(1)));
		txtFim.setText(Estilo.data(hoje));
		txtAluno.setText("");
		gerar();
	}

	private void gerar() {
		GymSingleton gym = GymSingleton.getInstance();
		gym.executar(() -> {
			LocalDate inicio = Estilo.lerData(txtInicio.getText(), "Data inicial");
			LocalDate fim = Estilo.lerData(txtFim.getText(), "Data final");
			String termo = txtAluno.getText().trim().toLowerCase();

			registros.clear();
			for (RegistroAcesso r : gym.acessos().listarPorPeriodo(inicio, fim)) {
				if (!termo.isEmpty() && (r.getAluno() == null
						|| !(r.getAluno().getNome().toLowerCase().contains(termo) || r.getAluno().getCpf().contains(termo))))
					continue;
				registros.add(r);
			}

			List<Object[]> linhas = new ArrayList<>();
			long entradas = 0;
			for (RegistroAcesso r : registros) {
				if (r.getTipo() == RegistroAcesso.Tipo.CHECK_IN) entradas++;
				linhas.add(new Object[]{Estilo.dataHora(r.getDataHora()),
						r.getAluno() == null ? "-" : r.getAluno().getNome(),
						r.getAluno() == null ? "-" : r.getAluno().getCpf(),
						Estilo.nome(r.getTipo()), r.getObservacao()});
			}
			Estilo.preencher(tabela, linhas);
			lblTotal.setText(registros.size() + " registros  ·  " + entradas + " entradas no período");
		});
	}

	private void exportar() {
		GymSingleton gym = GymSingleton.getInstance();
		if (registros.isEmpty()) { gym.showMsg("Não há frequências para exportar no período."); return; }

		JFileChooser escolha = new JFileChooser();
		escolha.setDialogTitle("Salvar frequências");
		escolha.setFileFilter(new FileNameExtensionFilter("Planilha CSV", "csv"));
		escolha.setSelectedFile(new File("frequencias-" + LocalDate.now() + ".csv"));
		if (escolha.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

		File arquivo = escolha.getSelectedFile();
		File destino = arquivo.getName().toLowerCase().endsWith(".csv") ? arquivo : new File(arquivo.getPath() + ".csv");
		if (gym.executar(() -> ExportadorCSV.exportarAcessos(registros, destino.toPath())))
			gym.showMsg("Arquivo exportado com sucesso");
	}
}
