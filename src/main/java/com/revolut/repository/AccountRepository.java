package com.revolut.repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import com.revolut.model.Account;

@Singleton
public class AccountRepository {
	private static final String ACCOUNT_NOT_FOUND = "Account not found";
	private final Map<Integer, Account> accounts = Maps.newConcurrentMap();

	private final AtomicInteger ids = new AtomicInteger(1);

	public Account findAccount(final int id) {
		final Optional<Account> account = Optional.ofNullable(accounts.get(id));
		return account.orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));
	}

	public Account createAccount(final BigDecimal balance) {
		final int id = ids.getAndIncrement();
		final Account newAccount = Account.create(id).balance(balance);
		accounts.put(id, newAccount);
		return newAccount;
	}

	public void save(final Account account) {
		accounts.put(account.getAccountId(), account);
	}
}
