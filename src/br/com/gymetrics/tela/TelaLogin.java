package br.com.gymetrics.tela;

import java.awt.*;
import javax.swing.*;

import br.com.gymetrics.GymSingleton;

public class TelaLogin extends JPanel {
	public TelaLogin() {
		this.setLayout(new GridBagLayout());

		JPanel formulario = new JPanel(new GridLayout(0, 1, 5, 5));
		JLabel titulo = new JLabel("GyMetrics", SwingConstants.CENTER);
		JTextField usuario = new JTextField(15);
		JPasswordField senha = new JPasswordField(15);
		JButton entrar = new JButton("Entrar");

		formulario.add(titulo);
		formulario.add(new JLabel("Usuário:"));
		formulario.add(usuario);
		formulario.add(new JLabel("Senha:"));
		formulario.add(senha);
		formulario.add(entrar);
		this.add(formulario);

		entrar.addActionListener(e -> GymSingleton.getInstance().showTela("principal"));
	}
}
