package br.com.zenon;

import java.util.List;

public class IngestionMain {
	
	public static void main(String[] args) {
		
		var repository = new TransactionSQLRepository();
		
		var transactionIngestor = new TransactionIngestor();
		
		long startTimeSQL = System.nanoTime();
		List<Transaction> transactions = transactionIngestor.read("data/arq.csv");
		System.out.println(transactions.size());		
		
			
		System.out.println("Iniciando adicao das transacoes....");
	    repository.saveAll(transactions);

	    long endTimeSQL = System.nanoTime();
	    System.out.println("Tempo de ingestão no BD (ms): " + (endTimeSQL - startTimeSQL) / 1_000_000.0);

	}

}
