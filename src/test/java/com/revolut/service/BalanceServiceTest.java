package com.revolut.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;

import com.revolut.model.Account;
import com.revolut.repository.AccountRepository;

public class BalanceServiceTest {
	private AccountRepository repository = mock(AccountRepository.class);	
	private BalanceService service = new BalanceService(repository);
	private Account account1 = Account.create(1).balance(BigDecimal.ONE);
	
	@Test
	public void testGetBalance() {
		when(repository.findAccount(1)).thenReturn(account1);
		assertEquals(BigDecimal.ONE,service.getBalance(1));	
	}	
}
