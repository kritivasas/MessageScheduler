package com.reminder3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_YEAR = "mYear";
	public static final String KEY_MONTH = "mMonth";
	public static final String KEY_DATE = "mDay";
	public static final String KEY_HOUR = "mHour";
	public static final String KEY_MIN = "mMinute";
	public static final String KEY_TOPIC = "topic";
	public static final String KEY_PHNO = "phnNo";
	public static final String KEY_MSG = "msgTosend";
	
	private static final String DATABASE_TABLE = "ReminderData";
	private Context context;
	private SQLiteDatabase database;
	private DataBaseHelper dbHelper;

	public DbAdapter(Context context) {
		this.context = context;
	}

	public DbAdapter open() throws SQLException {
		dbHelper = new DataBaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	
/** * Create a new todo If the todo is successfully created return the new * rowId for that note, otherwise return a -1 to indicate failure. */

	public long createReminderData(int year,int month,int day,
			int hour,int minute,String topic,
			String phoneno,String msgtosend) {
		ContentValues initialValues = createContentValues(year, month,day,hour,minute,topic,
				phoneno,msgtosend);

		return database.insert(DATABASE_TABLE, null, initialValues);
	}

	
/** * Update the todo */

	public boolean updateReminderData(long rowId, int year,int month,int day,
			int hour,int minute,String topic,
			String phoneno,String msgtosend) {
		ContentValues updateValues = createContentValues(year, month,day,hour,minute,topic,
				phoneno,msgtosend);

		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	
/** * Deletes todo */

	public boolean deleteReminderData(long rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	public boolean deleteAllReminderData() {
		return database.delete(DATABASE_TABLE, null, null) > 0;
	}

	
/** * Return a Cursor over the list of all todo in the database * * @return Cursor over all notes */

	public Cursor fetchAllReminderData() {
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_YEAR,KEY_MONTH, KEY_DATE, KEY_HOUR,KEY_MIN,KEY_TOPIC,KEY_PHNO,KEY_MSG},
				null, null, null,
				null, null);
	}

	
/** * Return a Cursor positioned at the defined todo */

	public Cursor fetchReminderData(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID,
				KEY_YEAR,KEY_MONTH, KEY_DATE, KEY_HOUR,KEY_MIN,KEY_TOPIC,KEY_PHNO,KEY_MSG},
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private ContentValues createContentValues( int year,
			int month,int day,int hour,int minute,String topic,
			String phoneno,String msgtosend) {
		ContentValues values = new ContentValues();
		values.put(KEY_YEAR, year);
		values.put(KEY_MONTH, month);
		values.put(KEY_DATE, day);
		values.put(KEY_HOUR, hour);
		values.put(KEY_MIN, minute);
		values.put(KEY_TOPIC, topic);
		values.put(KEY_PHNO, phoneno);
		values.put(KEY_MSG, msgtosend);
		return values;
	}
}
