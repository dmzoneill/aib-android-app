package ie.aib;

import ie.aib.entities.Person;
import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PaymentlogActivity extends ListActivity implements onTimedOut
{
	private PaymentlogAdapter adapter;
	private String title;
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.paymentlogview );
		
		this.title = "Payment log";		
		this.setTitle( this.title );
		
		Timeout.getInstance().attach( this );	
		Timeout.getInstance().autoLogoutTimer();

		this.getListView();
		
		this.adapter = new PaymentlogAdapter( Person.getInstance() , this );	
		this.setListAdapter( this.adapter );	
	}

	@Override
	protected void onListItemClick( ListView l , View v , int position , long id )
	{				
		LinearLayout row = (LinearLayout) v;
		
		if ( PaymentlogAdapter.isSelected( position ) )
		{
			LinearLayout rowDetails1 = (LinearLayout) row.findViewById( R.id.paymentDetails1 );
			rowDetails1.setVisibility(  LinearLayout.GONE );
			
			LinearLayout rowDetails2 = (LinearLayout) row.findViewById( R.id.paymentDetails2 );
			rowDetails2.setVisibility(  LinearLayout.GONE );
			
			LinearLayout rowDetails3 = (LinearLayout) row.findViewById( R.id.paymentDetails3 );
			rowDetails3.setVisibility(  LinearLayout.GONE );	
			
			row.setBackgroundColor( Color.argb( 0 , 0 , 0 , 0 ) );	

		}
		else
		{
			row.setBackgroundColor( Color.argb( 140 , 0 , 140 , 0 ) );	
			
			LinearLayout rowDetails1 = (LinearLayout) row.findViewById( R.id.paymentDetails1 );
			rowDetails1.setVisibility(  LinearLayout.VISIBLE );
			
			LinearLayout rowDetails2 = (LinearLayout) row.findViewById( R.id.paymentDetails2 );
			rowDetails2.setVisibility(  LinearLayout.VISIBLE );
			
			LinearLayout rowDetails3 = (LinearLayout) row.findViewById( R.id.paymentDetails3 );
			rowDetails3.setVisibility(  LinearLayout.VISIBLE );	
			
			TextView duedate = ( TextView ) rowDetails3.findViewById( R.id.paymentDuedate );
			
			if( duedate.getText().toString() == "" )
			{
				rowDetails3.setVisibility(  LinearLayout.GONE );
			}
			else
			{
				rowDetails3.setVisibility(  LinearLayout.VISIBLE );
			}
		}			
			
		PaymentlogAdapter.changeSelected( position );
		
	}
			

	@Override
	public void timedOut()
	{
		try
		{
			this.finish();
		}
		catch( Exception e ){}
	}

	
	@Override
	public void countDownTitle( String title )
	{
		if( title == null )
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
		MenuItem viewInternationalPayments = menu.findItem( R.id.viewInternationalPayments );
		
		viewInternationalPayments.setEnabled( false );

		return super.onPrepareOptionsMenu( menu );
	}
	
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.paymentslogmenu , menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		return super.onOptionsItemSelected( item );
		
	}
	
	
}
