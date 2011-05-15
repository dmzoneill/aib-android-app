package ie.aib;

import java.util.ArrayList;

import ie.aib.entities.Person;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaymentlogAdapter extends BaseAdapter
{

	private Person person;
	private Context context;
	private static ArrayList<Integer> selected;

	public PaymentlogAdapter( Person p , Context context )
	{
		this.person = p;
		this.context = context;
		PaymentlogAdapter.selected = new ArrayList<Integer>();
	}

	public int getCount()
	{
		return this.person.getPaymentsCount();
	}

	public String[] getItem( int position )
	{
		return this.person.getPayments().get( position );
	}

	public long getItemId( int position )
	{
		return position;
	}

	public View getView( int position , View convertView , ViewGroup parent )
	{
		LinearLayout itemLayout;

		String[] logEntry = this.person.getPayments().get( position );

		itemLayout = (LinearLayout) LayoutInflater.from( context ).inflate( R.layout.paymentloglist , parent , false );

		TextView paymentDate = (TextView) itemLayout.findViewById( R.id.paymentDate );
		paymentDate.setText( logEntry[0] );

		TextView paymentType = (TextView) itemLayout.findViewById( R.id.paymentType );
		paymentType.setText( logEntry[1] );

		TextView paymentFrom = (TextView) itemLayout.findViewById( R.id.paymentFrom );
		paymentFrom.setText( logEntry[2] );

		TextView paymentReceiver = (TextView) itemLayout.findViewById( R.id.paymentReceiver );
		paymentReceiver.setText( logEntry[3] );

		TextView paymentDuedate = (TextView) itemLayout.findViewById( R.id.paymentDuedate );
		paymentDuedate.setText( logEntry[4] );

		TextView paymentAmount = (TextView) itemLayout.findViewById( R.id.paymentAmount );
		paymentAmount.setText( logEntry[5] );

		if ( PaymentlogAdapter.selected.contains( position ) )
		{
			itemLayout.setBackgroundColor( Color.argb( 140 , 0 , 140 , 0 ) );

			LinearLayout rowDetails1 = (LinearLayout) itemLayout.findViewById( R.id.paymentDetails1 );
			rowDetails1.setVisibility( LinearLayout.VISIBLE );

			LinearLayout rowDetails2 = (LinearLayout) itemLayout.findViewById( R.id.paymentDetails2 );
			rowDetails2.setVisibility( LinearLayout.VISIBLE );

			LinearLayout rowDetails3 = (LinearLayout) itemLayout.findViewById( R.id.paymentDetails3 );
			rowDetails3.setVisibility( LinearLayout.VISIBLE );

			TextView duedate = (TextView) rowDetails3.findViewById( R.id.paymentDuedate );

			if ( duedate.getText().toString() == "" )
			{
				rowDetails3.setVisibility( LinearLayout.GONE );
			}
			else
			{
				rowDetails3.setVisibility( LinearLayout.VISIBLE );
			}
		}
		else
		{
			LinearLayout rowDetails1 = (LinearLayout) itemLayout.findViewById( R.id.paymentDetails1 );
			rowDetails1.setVisibility( LinearLayout.GONE );

			LinearLayout rowDetails2 = (LinearLayout) itemLayout.findViewById( R.id.paymentDetails2 );
			rowDetails2.setVisibility( LinearLayout.GONE );

			LinearLayout rowDetails3 = (LinearLayout) itemLayout.findViewById( R.id.paymentDetails3 );
			rowDetails3.setVisibility( LinearLayout.GONE );
		}

		return itemLayout;
	}

	public static void changeSelected( int pos )
	{
		if ( PaymentlogAdapter.selected.contains( pos ) )
		{
			PaymentlogAdapter.selected.remove( (Integer) pos );
		}
		else
		{
			PaymentlogAdapter.selected.add( (Integer) pos );
		}
	}

	public static boolean isSelected( int pos )
	{
		if ( PaymentlogAdapter.selected.contains( pos ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
