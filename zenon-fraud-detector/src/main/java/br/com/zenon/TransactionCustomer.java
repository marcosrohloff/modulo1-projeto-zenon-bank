package br.com.zenon;

import java.math.BigDecimal;
import java.util.Objects;

public record TransactionCustomer(
		String name, 
		BigDecimal oldBalance, 
		BigDecimal newBalance) {
	
	public TransactionCustomer {
		
        Objects.requireNonNull(name, "O nome do cliente não pode ser nulo");
        Objects.requireNonNull(oldBalance, "O saldo anterior não pode ser nulo");
        Objects.requireNonNull(newBalance, "O novo saldo não pode ser nulo");
        
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
