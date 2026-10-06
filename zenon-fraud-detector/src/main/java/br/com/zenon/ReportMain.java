package br.com.zenon;

import br.com.zenon.TransactionReport.Statistics;

public class ReportMain {
	
	public static void main(String[] args) {
		
		var transactionReport = new TransactionReport();
		Statistics statistics = transactionReport.generateReport("data/arq.csv");
		System.out.println("Total de linhas: " + statistics.totalTransactions());
		System.out.println("Total de fraudes: " + statistics.totalFrauds());
		System.out.println("Valor total transacionado: " + statistics.totalAmount().setScale(2).toPlainString());
	}

}
