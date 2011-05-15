package ie.aib;

import ie.aib.entities.Account;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatementAdapter extends BaseAdapter
{
	private static ArrayList< Integer > selected;
	private Account account;
	private Context context;

	public StatementAdapter( Account a , Context context )
	{
		this.account = a;
		this.context = context;
		StatementAdapter.selected = new ArrayList< Integer >();
	}

	public int getCount()
	{
		return this.account.getStatement().size();
	}

	public String[] getItem( int position )
	{
		return this.account.getStatement().get( position );
	}

	public long getItemId( int position )
	{
		return position;
	}

	public View getView( int position , View convertView , ViewGroup parent )
	{
		LinearLayout itemLayout;
		LinearLayout p;
		String[] statement = this.account.getStatement().get( position );

		itemLayout = (LinearLayout) LayoutInflater.from( context ).inflate( R.layout.statementlist , parent , false );
		p = (LinearLayout) itemLayout.findViewById( R.id.statementLayout );

		TextView p1 = (TextView) itemLayout.findViewById( R.id.transactionName1 );
		p1.setText( statement[0] );

		TextView p2 = (TextView) itemLayout.findViewById( R.id.transactionName2 );
		p2.setText( statement[1] );

		TextView p3 = (TextView) itemLayout.findViewById( R.id.transactionName3 );
		p3.setText( "€" + statement[2] );

		if ( statement[2].length() < 2 )
		{
			LinearLayout row = (LinearLayout) itemLayout.findViewById( R.id.debitRow );
			p.removeView( row );
		}
		else
		{
			LinearLayout row = (LinearLayout) itemLayout.findViewById( R.id.statementLayout );
			row.setBackgroundColor( Color.argb( 140 , 140 , 0 , 0 ) );
		}

		TextView p4 = (TextView) itemLayout.findViewById( R.id.transactionName4 );
		p4.setText( "€" + statement[3] );

		if ( statement[3].length() < 2 )
		{
			LinearLayout row = (LinearLayout) itemLayout.findViewById( R.id.creditRow );
			p.removeView( row );
		}
		else
		{
			LinearLayout row = (LinearLayout) itemLayout.findViewById( R.id.statementLayout );
			row.setBackgroundColor( Color.argb( 140 , 0 , 140 , 0 ) );
		}
		
		
		TextView p5 = (TextView) itemLayout.findViewById( R.id.transactionName5 );
		p5.setText( "€" + statement[4] );
	
		if ( statement[4].length() < 2 )
		{
			LinearLayout row = (LinearLayout) itemLayout.findViewById( R.id.balanceRow );
			p.removeView( row );
		}
		
		if( StatementAdapter.selected.contains( position ) )
		{
			LinearLayout row = (LinearLayout) itemLayout.findViewById( R.id.statementLayout );
			row.setBackgroundColor( Color.argb( 140 , 0 , 0 , 140 ) );
		}
		
		return itemLayout;
	}
	
	
	public static void changeSelected( int pos )
	{
		if( StatementAdapter.selected.contains( pos ) )
		{
			StatementAdapter.selected.remove( (Integer) pos );
		}
		else
		{
			StatementAdapter.selected.add( (Integer) pos );
		}
	}
	
	
	public static boolean isSelected( int pos )
	{
		if( StatementAdapter.selected.contains( pos ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
