package br.com.gymetrics.tela;

import br.com.gymetrics.GymSingleton;

import javax.swing.*;
import java.awt.*;

public class TelaCheckin extends JPanel {

    private final JTextField txtCode = new JTextField();

    public TelaCheckin() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- PAINEL SUPERIOR (Botão Voltar e Título) ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));

        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVoltar.addActionListener(e -> GymSingleton.getInstance().showTela("principal"));

        JLabel lblTitle = new JLabel("Check-in", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        // Gambiarra de layout para centralizar o título em relação ao botão
        JPanel placeholder = new JPanel();
        placeholder.setPreferredSize(btnVoltar.getPreferredSize());

        topPanel.add(btnVoltar, BorderLayout.WEST);
        topPanel.add(lblTitle, BorderLayout.CENTER);
        topPanel.add(placeholder, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // --- PAINEL CENTRAL (Campo de Entrada + Teclado) ---
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Campo de Texto para a Matrícula/CPF
        txtCode.setFont(new Font("Arial", Font.BOLD, 28));
        txtCode.setHorizontalAlignment(JTextField.CENTER);
        txtCode.setEditable(false);
        txtCode.setPreferredSize(new Dimension(0, 50));
        centerPanel.add(txtCode, BorderLayout.NORTH);

        // Teclado Numérico
        JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 8, 8));
        String[] keys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "C", "0", "←"};

        for (String key : keys) {
            JButton btnKey = new JButton(key);
            btnKey.setFont(new Font("Arial", Font.BOLD, 20));
            btnKey.setFocusable(false);

            btnKey.addActionListener(e -> {
                switch (key) {
                    case "C" -> txtCode.setText("");
                    case "←" -> {
                        String current = txtCode.getText();
                        if (!current.isEmpty()) {
                            txtCode.setText(current.substring(0, current.length() - 1));
                        }
                    }
                    default -> txtCode.setText(txtCode.getText() + key);
                }
            });
            keypadPanel.add(btnKey);
        }

        centerPanel.add(keypadPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // --- PAINEL INFERIOR (Botão Confirmar) ---
        JButton btnConfirmar = new JButton("CONFIRMAR CHECK-IN");
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 18));
        btnConfirmar.setPreferredSize(new Dimension(0, 50));
        
        btnConfirmar.addActionListener(e -> {
            String codigo = txtCode.getText();
            if (codigo.isBlank()) {
                GymSingleton.getInstance().showMsg("Por favor, digite seu código de acesso!");
                return;
            }

            // Ação de sucesso
            GymSingleton.getInstance().showMsg("Check-in realizado com sucesso! Matrícula: " + codigo);
            txtCode.setText("");
            GymSingleton.getInstance().showTela("principal");
        });

        add(btnConfirmar, BorderLayout.SOUTH);
    }
}