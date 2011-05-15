package ie.aib.entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Payee
{
	private String name;
	private String paymentType;
	private String referenceNumber;
	private String NSC;
	private String accountNumber;
	
	private String ammendToken;
	private String ammendBillkey;
	private String ammendBillType;
	
	private String deleteToken;
	private String deleteBillkey;
	private String deleteBillType;
	
	private String selectToken;
	private String deletedBillkey;
	
	public Payee( String name, String type, String ref, String NSC , String accnum , String options )
	{
		this.name = name;
		this.paymentType = type;
		this.referenceNumber = ref;
		this.NSC = NSC;
		this.accountNumber = accnum;
		
		int count = 0;
		
		Pattern forms = Pattern.compile( "<form[^>]*>(.*?)</form>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
		Matcher matcher = forms.matcher( options );

		while ( matcher.find() )
		{
			Pattern inputs = Pattern.compile( "<input[^>]*>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
			Matcher inputsMatch = inputs.matcher( matcher.group() );
			
			while ( inputsMatch.find() )
			{
				if( count == 0 )
				{
					if( inputsMatch.group().contains( "Token" ) )
					{
						String[] bits = inputsMatch.group().split( "\"" );
						this.ammendToken = bits[5];
					}
					else if( inputsMatch.group().contains( "billkey" ) )
					{
						String[] bits = inputsMatch.group().split( "\"" );
						this.ammendBillkey = bits[5];
					}
					else if( inputsMatch.group().contains( "billtype" ) )
					{
						String[] bits = inputsMatch.group().split( "\"" );
						this.ammendBillkey = bits[5];
					}					
				}
				
				if( count == 1 )
				{
					if( inputsMatch.group().contains( "Token" ) )
					{
						String[] bits = inputsMatch.group().split( "\"" );
						this.deleteToken = bits[5];
					}
					else if( inputsMatch.group().contains( "billkey" ) )
					{
						String[] bits = inputsMatch.group().split( "\"" );
						this.deleteBillkey = bits[5];
					}
					else if( inputsMatch.group().contains( "billtype" ) )
					{
						String[] bits = inputsMatch.group().split( "\"" );
						this.deleteBillType = bits[5];
					}					
				}
				
				if( count == 2 )
				{
					if( inputsMatch.group().contains( "Token" ) )
					{
						String[] bits = inputsMatch.group().split( "\"" );
						this.deleteToken = bits[5];
					}
					else if( inputsMatch.group().contains( "selectedBill" ) )
					{
						String[] bits = inputsMatch.group().split( "\"" );
						this.deleteBillkey = bits[5];
					}									
				}
			}
			
			count++;
		}
		
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public String getPaymentType()
	{
		return paymentType;
	}

	public void setPaymentType( String paymentType )
	{
		this.paymentType = paymentType;
	}

	public String getReferenceNumber()
	{
		return referenceNumber;
	}

	public void setReferenceNumber( String referenceNumber )
	{
		this.referenceNumber = referenceNumber;
	}

	public String getNSC()
	{
		return NSC;
	}

	public void setNSC( String nSC )
	{
		NSC = nSC;
	}

	public String getAccountNumber()
	{
		return accountNumber;
	}

	public void setAccountNumber( String accountNumber )
	{
		this.accountNumber = accountNumber;
	}

	public String getAmmendToken()
	{
		return ammendToken;
	}

	public void setAmmendToken( String ammendToken )
	{
		this.ammendToken = ammendToken;
	}

	public String getAmmendBillkey()
	{
		return ammendBillkey;
	}

	public void setAmmendBillkey( String ammendBillkey )
	{
		this.ammendBillkey = ammendBillkey;
	}

	public String getAmmendBillType()
	{
		return ammendBillType;
	}

	public void setAmmendBillType( String ammendBillType )
	{
		this.ammendBillType = ammendBillType;
	}

	public String getDeleteToken()
	{
		return deleteToken;
	}

	public void setDeleteToken( String deleteToken )
	{
		this.deleteToken = deleteToken;
	}

	public String getDeleteBillkey()
	{
		return deleteBillkey;
	}

	public void setDeleteBillkey( String deleteBillkey )
	{
		this.deleteBillkey = deleteBillkey;
	}

	public String getDeleteBillType()
	{
		return deleteBillType;
	}

	public void setDeleteBillType( String deleteBillType )
	{
		this.deleteBillType = deleteBillType;
	}

	public String getSelectToken()
	{
		return selectToken;
	}

	public void setSelectToken( String selectToken )
	{
		this.selectToken = selectToken;
	}

	public String getDeletedBillkey()
	{
		return deletedBillkey;
	}

	public void setDeletedBillkey( String deletedBillkey )
	{
		this.deletedBillkey = deletedBillkey;
	}
}
