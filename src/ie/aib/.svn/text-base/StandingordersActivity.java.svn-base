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

public class StandingordersActivity extends ListActivity implements onTimedOut
{
	private StandingordersAdapter adapter;
	private String title;

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.paymentlogview );

		this.title = "Active standing orders";
		this.setTitle( this.title );

		Timeout.getInstance().attach( this );
		Timeout.getInstance().autoLogoutTimer();

		this.getListView();

		this.adapter = new StandingordersAdapter( Person.getInstance() , this );
		this.setListAdapter( this.adapter );

	}

	@Override
	protected void onListItemClick( ListView l , View v , int position , long id )
	{
		try
		{
			if( StandingordersAdapter.getSelected() > -1 )
			{
				View lv = (View) l.getChildAt( StandingordersAdapter.getSelected() ); 			        
				LinearLayout row = (LinearLayout) lv;			
				row.setBackgroundColor( Color.argb( 0 , 0 , 0 , 0 ) );
				
				LinearLayout optrow = (LinearLayout) row.findViewById( R.id.standingOrderRowOptions );
				optrow.setVisibility( LinearLayout.GONE );
			}
	
			LinearLayout row = (LinearLayout) v;
			
			LinearLayout optrow = (LinearLayout) row.findViewById( R.id.standingOrderRowOptions );
			optrow.setVisibility( LinearLayout.VISIBLE );	
			
			row.setBackgroundColor( Color.argb( 140 , 0 , 140 , 0 ) );
			
			StandingordersAdapter.setSelected( position );
		}
		catch( Exception p )
		{
			StandingordersAdapter.setSelected( -1 );
		}		
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
		MenuItem addNewStandingOrder = menu.findItem( R.id.addNewStandingOrder );
		
		addNewStandingOrder.setEnabled( false );

		return super.onPrepareOptionsMenu( menu );
	}
	

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.standingordermenu , menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		return super.onOptionsItemSelected( item );
		
	}

}
