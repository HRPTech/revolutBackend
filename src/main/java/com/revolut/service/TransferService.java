package com.revolut.service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
		ReentrantReadWriteLock senderLock = sender.getLock();
		ReentrantReadWriteLock receiverLock = receiver.getLock();

		validateBalance(sender, amount);

		try {
			if (senderLock.writeLock().tryLock(1, TimeUnit.SECONDS)
					&& receiverLock.writeLock().tryLock(1, TimeUnit.SECONDS)) {
				repository.save(sender.balance(sender.getBalance().subtract(amount)));
				repository.save(receiver.balance(receiver.getBalance().add(amount)));
			} else {
				throw new RuntimeException(TRANSFER_FAILED);
			}
		} catch (InterruptedException exception) {
			throw new RuntimeException(TRANSFER_FAILED, exception);
		} finally {
				senderLock.writeLock().unlock();
				receiverLock.writeLock().unlock();
		}
	}
	
	private void validateBalance(final Account sender, final BigDecimal amount){
		ReentrantReadWriteLock senderLock = sender.getLock();
		try {
			senderLock.readLock().lock();
			if (amount.compareTo(sender.getBalance()) > 0) {
				throw new RuntimeException(NO_FUNDS);
			}
		} finally {
			senderLock.readLock().unlock();
		}
	}
}
