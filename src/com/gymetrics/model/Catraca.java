package br.com.gymetrics.model;

public class Catraca {
    public RegistroAcesso registrarEntrada(Aluno aluno) {
        boolean liberado=aluno!=null&&aluno.podeAcessar();
        String obs=aluno==null?"Aluno não encontrado":liberado?"Acesso Liberado":aluno.cadastroCompleto()?"Acesso bloqueado":"Cadastro incompleto";
        return new RegistroAcesso(0,aluno,null,liberado?RegistroAcesso.Tipo.CHECK_IN:RegistroAcesso.Tipo.TENTATIVA_NEGADA,obs);
    }
    public RegistroAcesso registrarSaida(Aluno aluno){return new RegistroAcesso(0,aluno,null,RegistroAcesso.Tipo.CHECK_OUT,"Saída registrada");}
}
