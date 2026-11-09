package br.com.gymetrics.telas;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import br.com.gymetrics.GymSingleton;
import br.com.gymetrics.negocio.Plano;

/** CSU06 / RF11: cadastro de planos de mensalidade (somente administrador). */
public class TelaGerenciamentoPlanos extends JPanel implements Atualizavel {
	private final Estilo.Campo txtNome = new Estilo.Campo("Nome do plano", 240);
	private final Estilo.Campo txtValor = new Estilo.Campo("Valor mensal", 160);
	private final Estilo.Campo txtDia = new Estilo.Campo("Dia de vencimento", 170);
	private final JTable tabela = Estilo.tabela("Plano", "Valor Mensal", "Dia de Vencimento", "Situação");
	private List<Plano> planos = new ArrayList<>();

	public TelaGerenciamentoPlanos() {
		setLayout(new BorderLayout());
		setBackground(Estilo.FUNDO);

		JPanel pagina = Estilo.pagina();
		pagina.add(Estilo.cabecalho("PLANOS\nCADASTRADOS", "Cadastre e gerencie os planos da academia"), BorderLayout.NORTH);

		JPanel centro = new JPanel(new BorderLayout(0, 14));
		centro.setOpaque(false);
		centro.add(Estilo.linha(FlowLayout.CENTER, txtNome, txtValor, txtDia), BorderLayout.NORTH);
		centro.add(Estilo.rolagem(tabela), BorderLayout.CENTER);
		pagina.add(centro, BorderLayout.CENTER);

		pagina.add(Estilo.linha(FlowLayout.RIGHT,
			Estilo.botao("Novo Plano", this::cadastrar),
			Estilo.botao("Salvar Alteração", this::editar),
			Estilo.botao("Ativar / Desativar", this::alternar),
			Estilo.botaoSecundario("Limpar", this::limpar),
			Estilo.botaoSecundario("Voltar", () -> GymSingleton.getInstance().showTela("principal"))
		), BorderLayout.SOUTH);
		add(pagina);

		// Selecionar um plano preenche os campos para edição
		tabela.getSelectionModel().addListSelectionListener(e -> {
			if (e.getValueIsAdjusting() || tabela.getSelectedRow() < 0) return;
			Plano p = planos.get(tabela.getSelectedRow());
			txtNome.setText(p.getNome());
			txtValor.setText(p.getValorMensal().toPlainString().replace('.', ','));
			txtDia.setText(String.valueOf(p.getDiaVencimento()));
		});
	}

	@Override
	public void atualizar() {
		GymSingleton gym = GymSingleton.getInstance();
		if (!gym.isAdmin()) {
			gym.showMsg("Somente o administrador pode gerenciar planos.");
			SwingUtilities.invokeLater(() -> gym.showTela("principal"));
			return;
		}
		gym.executar(() -> {
			planos = gym.planos().listar();
			List<Object[]> linhas = new ArrayList<>();
			for (Plano p : planos)
				linhas.add(new Object[]{p.getNome(), Estilo.moeda(p.getValorMensal()),
						"Todo dia " + p.getDiaVencimento(), p.isAtivo() ? "Ativo" : "Inativo"});
			Estilo.preencher(tabela, linhas);
		});
	}

	private void lerCampos(Plano p) {
		p.setNome(txtNome.getText().trim());
		p.setValorMensal(Estilo.lerValor(txtValor.getText(), "Valor mensal"));
		try {
			p.setDiaVencimento(Integer.parseInt(txtDia.getText().trim()));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Dia de vencimento deve ser um número entre 1 e 31");
		}
	}

	private void cadastrar() {
		GymSingleton gym = GymSingleton.getInstance();
		Plano p = new Plano();
		if (gym.executar(() -> { lerCampos(p); gym.planos().salvar(p); })) {
			gym.showMsg("Plano cadastrado com sucesso");
			limpar();
			atualizar();
		}
	}

	private void editar() {
		GymSingleton gym = GymSingleton.getInstance();
		int i = tabela.getSelectedRow();
		if (i < 0) { gym.showMsg("Selecione um plano na tabela."); return; }
		Plano p = planos.get(i);
		if (gym.executar(() -> { lerCampos(p); gym.planos().atualizar(p); })) {
			gym.showMsg("Plano atualizado com sucesso");
			limpar();
		}
		atualizar();
	}

	private void alternar() {
		GymSingleton gym = GymSingleton.getInstance();
		int i = tabela.getSelectedRow();
		if (i < 0) { gym.showMsg("Selecione um plano na tabela."); return; }
		Plano p = planos.get(i);
		p.setAtivo(!p.isAtivo());
		gym.executar(() -> gym.planos().atualizar(p));
		limpar();
		atualizar();
	}

	private void limpar() {
		tabela.clearSelection();
		txtNome.setText("");
		txtValor.setText("");
		txtDia.setText("");
	}
}
