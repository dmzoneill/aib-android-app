package ie.aib;

import ie.aib.entities.Person;
import ie.aib.entities.StandingOrder;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StandingordersAdapter extends BaseAdapter
{
	private static int selected;
	private Person person;
	private Context context;

	public StandingordersAdapter( Person p , Context context )
	{
		StandingordersAdapter.selected = -1;
		this.person = p;
		this.context = context;
	}

	public int getCount()
	{
		return this.person.getStandingOrdersCount();
	}

	public StandingOrder getItem( int position )
	{
		return this.person.getStandingorders().get( position );
	}

	public long getItemId( int position )
	{
		return position;
	}

	public View getView( int position , View convertView , ViewGroup parent )
	{
		LinearLayout itemLayout;

		StandingOrder logEntry = this.person.getStandingorders().get( position );

		itemLayout = (LinearLayout) LayoutInflater.from( context ).inflate( R.layout.standingorderslist , parent , false );

		TextView frequency = (TextView) itemLayout.findViewById( R.id.standingordersFrequency );
		frequency.setText( logEntry.getFrequency() );

		TextView fromAccount = (TextView) itemLayout.findViewById( R.id.standingordersFromAccount );
		fromAccount.setText( logEntry.getFromAccount() );

		TextView receiver = (TextView) itemLayout.findViewById( R.id.standingordersReceiver );
		receiver.setText( logEntry.getReceiver() );

		TextView nextAmount = (TextView) itemLayout.findViewById( R.id.standingordersNextAmount );
		nextAmount.setText( logEntry.getNextAmount() );

		TextView nextDate = (TextView) itemLayout.findViewById( R.id.standingordersNextDate );
		nextDate.setText( logEntry.getNextDate() );

		LinearLayout optrow = (LinearLayout) itemLayout.findViewById( R.id.standingOrderRowOptions );
		optrow.setVisibility( LinearLayout.GONE );
		
		itemLayout.setBackgroundColor( Color.argb( 0 , 0 , 0 , 0 ) );
		
		if( StandingordersAdapter.selected == position )
		{
			itemLayout.setBackgroundColor( Color.argb( 140 , 0 , 140 , 0 ) );
			optrow.setVisibility( LinearLayout.VISIBLE );	
		}

		return itemLayout;
	}
		
	public static void setSelected( int pos )
	{
		StandingordersAdapter.selected = pos;		
	}
	
	
	public static int getSelected()
	{
		return StandingordersAdapter.selected;
	}
}
