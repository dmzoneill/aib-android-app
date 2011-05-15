package ie.aib.entities;

public class StandingOrder
{
	private String fromAccount;
	private String receiver;
	private String nextAmount;
	private String frequency;
	private String nextDate;

	private String ammendToken;
	private String cancelToken;
	private String moreInfoToken;

	public StandingOrder( String fromAcc , String receiver , String nextAmount , String Frequency , String nextDate , String options )
	{
		this.fromAccount = fromAcc;
		this.receiver = receiver;
		this.nextAmount = nextAmount;
		this.frequency = Frequency;
		this.nextDate = nextDate;
	}

	public String getFromAccount()
	{
		return fromAccount;
	}

	public void setFromAccount( String fromAccount )
	{
		this.fromAccount = fromAccount;
	}

	public String getReceiver()
	{
		return receiver;
	}

	public void setReceiver( String receiver )
	{
		this.receiver = receiver;
	}

	public String getNextAmount()
	{
		return nextAmount;
	}

	public void setNextAmount( String nextAmount )
	{
		this.nextAmount = nextAmount;
	}

	public String getFrequency()
	{
		return frequency;
	}

	public void setFrequency( String frequency )
	{
		this.frequency = frequency;
	}

	public String getNextDate()
	{
		return nextDate;
	}

	public void setNextDate( String nextDate )
	{
		this.nextDate = nextDate;
	}

	public String getAmmendToken()
	{
		return ammendToken;
	}

	public void setAmmendToken( String ammendToken )
	{
		this.ammendToken = ammendToken;
	}

	public String getCancelToken()
	{
		return cancelToken;
	}

	public void setCancelToken( String cancelToken )
	{
		this.cancelToken = cancelToken;
	}

	public String getMoreInfoToken()
	{
		return moreInfoToken;
	}

	public void setMoreInfoToken( String moreInfoToken )
	{
		this.moreInfoToken = moreInfoToken;
	}

}
