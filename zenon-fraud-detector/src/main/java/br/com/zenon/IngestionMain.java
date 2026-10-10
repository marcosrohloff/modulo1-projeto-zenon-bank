package br.com.zenon;

import java.util.List;

public class IngestionMain {
	
	public static void main(String[] args) {
		
		var repository = new TransactionSQLRepository();
		
		var transactionIngestor = new EfficientTransactionIngestor();
		
		long startTimeSQL = System.nanoTime();
		transactionIngestor.readAsBatch("data/arq.csv", repository::saveAll);
				
		
	    long endTimeSQL = System.nanoTime();
	    System.out.println("Tempo de ingestão no BD (ms): " + (endTimeSQL - startTimeSQL) / 1_000_000.0);

	}

}
