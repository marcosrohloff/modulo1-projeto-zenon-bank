package br.com.zenon;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {

		Transaction transaction1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9839.64"),
				new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
				new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")), false, false);

		Transaction transaction2 = new Transaction(743, TransactionType.CASH_OUT, new BigDecimal("850002.52"),
				new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")),
				new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")), true,
				false);

		System.out.println(transaction1);
		System.out.println(transaction2);
		
		System.out.println("-------------------------------------------------------------");
		
		var transactionIngestor = new TransactionIngestor();
		List<Transaction> transactions = transactionIngestor.read("data/arq.csv");
		System.out.println("Transactions read: " + transactions.size());
		
		transactions.stream().limit(10).forEach(System.out::println);
		
	}

}
