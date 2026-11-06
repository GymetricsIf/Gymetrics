package br.com.gymetrics.telas;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import br.com.gymetrics.GymSingleton;
import br.com.gymetrics.negocio.Aluno;
import br.com.gymetrics.negocio.RegistroAcesso;

/** CSU08 / RF02-RF06, RF13, RF14, RF18: entrada e saída pela catraca usando CPF ou código. */
public class TelaCheckin extends JPanel implements Atualizavel {
	private static final int LARGURA_RESULTADO = 440;

	private final Estilo.Campo txtCodigo = new Estilo.Campo("CPF ou código de acesso", 420);
	private final JLabel lblResultado = new JLabel(" ", SwingConstants.CENTER);
	private final JLabel lblDetalhe = new JLabel(" ", SwingConstants.CENTER);
	private final JPanel painelResultado = new JPanel(new BorderLayout(0, 4));

	public TelaCheckin() {
		setLayout(new BorderLayout());
		setBackground(Estilo.FUNDO);

		JPanel pagina = Estilo.pagina();
		pagina.add(Estilo.cabecalho("CHECK-IN\nDE ALUNOS", "Digite seu código ou CPF para registrar a entrada ou a saída"), BorderLayout.NORTH);

		txtCodigo.setFont(Estilo.fonte(Font.BOLD, 24));
		txtCodigo.setPreferredSize(new Dimension(420, 56));
		txtCodigo.addActionListener(e -> confirmar());

		JPanel teclado = new JPanel(new GridLayout(4, 3, 8, 8));
		teclado.setOpaque(false);
		for (String k : new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "C", "0", "←"}) {
			Estilo.Botao b = new Estilo.Botao(k, Estilo.CAMPO);
			b.setFont(Estilo.fonte(Font.BOLD, 20));
			b.setFocusable(false);
			b.addActionListener(e -> tecla(k));
			teclado.add(b);
		}
		teclado.setPreferredSize(new Dimension(420, 232));

		lblResultado.setFont(Estilo.fonte(Font.BOLD, 40)); // RNF08: legível a 1 metro
		lblDetalhe.setFont(Estilo.fonte(Font.PLAIN, 18));
		painelResultado.setBorder(new EmptyBorder(18, 20, 18, 20));
		lblResultado.setVerticalAlignment(SwingConstants.BOTTOM);
		lblDetalhe.setVerticalAlignment(SwingConstants.TOP);
		JPanel textos = new JPanel(new GridLayout(2, 1, 0, 8));
		textos.setOpaque(false);
		textos.add(lblResultado);
		textos.add(lblDetalhe);
		painelResultado.add(textos, BorderLayout.CENTER);
		limparResultado();

		JPanel coluna = new JPanel();
		coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
		coluna.setOpaque(false);
		Estilo.Botao confirmar = Estilo.botaoDestaque("CONFIRMAR", Estilo.VERDE, 420, 64, this::confirmar);
		confirmar.setFocusable(false);
		JComponent[] itens = {txtCodigo, teclado, confirmar};
		for (int i = 0; i < itens.length; i++) {
			itens[i].setAlignmentX(CENTER_ALIGNMENT);
			itens[i].setMaximumSize(itens[i].getPreferredSize());
			coluna.add(itens[i]);
			if (i < itens.length - 1) coluna.add(Box.createVerticalStrut(12));
		}

		// Resultado ao lado do teclado: cabe inteiro mesmo na janela mínima (1024x680)
		painelResultado.setPreferredSize(new Dimension(LARGURA_RESULTADO, coluna.getPreferredSize().height));

		JPanel centro = new JPanel(new GridBagLayout());
		centro.setOpaque(false);
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(0, 12, 0, 12);
		centro.add(coluna, gc);
		centro.add(painelResultado, gc);
		pagina.add(centro, BorderLayout.CENTER);

		pagina.add(Estilo.linha(FlowLayout.RIGHT,
			Estilo.botaoSecundario("Voltar", () -> GymSingleton.getInstance().showTela("principal"))
		), BorderLayout.SOUTH);
		add(pagina);
	}

	private void tecla(String k) {
		String t = txtCodigo.getText();
		if (k.equals("C")) txtCodigo.setText("");
		else if (k.equals("←")) { if (!t.isEmpty()) txtCodigo.setText(t.substring(0, t.length() - 1)); }
		else txtCodigo.setText(t + k);
	}

	/** Um botão só, como a catraca: se o aluno já está dentro registra a saída, senão valida a entrada. */
	private void confirmar() {
		GymSingleton gym = GymSingleton.getInstance();
		String codigo = txtCodigo.getText().trim();
		if (codigo.isEmpty()) { gym.showMsg("Digite o CPF ou o código de acesso."); return; }

		gym.executar(() -> {
			Aluno aluno = gym.alunos().buscarPorCpfOuCodigo(codigo);
			if (aluno != null && gym.acessos().estaPresente(aluno, LocalDate.now())) {
				gym.acessos().registrarSaida(aluno);
				mostrar(true, "SAÍDA REGISTRADA", aluno.getNome() + " · até a próxima!");
			} else {
				RegistroAcesso r = gym.acessos().registrarEntrada(aluno); // RF06/RF18: grava liberação e tentativa negada
				if (r.getTipo() == RegistroAcesso.Tipo.CHECK_IN)
					mostrar(true, "ACESSO LIBERADO", aluno.getNome() + " · " + Estilo.nome(aluno.getStatus()));
				else if (aluno == null)
					mostrar(false, "ALUNO NÃO ENCONTRADO", "Confira o CPF ou código digitado");
				else
					mostrar(false, r.getObservacao().toUpperCase(), aluno.getNome() + " · procure a recepção");
			}
			txtCodigo.setText("");
		});
	}

	private void mostrar(boolean ok, String titulo, String detalhe) {
		Color cor = ok ? Estilo.VERDE : Estilo.VERMELHO;
		painelResultado.setOpaque(true);
		painelResultado.setBackground(cor);
		lblResultado.setForeground(Color.WHITE);
		lblDetalhe.setForeground(Color.WHITE);
		caber(lblResultado, titulo, 40);
		caber(lblDetalhe, detalhe, 18);
	}

	private void limparResultado() {
		painelResultado.setOpaque(true);
		painelResultado.setBackground(Estilo.CAMPO);
		lblResultado.setForeground(Estilo.TEXTO_SUAVE);
		lblDetalhe.setForeground(Estilo.TEXTO_SUAVE);
		caber(lblResultado, "AGUARDANDO", 40);
		caber(lblDetalhe, "O resultado do acesso aparece aqui", 18);
	}

	/** Diminui a fonte até o texto caber na largura do painel (o máximo mantém o RNF08). */
	private void caber(JLabel l, String texto, int tamanhoMaximo) {
		int largura = LARGURA_RESULTADO - 40;
		int tamanho = tamanhoMaximo;
		Font f = Estilo.fonte(l.getFont().getStyle(), tamanho);
		while (tamanho > 12 && l.getFontMetrics(f).stringWidth(texto) > largura)
			f = Estilo.fonte(l.getFont().getStyle(), --tamanho);
		l.setFont(f);
		l.setText(texto);
	}

	@Override
	public void atualizar() {
		txtCodigo.setText("");
		limparResultado();
		SwingUtilities.invokeLater(txtCodigo::requestFocusInWindow);
	}
}
