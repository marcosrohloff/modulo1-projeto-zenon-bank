package br.com.zenon;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class TransactionIngestor {

	public List<Transaction> read(String fileName) {
		return read(Path.of(fileName));
	}

	public List<Transaction> read(Path file) {
		if (file == null) {
			throw new IllegalArgumentException("O caminho do arquivo não pode ser nulo.");
		}

		// Mede o tempo total da leitura e conversão do arquivo CSV.
		long inicioTotal = System.nanoTime();
		try {
			List<String> lines = Files.readAllLines(file);
			List<Transaction> transactions = lines.stream().skip(1).map(String::trim).filter(line -> !line.isEmpty())
					.map(this::parseTransaction).toList();

			long tempoTotal = System.nanoTime() - inicioTotal;
			double tempoTotalEmSegundos = tempoTotal / 1_000_000_000.0;
			System.out.println("Tempo total da leitura do arquivo: " + tempoTotalEmSegundos + " s");
			return transactions;
		} catch (IOException e) {
			throw new IllegalStateException("Erro ao ler o arquivo: " + file, e);
		}
	}

	private Transaction parseTransaction(String line) {
		String[] chunks = line.split(",", -1);

		if (chunks.length != 11) {
			throw new IllegalArgumentException("Linha inválida no CSV: " + line);
		}

		int step = Integer.parseInt(chunks[0].trim());
		TransactionType type = TransactionType.valueOf(chunks[1].trim().toUpperCase(Locale.ROOT));
		BigDecimal amount = new BigDecimal(chunks[2].trim());

		var origin = new TransactionCustomer(chunks[3].trim(), new BigDecimal(chunks[4].trim()),
				new BigDecimal(chunks[5].trim()));
		var recipient = new TransactionCustomer(chunks[6].trim(), new BigDecimal(chunks[7].trim()),
				new BigDecimal(chunks[8].trim()));

		boolean isFraud = parseBooleanColumn(chunks[9]);
		boolean isFlaggedFraud = parseBooleanColumn(chunks[10]);

		return new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud);
	}

	private boolean parseBooleanColumn(String value) {
		String normalized = value.trim();
		if ("0".equals(normalized) || "1".equals(normalized)) {
			return "1".equals(normalized);
		}
		throw new IllegalArgumentException("Valor booleano inválido: " + value);
	}
	

	public List<Transaction> read2(String fileName) {

		Path path = Path.of(fileName);
		try {
			List<String> lines = Files.readAllLines(path);
			return lines.stream()
					.skip(1)
					.limit(1000)
					.map(this::parseTransaction2)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.toList();
		} catch (IOException e) {
			throw new IllegalStateException("Erro ao ler o arquivo: " + fileName, e);
		}
	}	

	private Optional<Transaction> parseTransaction2(String line) {
		try {
			String[] chunks = line.split(",", -1);

			int step = Integer.parseInt(chunks[0]);
			TransactionType type = TransactionType.valueOf(chunks[1]);


			if (chunks[2] == null || chunks[2].trim().isEmpty()) 
				throw new IllegalArgumentException("Valor de amount nao pode ser null ou vazio: " + chunks[2]);
			
			BigDecimal amount = new BigDecimal(chunks[2]);

			var origin = new TransactionCustomer(chunks[3], new BigDecimal(chunks[4]), new BigDecimal(chunks[5]));
			var recipient = new TransactionCustomer(chunks[6], new BigDecimal(chunks[7]), new BigDecimal(chunks[8]));

			boolean isFraud = "1".equals(chunks[9]);
			boolean isFlaggedFraud = "1".equals(chunks[10]);

			return Optional.of(new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud));
		} catch (Exception e) {
			System.err.println("Erro ao processar a linha: " + line + " - " + e.getMessage());
			return Optional.empty();
		}
	}
}