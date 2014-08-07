package com.chaos.readsms;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ListView listView = null;
	private List<SmsInfo> infos = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Uri uri = Uri.parse(AllFinalInfo.SMS_URI_INBOX);
		SmsContent smsContent = new SmsContent(MainActivity.this, uri);
		infos = smsContent.getSmsInfo();
		listView = (ListView) this.findViewById(R.id.sms_list);
		listView.setAdapter(new SmsListAdapter(getApplicationContext(), infos));
	}

	class SmsListAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater = null;
		private List<SmsInfo> infos = null;

		public SmsListAdapter(Context context, List<SmsInfo> infos) {
			this.layoutInflater = LayoutInflater.from(context);
			this.infos = infos;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.smsitem, null);
			}
			((TextView) convertView.findViewById(R.id.sms_body)).setText(infos
					.get(position).getSmsbody());
			((TextView) convertView.findViewById(R.id.sms_name)).setText(infos
					.get(position).getName());
			return convertView;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
