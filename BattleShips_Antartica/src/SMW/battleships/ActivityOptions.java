package SMW.battleships;

import SMW.battleships.SessionEvents.AuthListener;
import SMW.battleships.SessionEvents.LogoutListener;
import SMW.battleships.core.OptionValues;
import SMW.battleships.core.OptionValues.Difficult;
import SMW.facebook.AsyncFacebookRunner;
import SMW.facebook.Facebook;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;




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
        
        
        //back button settings
        backMenu.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = new Intent(ActivityOptions.this, MainMenu.class);
				ActivityOptions.this.startActivity(intent);
				return false;
			}
		}); 
        
        //facebook settings
    	mFacebook = new Facebook();
       	AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(mFacebook);
       	SessionStore.restore(mFacebook, this);
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
       	activateFacebook.init(mFacebook, PERMISSIONS);
        
       //difficult settings
       	Button btnShowDifficultLevels=(Button) findViewById(R.id.ButtonSelectDifficult);
        btnShowDifficultLevels.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinearLayout startGameMenu= (LinearLayout)findViewById(R.id.menu_select_difficult);
 				
				if(startGameMenu.getVisibility()==View.VISIBLE){
					startGameMenu.setVisibility(View.GONE);	
				}else{
					startGameMenu.setVisibility(View.VISIBLE);
				}
				
			}
		}); 
        
        final Button btnShowVeryEasy=(Button) findViewById(R.id.btn_veryeasy);
        final Button btnShowEasy=(Button) findViewById(R.id.btn_easy);
        final Button btnShowNormal=(Button) findViewById(R.id.btn_normal);
        final Button btnShowHard=(Button) findViewById(R.id.btn_hard);
       
        if(OptionValues.difficult == Difficult.VERYEASY) btnShowVeryEasy.setEnabled(false);
        if(OptionValues.difficult == Difficult.EASY) btnShowEasy.setEnabled(false);
        if(OptionValues.difficult == Difficult.NORMAL) btnShowNormal.setEnabled(false);
        if(OptionValues.difficult == Difficult.HARD) btnShowHard.setEnabled(false);
        
        
        btnShowVeryEasy.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				OptionValues.difficult=Difficult.VERYEASY;
			    btnShowVeryEasy.setEnabled(false);
		         btnShowEasy.setEnabled(true);
		         btnShowNormal.setEnabled(true);
		         btnShowHard.setEnabled(true);
		        
				
			}
		}); 
      
        btnShowEasy.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				OptionValues.difficult=Difficult.EASY;
			    btnShowVeryEasy.setEnabled(true);
		         btnShowEasy.setEnabled(false);
		         btnShowNormal.setEnabled(true);
		         btnShowHard.setEnabled(true);
		        
				
			}
		}); 
        btnShowNormal.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				OptionValues.difficult=Difficult.NORMAL;
			    btnShowVeryEasy.setEnabled(true);
		         btnShowEasy.setEnabled(true);
		         btnShowNormal.setEnabled(false);
		         btnShowHard.setEnabled(true);
		        
				
			}
		});
        
        btnShowHard.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				OptionValues.difficult=Difficult.HARD;
			    btnShowVeryEasy.setEnabled(true);
		         btnShowEasy.setEnabled(true);
		         btnShowNormal.setEnabled(true);
		         btnShowHard.setEnabled(false);
		        
				
			}
		}); 
        
    }
}
