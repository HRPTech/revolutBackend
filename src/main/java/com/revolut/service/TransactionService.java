package com.revolut.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.model.Account;
import com.revolut.model.Transaction;
import com.revolut.repository.AccountRepository;

@Singleton
public class TransactionService {

	private final AccountRepository repository;
	private final TransferService moneyTransferService;

	@Inject
	public TransactionService(final AccountRepository repository, final TransferService moneyTransferService) {
		super();
		this.repository = repository;
		this.moneyTransferService = moneyTransferService;
	}

	public void transfer(final Transaction transaction) {
		final Account sender = repository.findAccount(transaction.getSenderAccount());
		final Account receiver = repository.findAccount(transaction.getReceiverAccount());

		moneyTransferService.transfer(sender, receiver, transaction.getAmount());
	}

}
