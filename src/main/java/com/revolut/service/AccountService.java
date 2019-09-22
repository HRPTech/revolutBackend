package com.revolut.service;

import java.math.BigDecimal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.repository.AccountRepository;

@Singleton
public class AccountService {

	private final AccountRepository repository;

	@Inject
	public AccountService(final AccountRepository repository) {
		super();
		this.repository = repository;
	}

	public Integer createAccount(final BigDecimal balance) {
		return repository.createAccount(balance).getAccountId();
	}
}
