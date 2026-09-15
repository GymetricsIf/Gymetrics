package br.com.gymetrics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Data {
	public Data() {}
	private Connection mConnection = null;
	
	public void open(String fName) throws SQLException {
		if (null != mConnection)
			throw new RuntimeException("BUG: BD já esta aberto.");
		
        mConnection = DriverManager.getConnection("jdbc:sqlite:" + fName);
	}
	
	public ResultSet exec(String sql, SetupExecCallback setup) throws SQLException {
		if (null == mConnection)
			throw new RuntimeException("BUG: Tentando executar em um BD que está fechado!");
		
		try (PreparedStatement pStmt = mConnection.prepareStatement(sql)) {
			setup.setupExecParams(pStmt);
			return pStmt.executeQuery();
		} 
	}

	public ResultSet run(String sql) throws SQLException {
		return exec(sql, (pStmt) -> {});
	}
	
	public void suicide() throws SQLException {
		if (null == mConnection)
			throw new RuntimeException("BUG: Tentando fechar um BD já fechado");
		
		mConnection.close();
		mConnection = null;
	}
}
