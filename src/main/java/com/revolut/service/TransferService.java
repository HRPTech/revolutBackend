package com.revolut.service;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jooby.Err;
import org.jooby.Status;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.model.Account;

@Singleton
public class TransferService {

	private static final String TRANSFER_FAILED = "Transfer failed. Please try again.";

	@Inject
	public TransferService() {
		super();
	}

	public void transfer(final Account sender, final Account receiver, final BigDecimal amount) {
		ReentrantReadWriteLock senderLock = sender.getLock();
		ReentrantReadWriteLock receiverLock = receiver.getLock();

		try {
			if (senderLock.writeLock().tryLock() && receiverLock.writeLock().tryLock()) {
				sender.withdrawAmount(amount);
				receiver.addAmount(amount);
			} else {
				throw new Err(Status.SERVER_ERROR, TRANSFER_FAILED);
			}
		} finally {
			senderLock.writeLock().unlock();
			receiverLock.writeLock().unlock();
		}
	}
}
