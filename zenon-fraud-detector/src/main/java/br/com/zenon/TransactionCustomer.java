package br.com.zenon;

import java.math.BigDecimal;

public record TransactionCustomer(
		String name, 
		BigDecimal oldBalance, 
		BigDecimal newBalance) {
	
	public TransactionCustomer {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("O nome do cliente não pode ser nulo ou vazio.");
		}
		
		if (oldBalance == null || oldBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("O saldo antigo do cliente não pode ser nulo ou negativo.");
		}
		
		if (newBalance == null || newBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("O saldo novo do cliente não pode ser nulo ou negativo.");
		}
	}
}
