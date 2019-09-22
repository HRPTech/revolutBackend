package com.revolut.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;

import com.revolut.model.Account;
import com.revolut.repository.AccountRepository;

public class AccountServiceTest {
	private AccountRepository repository = mock(AccountRepository.class);
	private AccountService service = new AccountService(repository);
	private Account account1 = Account.create(1).balance(BigDecimal.ONE);

	@Test
	public void testCreateAccount() {
		when(repository.createAccount(BigDecimal.ONE)).thenReturn(account1);
		assertEquals(Integer.valueOf(1), service.createAccount(BigDecimal.ONE));
	}
}
