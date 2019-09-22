package com.revolut.service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.model.Account;
import com.revolut.repository.AccountRepository;

@Singleton
public class TransferService {

	private static final String TRANSFER_FAILED = "Transfer failed. Please try again.";
	private static final String NO_FUNDS = "Insufficient funds available to process transaction";
	private final AccountRepository repository;

	@Inject
	public TransferService(final AccountRepository repository) {
		super();
		this.repository = repository;
	}

	public void transfer(final Account sender, final Account receiver, final BigDecimal amount) {
		ReadWriteLock senderLock = sender.getLock();
		ReadWriteLock receiverLock = receiver.getLock();

		if (amount.compareTo(sender.getBalance()) > 0) {
			throw new RuntimeException(NO_FUNDS);
		}

		try {
			if (senderLock.writeLock().tryLock(2, TimeUnit.SECONDS)) {
				receiverLock.writeLock().tryLock(2, TimeUnit.SECONDS);
			}

			repository.save(sender.balance(sender.getBalance().subtract(amount)));
			repository.save(receiver.balance(receiver.getBalance().add(amount)));

		} catch (InterruptedException exception) {
			throw new RuntimeException(TRANSFER_FAILED, exception);
		} finally {
			senderLock.writeLock().unlock();
			receiverLock.writeLock().unlock();
		}
	}
}
