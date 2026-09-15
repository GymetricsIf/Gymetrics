package br.com.gymetrics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Data {
	public Data() {}
	private Connection mConnection = null;
	
	public void open(String fName) throws SQLException {
		if (null != mConnection)
			throw new RuntimeException("BUG: BD já esta aberto.");
		
        mConnection = DriverManager.getConnection("jdbc:sqlite:" + fName);
	}
	
	
	public void suicide() throws SQLException {
		if (null == mConnection)
			throw new RuntimeException("BUG: Tentando fechar um BD já fechado");
		
		mConnection.close();
		mConnection = null;
	}
}
