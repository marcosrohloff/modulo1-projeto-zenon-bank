package br.com.zenon;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {

	private final java.util.Map<String, Transaction> transactionsByOriginCustomer;

	public TransactionMapRepository(List<Transaction> transactions) {
		Objects.requireNonNull(transactions);

		this.transactionsByOriginCustomer = transactions.stream()
				.collect(Collectors.toMap(transaction -> transaction.origin().name(), Function.identity()));
	}

	@Override
	public java.util.Optional<Transaction> findByOriginCustomerName(String customerName) {
		return java.util.Optional.ofNullable(transactionsByOriginCustomer.get(customerName));
	}

	@Override
	public void save(Transaction transaction) {
		this.transactionsByOriginCustomer.putIfAbsent(transaction.origin().name(), transaction);
		
	}

}
