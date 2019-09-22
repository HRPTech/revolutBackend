package com.revolut.controller;

import org.jooby.Err;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Status;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.model.Transaction;
import com.revolut.service.TransactionService;

@Path("/transaction")
@Singleton
public class TransactionController {

	private final TransactionService transactionService;

	@Inject
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@POST
	public Result transaction(final Request request) throws Exception {
		final Transaction transaction = request.body(Transaction.class);
		try {
			transactionService.transfer(transaction);
			return new Result().status(Status.OK);
		} catch (final Exception exception) {
			throw new Err(Status.BAD_REQUEST, exception.getMessage());
		}
	}
}
