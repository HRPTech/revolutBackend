package com.revolut.model;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jooby.Err;
import org.jooby.Status;

public class Account {
	private Integer accountId;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private BigDecimal balance;
	private static final String NO_FUNDS = "Insufficient funds available to process transaction";

	public Account(final Integer accountId) {
		super();
		this.accountId = accountId;
		this.lock = new ReentrantReadWriteLock();
		this.balance = BigDecimal.ZERO;
	}

	public Account(final int accountId,final BigDecimal balance) {
		super();
		this.accountId = accountId;
		this.lock = new ReentrantReadWriteLock();
		this.balance = balance;
	}

	public final Integer getAccountId() {
		return accountId;
	}

	public final ReentrantReadWriteLock getLock() {
		return lock;
	}

	public final BigDecimal getBalance() {
		this.lock.readLock().lock();
		try {
			return balance;
		} finally {
			this.lock.readLock().unlock();
		}
	}

	public final void addAmount(final BigDecimal amount) {
		this.lock.writeLock().lock();
		try {
			this.balance = this.balance.add(amount);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	public final void withdrawAmount(final BigDecimal amount) {
		this.lock.writeLock().lock();
		try {
			if (amount.compareTo(this.balance) > 0) {
				throw new Err(Status.BAD_REQUEST, NO_FUNDS);
			}
			this.balance = this.balance.subtract(amount);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", balance=" + balance + "]";
	}

}