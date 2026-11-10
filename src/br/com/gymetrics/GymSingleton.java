package br.com.gymetrics;

import java.awt.CardLayout;
import java.awt.Component;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.com.gymetrics.dao.*;
import br.com.gymetrics.negocio.Usuario;
import br.com.gymetrics.telas.*;

public class GymSingleton {
	public static final String ADMINISTRADOR = "ADMINISTRADOR";
	public static final String RECEPCIONISTA = "RECEPCIONISTA";

	private static GymSingleton instance = null;

	private final CardLayout telasLayout = new CardLayout();
	private final JPanel telas = new JPanel(telasLayout);
	private final Map<String, Component> porNome = new HashMap<>();
	private boolean mp = false;

	private final Data data = new Data();
	private final AlunoDAO alunoDAO = new AlunoDAO(data);
	private final PlanoDAO planoDAO = new PlanoDAO(data);
	private final UsuarioDAO usuarioDAO = new UsuarioDAO(data);
	private final PagamentoDAO pagamentoDAO = new PagamentoDAO(data);
	private final MensalidadeDAO mensalidadeDAO = new MensalidadeDAO(data);
	private final RegistroAcessoDAO registroAcessoDAO = new RegistroAcessoDAO(data);

	private Usuario usuarioLogado = null;

	private GymSingleton() {
		System.out.println("Gym Core!");

		try {
			data.open("gymetrics.db");
			data.criarTabelas();
			criarUsuariosIniciais();
			mensalidadeDAO.prepararMes(LocalDate.now());
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível abrir o banco de dados", e);
		}

		// As telas não podem chamar getInstance() no construtor: a instância ainda está sendo criada.
		adicionar(new TelaDebug(), "debug");
		adicionar(new TelaLogin(), "login");
		adicionar(new TelaPrincipal(), "principal");
		adicionar(new TelaCheckin(), "check-in");
		adicionar(new TelaGerenciamentoPlanos(), "genPlanos");
		adicionar(new TelaGerenciamentoAlunos(), "genAlunos");
		adicionar(new TelaGerenciamentoFinanceiro(), "genFinanceiro");
		adicionar(new TelaRelatorios(), "relatorios");
	}

	/** Banco novo: cria um administrador e um recepcionista para o primeiro acesso. */
	private void criarUsuariosIniciais() throws SQLException {
		if (usuarioDAO.contar() > 0) return;
		usuarioDAO.salvar(new Usuario(0, "admin", "admin123", "Administrador", ADMINISTRADOR, true));
		usuarioDAO.salvar(new Usuario(0, "recepcao", "recepcao123", "Recepcionista", RECEPCIONISTA, true));
	}

	private void adicionar(Component tela, String nome) {
		telas.add(tela, nome);
		porNome.put(nome, tela);
	}

	public void showTela(String nome) {
		System.out.printf("> mostrando %s!\n", nome);
		Component tela = porNome.get(nome);
		if (tela instanceof Atualizavel a) a.atualizar();
		telasLayout.show(telas, nome);
	}

	// ---------- sessão ----------

	public Usuario getUsuarioLogado() { return usuarioLogado; }

	public void setUsuarioLogado(Usuario u) { this.usuarioLogado = u; }

	public boolean isAdmin() {
		return usuarioLogado != null && ADMINISTRADOR.equals(usuarioLogado.getPerfil());
	}

	public void sair() {
		usuarioLogado = null;
		showTela("login");
	}

	// ---------- acesso a dados ----------

	public Data getData() { return data; }
	public AlunoDAO alunos() { return alunoDAO; }
	public PlanoDAO planos() { return planoDAO; }
	public UsuarioDAO usuarios() { return usuarioDAO; }
	public PagamentoDAO pagamentos() { return pagamentoDAO; }
	public MensalidadeDAO mensalidades() { return mensalidadeDAO; }
	public RegistroAcessoDAO acessos() { return registroAcessoDAO; }

	// ---------- mensagens ----------

	public void showMsg(String text) {
		JOptionPane.showMessageDialog(telas, text, "Gymetrics", JOptionPane.INFORMATION_MESSAGE);
	}

	/** Mensagem de erro em linguagem simples, sem termos técnicos (RNF07). */
	public void showErro(Exception e) {
		String msg;
		if (e instanceof IllegalArgumentException) msg = e.getMessage();
		else if (e instanceof SQLException && e.getMessage() != null && e.getMessage().contains("UNIQUE"))
			msg = "Já existe um cadastro com esse CPF ou código de acesso.";
		else {
			e.printStackTrace();
			msg = "Não foi possível concluir a operação. Tente novamente.";
		}
		JOptionPane.showMessageDialog(telas, msg, "Gymetrics", JOptionPane.WARNING_MESSAGE);
	}

	public interface Acao {
		void run() throws Exception;
	}

	/** Executa uma ação da tela mostrando o erro de forma amigável. Retorna true se deu certo. */
	public boolean executar(Acao acao) {
		try {
			acao.run();
			return true;
		} catch (Exception e) {
			showErro(e);
			return false;
		}
	}

	public JPanel takeFrame() {
		if (mp) throw new Error("BUG: Alguem ja tem o frame das telas owned");
		mp = true;
		return this.telas;
	}

	public static GymSingleton getInstance() {
		if (instance == null) {
			instance = new GymSingleton();
		}
		return instance;
	}
}
