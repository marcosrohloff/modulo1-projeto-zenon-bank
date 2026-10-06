package br.com.zenon;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {
	
	private record ReportTransaction(BigDecimal amount, boolean isFraud) {}
	
	public record Statistics(long totalTransactions, long totalFrauds, BigDecimal totalAmount) {
		
	}
	
	public Statistics generateReport(String fileName) {	
		
		Path path = Path.of(fileName);		
		try (Stream<String> lines = Files.lines(path)) {
			return lines
				.skip(1)
				.map(this::parseReportTransaction)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.reduce(new Statistics(0, 0, BigDecimal.ZERO), (stats, report) -> {
					long totalTransactions = stats.totalTransactions() + 1;
					long totalFrauds = stats.totalFrauds() + (report.isFraud() ? 1 : 0);
					BigDecimal totalAmount = stats.totalAmount().add(report.amount());
					return new Statistics(totalTransactions, totalFrauds, totalAmount);
				}, (stats1, stats2) -> {
					long totalTransactions = stats1.totalTransactions() + stats2.totalTransactions();
					long totalFrauds = stats1.totalFrauds() + stats2.totalFrauds();
					BigDecimal totalAmount = stats1.totalAmount().add(stats2.totalAmount());
					return new Statistics(totalTransactions, totalFrauds, totalAmount);
				});
		} catch (IOException e) {
			throw new IllegalStateException("Erro ao ler o arquivo: " + fileName, e);
		}
		
	}
	
	private Optional<ReportTransaction> parseReportTransaction(String line) {
		
		try {
			String[] chunks = line.split(",", -1);
			
			if (chunks[2] == null || chunks[2].trim().isEmpty()) 
				throw new IllegalArgumentException("Valor de amount nao pode ser null ou vazio: " + chunks[2]);
			BigDecimal amount = new BigDecimal(chunks[2]);
			
			boolean isFraud = "1".equals(chunks[9]);
			
			return Optional.of(new ReportTransaction(amount, isFraud));
			
			} catch (Exception e) {
			System.err.println("Erro ao processar a linha: " + line + " - " + e.getMessage());
			return Optional.empty();
		}
	}

}
