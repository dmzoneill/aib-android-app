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

public class PayeesActivity extends ListActivity implements onTimedOut
{
	private PayeesAdapter adapter;
	private String title;
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.payeesview );
		
		this.title = "Manage My Payees";		
		this.setTitle( this.title );
		
		Timeout.getInstance().attach( this );	
		Timeout.getInstance().autoLogoutTimer();

		this.getListView();
		
		this.adapter = new PayeesAdapter( Person.getInstance() , this );	
		this.setListAdapter( this.adapter );
	}

	@Override
	protected void onListItemClick( ListView l , View v , int position , long id )
	{				
		try
		{
			if( PayeesAdapter.getSelected() > -1 )
			{
				View lv = (View) l.getChildAt( PayeesAdapter.getSelected() ); 			        
				LinearLayout row = (LinearLayout) lv;			
				row.setBackgroundColor( Color.argb( 0 , 0 , 0 , 0 ) );
				
				LinearLayout optrow = (LinearLayout) row.findViewById( R.id.payeeRowOptions );
				optrow.setVisibility( LinearLayout.GONE );
			}
	
			LinearLayout row = (LinearLayout) v;
			
			LinearLayout optrow = (LinearLayout) row.findViewById( R.id.payeeRowOptions );
			optrow.setVisibility( LinearLayout.VISIBLE );	
			
			row.setBackgroundColor( Color.argb( 140 , 0 , 140 , 0 ) );
			
			PayeesAdapter.setSelected( position );
		}
		catch( Exception p )
		{
			PayeesAdapter.setSelected( -1 );
		}
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
		MenuItem addPayeeItem1 = menu.findItem( R.id.addnewpayeeoption );
		MenuItem addPayeeItem2 = menu.findItem( R.id.addnewbilloption );
		MenuItem addPayeeItem3 = menu.findItem( R.id.addnewcreditcardoption );
		
		addPayeeItem1.setEnabled( false );
		addPayeeItem2.setEnabled( false );
		addPayeeItem3.setEnabled( false );

		return super.onPrepareOptionsMenu( menu );
	}
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.payeemenu , menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		return super.onOptionsItemSelected( item );
		
	}
	
}
