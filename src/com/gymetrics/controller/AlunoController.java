package br.com.gymetrics.controller;

import br.com.gymetrics.dao.AlunoDAO; import br.com.gymetrics.model.*; import java.sql.SQLException; import java.util.List;

public class AlunoController {
    private final AlunoDAO dao=new AlunoDAO();
    public void cadastrar(Aluno a)throws SQLException{validar(a);dao.salvar(a);} public void atualizar(Aluno a)throws SQLException{validar(a);dao.atualizar(a);} public List<Aluno> buscar(String t)throws SQLException{return dao.buscar(t);} public void bloquear(Aluno a,String motivo,Usuario u)throws SQLException{dao.bloquear(a,motivo,u);}
    private void validar(Aluno a){if(a==null||!a.cadastroCompleto())throw new IllegalArgumentException("Preencha nome, CPF, endereço, telefone e data de nascimento");if(a.getCodigoAcesso()!=null&&!a.getCodigoAcesso().matches("\\d+"))throw new IllegalArgumentException("O código de acesso deve ser numérico");}
}
