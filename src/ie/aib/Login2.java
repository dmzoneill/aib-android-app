package ie.aib;

import ie.aib.entities.Person;
import ie.aib.web.WebHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
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
import android.widget.TextView;
import android.widget.Toast;

public class Login2 extends Activity implements OnClickListener, Runnable, onTimedOut
{
	private ProgressDialog pd;
	private String title;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.login2 );
		
		this.title = "Step 2 of 2";		
		this.setTitle( this.title );
		
		Timeout.getInstance().attach( this );	
		Timeout.getInstance().autoLogoutTimer();

		WebHelper httpHelper = WebHelper.getInstance();

		TextView pac1Chal = (TextView) this.findViewById( R.id.dpac1 );
		TextView pac2Chal = (TextView) this.findViewById( R.id.dpac2 );
		TextView pac3Chal = (TextView) this.findViewById( R.id.dpac3 );
		TextView challengePhrase = (TextView) this.findViewById( R.id.challengePhrase );

		pac1Chal.setText( String.valueOf( httpHelper.getPac1() ) );
		pac2Chal.setText( String.valueOf( httpHelper.getPac2() ) );
		pac3Chal.setText( String.valueOf( httpHelper.getPac3() ) );

		if ( httpHelper.getChallenge() == 1 )
		{
			challengePhrase.setText( "Please enter the last four digits from your home phone number" );
		}
		else if ( httpHelper.getChallenge() == 3 )
		{
			challengePhrase.setText( "Please enter the last four digits from your work phone number" );
		}
		else if ( httpHelper.getChallenge() == 4 )
		{
			challengePhrase.setText( "Please enter the last four digits from your mobile phone number" );
		}
		else
		{
			challengePhrase.setText( "Please enter the last four digits from your primary AIB Visa Card" );
		}
		
		EditText pac1Input1 = (EditText) findViewById( R.id.pac1 );
		pac1Input1.addTextChangedListener( this.pacWatcher1 );
		
		EditText pac1Input2 = (EditText) findViewById( R.id.pac2 );
		pac1Input2.addTextChangedListener( this.pacWatcher2 );
		
		EditText pac1Input3 = (EditText) findViewById( R.id.pac3 );
		pac1Input3.addTextChangedListener( this.pacWatcher3 );
		
		EditText chalInput = (EditText) findViewById( R.id.challengeNumber );
		chalInput.addTextChangedListener( this.chalWatcher );
		
	}
	
	
	private TextWatcher pacWatcher1 = new TextWatcher()
	{
		public void afterTextChanged( Editable s )
		{}

		public void beforeTextChanged( CharSequence s , int start , int count , int after )
		{}

		public void onTextChanged( CharSequence s , int start , int before , int count )
		{
			InputMethodManager imm = (InputMethodManager) Login2.this.getSystemService( Service.INPUT_METHOD_SERVICE );

			EditText pac1Input = ( EditText ) findViewById( R.id.pac1 );
			EditText pac2Input = ( EditText ) findViewById( R.id.pac2 );
			
			if( Login2.this.containsOnlyNumerics( pac1Input.getText().toString() ) == false )
			{
				pac1Input.setText( Login2.this.getOnlyNumerics( pac1Input.getText().toString() ) );
			}
			
			int counter = pac1Input.getText().toString().length();
			
			if ( counter == 1 )
			{
				imm.hideSoftInputFromWindow( pac1Input.getWindowToken() , 0 );
				pac2Input.requestFocus();
				imm.showSoftInput( pac2Input , 1 );
			}	
			else if ( counter > 1 )
			{
				pac1Input.setText( pac1Input.getText().toString().substring( 0 , 1 ) );
			}	
		}

	};
	
	
	private TextWatcher pacWatcher2 = new TextWatcher()
	{
		public void afterTextChanged( Editable s )
		{}

		public void beforeTextChanged( CharSequence s , int start , int count , int after )
		{}

		public void onTextChanged( CharSequence s , int start , int before , int count )
		{
			InputMethodManager imm = (InputMethodManager) Login2.this.getSystemService( Service.INPUT_METHOD_SERVICE );

			EditText pac1Input = ( EditText ) findViewById( R.id.pac2 );
			EditText pac2Input = ( EditText ) findViewById( R.id.pac3 );
			
			if( Login2.this.containsOnlyNumerics( pac1Input.getText().toString() ) == false )
			{
				pac1Input.setText( Login2.this.getOnlyNumerics( pac1Input.getText().toString() ) );
			}
			
			int counter = pac1Input.getText().toString().length();
			
			if ( counter == 1 )
			{
				imm.hideSoftInputFromWindow( pac1Input.getWindowToken() , 0 );
				pac2Input.requestFocus();
				imm.showSoftInput( pac2Input , 1 );
			}	
			else if ( counter > 1 )
			{
				pac1Input.setText( pac1Input.getText().toString().substring( 0 , 1 ) );
			}	
		}

	};
	
	
	private TextWatcher pacWatcher3 = new TextWatcher()
	{
		public void afterTextChanged( Editable s )
		{}

		public void beforeTextChanged( CharSequence s , int start , int count , int after )
		{}

		public void onTextChanged( CharSequence s , int start , int before , int count )
		{
			InputMethodManager imm = (InputMethodManager) Login2.this.getSystemService( Service.INPUT_METHOD_SERVICE );

			EditText pac1Input = ( EditText ) findViewById( R.id.pac3 );
			EditText pac2Input = ( EditText ) findViewById( R.id.challengeNumber );
			
			if( Login2.this.containsOnlyNumerics( pac1Input.getText().toString() ) == false )
			{
				pac1Input.setText( Login2.this.getOnlyNumerics( pac1Input.getText().toString() ) );
			}
			
			int counter = pac1Input.getText().toString().length();
			
			if ( counter == 1 )
			{
				imm.hideSoftInputFromWindow( pac1Input.getWindowToken() , 0 );
				pac2Input.requestFocus();
				imm.showSoftInput( pac2Input , 1 );
			}	
			else if ( counter > 1 )
			{
				pac1Input.setText( pac1Input.getText().toString().substring( 0 , 1 ) );
			}	
		}

	};
	
	
	private TextWatcher chalWatcher = new TextWatcher()
	{
		public void afterTextChanged( Editable s )
		{}

		public void beforeTextChanged( CharSequence s , int start , int count , int after )
		{}

		public void onTextChanged( CharSequence s , int start , int before , int count )
		{
			InputMethodManager imm = (InputMethodManager) Login2.this.getSystemService( Service.INPUT_METHOD_SERVICE );

			EditText pac1Input = ( EditText ) findViewById( R.id.challengeNumber );
			Button loginButton = ( Button ) findViewById( R.id.loginButton );
			
			if( Login2.this.containsOnlyNumerics( pac1Input.getText().toString() ) == false )
			{
				pac1Input.setText( Login2.this.getOnlyNumerics( pac1Input.getText().toString() ) );
			}
			
			int counter = pac1Input.getText().toString().length();
			
			if ( counter == 4 )
			{
				imm.hideSoftInputFromWindow( pac1Input.getWindowToken() , 0 );
				loginButton.requestFocus();
			}	
			else if ( counter > 4 )
			{
				pac1Input.setText( pac1Input.getText().toString().substring( 0 , 4 ) );
			}	
		}

	};
	

	public void onClick( View v )
	{
		switch ( v.getId() )
		{
			case R.id.loginButton:

				this.pd = ProgressDialog.show( this , "Step 2 of 2" , "Please standby while we complete your login" , true , false );
				Thread thread = new Thread( this );
				thread.start();

			break;
		}
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

	@Override
	public void run()
	{
		WebHelper httpHelper = WebHelper.getInstance();
		EditText pac1Chal = (EditText) this.findViewById( R.id.pac1 );
		EditText pac2Chal = (EditText) this.findViewById( R.id.pac2 );
		EditText pac3Chal = (EditText) this.findViewById( R.id.pac3 );
		EditText Chal = (EditText) this.findViewById( R.id.challengeNumber );

		if ( httpHelper.login2( pac1Chal.getText().toString() , pac2Chal.getText().toString() , pac3Chal.getText().toString() , Chal.getText().toString() ) == true )
		{
			try
			{
				Login2.this.handler.sendEmptyMessage( 0 );
				Person.getInstance();
				Intent myIntent = new Intent( Login2.this , AccountActivity.class );
				Login2.this.startActivity( myIntent );
				Login2.this.finish();
			}
			catch( Exception e )
			{
				
			}
		}
		else
		{
			Message msg = new Message();
			msg.obj = httpHelper.getLastError();
			Login2.this.handler.sendMessage( msg );
		}
	}


	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage( Message msg )
		{
			Login2.this.pd.dismiss();
			
			try
			{				
				Toast.makeText( Login2.this.getApplicationContext() , msg.obj.toString() , 5000 ).show();
				Intent myIntent = new Intent( Login2.this , Login1.class );
				Login2.this.startActivity( myIntent );
				Login2.this.finish();
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
