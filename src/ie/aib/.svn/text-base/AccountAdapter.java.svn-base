package ie.aib;

import ie.aib.entities.Account;
import ie.aib.entities.Person;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter
{

	private Person person;

	private Context context;

	public AccountAdapter( Person p , Context context )
	{
		this.person = p;
		this.context = context;
	}

	public int getCount()
	{
		return this.person.getAccountCount();
	}

	public Account getItem( int position )
	{
		return this.person.getAccounts().get( position );
	}

	public long getItemId( int position )
	{
		return position;
	}

	public View getView( int position , View convertView , ViewGroup parent )
	{
		LinearLayout itemLayout;
		Account acc = this.person.getAccounts().get( position );

		itemLayout = (LinearLayout) LayoutInflater.from( context ).inflate( R.layout.accountlist , parent , false );

		TextView tvUser = (TextView) itemLayout.findViewById( R.id.accountName );
		tvUser.setText( acc.getAccount() );

		TextView tvText = (TextView) itemLayout.findViewById( R.id.accountBalance );
		tvText.setText( acc.getBalance() );

		return itemLayout;
	}
}
