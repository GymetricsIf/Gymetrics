package br.com.gymetrics.telas;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/** Visual do protótipo (documento, seção 4.1): campos cinza, botão cinza escuro, tabela com cabeçalho cinza. */
public final class Estilo {
	private Estilo() {}

	public static final Color FUNDO = Color.WHITE;
	public static final Color TEXTO = new Color(0x26, 0x26, 0x26);
	public static final Color TEXTO_SUAVE = new Color(0x5A, 0x5A, 0x5A);
	public static final Color PLACEHOLDER = new Color(0x6E, 0x6E, 0x6E);
	public static final Color CAMPO = new Color(0xE8, 0xE8, 0xE8);
	public static final Color BOTAO = new Color(0xB4, 0xB4, 0xB4);
	public static final Color BOTAO_HOVER = new Color(0xA3, 0xA3, 0xA3);
	public static final Color CABECALHO = new Color(0xE6, 0xE6, 0xE6);
	public static final Color LINHA = new Color(0xD4, 0xD4, 0xD4);
	public static final Color SELECAO = new Color(0xDD, 0xE3, 0xEA);
	public static final Color AVATAR = new Color(0xDD, 0xDD, 0xDD);
	public static final Color VERDE = new Color(0x2E, 0x8B, 0x57);
	public static final Color VERMELHO = new Color(0xC0, 0x39, 0x2B);
	public static final Color AMARELO = new Color(0xFF, 0xF4, 0xCE);

	private static final String FAMILIA = escolherFamilia();
	private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

	private static String escolherFamilia() {
		String[] disponiveis = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (String f : new String[]{"Inter", "Segoe UI", "Helvetica Neue", "Arial"})
			if (Arrays.asList(disponiveis).contains(f)) return f;
		return Font.SANS_SERIF;
	}

	public static Font fonte(int estilo, float tamanho) {
		return new Font(FAMILIA, estilo, Math.round(tamanho));
	}

	// ---------- estrutura de página ----------

	public static JPanel pagina() {
		JPanel p = new JPanel(new BorderLayout(0, 18));
		p.setBackground(FUNDO);
		p.setBorder(new EmptyBorder(28, 40, 24, 40));
		return p;
	}

	/** Título grande (aceita quebra de linha com \n) + subtítulo pequeno em maiúsculas. */
	public static JPanel cabecalho(String titulo, String subtitulo) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setOpaque(false);

		JLabel t = new JLabel("<html><div style='text-align:center'>" + titulo.replace("\n", "<br>") + "</div></html>");
		t.setFont(fonte(Font.BOLD, 30));
		t.setForeground(TEXTO);
		t.setHorizontalAlignment(SwingConstants.CENTER);
		t.setAlignmentX(Component.CENTER_ALIGNMENT);
		t.setMaximumSize(t.getPreferredSize());
		p.add(t);

