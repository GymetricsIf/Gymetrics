package br.com.gymetrics.controller;

import br.com.gymetrics.dao.*; import br.com.gymetrics.model.*; import java.sql.SQLException;

public class AcessoController {
    private final AlunoDAO alunos=new AlunoDAO(); private final RegistroAcessoDAO acessos=new RegistroAcessoDAO(); private final Catraca catraca=new Catraca();
    public RegistroAcesso checkIn(String cpfOuCodigo)throws SQLException{Aluno a=alunos.buscarPorCpfOuCodigo(cpfOuCodigo);RegistroAcesso r=catraca.registrarEntrada(a);if(a!=null)acessos.salvar(r);return r;}
    public RegistroAcesso checkOut(Aluno a)throws SQLException{RegistroAcesso r=catraca.registrarSaida(a);acessos.salvar(r);return r;}
}
