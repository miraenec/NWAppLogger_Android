package com.nextweb.nwapplogger;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author nextweb
 * 
 */
class DbOpenHelper {
	
	private static final DbOpenHelper instance = new DbOpenHelper();

	private static SQLiteDatabase mDB;
	private DatabaseHelper mDBHelper;

	private class DatabaseHelper extends SQLiteOpenHelper {

		// 생성자
		public DatabaseHelper(Context mCtx, String name, CursorFactory factory, int version) {
			super(mCtx, name, factory, version);
		}

		// 최초 DB를 만들때 한번만 호출된다.
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table " + Constants.TABLE_NAME + "(id integer primary key autoincrement, url_str text, update_dt text);");
		}

		// 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists " + Constants.TABLE_NAME);
			onCreate(db);
		}
	}
	
	private DbOpenHelper() {
	}
	
	static DbOpenHelper getInstance() {
		return instance;
	}

	DbOpenHelper open(Context mCtx) throws SQLException {
		try {
			mDBHelper = new DatabaseHelper(mCtx, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
			mDB = mDBHelper.getWritableDatabase();

			return this;
		} catch (SQLiteException e) {
			// e.printStackTrace();

			if (Constants.IS_DEBUG_MODE)
				Log.e("DbOpenHelper", "Can't get writable Database");
		}

		return null;
	}

	void close() {

		if (mDB != null)
			mDB.close();
	}

	void insert(String urlStr) {
		String qry = "insert into " + Constants.TABLE_NAME + "(url_str, update_dt) values('" + urlStr + "','" + CalendarUtil.currentTime() + "');";

		if (Constants.IS_DEBUG_MODE)
			Log.i("insert", qry);

		mDB.execSQL(qry);
	}

	Cursor selectAll() {
		String qry = "select id, url_str, update_dt from " + Constants.TABLE_NAME + ";";

		if (Constants.IS_DEBUG_MODE)
			Log.i("selectAll", qry);

		return mDB.rawQuery(qry, null);
	}

	void delete(int id) {
		String qry = "delete from " + Constants.TABLE_NAME + " where id=" + id + ";";

		if (Constants.IS_DEBUG_MODE)
			Log.i("delete", qry);

		mDB.execSQL(qry);
	}
}
