package com.jb1services.mc.muttley.simpleloans.main;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.jb1services.mc.muttley.simpleloans.structure.Loan;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class TestLoan extends Loan
{
	public TestLoan(UUID loanShark, UUID loanee, long loanStart, double balance, int daysToRepay, int interestRate)
	{
		super(loanShark, loanee, loanStart, balance, daysToRepay, interestRate);
	}

	public void setRemainingBalance(double val)
	{
		this.remainingBalance = val;
	}
}
