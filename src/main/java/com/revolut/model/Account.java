package com.revolut.model;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Account {
	private Integer accountId;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private BigDecimal balance;

	private Account() {
	}

	private Account(final Integer accountId) {
		super();
		this.accountId = accountId;
		this.lock = new ReentrantReadWriteLock();
		this.balance = BigDecimal.ZERO;
	}

	public static Account create(final Integer accountId) {
		return new Account(accountId);
	}

	public Account balance(final BigDecimal balance) {
		this.balance = balance;
		return this;
	}

	public final Integer getAccountId() {
		return accountId;
	}

	public final ReentrantReadWriteLock getLock() {
		return lock;
	}

	public final BigDecimal getBalance() {
		return balance;
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