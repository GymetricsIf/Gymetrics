package br.com.gymetrics.telas;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import br.com.gymetrics.GymSingleton;
import br.com.gymetrics.negocio.Aluno;
import br.com.gymetrics.negocio.Mensalidade;
import br.com.gymetrics.negocio.RegistroAcesso;

/** RF08, RF12, RF19, RF20: indicadores do dia, aviso de vencimentos e acessos de hoje. */
public class TelaPrincipal extends JPanel implements Atualizavel {
	private final JLabel lblUsuario = new JLabel(" ", SwingConstants.CENTER);
	private final JLabel[] kpis = new JLabel[4];
	private final JLabel lblAviso = new JLabel(" ");
	private final JPanel aviso = new JPanel(new BorderLayout());
	private final JTable tabela = Estilo.tabela("Horário", "Aluno", "CPF", "Tipo", "Observação");
	private final Estilo.Botao btnPlanos;

	public TelaPrincipal() {
		setLayout(new BorderLayout());
		setBackground(Estilo.FUNDO);

		JPanel pagina = Estilo.pagina();

		JPanel topo = new JPanel(new BorderLayout(0, 6));
		topo.setOpaque(false);
		topo.add(Estilo.cabecalho("GYMETRICS", "Painel geral e visão geral das atividades"), BorderLayout.CENTER);
		lblUsuario.setFont(Estilo.fonte(Font.PLAIN, 12));
		lblUsuario.setForeground(Estilo.TEXTO_SUAVE);
		topo.add(lblUsuario, BorderLayout.SOUTH);
		pagina.add(topo, BorderLayout.NORTH);

		JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
		cards.setOpaque(false);
		String[] nomes = {"Presentes agora", "Entradas hoje", "Bloqueados", "Vencem em 5 dias"};
		for (int i = 0; i < 4; i++) {
			JPanel card = new JPanel(new BorderLayout(0, 2));
			card.setBackground(Estilo.CAMPO);
			card.setBorder(new EmptyBorder(14, 10, 14, 10));
			kpis[i] = new JLabel("0", SwingConstants.CENTER);
			kpis[i].setFont(Estilo.fonte(Font.BOLD, 30));
			kpis[i].setForeground(Estilo.TEXTO);
			JLabel n = new JLabel(nomes[i].toUpperCase(), SwingConstants.CENTER);
			n.setFont(Estilo.fonte(Font.PLAIN, 10));
			n.setForeground(Estilo.TEXTO_SUAVE);
			card.add(kpis[i], BorderLayout.CENTER);
			card.add(n, BorderLayout.SOUTH);
			cards.add(card);
		}

		lblAviso.setFont(Estilo.fonte(Font.PLAIN, 13));
		lblAviso.setForeground(Estilo.TEXTO);
		aviso.setBackground(Estilo.AMARELO);
		aviso.setBorder(BorderFactory.createCompoundBorder(
				new MatteBorder(0, 4, 0, 0, new Color(0xE0, 0xB0, 0x20)), new EmptyBorder(10, 14, 10, 14)));
		aviso.add(lblAviso);

		JLabel tituloTabela = new JLabel("ACESSOS DE HOJE");
		tituloTabela.setFont(Estilo.fonte(Font.BOLD, 13));
		tituloTabela.setForeground(Estilo.TEXTO);

		JPanel alto = new JPanel();
		alto.setLayout(new BoxLayout(alto, BoxLayout.Y_AXIS));
		alto.setOpaque(false);
		for (JComponent c : new JComponent[]{cards, aviso, tituloTabela}) {
			c.setAlignmentX(LEFT_ALIGNMENT);
			alto.add(c);
			alto.add(Box.createVerticalStrut(12));
		}

		JPanel centro = new JPanel(new BorderLayout());
		centro.setOpaque(false);
		centro.add(alto, BorderLayout.NORTH);
		centro.add(Estilo.rolagem(tabela), BorderLayout.CENTER);
		pagina.add(centro, BorderLayout.CENTER);

		btnPlanos = Estilo.botao("Planos", () -> ir("genPlanos"));
		pagina.add(Estilo.linha(FlowLayout.CENTER,
			Estilo.botao("Check-in", () -> ir("check-in")),
			Estilo.botao("Alunos", () -> ir("genAlunos")),
			btnPlanos,
			Estilo.botao("Financeiro", () -> ir("genFinanceiro")),
			Estilo.botao("Frequências", () -> ir("relatorios")),
			Estilo.botaoSecundario("Sair", () -> GymSingleton.getInstance().sair())
		), BorderLayout.SOUTH);
		add(pagina);
	}

	private static void ir(String tela) {
		GymSingleton.getInstance().showTela(tela);
	}

	@Override
	public void atualizar() {
		GymSingleton gym = GymSingleton.getInstance();
		if (gym.getUsuarioLogado() == null) {
			SwingUtilities.invokeLater(() -> gym.showTela("login"));
			return;
		}
		boolean admin = gym.isAdmin();
		lblUsuario.setText("Olá, " + gym.getUsuarioLogado().getNome() + " (" + (admin ? "Administrador" : "Recepcionista") + ")");
		btnPlanos.setVisible(admin); // RNF13: só mostra o que o perfil pode usar

		LocalDate hoje = LocalDate.now();
		gym.executar(() -> {
			gym.mensalidades().prepararMes(hoje);
			List<Mensalidade> vencendo = gym.mensalidades().vencendo(hoje, 5);

			kpis[0].setText(String.valueOf(gym.acessos().contarPresentes(hoje)));
			kpis[1].setText(String.valueOf(gym.acessos().contarEntradas(hoje)));
			kpis[2].setText(String.valueOf(gym.alunos().contar(Aluno.Status.BLOQUEADO)));
			kpis[3].setText(String.valueOf(vencendo.size()));

			// RF12 / RNF09: aviso logo na abertura
			aviso.setVisible(!vencendo.isEmpty());
			if (!vencendo.isEmpty()) {
				StringBuilder s = new StringBuilder("<html><b>Mensalidades vencendo nos próximos 5 dias:</b> ");
				for (int i = 0; i < vencendo.size(); i++) {
					Mensalidade m = vencendo.get(i);
					if (i > 0) s.append(", ");
					if (i == 8) { s.append("e mais ").append(vencendo.size() - 8); break; }
					s.append(m.getAluno().getNome()).append(" (").append(Estilo.data(m.getVencimento())).append(")");
				}
				lblAviso.setText(s.append("</html>").toString());
			}

			List<Object[]> linhas = new ArrayList<>();
			for (RegistroAcesso r : gym.acessos().listarPorPeriodo(hoje, hoje))
				linhas.add(0, new Object[]{r.getDataHora().toLocalTime().toString(),
						r.getAluno() == null ? "-" : r.getAluno().getNome(),
						r.getAluno() == null ? "-" : r.getAluno().getCpf(),
						Estilo.nome(r.getTipo()), r.getObservacao()});
			Estilo.preencher(tabela, linhas);
		});
	}
}
