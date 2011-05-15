package ie.aib;

import ie.aib.entities.Person;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MakePaymentActivity extends Activity implements Runnable, onTimedOut
{
	private ProgressDialog pd;
	private String title;
	private int runnableRequest;
	private Thread thread;
	private boolean interrupt;
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		
		this.title = "Select payment option";
		this.setTitle( this.title );

		Timeout.getInstance().attach( this );
		Timeout.getInstance().autoLogoutTimer();
		
		this.setContentView( R.layout.makepaymentmain );

		ListView list = ( ListView ) this.findViewById( R.id.paymenttypes );

		ArrayList< HashMap< String , String >> mylist = new ArrayList< HashMap< String , String >>();
		
		HashMap< String , String > map = new HashMap< String , String >();
		map.put( "header" , "I want to transfer money between my own AIB accounts" );
		map.put( "description" , "Transfer money between the accounts on your Internet Banking" );
		mylist.add( map );
		
		map = new HashMap< String , String >();
		map.put( "header" , "I want to pay a bill" );
		map.put( "description" , "Pay a range of bills including credit cards, gas, electricity, mobile and landline phones" );
		mylist.add( map );
		
		map = new HashMap< String , String >();
		map.put( "header" , "I want to transfer money to another ROI account" );
		map.put( "description" , "Transfer money in euro to another account within the Republic of Ireland" );
		mylist.add( map );
		
		map = new HashMap< String , String >();
		map.put( "header" , "I want to transfer money internationally" );
		map.put( "description" , "Transfer money to an international destination or in a foreign currency" );
		mylist.add( map );
		
		map = new HashMap< String , String >();
		map.put( "header" , "I want to set up a standing order" );
		map.put( "description" , "Set up a regular payment to any account within the Republic of Ireland" );
		mylist.add( map );
		
		
		SimpleAdapter mSchedule = new SimpleAdapter( this , mylist , R.layout.makepaymentlist , new String[] { "header" , "description" } , new int[] { R.id.makePaymentHeader , R.id.makePaymentDescription } );
		list.setAdapter( mSchedule );
	}
	
	@Override
	public void run()
	{
		if ( this.runnableRequest == 1 )
		{
			this.createCancelProgressDialog( "Pay a bill" , "Please standby while we fetch your, 'pay a bill' options" );
			Person.getInstance().getAccounts().get( StatementActivity.getAccountNumber() ).getStatement();
			if( this.interrupt == false )				
				MakePaymentActivity.this.payABillhandler.sendEmptyMessage( 0 );
		}		
	}
	
	
	private Handler payABillhandler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			MakePaymentActivity.this.pd.dismiss();

			try
			{
				Toast.makeText( MakePaymentActivity.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
			}
			catch( Exception e )
			{
				Intent myIntent = new Intent( MakePaymentActivity.this , StatementActivity.class );
				MakePaymentActivity.this.startActivity( myIntent );
			}
		}
	};
	
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
					MakePaymentActivity.this.interrupt = true;
					MakePaymentActivity.this.thread.interrupt();					
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
	public boolean onPrepareOptionsMenu( Menu menu )
	{
		MenuItem paymentOption = menu.findItem( R.id.paymentOption );
		paymentOption.setEnabled( false );

		return super.onPrepareOptionsMenu( menu );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.paymentoptionsmenu , menu );
		return true;
	}
	
}
