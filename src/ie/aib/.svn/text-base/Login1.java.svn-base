package ie.aib;

import ie.aib.web.WebHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login1 extends Activity implements OnClickListener, Runnable, onTimedOut
{
	private static Login1 instance;
	private ProgressDialog pd;
	private String title;

	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{	
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.login1 );
		
		this.title = "Step 1 of 2";		
		this.setTitle( this.title );
		
		Login1.instance = this;
		Timeout.getInstance().attach( this );	
		Timeout.getInstance().autoLogoutTimer();

		
		EditText regInput = (EditText) findViewById( R.id.registrationNumber );

		regInput.addTextChangedListener( new TextWatcher()
		{

			public void afterTextChanged( Editable s )
			{}

			public void beforeTextChanged( CharSequence s , int start , int count , int after )
			{}

			public void onTextChanged( CharSequence s , int start , int before , int count )
			{
				InputMethodManager imm = (InputMethodManager) Login1.this.getSystemService( Service.INPUT_METHOD_SERVICE );
				
				EditText regInput = (EditText) findViewById( R.id.registrationNumber );
				
				if( Login1.this.containsOnlyNumerics( regInput.getText().toString() ) == false )
				{
					regInput.setText( Login1.this.getOnlyNumerics( regInput.getText().toString() ) );
				}
			
				int counter = regInput.getText().toString().length();
				
				if ( counter == 8 )
				{
					Button myBtn = (Button) findViewById( R.id.loginButton );
					myBtn.requestFocus();
					imm.hideSoftInputFromWindow( regInput.getWindowToken() , 0 );
				}
				else if ( counter > 8 )
				{
					regInput.setText( regInput.getText().toString().substring( 0 , 8 ) );
				}			
				
			}

		} );
		
	}

	
	public String getOnlyNumerics( String str )
	{
		if ( str == null )
		{
			return null;
		}

		StringBuffer strBuff = new StringBuffer();
		char c;

		for ( int i = 0; i < str.length(); i++ )
		{
			c = str.charAt( i );

			if ( Character.isDigit( c ) )
			{
				strBuff.append( c );
			}
		}
		
		return strBuff.toString();
	}
	
	
	public boolean containsOnlyNumerics( String str )
	{
		if ( str == null )
		{
			return false;
		}

		char c;

		for ( int i = 0; i < str.length(); i++ )
		{
			c = str.charAt( i );

			if ( !Character.isDigit( c ) )
			{
				return false;
			}
		}
		
		return true;
	}

	
	public void onClick( View v )
	{
		switch ( v.getId() )
		{
			case R.id.loginButton:
				
				EditText regInput = (EditText) findViewById( R.id.registrationNumber );
				int counter = regInput.getText().toString().length();
				
				if( counter == 8 )
				{
					this.pd = ProgressDialog.show( this , "Step 1 of 2" , "Please standby while we start your banking session" , true , false );
					Thread thread = new Thread( this );
					thread.start();
				}
				else
				{
					Toast.makeText( Login1.this.getApplicationContext() , "Please enter your eight digit registration number" , 5000 ).show();
				}

			break;
		}
	}
	

	@Override
	public void run()
	{
		WebHelper httpHelper = WebHelper.getInstance();
		httpHelper.initHTTPClient();
		EditText regText = (EditText) this.findViewById( R.id.registrationNumber );
		if ( httpHelper.login1( regText.getText().toString() ) == true )
		{
			Login1.this.handler.sendEmptyMessage( 0 );
			Intent myIntent = new Intent( Login1.this , Login2.class );
			Login1.this.startActivity( myIntent );
		}
		else
		{
			Message msg = new Message();
			msg.obj = httpHelper.getLastError();
			Login1.this.handler.sendMessage( msg );
		}
	}
	

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			Login1.this.pd.dismiss();
			Login1.this.finish();
			try
			{
				Toast.makeText( Login1.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
			}
			catch( Exception e )
			{

			}
		}
	};

	
	@Override
	public void onClick( DialogInterface dialog , int which )
	{
	}
	

	public static Context getContext()
	{
		return Login1.instance.getApplicationContext();
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

}
