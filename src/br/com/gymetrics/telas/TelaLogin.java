package br.com.gymetrics.telas;

import java.awt.*;
import javax.swing.*;

import br.com.gymetrics.GymSingleton;
import br.com.gymetrics.negocio.Usuario;

/** Protótipo da seção 4.1 (página 41 do documento). CSU03 / RF10. */
public class TelaLogin extends JPanel implements Atualizavel {
	private final Estilo.Campo txtUsuario = new Estilo.Campo("Usuario", 280);
	private final Estilo.CampoSenha txtSenha = new Estilo.CampoSenha("Senha", 280);

	public TelaLogin() {
		setLayout(new GridBagLayout());
		setBackground(Estilo.FUNDO);

		JPanel coluna = new JPanel();
		coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
		coluna.setOpaque(false);

		coluna.add(Estilo.avatar(72));
		coluna.add(Box.createVerticalStrut(14));

		JLabel titulo = new JLabel("CADASTRO");
		titulo.setFont(Estilo.fonte(Font.BOLD, 34));
		titulo.setForeground(Estilo.TEXTO);
		titulo.setAlignmentX(CENTER_ALIGNMENT);
		coluna.add(titulo);

		JLabel subtitulo = new JLabel("Digite seus dados");
		subtitulo.setFont(Estilo.fonte(Font.PLAIN, 10));
		subtitulo.setForeground(Estilo.TEXTO_SUAVE);
		subtitulo.setAlignmentX(CENTER_ALIGNMENT);
		coluna.add(subtitulo);

		coluna.add(Box.createVerticalStrut(28));
		txtUsuario.setAlignmentX(CENTER_ALIGNMENT);
		coluna.add(txtUsuario);
		coluna.add(Box.createVerticalStrut(10));
		txtSenha.setAlignmentX(CENTER_ALIGNMENT);
		coluna.add(txtSenha);
		coluna.add(Box.createVerticalStrut(32));

		Estilo.Botao entrar = Estilo.botao("Entrar", this::entrar);
		entrar.setFont(Estilo.fonte(Font.BOLD, 16));
		entrar.setPreferredSize(new Dimension(280, 46));
		entrar.setMaximumSize(new Dimension(280, 46));
		entrar.setAlignmentX(CENTER_ALIGNMENT);
		coluna.add(entrar);

		txtUsuario.addActionListener(e -> txtSenha.requestFocusInWindow());
		txtSenha.addActionListener(e -> entrar());

		add(coluna);
	}

	private void entrar() {
		GymSingleton gym = GymSingleton.getInstance();
		String login = txtUsuario.getText();
		String senha = new String(txtSenha.getPassword());

		gym.executar(() -> {
			if (login.isBlank() || senha.isEmpty())
				throw new IllegalArgumentException("Informe o usuário e a senha.");

			Usuario u = gym.usuarios().autenticar(login, senha);
			if (u == null) {
				txtSenha.setText("");
				throw new IllegalArgumentException(gym.usuarios().estaBloqueado(login)
						? "Usuário bloqueado após 10 tentativas erradas. Procure o administrador."
						: "Usuário ou senha incorretos.");
			}
			gym.setUsuarioLogado(u);
			gym.showTela("principal");
		});
	}

	@Override
	public void atualizar() {
		txtUsuario.setText("");
		txtSenha.setText("");
		SwingUtilities.invokeLater(txtUsuario::requestFocusInWindow);
	}
}
