package com.jb1services.mc.muttley.simpleloans.main;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import com.jb1services.mc.muttley.simpleloans.structure.Loan;
import com.jb1services.mc.muttley.simpleloans.structure.LoansDatabase;

class TestSimpleLoansPlugin
{	
	private LoansDatabase db = new LoansDatabase();
	
	@Test
	void testOfferLoan()
	{
		UUID shark = UUID.randomUUID();
		UUID loanee = UUID.randomUUID();
		Loan loan = new Loan(shark, loanee, DateTime.now().getMillis(), 5.0,  4, 3);
		db.newLoanOffer(loan);
		Map<UUID, Loan> loans = db.getOfferedLoansOfLoanee(loanee);
		assertTrue(!loans.isEmpty());
		Loan extractedLoan = loans.get(shark);
		assertTrue(extractedLoan != null);
		assertTrue(extractedLoan.equals(loan));
	}
	
	@Test
	void testAcceptOfferedLoan()
	{
		UUID shark = UUID.randomUUID();
		UUID loanee = UUID.randomUUID();
		Loan loan = new Loan(shark, loanee, DateTime.now().getMillis(), 5.0, 4, 3);
		db.newLoanOffer(loan);
		Map<UUID, Loan> loans = db.getOfferedLoansOfLoanee(loanee);
		assertTrue(!loans.isEmpty());
		Loan extractedLoan = loans.get(shark);
		assertTrue(extractedLoan != null);
		assertTrue(extractedLoan.equals(loan));
		db.finalizeLoan(extractedLoan);
		Map<UUID, List<Loan>> liveLoans = db.getLiveLoansForLoanee(loanee);
		assertTrue(!liveLoans.isEmpty());
		assertTrue(liveLoans.size() == 1);
		assertTrue(liveLoans.keySet().contains(shark));
		assertTrue(liveLoans.get(shark) != null);
		assertTrue(!liveLoans.get(shark).isEmpty());
		assertTrue(liveLoans.get(shark).size() == 1);
		assertTrue(liveLoans.get(shark).get(0).equals(loan));
	}
}
