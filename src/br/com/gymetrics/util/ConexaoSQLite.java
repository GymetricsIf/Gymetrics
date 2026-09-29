package br.com.gymetrics.util;

import java.sql.*;

public final class ConexaoSQLite {
    private static final String URL="jdbc:sqlite:gymetrics.db";
    private ConexaoSQLite(){}
    public static Connection conectar() throws SQLException { Connection c=DriverManager.getConnection(URL); criarTabelas(c); return c; }
    private static void criarTabelas(Connection c) throws SQLException {
        try(Statement s=c.createStatement()){
            s.executeUpdate("CREATE TABLE IF NOT EXISTS planos(id INTEGER PRIMARY KEY AUTOINCREMENT,nome TEXT NOT NULL,valor_mensal REAL NOT NULL,dia_vencimento INTEGER NOT NULL,ativo INTEGER NOT NULL DEFAULT 1)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS alunos(id INTEGER PRIMARY KEY AUTOINCREMENT,nome TEXT NOT NULL,cpf TEXT NOT NULL UNIQUE,endereco TEXT,telefone TEXT,data_nascimento TEXT,codigo_acesso TEXT UNIQUE,status TEXT NOT NULL,plano_id INTEGER REFERENCES planos(id))");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios(id INTEGER PRIMARY KEY AUTOINCREMENT,login TEXT NOT NULL UNIQUE,senha TEXT NOT NULL,nome TEXT NOT NULL,perfil TEXT NOT NULL,ativo INTEGER NOT NULL DEFAULT 1)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS mensalidades(id INTEGER PRIMARY KEY AUTOINCREMENT,aluno_id INTEGER NOT NULL REFERENCES alunos(id),vencimento TEXT NOT NULL,valor REAL NOT NULL,status TEXT NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS pagamentos(id INTEGER PRIMARY KEY AUTOINCREMENT,mensalidade_id INTEGER NOT NULL REFERENCES mensalidades(id),mes_referencia TEXT NOT NULL,valor_pago REAL NOT NULL,data_pagamento TEXT NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS registros_acesso(id INTEGER PRIMARY KEY AUTOINCREMENT,aluno_id INTEGER REFERENCES alunos(id),data_hora TEXT NOT NULL,tipo TEXT NOT NULL,observacao TEXT)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS bloqueios_acesso(id INTEGER PRIMARY KEY AUTOINCREMENT,aluno_id INTEGER NOT NULL REFERENCES alunos(id),motivo TEXT NOT NULL,data_hora TEXT NOT NULL,usuario_id INTEGER REFERENCES usuarios(id))");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS logs_sistema(id INTEGER PRIMARY KEY AUTOINCREMENT,usuario_id INTEGER REFERENCES usuarios(id),acao TEXT NOT NULL,data_hora TEXT NOT NULL)");
        }
    }
}
