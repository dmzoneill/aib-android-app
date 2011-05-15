package ie.aib.entities;

import ie.aib.web.WebHelper;

import java.util.ArrayList;

public class Person
{
	private static Person thisPerson;
	private ArrayList<Account> accounts;
	private ArrayList<Payee> payees;
	private ArrayList<String[]> payments;
	private ArrayList<StandingOrder> standingOrders;

	private Person()
	{
		WebHelper httpHelper = WebHelper.getInstance();
		this.accounts = httpHelper.getAccounts();
	}

	public ArrayList<Payee> getPayees()
	{
		if ( this.payees == null )
		{
			WebHelper httpHelper = WebHelper.getInstance();
			this.payees = httpHelper.getPayees();
		}

		return this.payees;
	}

	public ArrayList<String[]> getPayments()
	{
		if ( this.payments == null )
		{
			WebHelper httpHelper = WebHelper.getInstance();
			this.payments = httpHelper.getPaymentlog();
		}

		return this.payments;
	}

	public ArrayList<StandingOrder> getStandingorders()
	{
		if ( this.standingOrders == null )
		{
			WebHelper httpHelper = WebHelper.getInstance();
			this.standingOrders = httpHelper.getStandingOrders();
		}

		return this.standingOrders;
	}

	public int getPayeeCount()
	{
		return this.payees.size();
	}

	public int getPaymentsCount()
	{
		return this.payments.size();
	}

	public int getStandingOrdersCount()
	{
		return this.standingOrders.size();
	}

	public ArrayList<Account> getAccounts()
	{
		return this.accounts;
	}

	public void setAccounts( ArrayList<Account> accounts )
	{
		this.accounts = accounts;
	}

	public int getAccountCount()
	{
		return this.accounts.size();
	}

	public static Person getInstance()
	{
		if ( Person.thisPerson == null )
		{
			Person.thisPerson = new Person();
		}

		return Person.thisPerson;
	}

}
