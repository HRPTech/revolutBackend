package com.revolut.service;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.model.Account;
import com.revolut.repository.AccountRepository;

@Singleton
public class BalanceService {

	private final AccountRepository repository;

	@Inject
	public BalanceService(final AccountRepository repository) {
		super();
		this.repository = repository;
	}

	public BigDecimal getBalance(final Integer accountId) {
		final Account account = repository.findAccount(accountId);
		return account.getBalance();		
	}

}
