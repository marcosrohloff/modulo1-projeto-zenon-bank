package br.com.zenon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/zenon_fraud" + "?useSSL=false"
			+ "&allowPublicKeyRetrieval=true" + "&serverTimezone=UTC" + "&rewriteBatchedStatements=true"
			+ "&cachePrepStmts=true" + "&useServerPrepStmts=true";

	private ConnectionFactory() {
	}

	public static Connection getConnection() {

		try {
			return DriverManager.getConnection(JDBC_URL, "root", "root");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao conectar com o BD", e);
		}
	}

}
