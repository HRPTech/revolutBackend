package com.revolut.controller;

import java.math.BigDecimal;

import javax.inject.Named;

import org.jooby.Err;
import org.jooby.Status;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.service.BalanceService;

@Path("/balance")
@Singleton
public class BalanceController {

	private final BalanceService balanceService;

	@Inject
	public BalanceController(BalanceService balanceService) {
		this.balanceService = balanceService;
	}

	@GET
	@Path(":accountId")
	public BigDecimal balance(@Named("accountId") final int accountId) throws Exception {
		try {
			return balanceService.getBalance(accountId);
		} catch (final Exception exception) {
			throw new Err(Status.BAD_REQUEST, exception.getMessage());
		}
	}

}
