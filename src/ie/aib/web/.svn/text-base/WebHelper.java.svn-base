package ie.aib.web;

import ie.aib.Timeout;
import ie.aib.entities.Account;
import ie.aib.entities.Payee;
import ie.aib.entities.StandingOrder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class WebHelper
{
	// web pages
	private String baseUrl;
	private String pageAccountOverview;
	private String pageLogin;
	private String pageLogout;
	private String pageStatement;
	private String makePayment;
	private String paymentsLog;
	private String managePayees;
	private String standingOrders;

	// broswer
	private String userAgent;

	// session tokens
	private String token;
	private String tokenStatement;
	private String tokenTransfer;
	private String tokenMobiletopup;

	// transfer and payment tokens
	private String tokenMakePayment;
	private String tokenPaymentLog;
	private String tokenManagePayees;
	private String tokenStandingOrders;
	private String tokenPaymentLogExpanded;

	// logout token
	private String tokenLogout;
	
	
	// quickpay
	private String quickPayToken;
	private String quickPayPacDigit;
	private ArrayList< String > paymentTypes;
	private ArrayList< String > paymentFrom;
	private ArrayList< String > paymentTo;	
	private String paymentTypesSelected;
	private String paymentFromSelected;
	private String paymentToSelected;

	// temp working vars
	private String regNumber;
	private int pac1;
	private int pac2;
	private int pac3;
	private int challenge;

	// http stuff
	private List<Cookie> cookies;
	private DefaultHttpClient httpclient;
	private HttpResponse response;
	private HttpEntity entity;
	private List<NameValuePair> postFields;

	// result holders
	private String lastError;
	private boolean error;
	private String result;
	private boolean output;

	// this instance
	private static WebHelper instance = null;

	private WebHelper()
	{
		this.baseUrl = "https://aibinternetbanking.aib.ie/inet/roi/";
		this.userAgent = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.1.4pre) Gecko/20090829 Ubuntu/9.04 (jaunty) Shiretoko/3.5.4pre";
		this.pageAccountOverview = "accountoverview.htm";
		this.pageLogin = "login.htm";
		this.pageLogout = "logout.htm";
		this.pageStatement = "statement.htm";
		this.makePayment = "transfersandpaymentslanding.htm";
		this.paymentsLog = "paymentlogs.htm";
		this.managePayees = "managemypayees.htm";
		this.standingOrders = "standingorderlist.htm";
		this.token = "";
	}

	public void initHTTPClient()
	{
		try
		{
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register( new Scheme( "http" , PlainSocketFactory.getSocketFactory() , 80 ) );
			schemeRegistry.register( new Scheme( "https" , new EasySSLSocketFactory() , 443 ) );

			HttpParams params = new BasicHttpParams();
			params.setParameter( ConnManagerPNames.MAX_TOTAL_CONNECTIONS , 30 );
			params.setParameter( ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE , new ConnPerRouteBean( 30 ) );
			params.setParameter( CoreProtocolPNames.USE_EXPECT_CONTINUE , false );
			HttpProtocolParams.setVersion( params , HttpVersion.HTTP_1_1 );

			ClientConnectionManager cm = new SingleClientConnManager( params , schemeRegistry );
			this.httpclient = new DefaultHttpClient( cm , params );

		}
		catch( Exception e )
		{
			this.error = true;
			this.lastError = e.getMessage();
		}
	}

	private boolean getRequest( String url )
	{
		Timeout.getInstance().autoLogoutTimer();

		try
		{
			HttpGet httpget = new HttpGet( url );
			httpget.setHeader( "User-Agent" , this.userAgent );
			this.response = this.httpclient.execute( httpget );
			this.readPage();
			this.getTokens();
			return true;
		}
		catch( Exception e )
		{
			this.error = true;
			this.lastError = e.getMessage();
			return false;
		}
	}

	private boolean postRequest( String url , List<NameValuePair> fields )
	{
		Timeout.getInstance().autoLogoutTimer();

		try
		{
			HttpPost reqPost = new HttpPost( url );
			reqPost.setHeader( "User-Agent" , this.userAgent );
			reqPost.setEntity( new UrlEncodedFormEntity( fields , HTTP.UTF_8 ) );
			this.response = this.httpclient.execute( reqPost );
			this.readPage();
			this.getTokens();
			this.prepareQuickPay();
			return true;
		}
		catch( Exception e )
		{
			this.error = true;
			this.lastError = e.getMessage();
			return false;
		}
	}
	
	
	public void prepareQuickPay()
	{
		try
		{
			Pattern quickPayRegex = Pattern.compile( "<form id=\"qtForm\"(.*?)</form>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
			Matcher quickPay = quickPayRegex.matcher( this.result );
	
	
			while ( quickPay.find() )
			{
				Pattern tokenRegex = Pattern.compile( "id=\"transactionToken\" value=\"(.*)\"" , Pattern.CASE_INSENSITIVE );
				Matcher tokenMatcher = tokenRegex.matcher( quickPay.group() );
				
				while ( tokenMatcher.find() )
				{
					String match[] = tokenMatcher.group().split( "\"" );
					this.quickPayToken = match[3];
					break;
				}
				
				
				Pattern pacDigitRegex = Pattern.compile( "<strong>Digit(.*?)</strong>" , Pattern.CASE_INSENSITIVE );
				Matcher pacMatcher = pacDigitRegex.matcher( quickPay.group() );
				
				while ( pacMatcher.find() )
				{
					String[] match = pacMatcher.group().split( ";" );
					String[] match2 = match[1].split( "<" );
					this.quickPayPacDigit = match2[0];
					break;
				}
				
							
				int count = 0;
								
				this.paymentTypes = new ArrayList< String >();
				this.paymentFrom = new ArrayList< String >();
				this.paymentTo = new ArrayList< String >();
				
				this.paymentTypesSelected = "-1";
				this.paymentFromSelected = "-1";
				this.paymentToSelected = "-1";
								
				Pattern selectRegex = Pattern.compile( "<select[^>]*>(.*?)</select>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
				Matcher selectMatcher = selectRegex.matcher( quickPay.group() );
				
				while ( selectMatcher.find() )
				{
					Pattern optionsRegex = Pattern.compile( "<option[^>]*>(.*?)</option>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
					Matcher options = optionsRegex.matcher( selectMatcher.group() );		
					
					while ( options.find() )
					{
						if( options.group().contains( "selected" ) )
						{
							String[] selected = options.group().split( "\"" );
							
							if( count == 0 )
							{
								this.paymentTypesSelected = selected[ 1 ];
								if( this.paymentTypesSelected.length() == 0 )
								{
									this.paymentTypesSelected = "-1";
								}
							}
							else if( count == 1 )
							{
								this.paymentFromSelected = selected[ 1 ];
							}
							else if( count == 2 )
							{
								this.paymentToSelected = selected[ 1 ];
							}
						}
						
						String[] opt = options.group().split( ">" );
						opt = opt[1].split( "<" );
						String entry = opt[0].replace( ":" , "" );	
						
						if( count == 0 )
						{
							this.paymentTypes.add( entry );
						}
						else if( count == 1 )
						{
							this.paymentFrom.add( entry );
						}
						else if( count == 2 )
						{
							this.paymentTo.add( entry );
						}
									
					}	
					
					count++;
				}
			}
		}
		catch( Exception e )
		{
			
		}
	}
	

	public boolean login1( String registrationNumber )
	{
		this.regNumber = registrationNumber;
		Pattern tokenRegex = Pattern.compile( "id=\"transactionToken\" value=\"(.*)\"" , Pattern.CASE_INSENSITIVE );
		Pattern pac1Regex = Pattern.compile( "<label for=\"digit1\"><strong>Digit (.*)</strong></label>" , Pattern.CASE_INSENSITIVE );
		Pattern pac2Regex = Pattern.compile( "<label for=\"digit2\"><strong>Digit (.*)</strong></label>" , Pattern.CASE_INSENSITIVE );
		Pattern pac3Regex = Pattern.compile( "<label for=\"digit3\"><strong>Digit (.*)</strong></label>" , Pattern.CASE_INSENSITIVE );

		if ( this.getRequest( this.baseUrl + this.pageLogin ) == true )
		{
			Matcher tokenMatcher = tokenRegex.matcher( this.result );

			while ( tokenMatcher.find() )
			{
				String match[] = tokenMatcher.group().split( "\"" );
				this.token = match[3];
				break;
			}

			if ( this.token == "" )
			{
				this.error = true;
				this.lastError = "Unable to find token";
				return false;
			}

			this.postFields = new ArrayList<NameValuePair>();
			this.postFields.add( new BasicNameValuePair( "jsEnabled" , "TRUE" ) );
			this.postFields.add( new BasicNameValuePair( "transactionToken" , this.token ) );
			this.postFields.add( new BasicNameValuePair( "regNumber" , this.regNumber ) );
			this.postFields.add( new BasicNameValuePair( "_target1" , "true" ) );

			if ( this.postRequest( this.baseUrl + this.pageLogin , this.postFields ) == true )
			{
				if ( !this.result.contains( "last four digits" ) )
				{
					this.lastError = "You must enter an eight digit registration number";
					return false;
				}

				tokenMatcher = tokenRegex.matcher( this.result );

				while ( tokenMatcher.find() )
				{
					String match[] = tokenMatcher.group().split( "\"" );
					this.token = match[3];
					break;
				}

				Matcher pac1Matcher = pac1Regex.matcher( this.result );
				Matcher pac2Matcher = pac2Regex.matcher( this.result );
				Matcher pac3Matcher = pac3Regex.matcher( this.result );

				while ( pac1Matcher.find() )
				{
					this.pac1 = Integer.parseInt( this.getPacRequested( pac1Matcher.group() ) );
					break;
				}

				while ( pac2Matcher.find() )
				{
					this.pac2 = Integer.parseInt( this.getPacRequested( pac2Matcher.group() ) );
					break;
				}

				while ( pac3Matcher.find() )
				{
					this.pac3 = Integer.parseInt( this.getPacRequested( pac3Matcher.group() ) );
					break;
				}

				if ( this.result.contains( "home phone number" ) )
				{
					this.challenge = 1;
				}
				else if ( this.result.contains( "work phone number" ) )
				{
					this.challenge = 3;
				}
				else if ( this.result.contains( "mobile phone number" ) )
				{
					this.challenge = 4;
				}
				else
				{
					this.challenge = 2;
				}

				return true;

			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	public boolean login2( String pac1 , String pac2 , String pac3 , String challenge )
	{

		this.postFields = new ArrayList<NameValuePair>();
		this.postFields.add( new BasicNameValuePair( "_finish" , "true" ) );
		this.postFields.add( new BasicNameValuePair( "transactionToken" , this.token ) );
		this.postFields.add( new BasicNameValuePair( "regNumber" , this.regNumber ) );
		this.postFields.add( new BasicNameValuePair( "pacDetails.pacDigit1" , pac1 ) );
		this.postFields.add( new BasicNameValuePair( "pacDetails.pacDigit2" , pac2 ) );
		this.postFields.add( new BasicNameValuePair( "pacDetails.pacDigit3" , pac3 ) );
		this.postFields.add( new BasicNameValuePair( "challengeDetails.challengeEntered" , challenge ) );

		if ( this.postRequest( this.baseUrl + this.pageLogin , this.postFields ) == true )
		{
			if ( this.result.contains( "continue to account overview" ) )
			{
				Pattern tokenRegex = Pattern.compile( "id=\"transactionToken\" value=\"(.*)\"><input" , Pattern.CASE_INSENSITIVE );
				Matcher tokenMatcher = tokenRegex.matcher( this.result );

				while ( tokenMatcher.find() )
				{
					String match[] = tokenMatcher.group().split( "\"" );
					this.token = match[3];
					break;
				}

				this.postFields = new ArrayList<NameValuePair>();
				this.postFields.add( new BasicNameValuePair( "iBankFormSubmission" , "false" ) );
				this.postFields.add( new BasicNameValuePair( "transactionToken" , this.token ) );
				if ( this.postRequest( this.baseUrl + this.pageAccountOverview , this.postFields ) == false )
				{
					return false;
				}
			}

			if ( this.result.contains( "You are securely logged in." ) )
			{
				return true;
			}
			else if ( this.result.contains( "unsupported browser action" ) )
			{
				this.lastError = "You have been logged out of AIB Internet banking due to an unsupported browser action.";
				return false;
			}
			else
			{
				this.lastError = "The details you have entered are incorrect";
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	public String getResult()
	{
		return this.result;
	}

	public void getTokens()
	{
		Pattern mainMenuRegex = Pattern.compile( "<div class=\"mainNavDivlet\">(.*?)</ul></div>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
		Matcher mainMenuMatcher = mainMenuRegex.matcher( this.result );

		int count = 0;

		while ( mainMenuMatcher.find() )
		{
			Pattern entriesRegex = Pattern.compile( "<li>(.*?)</li>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
			Matcher entries = entriesRegex.matcher( mainMenuMatcher.group() );

			while ( entries.find() )
			{

				Pattern statementTokenRegex = Pattern.compile( "name=\"transactionToken\" id=\"transactionToken\" value=\"(.*)\">(.*?)</form>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
				Matcher tokenMatcher = statementTokenRegex.matcher( entries.group() );

				while ( tokenMatcher.find() )
				{
					String match[] = tokenMatcher.group().split( "\"" );

					if ( count == 0 )
					{

					}
					else if ( count == 1 )
					{
						this.tokenStatement = match[5];
					}
					else if ( count == 2 )
					{
						this.tokenTransfer = match[5];
					}
					else if ( count == 3 )
					{
						this.tokenMobiletopup = match[5];
					}

				}

				count++;
			}

			break;
		}

		Pattern submainMenuRegex = Pattern.compile( "<div class=\"subNavDivlet\">(.*?)</ul></div>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
		Matcher submainMenuMatcher = submainMenuRegex.matcher( this.result );

		count = 0;

		while ( submainMenuMatcher.find() )
		{
			Pattern entriesRegex = Pattern.compile( "<li>(.*?)</li>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
			Matcher entries = entriesRegex.matcher( mainMenuMatcher.group() );

			while ( entries.find() )
			{

				Pattern statementTokenRegex = Pattern.compile( "name=\"transactionToken\" id=\"transactionToken\" value=\"(.*)\">(.*?)</form>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
				Matcher tokenMatcher = statementTokenRegex.matcher( entries.group() );

				while ( tokenMatcher.find() )
				{
					String match[] = tokenMatcher.group().split( "\"" );

					if ( count == 0 )
					{
						this.tokenMakePayment = match[5];
					}
					else if ( count == 1 )
					{
						this.tokenPaymentLog = match[5];
					}
					else if ( count == 2 )
					{
						this.tokenManagePayees = match[5];
					}
					else if ( count == 3 )
					{
						this.tokenStandingOrders = match[5];
					}

				}

				count++;
			}

			break;
		}

		Pattern paymentLogTokenRegex = Pattern.compile( "name=\"transactionToken\" id=\"transactionToken\" value=\"(.*)\">(.*?)</form>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
		Matcher tokensMatcher = paymentLogTokenRegex.matcher( this.result );

		while ( tokensMatcher.find() )
		{
			if ( tokensMatcher.group().contains( "showAllBills" ) )
			{
				String match[] = tokensMatcher.group().split( "\"" );
				this.tokenPaymentLogExpanded = match[5];
			}
			else if ( tokensMatcher.group().contains( "log out" ) )
			{
				String match[] = tokensMatcher.group().split( "\"" );
				this.tokenLogout = match[5];
				break;
			}
		}

	}

	public ArrayList<Account> getAccounts()
	{
		ArrayList<Account> temp = new ArrayList<Account>();
		Pattern accountsRegex = Pattern.compile( "<img src=\"/roi/_img/buttons/plus_pl_db.gif\" alt=\"expand\" />(.*?)<span>(.*?)</span>(.*?)</button>(.*?)<h3>(.*?)</h3>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
		Matcher matcher = accountsRegex.matcher( this.result );

		int sequence = 0;

		while ( matcher.find() )
		{
			String name = "";
			String balance = "";

			Pattern accountName = Pattern.compile( "<span>(.*?)</span>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
			Matcher accountNameMatcher = accountName.matcher( matcher.group() );

			while ( accountNameMatcher.find() )
			{
				name = accountNameMatcher.group().trim();
				name = this.removeHTML( name );
			}

			Pattern accountBalance = Pattern.compile( "<h3>(.*?)</h3>" , Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
			Matcher accountBalanceMatcher = accountBalance.matcher( matcher.group() );

			while ( accountBalanceMatcher.find() )
			{
				balance = accountBalanceMatcher.group().trim();
				balance = this.removeHTML( balance );
			}

			temp.add( new Account( name , balance , sequence ) );

			sequence++;
		}

		return temp;
	}

	public ArrayList<Payee> getPayees()
	{
		if ( this.goToTransferAndPayments() == false )
		{
			return new ArrayList<Payee>();
		}

		this.postFields = new ArrayList<NameValuePair>();
		this.postFields.add( new BasicNameValuePair( "transactionToken" , this.tokenManagePayees ) );
		this.postFields.add( new BasicNameValuePair( "isFormButtonClicked" , "false" ) );

		if ( this.postRequest( this.baseUrl + this.managePayees , this.postFields ) == false )
		{
			return new ArrayList<Payee>();
		}

		ArrayList<Payee> temp = new ArrayList<Payee>();

		Pattern table = Pattern.compile( "<table[^>]*>(.*?)</table>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
		Matcher matcher = table.matcher( this.result );

		while ( matcher.find() )
		{

			Pattern rows = Pattern.compile( "<tr[^>]*>(.*?)</tr>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
			Matcher rowMatch = rows.matcher( matcher.group() );

			int cnt = 0;

			while ( rowMatch.find() )
			{
				if ( cnt == 0 )
				{
					cnt++;
					continue;
				}

				String[] payeeEntry = { "", "", "", "", "", "" };
				int count = 0;

				Pattern cols = Pattern.compile( "<td[^>]*>(.*?)</td>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
				Matcher colMatch = cols.matcher( rowMatch.group() );

				while ( colMatch.find() )
				{
					payeeEntry[count] = this.removeHTML( colMatch.group( 1 ) );
					count++;
				}

				temp.add( new Payee( payeeEntry[0] , payeeEntry[1] , payeeEntry[2] , payeeEntry[3] , payeeEntry[4] , payeeEntry[5] ) );

				cnt++;
			}
			break;
		}

		return temp;
	}

	public ArrayList<String[]> getPaymentlog()
	{
		if ( this.goToTransferAndPayments() == false )
		{
			return new ArrayList<String[]>();
		}

		if ( this.goToTransferLog() == false )
		{
			return new ArrayList<String[]>();
		}

		this.postFields = new ArrayList<NameValuePair>();
		this.postFields.add( new BasicNameValuePair( "transactionToken" , this.tokenPaymentLogExpanded ) );
		this.postFields.add( new BasicNameValuePair( "showAllBills" , "true" ) );

		if ( this.postRequest( this.baseUrl + this.paymentsLog , this.postFields ) == false )
		{
			return new ArrayList<String[]>();
		}

		ArrayList<String[]> temp = new ArrayList<String[]>();

		Pattern table = Pattern.compile( "<table[^>]*>(.*?)</table>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
		Matcher matcher = table.matcher( this.result );

		while ( matcher.find() )
		{
			Pattern rows = Pattern.compile( "<tr[^>]*>(.*?)</tr>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
			Matcher rowMatch = rows.matcher( matcher.group() );

			int cnt = 0;

			while ( rowMatch.find() )
			{
				if ( cnt == 0 )
				{
					cnt++;
					continue;
				}

				String[] logEntry = { "", "", "", "", "", "" };
				int count = 0;

				Pattern cols = Pattern.compile( "<td[^>]*>(.*?)</td>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
				Matcher colMatch = cols.matcher( rowMatch.group() );

				while ( colMatch.find() )
				{
					logEntry[count] = this.removeHTML( colMatch.group( 1 ) );
					count++;
				}

				temp.add( logEntry );

				cnt++;
			}
			break;
		}

		return temp;
	}

	public ArrayList<StandingOrder> getStandingOrders()
	{
		if ( this.goToTransferAndPayments() == false )
		{
			return new ArrayList<StandingOrder>();
		}

		this.postFields = new ArrayList<NameValuePair>();
		this.postFields.add( new BasicNameValuePair( "transactionToken" , this.tokenStandingOrders ) );
		this.postFields.add( new BasicNameValuePair( "isFormButtonClicked" , "false" ) );


		if ( this.postRequest( this.baseUrl + this.standingOrders , this.postFields ) == false )
		{
			return new ArrayList<StandingOrder>();
		}

		ArrayList<StandingOrder> temp = new ArrayList<StandingOrder>();

		Pattern table = Pattern.compile( "<table[^>]*>(.*?)</table>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
		Matcher matcher = table.matcher( this.result );

		while ( matcher.find() )
		{

			Pattern rows = Pattern.compile( "<tr[^>]*>(.*?)</tr>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
			Matcher rowMatch = rows.matcher( matcher.group() );

			int cnt = 0;

			while ( rowMatch.find() )
			{
				if ( cnt == 0 )
				{
					cnt++;
					continue;
				}

				String[] logEntry = { "", "", "", "", "", "" };
				int count = 0;

				Pattern cols = Pattern.compile( "<td[^>]*>(.*?)</td>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
				Matcher colMatch = cols.matcher( rowMatch.group() );

				while ( colMatch.find() )
				{
					logEntry[count] = this.removeHTML( colMatch.group( 1 ) );
					count++;
				}

				temp.add( new StandingOrder( logEntry[0] , logEntry[1] , logEntry[2] , logEntry[3] , logEntry[4] , logEntry[5] ) );

				cnt++;
			}
			break;
		}

		return temp;
	}

	private String removeHTML( String html )
	{
		html = html.trim();
		html = html.replaceAll( "\\<.*?>" , "" );
		html = html.trim();
		html = html.replaceAll( "&nbsp;" , "" );
		html = html.trim();
		html = html.replaceAll( "&amp;" , "" );
		html = html.trim();
		html = html.replaceAll( "\\n" , "" );
		html = html.trim();
		html = html.replaceAll( "\\n\\r" , "" );
		html = html.trim();
		html = html.replaceAll( "&euro;" , "€" );

		return html;
	}
	
	public boolean goToTransferAndPayments()
	{
		this.postFields = new ArrayList<NameValuePair>();
		this.postFields.add( new BasicNameValuePair( "transactionToken" , this.tokenTransfer ) );
		this.postFields.add( new BasicNameValuePair( "isFormButtonClicked" , "false" ) );

		if ( this.postRequest( this.baseUrl + this.makePayment , this.postFields ) )
		{
			return true;
		}

		return false;
	}

	public boolean changeQuickPayOptions( String selected )
	{
		this.postFields = new ArrayList<NameValuePair>();
		this.postFields.add( new BasicNameValuePair( "quickPay" , "true" ) );
		this.postFields.add( new BasicNameValuePair( "quickPaySubmitType" , "1" ) );
		this.postFields.add( new BasicNameValuePair( "transactionToken" , this.quickPayToken ) );
		this.postFields.add( new BasicNameValuePair( "isFormButtonClicked" , "false" ) );
		this.postFields.add( new BasicNameValuePair( "iBankFormSubmission" , "true" ) );
		this.postFields.add( new BasicNameValuePair( "transferType" , selected ) );

		if ( this.postRequest( this.baseUrl + this.pageAccountOverview , this.postFields ) )
		{
			return true;
		}

		return false;
	}
	
	
	public boolean submitQuickPayOptions( String fromIndex , String toIndex , String euro , String cent , String pac , String type )
	{
		this.postFields = new ArrayList<NameValuePair>();
		this.postFields.add( new BasicNameValuePair( "quickPay" , "true" ) );
		this.postFields.add( new BasicNameValuePair( "quickPaySubmitType" , "0" ) );
		this.postFields.add( new BasicNameValuePair( "transactionToken" , this.quickPayToken ) );
		this.postFields.add( new BasicNameValuePair( "isFormButtonClicked" , "true" ) );
		this.postFields.add( new BasicNameValuePair( "iBankFormSubmission" , "true" ) );
		this.postFields.add( new BasicNameValuePair( "transferType" , type ) );
		this.postFields.add( new BasicNameValuePair( "fromAccountIndex" , fromIndex ) );
		this.postFields.add( new BasicNameValuePair( "toAccountIndex" , toIndex ) );
		this.postFields.add( new BasicNameValuePair( "amount.euro" , euro ) );
		this.postFields.add( new BasicNameValuePair( "amount.cent" , cent ) );
		this.postFields.add( new BasicNameValuePair( "confirmPac.pacDigit" , pac ) );
		
		if ( this.postRequest( this.baseUrl + this.pageAccountOverview , this.postFields ) )
		{
			if( this.result.contains( "You have completed a payment from your" ) )
			{			
				return true;
			}
			else
			{
				if( this.result.contains( "Your payment cannot" ) )
				{
					this.lastError = "Your payment cannot be completed. Please check your details again";
				}
				
				return false;
			}
		}

		this.lastError = "Unable to process request, please try again";
		return false;
	}

	public boolean goToTransferLog()
	{
		this.postFields = new ArrayList<NameValuePair>();
		this.postFields.add( new BasicNameValuePair( "transactionToken" , this.tokenPaymentLog ) );
		this.postFields.add( new BasicNameValuePair( "isFormButtonClicked" , "false" ) );

		if ( this.postRequest( this.baseUrl + this.paymentsLog , this.postFields ) )
		{
			return true;
		}

		return false;
	}
	
	public boolean logout()
	{
		this.postFields = new ArrayList<NameValuePair>();
		this.postFields.add( new BasicNameValuePair( "transactionToken" , this.tokenLogout ) );

		if ( this.postRequest( this.baseUrl + this.pageLogout , this.postFields ) )
		{
			return true;
		}

		return false;
	}	

	public ArrayList<String[]> getAccountStatement( int index )
	{
		ArrayList<String[]> temp = new ArrayList<String[]>();

		this.postFields = new ArrayList<NameValuePair>();
		this.postFields.add( new BasicNameValuePair( "index" , String.valueOf( index ) ) );
		this.postFields.add( new BasicNameValuePair( "transactionToken" , this.tokenStatement ) );
		this.postFields.add( new BasicNameValuePair( "isFormButtonClicked" , "false" ) );
		this.postRequest( this.baseUrl + this.pageStatement , this.postFields );

		Pattern tokenRegex = Pattern.compile( "<input type=\"hidden\" name=\"transactionToken\" id=\"transactionToken\" value=\"(.*)\">" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );

		Matcher tokenMatcher = tokenRegex.matcher( this.result );

		while ( tokenMatcher.find() )
		{
			String match[] = tokenMatcher.group().split( "\"" );
			this.token = match[7];
			break;
		}

		this.postFields = new ArrayList<NameValuePair>();
		this.postFields.add( new BasicNameValuePair( "index" , String.valueOf( index ) ) );
		this.postFields.add( new BasicNameValuePair( "transactionToken" , this.token ) );
		this.postFields.add( new BasicNameValuePair( "iBankFormSubmission" , "true" ) );
		this.postRequest( this.baseUrl + this.pageStatement , this.postFields );

		Pattern table = Pattern.compile( "<table[^>]*>(.*?)</table>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
		Matcher matcher = table.matcher( this.result );

		while ( matcher.find() )
		{
			Pattern rows = Pattern.compile( "<tr[^>]*>(.*?)</tr>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
			Matcher rowMatch = rows.matcher( matcher.group() );

			while ( rowMatch.find() )
			{
				String[] statementEntry = { "", "", "", "", "" };
				int count = 0;

				Pattern cols = Pattern.compile( "<td[^>]*>(.*?)</td>" , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL );
				Matcher colMatch = cols.matcher( rowMatch.group() );

				while ( colMatch.find() )
				{
					statementEntry[count] = this.removeHTML( colMatch.group( 1 ) );
					count++;
				}

				temp.add( statementEntry );
			}
			break;
		}

		temp.remove( 0 );
		return temp;
	}

	private String getPacRequested( String html )
	{
		String match[] = html.split( "<strong>" );
		String exp[] = match[1].split( "</strong>" );
		String digits[] = exp[0].split( " " );
		return digits[1].trim();
	}

	private void readPage()
	{
		this.cookies = this.httpclient.getCookieStore().getCookies();

		String html = "";
		try
		{
			InputStream in = this.response.getEntity().getContent();
			BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );
			StringBuilder str = new StringBuilder();
			String line = null;
			while ( (line = reader.readLine()) != null )
			{
				if ( this.output == true )
				{
					Log.e( "WebHelper" , line );
				}
				str.append( line );
			}
			in.close();
			html = str.toString();
		}
		catch( Exception e )
		{
			html = e.getMessage();
			e.printStackTrace();
		}

		this.result = html;
	}

	public String getLastError()
	{
		this.error = false;
		return this.lastError;
	}

	public static WebHelper getInstance()
	{
		if ( WebHelper.instance == null )
		{
			WebHelper.instance = new WebHelper();
		}

		return WebHelper.instance;
	}

	public int getPac1()
	{
		return pac1;
	}

	public int getPac2()
	{
		return pac2;
	}

	public int getPac3()
	{
		return pac3;
	}

	public int getChallenge()
	{
		return challenge;
	}

	public String getRegNumber()
	{
		return regNumber;
	}

	public String[] getPaymentTypes()
	{
		String[] types = new String[ this.paymentTypes.size() ];
		
		for( int y = 0 ; y < this.paymentTypes.size(); y++ )
		{
			types[ y ] = this.paymentTypes.get( y );
		}
		
		return types;
	}

	public String[] getPaymentFrom()
	{
		String[] From = new String[ this.paymentFrom.size() ];
		
		for( int y = 0 ; y < this.paymentFrom.size(); y++ )
		{
			From[ y ] = this.paymentFrom.get( y );
		}
		
		return From;
	}

	public String[] getPaymentTo()
	{
		String[] To = new String[ this.paymentTo.size() ];
		
		for( int y = 0 ; y < this.paymentTo.size(); y++ )
		{
			To[ y ] = this.paymentTo.get( y );
		}
		
		return To;
	}

	public String getPaymentTypesSelected()
	{
		return this.paymentTypesSelected;
	}

	public String getPaymentFromSelected()
	{
		return this.paymentFromSelected;
	}

	public String getPaymentToSelected()
	{
		return this.paymentToSelected;
	}

	public String getQuickPayPacDigit()
	{
		return this.quickPayPacDigit + "";
	}

}
