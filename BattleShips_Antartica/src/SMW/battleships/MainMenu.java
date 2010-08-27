package SMW.battleships;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainMenu extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button buttonAbout=(Button) findViewById(R.id.ButtonAbout);
        buttonAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainMenu.this, AboutActivity.class);
				MainMenu.this.startActivity(myIntent);
				
			}
		});
        Button btnStartGame=(Button) findViewById(R.id.ButtonStartGame);
        btnStartGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinearLayout startGameMenu= (LinearLayout)findViewById(R.id.start_game_menu);
 				
				if(startGameMenu.getVisibility()==View.VISIBLE){
					startGameMenu.setVisibility(View.GONE);	
				}else{
					startGameMenu.setVisibility(View.VISIBLE);
				}
				
			}
		});
        
        Button btnSinglePlayerGame=(Button) findViewById(R.id.btn_start_single_game);
        btnSinglePlayerGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainMenu.this, GameActivity.class);
				MainMenu.this.startActivity(myIntent);
				
			}
		});
        
        Button btnHostNetGame=(Button) findViewById(R.id.btn_host_net_game);
        btnHostNetGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainMenu.this, GameNetServerActivity.class);
				MainMenu.this.startActivity(myIntent);
				
			}
		});
        
        Button btnJoinNetGame=(Button) findViewById(R.id.btn_join_net_game);
        btnJoinNetGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainMenu.this, GameNetClientActivity.class);
				MainMenu.this.startActivity(myIntent);
				
			}
		});
        
        
        
        Button btnOptions=(Button) findViewById(R.id.ButtonOptions);
        btnOptions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainMenu.this, OptionsActivity.class);
				MainMenu.this.startActivity(myIntent);
				
			}
		});
        
    }//end onCreate()
   
    
    
}