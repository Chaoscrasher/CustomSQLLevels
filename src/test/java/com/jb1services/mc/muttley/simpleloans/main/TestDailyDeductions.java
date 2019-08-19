package com.jb1services.mc.muttley.simpleloans.main;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.jupiter.api.Test;

import com.jb1services.mc.muttley.simpleloans.structure.Loan;
import com.jb1services.mc.muttley.simpleloans.structure.LoansDatabase;

import manifold.ext.api.Jailbreak;
import net.md_5.bungee.api.ChatColor;

class TestDailyDeductions
{	
	private LoansDatabase db = new LoansDatabase();
	
	@Test
	void testNormalDeduction()
	{
		UUID shark = UUID.randomUUID();
		UUID loanee = UUID.randomUUID();
		int amnt = 30, days = 5, interest = 5;
		TestLoan loan = new TestLoan(shark, loanee, DateTime.now().getMillis(), amnt, days, interest);
		
		double accInterest = 0;
		for (int i = 1; i <= 5; i++)
		{
			accInterest += loan.calculateInterest();
			deduct(loan, DateTime.now().plusDays(i), true);
			double expectedBalance = amnt - 6*i;
			double realBalance = loan.getRemainingBalance();
			assertTrue(realBalance == expectedBalance);
		}
		assertTrue(accInterest == 4.5);
	}
	
	@Test
	void test2FailedDeduction()
	{
		UUID shark = UUID.randomUUID();
		UUID loanee = UUID.randomUUID();
		int amnt = 30, days = 5, interest = 5;
		final List<Integer> failedDeductions = Arrays.asList(2, 5); //don't change, or interest test has to be adjusted
		TestLoan loan = new TestLoan(shark, loanee, DateTime.now().getMillis(), amnt, days, interest);
		
		double accInterest = 0;
		double expectedBalance = amnt;
		for (int i = 1; i <= 6; i++)
		{
			DateTime now = DateTime.now().plusDays(i);
			boolean enough = !failedDeductions.contains(i);
			if (enough)
			{
				accInterest += calculateInterest(loan, now);
				expectedBalance -= calculateCurrentlyMissingPayments(loan, now) > 0 ? (calculateCurrentlyMissingPayments(loan, now)+1) * 6.0 : 6.0;
				deduct(loan, now, enough);
			}
			else
			{
				deduct(loan, now, enough);
			}
			double realBalance = loan.getRemainingBalance();
			assertTrue(realBalance == expectedBalance);
		}
		assertTrue(Math.abs(accInterest-5.94) <= 1/100.0);
	}

	private void deduct(TestLoan loan, DateTime now, boolean enoughBalance)
	{
		//double interest = loan.calculateInterest();
		double payment = getPayableToday(loan, now);
		//double total = interest + payment;
		//OfflinePlayer loaneep = getLoaneeAsPlayer();
		//OfflinePlayer loanshp = getLoanSharkAsPlayer();
		
		if (!(loan.getRemainingBalance() <= 0.0))
		{
			if (/*econ.getBalance(getLoaneeAsPlayer()) > total*/enoughBalance)
			{
				loan.setRemainingBalance(loan.getRemainingBalance() - payment);
				//econ.withdrawPlayer(loaneep, total);
				/*if (loaneep.isOnline())
				{
					loaneep.getPlayer().sendMessage(ChatColor.GOLD + "" + payment + " + " + interest + " for a total of "+ total +" has been deduced from your account!");
				}*/
			}
			else
			{
				loan.latePayment();
				/*if (loaneep.isOnline())
				{
					loaneep.getPlayer().sendMessage(ChatColor.RED + "Your missed a payment... This was your "+(getLatePayments() == 1 ? "first" : getLatePayments() + ".")+" late payment. Your interest rate was increased by 1 and is now " + getTotalInterestRate() + ".");
				}
				*/
			}
		}
		else
		{
			/*
			if (!db.deleteLoanFromLoanSharkToLoanee(this))
			{
				System.err.println("Coulnd't delete loan!: " + this);
			}
			*/
		}
	}
	
	public double getPayableToday(TestLoan loan, DateTime now)
	{
		int ddif = -1*Days.daysBetween(now, loan.getLoanStartDateTime()).getDays();
		double requiredBalance = loan.getBaseAmount() - (loan.getDailyPayment() * ddif);
		double payableToday = loan.getRemainingBalance() - requiredBalance; 
		return payableToday;
	}
	
	public double calculateInterest(TestLoan loan, DateTime now)
	{
		double percentage = (loan.getBaseInterestRate() + 0.0 + loan.getLatePayments()) / 100;
		int currentlyMissingPayments = calculateCurrentlyMissingPayments(loan, now);
		double interest = loan.getRemainingBalance() * percentage * (currentlyMissingPayments > 0 ? (currentlyMissingPayments+1) : 1);
		return interest;
	}
	
	public int calculateCurrentlyMissingPayments(TestLoan loan, DateTime now)
	{
		int currentlyMissing = ((int) (getPayableToday(loan, now) / loan.getDailyPayment())) -1;
		return currentlyMissing; 
	}
}
