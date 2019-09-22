package com.revolut.controller;

import java.math.BigDecimal;

import org.jooby.Err;
import org.jooby.Request;
import org.jooby.Status;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.service.AccountService;

@Path("/account")
@Singleton
public class AccountController {
	private final AccountService accountService;

	@Inject
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@POST
	@Path("/create")
	public Integer createAccount(final Request request) throws Exception {
		try {
			return accountService.createAccount(new BigDecimal(request.param("balance").value()));
		} catch (final Exception exception) {
			throw new Err(Status.BAD_REQUEST, exception.getMessage());
		}
	}
}