		if (subtitulo != null) {
			p.add(Box.createVerticalStrut(6));
			JLabel s = new JLabel(subtitulo.toUpperCase());
			s.setFont(fonte(Font.PLAIN, 10));
			s.setForeground(TEXTO_SUAVE);
			s.setAlignmentX(Component.CENTER_ALIGNMENT);
			p.add(s);
		}
		return p;
	}

	public static JPanel linha(int alinhamento, Component... itens) {
		JPanel p = new JPanel(new FlowLayout(alinhamento, 10, 0));
		p.setOpaque(false);
		for (Component c : itens) p.add(c);
		return p;
	}

	public static JLabel rotulo(String texto) {
		JLabel l = new JLabel(texto);
		l.setFont(fonte(Font.BOLD, 12));
		l.setForeground(TEXTO);
		return l;
	}

	// ---------- campos ----------

	public static class Campo extends JTextField {
		private final String placeholder;

		public Campo(String placeholder, int largura) {
			this.placeholder = placeholder;
			estilizarCampo(this, largura);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			pintarPlaceholder(g, this, placeholder);
		}
	}

	public static class CampoSenha extends JPasswordField {
		private final String placeholder;

		public CampoSenha(String placeholder, int largura) {
			this.placeholder = placeholder;
			estilizarCampo(this, largura);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			pintarPlaceholder(g, this, placeholder);
		}
	}

	private static void estilizarCampo(JTextField c, int largura) {
		c.setFont(fonte(Font.PLAIN, 14));
		c.setForeground(TEXTO);
		c.setBackground(CAMPO);
		c.setCaretColor(TEXTO);
		c.setHorizontalAlignment(JTextField.CENTER);
		c.setBorder(new EmptyBorder(8, 12, 8, 12));
		c.setPreferredSize(new Dimension(largura, 44));
		c.setMaximumSize(new Dimension(largura, 44));
	}

	private static void pintarPlaceholder(Graphics g, JTextField c, String texto) {
		if (texto == null || !c.getText().isEmpty()) return;
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setFont(c.getFont());
		g2.setColor(PLACEHOLDER);
		FontMetrics fm = g2.getFontMetrics();
		int x = (c.getWidth() - fm.stringWidth(texto)) / 2;
		int y = (c.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
		g2.drawString(texto, x, y);
		g2.dispose();
	}

	public static <T> JComboBox<T> combo(T[] itens) {
		JComboBox<T> c = new JComboBox<>(itens);
		c.setFont(fonte(Font.PLAIN, 13));
		c.setBackground(CAMPO);
		c.setForeground(TEXTO);
		c.setBorder(BorderFactory.createEmptyBorder());
		c.setPreferredSize(new Dimension(170, 44));
		return c;
	}

	// ---------- botões ----------

	public static class Botao extends JButton {
		private final Color cor;
		private Color corTexto = TEXTO;
		private boolean hover;

		public Botao(String texto) {
			this(texto, BOTAO);
		}

		public Botao(String texto, Color cor) {
			super(texto);
			this.cor = cor;
			setFont(fonte(Font.BOLD, 14));
			setForeground(TEXTO);
			setContentAreaFilled(false);
			setBorderPainted(false);
			setFocusPainted(false);
			setOpaque(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			setBorder(new EmptyBorder(10, 22, 10, 22));
			addMouseListener(new MouseAdapter() {
				@Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
				@Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
			});
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension d = super.getPreferredSize();
			return new Dimension(Math.max(d.width, 120), Math.max(d.height, 44));
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			Color fundo = !isEnabled() ? CAMPO : hover ? cor.darker() : cor;
			if (cor == BOTAO && hover && isEnabled()) fundo = BOTAO_HOVER;
			g2.setColor(fundo);
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.dispose();
			setForeground(isEnabled() ? corTexto : PLACEHOLDER);
			super.paintComponent(g);
		}

		public Botao comTexto(Color c) {
			corTexto = c;
			return this;
		}
	}

	/** Botão grande de destaque (ex.: Confirmar do check-in). */
	public static Botao botaoDestaque(String texto, Color cor, int largura, int altura, Runnable acao) {
		Botao b = new Botao(texto, cor).comTexto(Color.WHITE);
		b.setFont(fonte(Font.BOLD, 22));
		b.setPreferredSize(new Dimension(largura, altura));
		b.setMaximumSize(new Dimension(largura, altura));
		b.addActionListener(e -> acao.run());
		return b;
	}

	/** Botão principal (cinza escuro do protótipo). */
	public static Botao botao(String texto, Runnable acao) {
		Botao b = new Botao(texto);
		b.addActionListener(e -> acao.run());
		return b;
	}

	/** Botão secundário (cinza claro, mesma cor dos campos). */
	public static Botao botaoSecundario(String texto, Runnable acao) {
		Botao b = new Botao(texto, CAMPO);
		b.addActionListener(e -> acao.run());
		return b;
	}

	// ---------- tabela ----------

	public static JTable tabela(String... colunas) {
		DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
			@Override public boolean isCellEditable(int r, int c) { return false; }
		};
		JTable t = new JTable(modelo);
		t.setFont(fonte(Font.PLAIN, 13));
		t.setForeground(TEXTO);
		t.setRowHeight(44);
		t.setShowGrid(true);
		t.setGridColor(LINHA);
		t.setIntercellSpacing(new Dimension(1, 1));
		t.setSelectionBackground(SELECAO);
		t.setSelectionForeground(TEXTO);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.setFillsViewportHeight(true);
		t.setBackground(FUNDO);

		DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
		centro.setHorizontalAlignment(SwingConstants.CENTER);
		t.setDefaultRenderer(Object.class, centro);

		JTableHeader h = t.getTableHeader();
		h.setReorderingAllowed(false);
		h.setPreferredSize(new Dimension(0, 44));
		h.setDefaultRenderer((tab, valor, sel, foco, lin, col) -> {
			JLabel l = new JLabel(valor == null ? "" : valor.toString(), SwingConstants.CENTER);
			l.setOpaque(true);
			l.setBackground(CABECALHO);
			l.setForeground(TEXTO);
			l.setFont(fonte(Font.BOLD, 13));
			l.setBorder(new MatteBorder(0, col == 0 ? 0 : 1, 1, 0, LINHA));
			return l;
		});
		return t;
	}

	public static JScrollPane rolagem(JTable t) {
		JScrollPane s = new JScrollPane(t);
		s.setColumnHeaderView(t.getTableHeader());
		s.setBorder(BorderFactory.createEmptyBorder());
		s.getViewport().setBackground(FUNDO);
		s.getVerticalScrollBar().setUnitIncrement(16);
		return s;
	}

	public static void preencher(JTable t, java.util.List<Object[]> linhas) {
		DefaultTableModel m = (DefaultTableModel) t.getModel();
		m.setRowCount(0);
		for (Object[] l : linhas) m.addRow(l);
	}

	// ---------- ícones ----------

	/** Círculo cinza com o ícone de usuário, igual ao topo do protótipo de login. */
	public static JComponent avatar(int tamanho) {
		JComponent c = new JComponent() {
			@Override protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				int x = (getWidth() - tamanho) / 2;
				g2.setColor(AVATAR);
				g2.fill(new Ellipse2D.Double(x, 0, tamanho - 1, tamanho - 1));
				g2.setColor(new Color(0xC6, 0xC6, 0xC6));
				g2.draw(new Ellipse2D.Double(x, 0, tamanho - 1, tamanho - 1));
				desenharPessoa(g2, x + tamanho / 2.0, tamanho / 2.0, tamanho * 0.36, new Color(0x44, 0x44, 0x44));
				g2.dispose();
			}
		};
		c.setPreferredSize(new Dimension(tamanho, tamanho));
		c.setMaximumSize(new Dimension(tamanho, tamanho));
		c.setAlignmentX(Component.CENTER_ALIGNMENT);
		return c;
	}

	private static void desenharPessoa(Graphics2D g2, double cx, double cy, double alt, Color cor) {
		g2.setColor(cor);
		g2.setStroke(new BasicStroke((float) Math.max(1.5, alt / 12), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		double r = alt * 0.24;
		g2.draw(new Ellipse2D.Double(cx - r, cy - alt / 2, r * 2, r * 2));
		double w = alt * 0.8, h = alt * 0.38;
		g2.draw(new RoundRectangle2D.Double(cx - w / 2, cy + alt / 2 - h, w, h, h, h));
	}

	/** Ícone da janela (o mesmo bonequinho da barra de título do protótipo). */
	public static Image iconeJanela() {
		BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		desenharPessoa(g2, 32, 32, 44, new Color(0x33, 0x33, 0x33));
		g2.dispose();
		return img;
	}

	// ---------- formatação ----------

	public static String data(LocalDate d) {
		return d == null ? "" : DATA.format(d);
	}

	public static String dataHora(LocalDateTime d) {
		return d == null ? "" : DATA_HORA.format(d);
	}

	public static String moeda(BigDecimal v) {
		return v == null ? "" : MOEDA.format(v);
	}

	/** ATIVO -> Ativo, CHECK_IN -> Check in */
	public static String nome(Enum<?> e) {
		if (e == null) return "";
		String s = e.name().replace('_', ' ').toLowerCase();
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	public static LocalDate lerData(String texto, String campo) {
		if (texto == null || texto.isBlank()) return null;
		try {
			return LocalDate.parse(texto.trim(), DATA);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(campo + " deve estar no formato dd/mm/aaaa");
		}
	}

	public static BigDecimal lerValor(String texto, String campo) {
		try {
			return new BigDecimal(texto.trim().replace("R$", "").replace(" ", "").replace(".", "").replace(',', '.'));
		} catch (RuntimeException e) {
			throw new IllegalArgumentException(campo + " inválido");
		}
	}

	/** Formulário simples em diálogo: rótulo + campo por linha. Retorna true se confirmou. */
	public static boolean formulario(Component pai, String titulo, String[] rotulos, JComponent[] campos) {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBackground(FUNDO);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		for (int i = 0; i < rotulos.length; i++) {
			c.gridy = i;
			c.gridx = 0; c.weightx = 0;
			p.add(rotulo(rotulos[i]), c);
			c.gridx = 1; c.weightx = 1;
			p.add(campos[i], c);
		}
		return JOptionPane.showConfirmDialog(pai, p, titulo, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
	}
}
