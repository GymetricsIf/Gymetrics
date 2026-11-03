package br.com.gymetrics.tela;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import br.com.gymetrics.GymSingleton;

public class TelaLogin extends JPanel {
	public TelaLogin() {
		setLayout(new GridBagLayout()); setBackground(Color.WHITE);

		JPanel card = new JPanel(new GridLayout(0, 1, 8, 8)); card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(220, 220, 220)), new EmptyBorder(30, 40, 30, 40)
		));

		JLabel titulo = new JLabel("GYMETRICS", SwingConstants.CENTER); titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		JLabel subtitulo = new JLabel("ACESSO AO SISTEMA", SwingConstants.CENTER); subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 10)); subtitulo.setForeground(Color.GRAY);
		card.add(titulo); card.add(subtitulo);

		JLabel lblUser = new JLabel("Usuário:"); lblUser.setFont(new Font("SansSerif", Font.BOLD, 12)); card.add(lblUser);
		JTextField txtUser = new JTextField(15); estilizarCampo(txtUser); card.add(txtUser);

		JLabel lblSenha = new JLabel("Senha:"); lblSenha.setFont(new Font("SansSerif", Font.BOLD, 12)); card.add(lblSenha);
		JPasswordField txtSenha = new JPasswordField(15); estilizarCampo(txtSenha); card.add(txtSenha);

		JButton btnEntrar = new JButton("Entrar"); btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEntrar.setBackground(new Color(40, 40, 40)); btnEntrar.setForeground(Color.WHITE); btnEntrar.setFocusable(false);
		btnEntrar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), new EmptyBorder(8, 0, 8, 0)));
		btnEntrar.addActionListener(e -> GymSingleton.getInstance().showTela("principal"));
		
		card.add(Box.createVerticalStrut(5)); card.add(btnEntrar);
		add(card);
	}

	private void estilizarCampo(JTextField campo) {
		campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
		campo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 8, 5, 8)));
	}
}