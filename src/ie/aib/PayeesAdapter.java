package ie.aib;

import ie.aib.entities.Payee;
import ie.aib.entities.Person;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PayeesAdapter extends BaseAdapter
{
	private static int selected;
	private Person person;
	private Context context;

	public PayeesAdapter( Person p , Context context )
	{
		PayeesAdapter.selected = -1;
		this.person = p;
		this.context = context;
	}

	public int getCount()
	{
		return this.person.getPayeeCount();
	}

	public Payee getItem( int position )
	{
		return this.person.getPayees().get( position );
	}

	public long getItemId( int position )
	{
		return position;
	}

	public View getView( int position , View convertView , ViewGroup parent )
	{
		LinearLayout itemLayout;
		LinearLayout p;
		
		Payee acc = this.person.getPayees().get( position );

		itemLayout = (LinearLayout) LayoutInflater.from( context ).inflate( R.layout.payeeslist , parent , false );
		p = (LinearLayout) itemLayout.findViewById( R.id.payeesLayout );

		TextView payeeName = (TextView) itemLayout.findViewById( R.id.payeeName );
		payeeName.setText( acc.getName() );

		TextView payeeType = (TextView) itemLayout.findViewById( R.id.payeeType );
		payeeType.setText( acc.getPaymentType() );
		
		TextView payeeRef = (TextView) itemLayout.findViewById( R.id.payeeReference );
		payeeRef.setText( acc.getReferenceNumber() );
		
		TextView payeeNSC = (TextView) itemLayout.findViewById( R.id.payeeNSC );
		payeeNSC.setText( acc.getNSC() );
		
		TextView payeeAcc = (TextView) itemLayout.findViewById( R.id.payeeAccount );
		payeeAcc.setText( acc.getAccountNumber() );
		
		LinearLayout accrow1 = (LinearLayout) itemLayout.findViewById( R.id.payeeRowAccount1 );
		LinearLayout accrow2 = (LinearLayout) itemLayout.findViewById( R.id.payeeRowAccount2 );
		LinearLayout refrow = (LinearLayout) itemLayout.findViewById( R.id.payeeRowReference );
		LinearLayout optrow = (LinearLayout) itemLayout.findViewById( R.id.payeeRowOptions );
		
		p.setBackgroundColor( Color.argb( 0 , 0 , 0 , 0 ) );
		
		if( acc.getNSC().compareToIgnoreCase( "n/a" ) == 0 || acc.getAccountNumber().compareToIgnoreCase( "n/a" ) == 0 )
		{			
			p.removeView( accrow1 );
			p.removeView( accrow2 );
		}
		else
		{
			p.removeView( refrow );
		}
		
		optrow.setVisibility( LinearLayout.GONE );
		
		if( PayeesAdapter.selected == position )
		{
			p.setBackgroundColor( Color.argb( 140 , 0 , 140 , 0 ) );
			optrow.setVisibility( LinearLayout.VISIBLE );	
		}
		
		return itemLayout;
	}
	
	
	public static void setSelected( int pos )
	{
		PayeesAdapter.selected = pos;
		
	}
	
	
	public static int getSelected()
	{
		return PayeesAdapter.selected;
	}
}
