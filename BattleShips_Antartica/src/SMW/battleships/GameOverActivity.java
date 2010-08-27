package SMW.battleships;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

import SMW.battleships.core.OptionValues;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;


public class GameOverActivity extends Activity {
	
	AsyncFacebookRunner mAsyncRunner= OptionValues.myAsyncRunner;
	Facebook mFacebook = OptionValues.facebook;
	 public class WallPostDeleteListener extends BaseRequestListener {
	        
	        public void onComplete(final String response) {
	            if (response.equals("true")) {
	                Log.d("Facebook-Example", "Successfully deleted wall post");
//	                Example.this.runOnUiThread(new Runnable() {
//	                    public void run() {
//	                        mDeleteButton.setVisibility(View.INVISIBLE);
//	                        mText.setText("Deleted Wall Post");
//	                    }
//	                });
	            } else {
	                Log.d("Facebook-Example", "Could not delete wall post");
	            }
	        }
	    }
	
	
	 public class SampleDialogListener extends BaseDialogListener {

	        public void onComplete(Bundle values) {
	            final String postId = values.getString("post_id");
	            if (postId != null) {
	                Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
	                mAsyncRunner.request(postId, new SMW.battleships.GameOverActivity.WallPostDeleteListener());
	                //mDeleteButton.setOnClickListener(new OnClickListener() {
//	                    public void onClick(View v) {
//	                        mAsyncRunner.request(postId, new Bundle(), "DELETE", 
//	                                new WallPostDeleteListener());
//	                    }
//	                });
	               // mDeleteButton.setVisibility(View.VISIBLE);
	            } else {
	                Log.d("Facebook-Example", "No wall post made");
	            }
	        }
	    }
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_activity);
        Button backMenu = (Button)findViewById(R.id.ButtonBackMenu);
        
        Button postResultOnFacebook = (Button)findViewById(R.id.PostResultOnFacebook);
        if(OptionValues.isFacebookNotify()) postResultOnFacebook.setVisibility(View.VISIBLE);
        else postResultOnFacebook.setVisibility(View.GONE);
        
        backMenu.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = new Intent(GameOverActivity.this, MainMenu.class);
				GameOverActivity.this.startActivity(intent);
				return false;
			}
		}); 
        
        postResultOnFacebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mFacebook.dialog(GameOverActivity.this, "stream.publish",
                        new SampleDialogListener());          
			}
		});
        
    }
}
