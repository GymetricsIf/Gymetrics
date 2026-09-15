package br.com.gymetrics.tela;

import br.com.gymetrics.GymSingleton;
import javax.swing.*;
import java.awt.*;

public class TelaCheckin extends JPanel {
    private final JTextField txtCode = new JTextField();
    public TelaCheckin() {
        setLayout(new BorderLayout(15, 15)); setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel top = new JPanel(new BorderLayout());
        JButton back = new JButton("← Voltar"); back.addActionListener(e -> GymSingleton.getInstance().showTela("principal"));
        JLabel title = new JLabel("Check-in", 0); title.setFont(new Font("Arial", Font.BOLD, 22));
        top.add(back, BorderLayout.WEST); top.add(title, BorderLayout.CENTER); add(top, BorderLayout.NORTH);
        
        JPanel center = new JPanel(new BorderLayout(10, 10));
        txtCode.setFont(new Font("Arial", Font.BOLD, 28)); txtCode.setHorizontalAlignment(JTextField.CENTER); txtCode.setEditable(false);
        center.add(txtCode, BorderLayout.NORTH);
        
        JPanel keys = new JPanel(new GridLayout(4, 3, 8, 8));
        for (String k : new String[]{"1","2","3","4","5","6","7","8","9","C","0","←"}) {
            JButton b = new JButton(k); b.setFont(new Font("Arial", Font.BOLD, 20)); b.setFocusable(false);
            b.addActionListener(e -> {
                if (k.equals("C")) txtCode.setText("");
                else if (k.equals("←")) { String t = txtCode.getText(); if(!t.isEmpty()) txtCode.setText(t.substring(0, t.length()-1)); }
                else txtCode.setText(txtCode.getText() + k);
            });
            keys.add(b);
        }
        center.add(keys, BorderLayout.CENTER); add(center, BorderLayout.CENTER);
        
        JButton confirm = new JButton("CONFIRMAR CHECK-IN"); confirm.setFont(new Font("Arial", Font.BOLD, 18));
        confirm.addActionListener(e -> {
            String c = txtCode.getText(); if (c.isBlank()) { GymSingleton.getInstance().showMsg("Digite o código!"); return; }
            GymSingleton.getInstance().showMsg("Check-in realizado! Matrícula: " + c); txtCode.setText(""); GymSingleton.getInstance().showTela("principal");
        });
        add(confirm, BorderLayout.SOUTH);
    }
}
