package com.revolut.model;

import java.math.BigDecimal;

public class Transaction {
	  	private final int senderAccount;
	    private final int receiverAccount;
	    private final BigDecimal amount;

	    public Transaction(int senderAccount, int receiverAccount, BigDecimal amount) {
	        this.senderAccount = senderAccount;
	        this.receiverAccount = receiverAccount;
	        this.amount = amount;
	    }

		public final int getSenderAccount() {
			return senderAccount;
		}

		public final int getReceiverAccount() {
			return receiverAccount;
		}

		public final BigDecimal getAmount() {
			return amount;
		}	 
}
