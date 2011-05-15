package ie.aib;
import java.util.ArrayList;

import android.os.CountDownTimer;
import android.os.Looper;


public class Timeout
{
	private String timeout;
	private CountDownTimer timer;
	private static Timeout instance = null;
	private ArrayList< onTimedOut > observers;
	private int counter;
	
	private Timeout()
	{
		this.observers = new ArrayList< onTimedOut>();
	}
	
	
	public void autoLogoutTimer()
	{
		if( this.timer != null )
		{
			this.timer.cancel();
		}
		
		try
		{
			Looper.prepare();
		}
		catch( Exception h ){}
		
		this.counter = 0;
		
		this.timer = new CountDownTimer( 300000 , 1000 ) 
		{
		     public void onTick( long millisUntilFinished ) 
		     {
		    	 Timeout.this.counter++;
		    	 
		    	 int mins = ( int ) Math.floor( ( millisUntilFinished / 1000 ) / 60 );
		    	 int secs = ( int ) ( millisUntilFinished / 1000 ) % 60;
		    	 
		    	 String timeout;
		    	 
		    	 if( mins > 0 )
		    	 {
		    		 timeout = mins + " minute" + ( mins > 1 ? "s" : "" ) + " " + ( secs < 10 ? "0" + secs : secs ) + " seconds";
		    	 }
		    	 else
		    	 {
		    		 timeout = "" + ( secs < 10 ? "0" + secs : secs ) + " seconds";
		    	 }
		    	 		    	 
		    	 Timeout.this.timeout = "Automatic logout in : " + timeout;
		    	 Timeout.this.changeTitles();
		     }

		     public void onFinish() 
		     {
		    	 Timeout.this.forceLogout();
		     }
		}.start();
	}
	
	
	private void forceLogout()
	{
		for( onTimedOut obj : this.observers )
		{
			if( obj != null )
			{
				obj.timedOut();
			}
		}
	}
	
	
	private void changeTitles()
	{
		for( onTimedOut obj : this.observers )
		{
			if( obj != null )
			{
				if( this.counter % 10 > 5 || this.counter > 280 )
				{
					obj.countDownTitle( this.timeout );
				}
				else
				{
					obj.countDownTitle( null );
				}				
			}
		}
	}
	
	
	public static Timeout getInstance()
	{
		if( Timeout.instance == null )
		{
			Timeout.instance = new Timeout();
		}
		
		return Timeout.instance;
	}
		
	
	public void attach( onTimedOut d )
	{
		this.observers.add( d );
	}
}
