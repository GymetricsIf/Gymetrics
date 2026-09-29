package br.com.gymetrics.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import br.com.gymetrics.entidade.eRegistroAcesso;

public final class ExportadorCSV {
	private ExportadorCSV() {}

	public static void exportarAcessos(List<eRegistroAcesso> lista, Path arquivo) throws IOException {
		StringBuilder s = new StringBuilder("aluno,cpf,data_hora,tipo,observacao\n");
		for (eRegistroAcesso r : lista) {
			String nome = r.getAluno() == null ? "" : r.getAluno().getNome();
			String cpf = r.getAluno() == null ? "" : r.getAluno().getCpf();
			s.append(q(nome)).append(',').append(q(cpf)).append(',')
			 .append(q(r.getDataHora().toString())).append(',').append(q(r.getTipo().name())).append(',')
			 .append(q(r.getObservacao())).append('\n');
		}
		Files.writeString(arquivo, s);
	}

	private static String q(String v) {
		return "\"" + (v == null ? "" : v.replace("\"", "\"\"")) + "\"";
	}
}
