package com.revolut.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.revolut.model.Account;
import com.revolut.repository.AccountRepository;

public class TransferServiceTest {
	private AccountRepository repository = new AccountRepository();
	private TransferService service = new TransferService(repository);

	@Test
	public void testTransferWithFunds() {
		Account account1 = Account.create(1).balance(BigDecimal.ONE);
		Account account2 = Account.create(2).balance(BigDecimal.ONE);
		service.transfer(account1, account2, BigDecimal.ONE);

		assertEquals(BigDecimal.ZERO, account1.getBalance());
		assertEquals(BigDecimal.valueOf(2), account2.getBalance());
	}

	@Test(expected = RuntimeException.class)
	public void testTransferWithInsufficentFunds() {
		Account account1 = Account.create(1).balance(BigDecimal.ONE);
		Account account2 = Account.create(2).balance(BigDecimal.ONE);
		service.transfer(account1, account2, BigDecimal.valueOf(100));
	}

	@Test
	public void testTransferFundsConcurrent() throws ExecutionException, InterruptedException {
		int executorSize = 100;
		Account account1 = Account.create(1).balance(BigDecimal.valueOf(1000));
		Account account2 = Account.create(2).balance(BigDecimal.valueOf(1000));
		Account account3 = Account.create(3).balance(BigDecimal.valueOf(1000));

		List<Future<Void>> tasks = Lists.newArrayList();

		for (int i = 0; i < executorSize; i++) {
			tasks.add(CompletableFuture.runAsync(() -> service.transfer(account1, account2, BigDecimal.ONE)));
			tasks.add(CompletableFuture.runAsync(() -> service.transfer(account2, account3, BigDecimal.ONE)));
			tasks.add(CompletableFuture.runAsync(() -> service.transfer(account3, account1, BigDecimal.ONE)));
		}
		try {
			CompletableFuture<Void> allFutures = CompletableFuture
					.allOf(tasks.toArray(new CompletableFuture[tasks.size()]));

			allFutures.join();

		} catch (Exception exception) {
			assertThat(exception.getMessage(), CoreMatchers.containsString("500"));
			// catching exception here as its possible some threads may not complete due to locking
		}

		// no account should be left in a bad state. All accounts should add up to 3000
		assertEquals(BigDecimal.valueOf(3000),
				account1.getBalance().add(account2.getBalance()).add(account3.getBalance()));

	}
}
