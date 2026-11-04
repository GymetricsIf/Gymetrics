package br.com.gymetrics;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SetupExecCallback {
	void setupExecParams(PreparedStatement pStmt) throws SQLException;
}
