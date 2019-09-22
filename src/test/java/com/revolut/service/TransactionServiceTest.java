package com.revolut.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;

import com.revolut.model.Account;
import com.revolut.model.Transaction;
import com.revolut.repository.AccountRepository;

public class TransactionServiceTest {

	private AccountRepository repository = mock(AccountRepository.class);
	private TransferService moneyTransferService = mock(TransferService.class);
	private TransactionService service = new TransactionService(repository, moneyTransferService);
	private Account account1 = Account.create(1).balance(BigDecimal.ONE);
	private Account account2 = Account.create(2).balance(BigDecimal.ONE);

	@Test
	public void testTransfer() {
		when(repository.findAccount(1)).thenReturn(account1);
		when(repository.findAccount(2)).thenReturn(account2);

		doNothing().when(moneyTransferService).transfer(account1, account2, BigDecimal.ONE);

		service.transfer(new Transaction(1, 2, BigDecimal.ONE));
	}
}
