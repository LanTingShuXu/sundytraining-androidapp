package station.mp3player;

import station.model.Mp3Info;
import station.mp3player.service.PlayerService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlayerActivity extends Activity {
	ImageButton startButton = null;
	ImageButton pauseButton = null;
	ImageButton stopButton = null;
	MediaPlayer mediaPlayer = null;
	
	private TextView lrcTextView = null;
	Mp3Info mp3Info = null;
	// private Handler handler = new Handler();
	// private UpdateTimeCallback updateTimeCallback = null;
	// private ArrayList<Queue> queues = null;
	// private long begin = 0;
	// private long nextTimeMill = 0;
	// private long pauseTimeMills = 0;
	// private long currentTimeMill = 0;
	// private String message = null;
//	private boolean isPlaying = false;
	private IntentFilter intentFilter = null;
	private BroadcastReceiver receiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);

		Intent intent = getIntent();
		mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");

		startButton = (ImageButton) findViewById(R.id.begin);
		pauseButton = (ImageButton) findViewById(R.id.pause);
		stopButton = (ImageButton) findViewById(R.id.stop);

		startButton.setOnClickListener(new startButtonListener());
		pauseButton.setOnClickListener(new pauseButtonListener());
		stopButton.setOnClickListener(new stopButtonListener());
		lrcTextView = (TextView) findViewById(R.id.textView1);
	}

	/**
	 * ��activity���ڲ��ɼ���ʱ��ͻ���øķ���
	 */
	@Override
	protected void onPause() {
		super.onPause();
		// ����㲥������
		unregisterReceiver(receiver);
	}

	/**
	 * ��activity���û���ý����ʱ����øķ���
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// ����ע��㲥������
		receiver = new LrcMessageBroadcastReceiver();
		registerReceiver(receiver, getIntentFilter());
	}

	/**
	 * ��������ֻҪ�����ƶ���action�Ĺ㲥
	 * @return
	 */
	private IntentFilter getIntentFilter() {
		if (intentFilter == null) {
			intentFilter = new IntentFilter();
			intentFilter.addAction(AppConstant.LRC_MESSAGE_ACTION);
		}
		return intentFilter;
	}

	/**
	 * �㲥��������Ҫ����Service���͵Ĺ㲥�����Ҹ��¸��(UI)
	 * @author luhan
	 *
	 */
	class LrcMessageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// ��Intent��ȡ�������Ϣ��Ȼ�����TextView
			String lrcMessage = intent.getStringExtra("lrcMessage");
			lrcTextView.setText(lrcMessage);
		}

	}

/*	*//**
	 * ���ݸ���ļ������֣�����ȡ����ļ����е���Ϣ
	 * 
	 * @param lrcName
	 *//*
	private void prepareLrc(String lrcName) {
		try {
			InputStream inputStream = new FileInputStream(
					Environment.getExternalStorageDirectory() + File.separator
							+ "mp3" + File.separator + mp3Info.getLrcname());
			LrcProcessor lrcProcessor = new LrcProcessor();
			// ��������ļ��󷵻ض���list
			queues = lrcProcessor.process(inputStream);
			// ����һ��UpdateTimeCallback����
			updateTimeCallback = new UpdateTimeCallback(queues);
			// ��ʼ������
			begin = 0;
			currentTimeMill = 0;
			nextTimeMill = 0;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	class UpdateTimeCallback implements Runnable {
		Queue times, messages = null;

		public UpdateTimeCallback(ArrayList<Queue> queues) {
			// ��ArrayList����ȡ����Ӧ�Ķ������
			times = queues.get(0);
			messages = queues.get(1);
		}

		@Override
		public void run() {
			// ����ƫ������Ҳ����˵�ӿ�ʼ����MP3������Ϊֹ���������˶���ʱ�䣬�Ժ���Ϊ��λ
			long offset = System.currentTimeMillis() - begin;
			if (currentTimeMill == 0) {
				nextTimeMill = (Long) times.poll();
				message = (String) messages.poll();
			}
			if (offset >= nextTimeMill) {
				lrcTextView.setText(message);
				message = (String) messages.poll();
				nextTimeMill = (Long) times.poll();
			}
			currentTimeMill = currentTimeMill + 10;
			handler.postDelayed(updateTimeCallback, 10);
		}

	}*/

	class startButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
			// ��Ϊ����MP3��Ҫһ��ʵ�������(Mp3Info)������������Լ����·��
			intent.putExtra("mp3Info", mp3Info);
			intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);

			// // �������ƶ�ȡ����ļ�
			// prepareLrc(mp3Info.getLrcname());
			startService(intent);
			// ��begin��ֵ��Ϊ��ǰʱ��(����)
			// begin = System.currentTimeMillis();
			// // 5�����ִ��updateTimeCallback�߳�
			// handler.postDelayed(updateTimeCallback, 5);
			// isPlaying = true;
		}
	}

	class pauseButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
			intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
			startService(intent);
			/*
			 * if (isPlaying) { handler.removeCallbacks(updateTimeCallback); //
			 * ��ͣʱ��ǰʱ�� pauseTimeMills = System.currentTimeMillis(); } else {
			 * handler.postDelayed(updateTimeCallback, 5); // �ٴο�ʼʱ�� begin =
			 * begin + System.currentTimeMillis() - pauseTimeMills; } isPlaying
			 * = isPlaying ? false : true;
			 */
		}

	}

	class stopButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
			intent.putExtra("MSG", AppConstant.PlayerMsg.STOP_MSG);
			// ֪ͨServiceֹͣ����MP3
			startService(intent);
			// handler.removeCallbacks(updateTimeCallback);
		}

	}

}
