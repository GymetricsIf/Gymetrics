package br.com.gymetrics.util;

import br.com.gymetrics.model.RegistroAcesso; import java.io.IOException; import java.nio.file.*; import java.util.List;

public final class ExportadorCSV {
    private ExportadorCSV(){}
    public static void exportarAcessos(List<RegistroAcesso> lista,Path arquivo)throws IOException{StringBuilder s=new StringBuilder("aluno,cpf,data_hora,tipo,observacao\n");for(RegistroAcesso r:lista){String n=r.getAluno()==null?"":r.getAluno().getNome(),c=r.getAluno()==null?"":r.getAluno().getCpf();s.append(q(n)).append(',').append(q(c)).append(',').append(q(r.getDataHora().toString())).append(',').append(q(r.getTipo().name())).append(',').append(q(r.getObservacao())).append('\n');}Files.writeString(arquivo,s);}
    private static String q(String v){return "\""+(v==null?"":v.replace("\"","\"\""))+"\"";}
}
