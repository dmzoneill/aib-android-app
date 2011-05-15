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

public class StatementActivity extends ListActivity implements onTimedOut
{
	private static int accountNumber;
	private StatementAdapter adapter;
	private String title;

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.statementview );

		this.title = Person.getInstance().getAccounts().get( StatementActivity.accountNumber ).getAccount();
		this.setTitle( this.title );

		Timeout.getInstance().attach( this );
		Timeout.getInstance().autoLogoutTimer();

		this.getListView();

		this.adapter = new StatementAdapter( Person.getInstance().getAccounts().get( StatementActivity.accountNumber ) , this );
		this.setListAdapter( this.adapter );
	}

	@Override
	protected void onListItemClick( ListView l , View v , int position , long id )
	{
		LinearLayout row = (LinearLayout) v;

		if ( StatementAdapter.isSelected( position ) )
		{

			LinearLayout creditRow = (LinearLayout) row.findViewById( R.id.creditRow );
			if ( creditRow != null )
			{
				row.setBackgroundColor( Color.argb( 140 , 0 , 140 , 0 ) );
			}

			LinearLayout debitRow = (LinearLayout) row.findViewById( R.id.debitRow );
			if ( debitRow != null )
			{
				row.setBackgroundColor( Color.argb( 140 , 140 , 0 , 0 ) );
			}

			if ( debitRow == null && creditRow == null )
			{
				row.setBackgroundColor( Color.argb( 255 , 0 , 0 , 0 ) );
			}

		}
		else
		{
			row.setBackgroundColor( Color.argb( 140 , 0 , 0 , 140 ) );
		}

		StatementAdapter.changeSelected( position );
	}
	
	
	@Override
	public boolean onPrepareOptionsMenu( Menu menu )
	{
		MenuItem statementSave = menu.findItem( R.id.statementSave );
		MenuItem statementSend = menu.findItem( R.id.statementSend );
		MenuItem statementRefresh = menu.findItem( R.id.statementRefresh );
		MenuItem statementHideDebits = menu.findItem( R.id.statementHideDebits );
		MenuItem statementHideDeposits = menu.findItem( R.id.statementHideDeposits );
		MenuItem statementHideOther = menu.findItem( R.id.statementHideOther );
		
		statementSave.setEnabled( false );
		statementSend.setEnabled( false );
		statementRefresh.setEnabled( false );
		statementHideDebits.setEnabled( false );
		statementHideDeposits.setEnabled( false );
		statementHideOther.setEnabled( false );

		return super.onPrepareOptionsMenu( menu );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.statementmenu , menu );
		return true;
	}

	public static int getAccountNumber()
	{
		return StatementActivity.accountNumber;
	}

	public static void setAccountNumber( int accountNumber )
	{
		StatementActivity.accountNumber = accountNumber;
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

}
