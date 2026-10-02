package br.com.zenon;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

	public static void main(String[] args) {

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
		/*
		 * Path transactionsFile = Path.of("data", "arq.csv"); if
		 * (!Files.exists(transactionsFile)) { throw new
		 * IllegalStateException("Arquivo de transações não encontrado: " +
		 * transactionsFile.toAbsolutePath()); }
		 * 
		 * var transactionIngestor = new TransactionIngestor(); List<Transaction>
		 * transactions = transactionIngestor.read(transactionsFile);
		 * System.out.println("Transactions read: " + transactions.size());
		 * 
		 * transactions.stream().limit(10).forEach(System.out::println);
		 * 
		 * System.out.println(
		 * "-------------------------------------------------------------");
		 */
		Path transactionsFile2 = Path.of("data", "paysim_with_bad_data.csv");
		if (!Files.exists(transactionsFile2)) {
			throw new IllegalStateException(
					"Arquivo de transações não encontrado: " + transactionsFile2.toAbsolutePath());
		}

		var transactionIngestor2 = new TransactionIngestor();
		List<Transaction> transactions2 = transactionIngestor2.read2("data/paysim_with_bad_data.csv");
		System.out.println("Total transacoes: " + transactions2.size());

		transactions2.forEach(System.out::println);

		System.out.println("-------------------------------------------------------------");

		FraudAnalyzer analyzer = new FraudAnalyzer(transactions2);
		System.out.println("Total de Fraudes: " + analyzer.countFrauds());

		System.out.println("-------------------------------------------------------------");
		List<Transaction> top3Frauds = analyzer.top3FraudsByAmount(3);
		top3Frauds.stream().map(Transaction::amount).forEach(System.out::println);

		System.out.println("-------------------------------------------------------------");
		List<String> suspiciousCustomers = analyzer.top5SuspiciousCustomers();
		System.out.println("Suspicious Customers: " + suspiciousCustomers);

		System.out.println("-------------------------------------------------------------");
		System.out.println("Prejuízo Total: " + analyzer.totalFraudLoss().setScale(2).toPlainString());

		System.out.println("-------------------------------------------------------------");
		System.out.println("Fraudes por Tipo:");
		analyzer.countFraudsByType().entrySet().stream()
				.sorted(Map.Entry.comparingByKey(Comparator.comparing(Enum::name)))
				.forEach(entry -> System.out.println(" - " + entry.getKey() + ": " + entry.getValue()));

		System.out.println("-------------------------------------------------------------");

		TransactionRepository listRepository = new TransactionListRepository(transactions2);
		TransactionRepository mapRepository = new TransactionMapRepository(transactions2);

		String existingCustomer = "C1231006815";
		String nonExistingCustomer = "C12345";
		String worstCaseCustomer = "C1868032458";

		System.out.println();
		System.out.println("Busca com List - cliente existente:");
		imprimirResultado(listRepository.findByOriginCustomerName(existingCustomer), existingCustomer);

		System.out.println();
		System.out.println("Busca com List - cliente inexistente:");
		imprimirResultado(listRepository.findByOriginCustomerName(nonExistingCustomer), nonExistingCustomer);

		System.out.println();
		System.out.println("Benchmark pior caso com List:");
		long startList = System.nanoTime();
		Optional<Transaction> listResult = listRepository.findByOriginCustomerName(worstCaseCustomer);
		long endList = System.nanoTime();
		imprimirResultado(listResult, worstCaseCustomer);
		System.out.println("Tempo List (ns): " + (endList - startList));

		System.out.println();
		System.out.println("Benchmark com Map:");
		long startMap = System.nanoTime();
		Optional<Transaction> mapResult = mapRepository.findByOriginCustomerName(worstCaseCustomer);
		long endMap = System.nanoTime();
		imprimirResultado(mapResult, worstCaseCustomer);
		System.out.println("Tempo Map (ns): " + (endMap - startMap));
	}

	private static void imprimirResultado(Optional<Transaction> transaction, String customerName) {
		if (transaction.isPresent()) {
			System.out.println(transaction.get());
		} else {
			System.out.println("Transação não encontrada para o cliente " + customerName);
		}
	}

}