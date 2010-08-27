package SMW.battleships;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        
        Button buttonBackMenu=(Button) findViewById(R.id.ButtonBackMenu);
        buttonBackMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(AboutActivity.this, MainMenu.class);
				AboutActivity.this.startActivity(myIntent);
				
			}
		});
        
      
		
        
    }
}
