package br.com.zenon;

import java.math.BigDecimal;

public record Transaction(
		int step, 
		TransactionType type, 
		BigDecimal amount, 
		TransactionCustomer origin,
		TransactionCustomer recipient, 
		boolean isFraud, 
		boolean isFlaggedFraud) {

	public Transaction {

		if (step <= 0) {
			throw new IllegalArgumentException("O valor do step deve ser positivo: " + step);
		}

		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("O valor deve ser positivo: " + amount);
		}
	}

}
