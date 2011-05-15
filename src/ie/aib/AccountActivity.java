package ie.aib;

import ie.aib.entities.Person;
import ie.aib.web.WebHelper;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AccountActivity extends ListActivity implements Runnable, onTimedOut
{
	private ProgressDialog pd;
	private String title;
	private int runnableRequest;
	private Thread thread;
	private boolean interrupt;
	private String quickPayType;
	private boolean quickPayWorking;
	private boolean quickPayVisible;

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.accountview );

		this.title = WebHelper.getInstance().getRegNumber();
		this.setTitle( this.title );

		Timeout.getInstance().attach( this );
		Timeout.getInstance().autoLogoutTimer();

		this.getListView();
		ListAdapter adapter = new AccountAdapter( Person.getInstance() , this );
		this.setListAdapter( adapter );

		EditText pac1Input1 = ( EditText ) findViewById( R.id.amountEuro );
		pac1Input1.addTextChangedListener( this.euroWatcher );

		pac1Input1 = ( EditText ) findViewById( R.id.amountCent );
		pac1Input1.addTextChangedListener( this.centWatcher );

		pac1Input1 = ( EditText ) findViewById( R.id.quickPayPacDigit );
		pac1Input1.addTextChangedListener( this.pacWatcher1 );

		this.prepareQuickPayFields();

		this.quickPayVisible = true;
	}

	private void prepareQuickPayFields()
	{
		int selected = -1;

		Spinner s1 = ( Spinner ) this.findViewById( R.id.spinner1 );
		Spinner s2 = ( Spinner ) this.findViewById( R.id.spinner2 );
		Spinner s3 = ( Spinner ) this.findViewById( R.id.spinner3 );
		EditText amountEuro = ( EditText ) this.findViewById( R.id.amountEuro );
		EditText amountCent = ( EditText ) this.findViewById( R.id.amountCent );
		EditText quickPayPac = ( EditText ) this.findViewById( R.id.quickPayPacDigit );
		Button quickPayButton = ( Button ) this.findViewById( R.id.quickPayButton );

		if ( this.quickPayWorking != true )
		{

			ArrayAdapter< String > spinnerArrayAdapter1 = new ArrayAdapter< String >( this , android.R.layout.simple_spinner_dropdown_item , WebHelper.getInstance().getPaymentTypes() );
			spinnerArrayAdapter1.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
			s1.setAdapter( spinnerArrayAdapter1 );
			selected = Integer.parseInt( WebHelper.getInstance().getPaymentTypesSelected() );
			selected = ( selected == -1 ? 0 : selected );
			s1.setSelection( selected );

			s1.setOnItemSelectedListener( new OnItemSelectedListener()
			{
				@Override
				public void onItemSelected( AdapterView< ? > parentView , View selectedItemView , int position , long id )
				{
					Spinner s2 = ( Spinner ) AccountActivity.this.findViewById( R.id.spinner2 );
					Spinner s3 = ( Spinner ) AccountActivity.this.findViewById( R.id.spinner3 );
					EditText amountEuro = ( EditText ) AccountActivity.this.findViewById( R.id.amountEuro );
					EditText amountCent = ( EditText ) AccountActivity.this.findViewById( R.id.amountCent );
					EditText quickPayPac = ( EditText ) AccountActivity.this.findViewById( R.id.quickPayPacDigit );
					Button quickPayButton = ( Button ) AccountActivity.this.findViewById( R.id.quickPayButton );

					if ( position > 0 )
					{
						AccountActivity.this.quickPayWorking = true;
						AccountActivity.this.quickPayType = String.valueOf( position );
						AccountActivity.this.thread = new Thread( AccountActivity.this );
						AccountActivity.this.runnableRequest = 10;
						AccountActivity.this.createCancelProgressDialog( "Fetching payment from options" , "Please standby while we fetch a list of payment from accounts" );
						AccountActivity.this.thread.start();
					}
					else
					{
						s2.setEnabled( false );
						s3.setEnabled( false );
						amountEuro.setEnabled( false );
						amountCent.setEnabled( false );
						quickPayPac.setEnabled( false );
						quickPayButton.setEnabled( false );
					}
				}

				@Override
				public void onNothingSelected( AdapterView< ? > parentView )
				{

				}

			} );
		}
		else
		{
			selected = s1.getSelectedItemPosition();
			this.quickPayWorking = false;
		}

		ArrayAdapter< String > spinnerArrayAdapter2 = new ArrayAdapter< String >( this , android.R.layout.simple_spinner_dropdown_item , WebHelper.getInstance().getPaymentFrom() );
		spinnerArrayAdapter2.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		s2.setAdapter( spinnerArrayAdapter2 );

		if ( selected == 0 )
		{
			s2.setEnabled( false );
		}
		else
		{
			s2.setEnabled( true );
		}

		selected = Integer.parseInt( WebHelper.getInstance().getPaymentFromSelected() );
		selected = ( selected == -1 ? 0 : selected + 1 );
		s2.setSelection( selected );

		s2.setOnItemSelectedListener( new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected( AdapterView< ? > parentView , View selectedItemView , int position , long id )
			{
				Spinner s3 = ( Spinner ) AccountActivity.this.findViewById( R.id.spinner3 );
				EditText amountEuro = ( EditText ) AccountActivity.this.findViewById( R.id.amountEuro );
				EditText amountCent = ( EditText ) AccountActivity.this.findViewById( R.id.amountCent );
				EditText quickPayPac = ( EditText ) AccountActivity.this.findViewById( R.id.quickPayPacDigit );
				Button quickPayButton = ( Button ) AccountActivity.this.findViewById( R.id.quickPayButton );

				if ( position > 0 )
				{
					s3.setEnabled( true );
				}
				else
				{
					s3.setEnabled( false );
					amountEuro.setEnabled( false );
					amountCent.setEnabled( false );
					quickPayPac.setEnabled( false );
					quickPayButton.setEnabled( false );
				}
			}

			@Override
			public void onNothingSelected( AdapterView< ? > parentView )
			{

			}

		} );

		ArrayAdapter< String > spinnerArrayAdapter3 = new ArrayAdapter< String >( this , android.R.layout.simple_spinner_dropdown_item , WebHelper.getInstance().getPaymentTo() );
		spinnerArrayAdapter3.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		s3.setAdapter( spinnerArrayAdapter3 );

		s3.setOnItemSelectedListener( new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected( AdapterView< ? > parentView , View selectedItemView , int position , long id )
			{
				EditText amountEuro = ( EditText ) AccountActivity.this.findViewById( R.id.amountEuro );
				EditText amountCent = ( EditText ) AccountActivity.this.findViewById( R.id.amountCent );
				EditText quickPayPac = ( EditText ) AccountActivity.this.findViewById( R.id.quickPayPacDigit );
				Button quickPayButton = ( Button ) AccountActivity.this.findViewById( R.id.quickPayButton );

				if ( position > 0 )
				{
					amountEuro.setEnabled( true );
					amountCent.setEnabled( true );
					quickPayPac.setEnabled( true );
					quickPayButton.setEnabled( true );
				}
				else
				{
					amountEuro.setEnabled( false );
					amountCent.setEnabled( false );
					quickPayPac.setEnabled( false );
					quickPayButton.setEnabled( false );
				}
			}

			@Override
			public void onNothingSelected( AdapterView< ? > parentView )
			{

			}

		} );

		if ( selected == 0 )
		{
			s3.setEnabled( false );
		}
		else
		{
			s3.setEnabled( true );
			amountEuro.setEnabled( true );
			amountCent.setEnabled( true );
			quickPayPac.setEnabled( true );
			quickPayButton.setEnabled( true );
		}

		selected = Integer.parseInt( WebHelper.getInstance().getPaymentToSelected() );
		selected = ( selected == -1 ? 0 : selected + 1 );
		s3.setSelection( selected );

		this.updateQuickPayPacDigit();

	}

	private void updateQuickPayPacDigit()
	{
		TextView pacView = ( TextView ) this.findViewById( R.id.quickPayPacview );
		pacView.setText( "PAC Digit " + WebHelper.getInstance().getQuickPayPacDigit() );
	}

	private void createCancelProgressDialog( String title , String message )
	{
		this.pd = new ProgressDialog( this );
		this.pd.setTitle( title );
		this.pd.setMessage( message );
		this.pd.setButton( "Cancel" , new DialogInterface.OnClickListener()
		{
			public void onClick( DialogInterface dialog , int which )
			{
				try
				{
					AccountActivity.this.interrupt = true;
					AccountActivity.this.thread.interrupt();
				}
				catch( Exception e )
				{

				}
				return;
			}
		} );
		this.pd.show();
	}

	@Override
	protected void onListItemClick( ListView l , View v , int position , long id )
	{
		this.runnableRequest = 1;
		StatementActivity.setAccountNumber( position );
		this.createCancelProgressDialog( "Requesting Statement" , "Please standby while we fetch your statement" );
		this.thread = new Thread( this );
		this.thread.start();
	}

	@Override
	public boolean onPrepareOptionsMenu( Menu menu )
	{
		MenuItem quickPayItem = menu.findItem( R.id.quickPay );
		MenuItem mobilePayItem = menu.findItem( R.id.mobileTopup );
		mobilePayItem.setEnabled( false );

		if ( this.quickPayVisible == true )
		{
			quickPayItem.setTitle( "Hide quick pay" );
		}
		else
		{
			quickPayItem.setTitle( "Show quick pay" );
		}

		return super.onPrepareOptionsMenu( menu );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.overviewmenu , menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		this.thread = new Thread( this );

		AccountActivity.this.interrupt = false;

		// Handle item selection
		switch ( item.getItemId() )
		{
			case R.id.managePayees:

				this.runnableRequest = 2;
				this.createCancelProgressDialog( "Fetching Payees" , "Please standby while we fetch a list of your payees" );
				this.thread.start();
				return true;

			case R.id.paymentLog:

				this.runnableRequest = 3;
				this.createCancelProgressDialog( "Fetching log" , "Please standby while we fetch your payment log" );
				this.thread.start();
				return true;

			case R.id.standingOrders:

				this.runnableRequest = 4;
				this.createCancelProgressDialog( "Fetching standing orders" , "Please standby while we fetch a list of your standing orders" );
				this.thread.start();
				return true;

			case R.id.logout:

				this.runnableRequest = 5;
				this.createCancelProgressDialog( "Closing AIB Session" , "Please standby while you are logged out of AIB" );
				this.thread.start();
				return true;

			case R.id.makePayment:

				this.runnableRequest = 6;
				this.createCancelProgressDialog( "Requesting payments options" , "Please standby while fetch available payment options" );
				this.thread.start();
				return true;

			case R.id.quickPay:

				LinearLayout quickPayControlBar = ( LinearLayout ) this.findViewById( R.id.quickPayControlBar );

				if ( this.quickPayVisible == true )
				{
					quickPayControlBar.setVisibility( LinearLayout.GONE );
					this.quickPayVisible = false;
				}
				else
				{
					quickPayControlBar.setVisibility( LinearLayout.VISIBLE );
					this.quickPayVisible = true;
				}

				return true;

			default:
				return super.onOptionsItemSelected( item );
		}
	}

	@Override
	public void onBackPressed()
	{
		new AlertDialog.Builder( this ).setIcon( android.R.drawable.ic_dialog_alert ).setTitle( "Exit" ).setMessage( "Are you sure you want to exit?" ).setPositiveButton( "yes" , new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog , int which )
			{
				AccountActivity.this.thread = new Thread( AccountActivity.this );
				AccountActivity.this.runnableRequest = 5;
				AccountActivity.this.createCancelProgressDialog( "Closing AIB Session" , "Please standby while you are logged out of AIB" );
				AccountActivity.this.thread.start();
			}

		} ).setNegativeButton( "no" , null ).show();

		return;
	}

	@Override
	public void timedOut()
	{
		try
		{
			this.finish();
		}
		catch( Exception e )
		{
		}
	}

	@Override
	public void countDownTitle( String title )
	{
		if ( title == null )
		{
			this.setTitle( this.title );
		}
		else
		{
			this.setTitle( title );
		}
	}

	@Override
	public void run()
	{
		if ( this.runnableRequest == 1 )
		{
			Person.getInstance().getAccounts().get( StatementActivity.getAccountNumber() ).getStatement();
			if ( this.interrupt == false )
				AccountActivity.this.statementHandler.sendEmptyMessage( 0 );
		}
		else if ( this.runnableRequest == 2 )
		{
			Person.getInstance().getPayees();
			if ( this.interrupt == false )
				AccountActivity.this.payeesHandler.sendEmptyMessage( 0 );
		}
		else if ( this.runnableRequest == 3 )
		{
			Person.getInstance().getPayments();
			if ( this.interrupt == false )
				AccountActivity.this.paymentLogHandler.sendEmptyMessage( 0 );
		}
		else if ( this.runnableRequest == 4 )
		{
			Person.getInstance().getStandingorders();
			if ( this.interrupt == false )
				AccountActivity.this.standingOrdersHandler.sendEmptyMessage( 0 );
		}
		else if ( this.runnableRequest == 5 )
		{
			WebHelper.getInstance().logout();
			if ( this.interrupt == false )
				AccountActivity.this.logoutHandler.sendEmptyMessage( 0 );
		}
		else if ( this.runnableRequest == 6 )
		{
			WebHelper.getInstance().goToTransferAndPayments();
			if ( this.interrupt == false )
				AccountActivity.this.paymentHandler.sendEmptyMessage( 0 );
		}
		else if ( this.runnableRequest == 10 )
		{
			WebHelper.getInstance().changeQuickPayOptions( AccountActivity.this.quickPayType );
			if ( this.interrupt == false )
				AccountActivity.this.changepayHandler.sendEmptyMessage( 0 );
		}
		else if ( this.runnableRequest == 11 )
		{
			EditText amountEuro = ( EditText ) AccountActivity.this.findViewById( R.id.amountEuro );
			String euro = amountEuro.getText().toString();

			EditText amountCent = ( EditText ) AccountActivity.this.findViewById( R.id.amountCent );
			String cent = amountCent.getText().toString();

			EditText quickPayPac = ( EditText ) AccountActivity.this.findViewById( R.id.quickPayPacDigit );
			String pac = quickPayPac.getText().toString();

			Spinner s1 = ( Spinner ) this.findViewById( R.id.spinner1 );
			String type = String.valueOf( s1.getSelectedItemPosition() );

			Spinner s2 = ( Spinner ) this.findViewById( R.id.spinner2 );
			String from = String.valueOf( s2.getSelectedItemPosition() - 1 );

			Spinner s3 = ( Spinner ) this.findViewById( R.id.spinner3 );
			String to = String.valueOf( s3.getSelectedItemPosition() - 1 );

			if ( WebHelper.getInstance().submitQuickPayOptions( from , to , euro , cent , pac , type ) == true )
			{
				if ( this.interrupt == false )
				{
					Message msg = new Message();
					msg.obj = "Transaction completed sucessfully";
					AccountActivity.this.quickpayHandler.sendMessage( msg );
				}
			}
			else
			{
				if ( this.interrupt == false )
				{
					Message msg = new Message();
					msg.obj = WebHelper.getInstance().getLastError();
					AccountActivity.this.quickpayHandler.sendMessage( msg );
				}
			}

		}

	}

	private Handler quickpayHandler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			AccountActivity.this.pd.dismiss();

			Spinner s1 = ( Spinner ) AccountActivity.this.findViewById( R.id.spinner1 );
			s1.setSelection( 0 );

			try
			{
				Toast.makeText( AccountActivity.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
			}
			catch( Exception e )
			{
				AccountActivity.this.prepareQuickPayFields();
			}
		}
	};

	private Handler changepayHandler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			AccountActivity.this.pd.dismiss();

			try
			{
				Toast.makeText( AccountActivity.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
			}
			catch( Exception e )
			{
				AccountActivity.this.prepareQuickPayFields();
			}
		}
	};

	private Handler paymentHandler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			AccountActivity.this.pd.dismiss();

			try
			{
				Toast.makeText( AccountActivity.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
			}
			catch( Exception e )
			{
				AccountActivity.this.updateQuickPayPacDigit();
				Intent myIntent = new Intent( AccountActivity.this , MakePaymentActivity.class );
				AccountActivity.this.startActivity( myIntent );
			}
		}
	};

	private Handler statementHandler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			AccountActivity.this.pd.dismiss();

			try
			{
				Toast.makeText( AccountActivity.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
			}
			catch( Exception e )
			{
				AccountActivity.this.updateQuickPayPacDigit();
				Intent myIntent = new Intent( AccountActivity.this , StatementActivity.class );
				AccountActivity.this.startActivity( myIntent );
			}
		}
	};

	private Handler payeesHandler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			AccountActivity.this.pd.dismiss();

			try
			{
				Toast.makeText( AccountActivity.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
			}
			catch( Exception e )
			{
				AccountActivity.this.updateQuickPayPacDigit();
				Intent myIntent = new Intent( AccountActivity.this , PayeesActivity.class );
				AccountActivity.this.startActivity( myIntent );
			}
		}
	};

	private Handler paymentLogHandler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			AccountActivity.this.pd.dismiss();

			try
			{
				Toast.makeText( AccountActivity.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
			}
			catch( Exception e )
			{
				AccountActivity.this.updateQuickPayPacDigit();
				Intent myIntent = new Intent( AccountActivity.this , PaymentlogActivity.class );
				AccountActivity.this.startActivity( myIntent );
			}
		}
	};

	private Handler standingOrdersHandler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			AccountActivity.this.pd.dismiss();

			try
			{
				Toast.makeText( AccountActivity.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
			}
			catch( Exception e )
			{
				AccountActivity.this.updateQuickPayPacDigit();
				Intent myIntent = new Intent( AccountActivity.this , StandingordersActivity.class );
				AccountActivity.this.startActivity( myIntent );
			}
		}
	};

	public void onClick( View v )
	{
		switch ( v.getId() )
		{
			case R.id.quickPayButton:

				this.runnableRequest = 11;
				this.pd = ProgressDialog.show( this , "Quick pay processing" , "Please standby while we complete your transaction" , true , false );
				Thread thread = new Thread( this );
				thread.start();

				break;
		}
	}

	private Handler logoutHandler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			AccountActivity.this.pd.dismiss();

			try
			{
				Toast.makeText( AccountActivity.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
			}
			catch( Exception e )
			{
				AccountActivity.this.finish();
			}
		}
	};

	private TextWatcher euroWatcher = new TextWatcher()
	{
		public void afterTextChanged( Editable s )
		{
		}

		public void beforeTextChanged( CharSequence s , int start , int count , int after )
		{
		}

		public void onTextChanged( CharSequence s , int start , int before , int count )
		{
			InputMethodManager imm = ( InputMethodManager ) AccountActivity.this.getSystemService( Service.INPUT_METHOD_SERVICE );

			EditText euroInput = ( EditText ) findViewById( R.id.amountEuro );
			EditText centInput = ( EditText ) findViewById( R.id.amountCent );

			if ( AccountActivity.this.containsOnlyNumerics( euroInput.getText().toString() ) == false )
			{
				euroInput.setText( AccountActivity.this.getOnlyNumerics( euroInput.getText().toString() ) );
			}

			int counter = euroInput.getText().toString().length();

			if ( counter > 4 )
			{
				imm.hideSoftInputFromWindow( euroInput.getWindowToken() , 0 );
				centInput.requestFocus();
				imm.hideSoftInputFromWindow( centInput.getWindowToken() , 1 );
			}
		}

	};

	private TextWatcher centWatcher = new TextWatcher()
	{
		public void afterTextChanged( Editable s )
		{
		}

		public void beforeTextChanged( CharSequence s , int start , int count , int after )
		{
		}

		public void onTextChanged( CharSequence s , int start , int before , int count )
		{
			InputMethodManager imm = ( InputMethodManager ) AccountActivity.this.getSystemService( Service.INPUT_METHOD_SERVICE );

			EditText centInput = ( EditText ) findViewById( R.id.amountCent );
			EditText pacInput = ( EditText ) findViewById( R.id.quickPayPacDigit );

			if ( AccountActivity.this.containsOnlyNumerics( centInput.getText().toString() ) == false )
			{
				centInput.setText( AccountActivity.this.getOnlyNumerics( centInput.getText().toString() ) );
			}

			int counter = centInput.getText().toString().length();

			if ( counter == 2 )
			{
				imm.hideSoftInputFromWindow( centInput.getWindowToken() , 0 );
				centInput.requestFocus();
				imm.hideSoftInputFromWindow( pacInput.getWindowToken() , 1 );
			}
			else if ( counter > 2 )
			{
				centInput.setText( centInput.getText().toString().substring( 0 , 1 ) );
			}
		}

	};

	private TextWatcher pacWatcher1 = new TextWatcher()
	{
		public void afterTextChanged( Editable s )
		{
		}

		public void beforeTextChanged( CharSequence s , int start , int count , int after )
		{
		}

		public void onTextChanged( CharSequence s , int start , int before , int count )
		{
			InputMethodManager imm = ( InputMethodManager ) AccountActivity.this.getSystemService( Service.INPUT_METHOD_SERVICE );

			EditText pac1Input = ( EditText ) findViewById( R.id.quickPayPacDigit );
			Button quickPayButton = ( Button ) findViewById( R.id.quickPayButton );

			if ( AccountActivity.this.containsOnlyNumerics( pac1Input.getText().toString() ) == false )
			{
				pac1Input.setText( AccountActivity.this.getOnlyNumerics( pac1Input.getText().toString() ) );
			}

			int counter = pac1Input.getText().toString().length();

			if ( counter == 1 )
			{
				imm.hideSoftInputFromWindow( pac1Input.getWindowToken() , 0 );
				quickPayButton.requestFocus();
			}
			else if ( counter > 1 )
			{
				pac1Input.setText( pac1Input.getText().toString().substring( 0 , 1 ) );
			}
		}

	};

	public String getOnlyNumerics( String str )
	{
		if ( str == null )
		{
			return null;
		}

		StringBuffer strBuff = new StringBuffer();
		char c;

		for ( int i = 0; i < str.length(); i++ )
		{
			c = str.charAt( i );

			if ( Character.isDigit( c ) )
			{
				strBuff.append( c );
			}
		}

		return strBuff.toString();
	}

	public boolean containsOnlyNumerics( String str )
	{
		if ( str == null )
		{
			return false;
		}

		char c;

		for ( int i = 0; i < str.length(); i++ )
		{
			c = str.charAt( i );

			if ( !Character.isDigit( c ) )
			{
				return false;
			}
		}

		return true;
	}

}
