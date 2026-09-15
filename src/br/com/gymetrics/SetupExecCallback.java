package br.com.gymetrics;

import java.sql.PreparedStatement;

@FunctionalInterface
public interface SetupExecCallback {
	void setupExecParams(PreparedStatement pStmt);
}
