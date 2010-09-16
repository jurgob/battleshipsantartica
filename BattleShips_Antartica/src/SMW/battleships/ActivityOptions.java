package SMW.battleships;

import SMW.battleships.SessionEvents.AuthListener;
import SMW.battleships.SessionEvents.LogoutListener;
import SMW.battleships.core.OptionValues;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;

import com.facebook.android.Facebook;


public class ActivityOptions extends Activity {
	
	 
	Facebook mFacebook ;
	
	private static final String[] PERMISSIONS =
        new String[] {"publish_stream", "read_stream", "offline_access"};
    private TextView fbLogResult;
    LoginButton activateFacebook;
    Button backMenu;
	 public class SampleAuthListener implements AuthListener {
         
         public void onAuthSucceed() {
             OptionValues.setFacebookNotify(true); 
             OptionValues.facebook=mFacebook;
        	 fbLogResult.setText(R.string.logged_in);
             fbLogResult.setTextColor(Color.GREEN);
             fbLogResult.setVisibility(View.VISIBLE);
             
             
        	 //mText.setText("You have logged in! ");
             //mRequestButton.setVisibility(View.VISIBLE);
             //mPostButton.setVisibility(View.VISIBLE);
         }

         public void onAuthFail(String error) {
        	 fbLogResult.setText(R.string.login_failed);
        	 fbLogResult.setTextColor(Color.RED);
             fbLogResult.setVisibility(View.VISIBLE);
             //mText.setText("Login Failed: " + error);
         }
     }
     
     public class SampleLogoutListener implements LogoutListener {
         public void onLogoutBegin() {
           //  mText.setText("Logging out...");
         }
         
         public void onLogoutFinish() {
        	 fbLogResult.setText(R.string.logged_out);
             fbLogResult.setVisibility(View.VISIBLE);
             //mText.setText("You have logged out! ");
             //mRequestButton.setVisibility(View.INVISIBLE);
             //mPostButton.setVisibility(View.INVISIBLE);
         }
     }
     
     
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_activity);
        fbLogResult=(TextView)findViewById(R.id.FBLoginResult);
        activateFacebook = (LoginButton)findViewById(R.id.LoginButton);
        backMenu = (Button)findViewById(R.id.ButtonBackMenu);
        
        backMenu.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = new Intent(ActivityOptions.this, MainMenu.class);
				ActivityOptions.this.startActivity(intent);
				return false;
			}
		}); 
    	mFacebook = new Facebook();
       	AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(mFacebook);
       	SessionStore.restore(mFacebook, this);
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
       	
       	activateFacebook.init(mFacebook, PERMISSIONS);
        
       
       	
       	
    }
}
