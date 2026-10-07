package br.com.zenon;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {

	private final List<Transaction> transactions;

	public TransactionListRepository(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	@Override
    public Optional<Transaction> findByOriginCustomerName(String customerName) {
        return transactions.stream()
                .filter(transaction -> transaction.origin().name().equals(customerName))
                .findFirst();
    }

	@Override
	public void save(Transaction transaction) {
		this.transactions.add(transaction);		
	}

}
