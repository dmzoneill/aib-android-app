package ie.aib.entities;

import ie.aib.web.WebHelper;

import java.util.ArrayList;


public class Account
{
	private String account;
	private String balance;
	private int sequence;
	private ArrayList< String[] > statement;
	
	
	public Account( String name , String Balance , int sequence )
	{		
		this.account = name;
		this.balance = Balance;
		this.sequence = sequence;
	}


	public String getAccount()
	{
		return account;
	}


	public void setAccount( String account )
	{
		this.account = account;
	}


	public String getBalance()
	{
		return balance;
	}


	public void setBalance( String balance )
	{
		this.balance = balance;
	}


	public int getSequence()
	{
		return sequence;
	}


	public void setSequence( int sequence )
	{
		this.sequence = sequence;
	}

	public ArrayList< String[] > getStatement()
	{
		if( this.statement == null )
		{
			WebHelper httpHelper = WebHelper.getInstance();
			this.statement = httpHelper.getAccountStatement( this.sequence );
		}

		return this.statement;
	}

}
