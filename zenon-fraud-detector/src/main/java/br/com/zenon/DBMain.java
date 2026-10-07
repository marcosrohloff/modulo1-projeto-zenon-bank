package br.com.zenon;

import java.util.List;

public class DBMain {

	public static void main(String[] args) {

		ConnectionFactory.getConnection();
		System.err.println("Conexão com o BD criada!");

		var repository = new TransactionSQLRepository();

		var transactionIngestor = new TransactionIngestor();

		long startTimeSQL = System.nanoTime();
		List<Transaction> transactions = transactionIngestor.read("data/arq.csv");
		System.out.println(transactions.size());

		System.out.println("Iniciando adicao das transacoes no BD...");
		transactions.forEach(repository::save);

		long endTimeSQL = System.nanoTime();
		System.out.println("Tempo de inserção - SQL (ms): " + (endTimeSQL - startTimeSQL) / 1_000_000.0);

		var customerName = "C1231006815";
		repository.findByOriginCustomerName(customerName).map(Object::toString)
        .orElse("Transacao nao encontrada para: " + customerName);


	}
}
