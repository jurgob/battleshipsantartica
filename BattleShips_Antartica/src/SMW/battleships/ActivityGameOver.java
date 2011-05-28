package SMW.battleships;

import SMW.battleships.core.OptionValues;
import SMW.facebook.AsyncFacebookRunner;
import SMW.facebook.Facebook;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class ActivityGameOver extends Activity {

	AsyncFacebookRunner mAsyncRunner = OptionValues.myAsyncRunner;
	Facebook mFacebook = OptionValues.facebook;

	public class WallPostDeleteListener extends BaseRequestListener {

		public void onComplete(final String response) {
			if (response.equals("true")) {
				Log.d("Facebook-Example", "Successfully deleted wall post");
				// Example.this.runOnUiThread(new Runnable() {
				// public void run() {
				// mDeleteButton.setVisibility(View.INVISIBLE);
				// mText.setText("Deleted Wall Post");
				// }
				// });
			} else {
				Log.d("Facebook-Example", "Could not delete wall post");
			}
		}
	}

	public class SampleDialogListener extends BaseDialogListener {

		public void onComplete(Bundle values) {
	            try{
		        	final String postId = values.getString("post_id");
		            if (postId != null) {
		                Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
		                mAsyncRunner.request(postId, new SMW.battleships.ActivityGameOver.WallPostDeleteListener());	              
	
		            } else {
		                Log.d("Facebook-Example", "No wall post made");
		            }
	            }catch (Exception e) {
					Log.d("BS Facebook","exeption while stream post: "+e);
				}
	        }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over_activity);
		Button backMenu = (Button) findViewById(R.id.ButtonBackMenu);

		Button postResultOnFacebook = (Button) findViewById(R.id.PostResultOnFacebook);
		if (OptionValues.isFacebookNotify())
			postResultOnFacebook.setVisibility(View.VISIBLE);
		else
			postResultOnFacebook.setVisibility(View.GONE);

		backMenu.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = new Intent(ActivityGameOver.this,
						MainMenu.class);
				ActivityGameOver.this.startActivity(intent);
				return false;
			}
		});

		postResultOnFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SampleDialogListener sdl = new SampleDialogListener();
				Bundle b = new Bundle();
				String src = "http://bsantartica.ilbello.com/img/bs-logo.jpg";
				String href = "http://apps.facebook.com/bs_antartica_demo/";
				String name = "I have just played with Battleships Antartica";
				String description = "I Won!!";
				b.putString("attachment", "{" + "'name': '" + name + "',"
						+ "'description': '" + description + "',"
						+ "'media': [{" + "'type': 'image'," + " 'src': '"
						+ src + "'," + "'href': '" + href + "'," + "}]}"

				);
				try {
					mFacebook.dialog(ActivityGameOver.this, "stream.publish",
							b, sdl);
				} catch (Exception e) {
					Log.d("Facebook", "error while stream on fb: " + e);
				}

			}
		});

	}
}
