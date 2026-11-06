package br.com.gymetrics.telas;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import br.com.gymetrics.GymSingleton;
import br.com.gymetrics.negocio.Aluno;
import br.com.gymetrics.negocio.Plano;

/** Protótipo da seção 4.1 (página 42). CSU01, CSU02, CSU05, CSU07 / RF01, RF11, RF13, RF16. */
public class TelaGerenciamentoAlunos extends JPanel implements Atualizavel {
	private final Estilo.Campo txtBusca = new Estilo.Campo("Buscar por nome ou CPF", 340);
	private final JTable tabela = Estilo.tabela("Nome do Aluno", "CPF", "Telefone", "Plano Vigente", "Situação");
	private List<Aluno> alunos = new ArrayList<>();

	public TelaGerenciamentoAlunos() {
		setLayout(new BorderLayout());
		setBackground(Estilo.FUNDO);

		JPanel pagina = Estilo.pagina();
		pagina.add(Estilo.cabecalho("ALUNOS\nCADASTRADOS", "Se vira com teus alunos ai, os dados estão abaixo (:"), BorderLayout.NORTH);

		JPanel centro = new JPanel(new BorderLayout(0, 14));
		centro.setOpaque(false);
		centro.add(Estilo.linha(FlowLayout.CENTER, txtBusca), BorderLayout.NORTH);
		centro.add(Estilo.rolagem(tabela), BorderLayout.CENTER);
		pagina.add(centro, BorderLayout.CENTER);

		pagina.add(Estilo.linha(FlowLayout.RIGHT,
			Estilo.botao("Cadastrar Aluno", this::cadastrar),
			Estilo.botao("Editar", this::editar),
			Estilo.botao("Bloquear Acesso", this::bloquear),
			Estilo.botaoSecundario("Voltar", () -> GymSingleton.getInstance().showTela("principal"))
		), BorderLayout.SOUTH);
		add(pagina);

		// CSU02: filtra em tempo real enquanto digita
		txtBusca.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void insertUpdate(DocumentEvent e) { atualizar(); }
			@Override public void removeUpdate(DocumentEvent e) { atualizar(); }
			@Override public void changedUpdate(DocumentEvent e) { atualizar(); }
		});
		tabela.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) editar(); }
		});
	}

	@Override
	public void atualizar() {
		GymSingleton.getInstance().executar(() -> {
			alunos = GymSingleton.getInstance().alunos().buscar(txtBusca.getText().trim());
			List<Object[]> linhas = new ArrayList<>();
			for (Aluno a : alunos)
				linhas.add(new Object[]{a.getNome(), a.getCpf(), a.getTelefone(),
						a.getPlano() == null ? "Sem plano" : a.getPlano().getNome(), Estilo.nome(a.getStatus())});
			Estilo.preencher(tabela, linhas);
		});
	}

	private Aluno selecionado() {
		int i = tabela.getSelectedRow();
		if (i < 0) {
			GymSingleton.getInstance().showMsg("Selecione um aluno na tabela.");
			return null;
		}
		return alunos.get(tabela.convertRowIndexToModel(i));
	}

	private void cadastrar() {
		Aluno a = new Aluno();
		if (abrirFormulario(a, "Cadastrar Aluno", false)) {
			if (GymSingleton.getInstance().executar(() -> GymSingleton.getInstance().alunos().salvar(a))) {
				GymSingleton.getInstance().showMsg("Aluno cadastrado com sucesso");
				atualizar();
			}
		}
	}

	private void editar() {
		Aluno a = selecionado();
		if (a == null) return;
		if (abrirFormulario(a, "Editar Aluno", true)) {
			if (GymSingleton.getInstance().executar(() -> GymSingleton.getInstance().alunos().atualizar(a))) {
				GymSingleton.getInstance().showMsg("Dados atualizados com sucesso");
			}
			atualizar();
		}
	}

	private void bloquear() {
		Aluno a = selecionado();
		if (a == null) return;
		if (a.getStatus() == Aluno.Status.BLOQUEADO) {
			GymSingleton.getInstance().showMsg("Este aluno já está bloqueado.");
			return;
		}
		String motivo = JOptionPane.showInputDialog(this, "Motivo do bloqueio de " + a.getNome() + ":",
				"Bloquear Acesso", JOptionPane.PLAIN_MESSAGE);
		if (motivo == null) return;
		GymSingleton gym = GymSingleton.getInstance();
		if (gym.executar(() -> gym.alunos().bloquear(a, motivo.trim(), gym.getUsuarioLogado()))) {
			gym.showMsg("Acesso de " + a.getNome() + " bloqueado.");
			atualizar();
		}
	}

	/** Preenche o aluno com o que foi digitado. Repete o formulário enquanto houver erro de digitação. */
	private boolean abrirFormulario(Aluno a, String titulo, boolean edicao) {
		GymSingleton gym = GymSingleton.getInstance();
		List<Plano> planos = new ArrayList<>();
		if (!gym.executar(() -> {
			for (Plano p : gym.planos().listar())
				if (p.isAtivo() || (a.getPlano() != null && a.getPlano().getId() == p.getId())) planos.add(p);
		})) return false;

		Estilo.Campo nome = new Estilo.Campo(null, 300);
		Estilo.Campo cpf = new Estilo.Campo("000.000.000-00", 300);
		Estilo.Campo endereco = new Estilo.Campo(null, 300);
		Estilo.Campo telefone = new Estilo.Campo("(00) 00000-0000", 300);
		Estilo.Campo nascimento = new Estilo.Campo("dd/mm/aaaa", 300);
		Estilo.Campo codigo = new Estilo.Campo("somente números", 300);

		String[] nomesPlanos = new String[planos.size() + 1];
		nomesPlanos[0] = "Sem plano";
		int planoAtual = 0;
		for (int i = 0; i < planos.size(); i++) {
			Plano p = planos.get(i);
			nomesPlanos[i + 1] = p.getNome() + " - " + Estilo.moeda(p.getValorMensal());
			if (a.getPlano() != null && a.getPlano().getId() == p.getId()) planoAtual = i + 1;
		}
		JComboBox<String> plano = Estilo.combo(nomesPlanos);
		plano.setSelectedIndex(planoAtual);
		JComboBox<Aluno.Status> status = Estilo.combo(Aluno.Status.values());
		status.setSelectedItem(a.getStatus());

		nome.setText(a.getNome()); cpf.setText(a.getCpf()); endereco.setText(a.getEndereco());
		telefone.setText(a.getTelefone()); nascimento.setText(Estilo.data(a.getDataNascimento()));
		codigo.setText(a.getCodigoAcesso());

		String[] rotulos = {"Nome", "CPF", "Endereço", "Telefone", "Nascimento", "Código de acesso", "Plano"};
		JComponent[] campos = {nome, cpf, endereco, telefone, nascimento, codigo, plano};
		if (edicao) {
			rotulos = java.util.Arrays.copyOf(rotulos, rotulos.length + 1);
			campos = java.util.Arrays.copyOf(campos, campos.length + 1);
			rotulos[rotulos.length - 1] = "Situação";
			campos[campos.length - 1] = status;
		}

		while (Estilo.formulario(this, titulo, rotulos, campos)) {
			boolean ok = gym.executar(() -> {
				a.setNome(nome.getText().trim());
				a.setCpf(cpf.getText().trim());
				a.setEndereco(endereco.getText().trim());
				a.setTelefone(telefone.getText().trim());
				a.setDataNascimento(Estilo.lerData(nascimento.getText(), "Data de nascimento"));
				a.setCodigoAcesso(codigo.getText().isBlank() ? null : codigo.getText().trim());
				a.setPlano(plano.getSelectedIndex() == 0 ? null : planos.get(plano.getSelectedIndex() - 1));
				if (edicao) a.setStatus((Aluno.Status) status.getSelectedItem());
				a.validar();
			});
			if (ok) return true;
		}
		return false;
	}
}
