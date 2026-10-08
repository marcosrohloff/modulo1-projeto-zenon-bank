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
		
		
		var transactionIngestor = new TransactionIngestor();
	    List<Transaction> transactions = transactionIngestor.read("data/arq.csv");
	    System.out.println(transactions.size());

	    transactions.stream().limit(10).forEach(t -> System.out.println(t));

	    System.out.println("--------------------------------------------------------------");

	    List<Transaction> transactionsBadData = transactionIngestor.read("data/paysim_with_bad_data.csv");
	    System.out.println(transactionsBadData.size());

	    transactionsBadData.forEach(t -> System.out.println(t));

	    //tentando criar objeto invalido
//	    new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9838.64"),
//	        null,
//	        new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
//	        false, false
//	    );

	    System.out.println("--------------------------------------------------------------");

	    var fraudAnalyzer = new FraudAnalyzer(transactions);

	    //     Apenas transações onde isFraud == true, imprima o tamanho da lista.
	    long fraudCount = fraudAnalyzer.countFrauds();
	    System.out.println("Total de fraudes: " + fraudCount);

	    //     Imprima as 3 fraudes de maior valor (amount).
	    List<BigDecimal> highestFraudAmounts = fraudAnalyzer.findHighestValueFraudAmounts(3);
	    System.out.println("Top 3 fraudes de maior valor:");
	    highestFraudAmounts.forEach(amount -> System.out.println("- %.2f".formatted(amount)));


	    //     Obter apenas os nomes dos clientes de origem (nameOrig) dessas fraudes e depois gere uma lista sem repetições (Set ou distinct) com os 5 maiores clientes suspeitos.
	    List<String> suspiciousClients = fraudAnalyzer.findTopSuspiciousClients(5);
	    System.out.println("Top 5 clientes suspeitos:");
	    suspiciousClients.forEach(t -> System.out.println("- " + t));

	    //     Calcule o prejuízo total causado pelas fraudes (soma dos amount).
	    BigDecimal totalFraudLoss = fraudAnalyzer.calculateTotalFraudLoss();
	    System.out.println("Prejuízo total: " + totalFraudLoss);

	    // Conte quantas fraudes ocorreram por tipo de transação (CASH_OUT, TRANSFER, etc...)
	    Map<TransactionType, Long> fraudCountByType = fraudAnalyzer.countFraudsByType();
	    System.out.println("Fraudes por tipo:");
	    fraudCountByType.forEach((type, count) -> System.out.println("- %s: %d".formatted(type, count)));

	    System.out.println("--------------------------------------------------------------");

	    TransactionRepository transactionRepository;

	    transactionRepository = new TransactionListRepository(transactions);
	    String notFoundOriginName = "C12345";
	    transactionRepository.findByOriginName(notFoundOriginName)
	        .ifPresentOrElse(transaction -> System.out.println(transaction), 
	        		() -> System.out.println("Transacao nao encontrada para " + notFoundOriginName));

	    String existingOriginName = "C1868032458";

	    long startTimeList = System.nanoTime();
	    transactionRepository.findByOriginName(existingOriginName)
	        .ifPresentOrElse(transaction -> System.out.println(transaction),
	        	    () -> System.out.println("Transacao nao encontrada para " + existingOriginName));
	    long endTimeList = System.nanoTime();
	    System.out.println("Tempo de busca - List (ms): " + (endTimeList - startTimeList) / 1_000_000.0);

	    transactionRepository = new TransactionMapRepository(transactions);
	    startTimeList = System.nanoTime();
	    transactionRepository.findByOriginName(existingOriginName)
	        .ifPresentOrElse(transaction -> System.out.println(transaction),
	        	    () -> System.out.println("Transacao nao encontrada para " + existingOriginName));
	    endTimeList = System.nanoTime();
	    System.out.println("Tempo de busca - Map (ms): " + (endTimeList - startTimeList) / 1_000_000.0);		

	}
}