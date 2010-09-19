package SMW.battleships;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import SMW.battleships.core.OptionValues;
import SMW.battleships.core.BattleShips.Player;
import SMW.battleships.core.network.BSClient;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ActivityGameNetClient extends Activity {
	BSView view;
	BSClient client;

	private class ConnectToServer extends Thread {
		@Override
		public void run() {
			
			try {
				Log.i("net","Try to connect with server");
				client.startSession("10.0.2.2");
				Log.i("net","Conneted with server");
				view.setPlayer(Player.ONE);
			} catch (ConnectException e) {
				finish();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class ConnectToServerView extends LinearLayout {

		public ConnectToServerView(Context context) {
			super(context);
			this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			this.setGravity(Gravity.CENTER);
			this.setOrientation(HORIZONTAL);

			TextView label = new TextView(context);
			Button connect = new Button(context);
			connect.setText("Connect");
			
			connect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ConnectToServer cts = new ConnectToServer();
					cts.start();
				}
			});

			addView(label);
			addView(connect);

		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_net_client_activity);
		LinearLayout gameLayout = (LinearLayout) findViewById(R.id.game_layout);

//		OptionValues.setRows(9);
//		OptionValues.setColumns(9);

		// user 2
		  view = new BSView(this);

		// view.setModel(bs);

		// Socket socket = new Socket();
		// BSUser remoteUser=new RemoteBSUser(socket);
		client = new BSClient(view);
		gameLayout.addView(new ConnectToServerView(this), 0);

	}

}
