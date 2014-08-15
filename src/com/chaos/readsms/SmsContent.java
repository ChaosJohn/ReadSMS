package com.chaos.readsms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class SmsContent {

	private Activity activity;
	private Uri uri;
	List<SmsInfo> infos;
	private String TAG = "SmsContent";

	public SmsContent(Activity activity, Uri uri) {
		infos = new ArrayList<SmsInfo>();
		this.activity = activity;
		this.uri = uri;
	}

	public List<SmsInfo> getSmsInfo() {
		String[] projection = new String[] { "_id", "address", "person",
				"body", "date", "type" };

//		@SuppressWarnings("deprecation")
//		Cursor cursor = activity.managedQuery(uri, projection, null, null,
//				"date desc");
		
		ContentResolver cr = activity.getContentResolver();
		Cursor cursor = cr.query(uri, projection, null, null, "date desc");

		int nameColumn = cursor.getColumnIndex("person");
		int phoneNumberColumn = cursor.getColumnIndex("address");
		int smsbodyColumn = cursor.getColumnIndex("body");
		int dateColumn = cursor.getColumnIndex("date");
		int typeColumn = cursor.getColumnIndex("type");
		if (cursor != null) {
			int i = 0;
			while (cursor.moveToNext() && i++ < 20) {
				SmsInfo smsInfo = new SmsInfo();
				smsInfo.setName(cursor.getString(nameColumn));
				smsInfo.setDate(dateFromLongToString(cursor.getString(dateColumn)));
				smsInfo.setPhoneNumber(cursor.getString(phoneNumberColumn));
				smsInfo.setSmsbody(cursor.getString(smsbodyColumn));
				smsInfo.setType(cursor.getString(typeColumn));
				String personName = getPeople2(smsInfo.getPhoneNumber());
				smsInfo.setName(null == personName ? smsInfo.getPhoneNumber()
						: personName);
				infos.add(smsInfo);
			}
			cursor.close();
		}
		return infos;
	}

	/*
	 * 根据电话号码取得联系人姓名
	 */
	public String getPeople(String phoneNumber) {
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };

		Log.d(TAG, "getPeople ---------");

		// 将自己添加到 msPeers 中
		Cursor cursor = activity.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				projection, // Which columns to return.
				ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
						+ phoneNumber + "'", // WHERE clause.
				null, // WHERE clause value substitution
				null); // Sort order.

		if (cursor == null) {
			Log.d(TAG, "getPeople null");
			return null;
		}
		Log.d(TAG, "getPeople cursor.getCount() = " + cursor.getCount());
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);

			// 取得联系人名字
			int nameFieldColumnIndex = cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			String name = cursor.getString(nameFieldColumnIndex);
			Log.i("Contacts", "" + name + " .... " + nameFieldColumnIndex); // 这里提示
																			// force
																			// close
																			// m_TextView.setText("联系人姓名："
																			// +
																			// name);
			return name;
		}
		return null;
	}

	public String getPeople2(String phoneNumber) {
		Cursor cursor = activity.getContentResolver().query(
				Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
						phoneNumber),
				new String[] { PhoneLookup._ID, PhoneLookup.NUMBER,
						PhoneLookup.DISPLAY_NAME, PhoneLookup.TYPE,
						PhoneLookup.LABEL }, null, null, null);

		if (cursor.getCount() == 0) {
			// 没找到电话号码
			return null;
		} else if (cursor.getCount() > 0) {

			cursor.moveToFirst();
			return cursor.getString(2); // 获取姓名
		}
		return null;
	}
	
	public String dateFromLongToString(String dateLong) {
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
		java.util.Date dt = new Date(Long.parseLong(dateLong));  
		return sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
	}

}
