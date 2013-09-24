package ee.itcollege.android.EMHI;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class IlmContentProvider extends ContentProvider {
	
	public static final Uri CONTENT_URI = Uri.parse("content://ee.itcollege.android.EMHI/forecasts");

	private static final String TABLE_NAME = "weather";
	
	public static final String ID = "_id";
	public static final String DATE = "date";
	public static final String NPHENOMENON = "nPhenomenon";
	public static final String NTEMPMIN = "nTempMin";
	public static final String NTEMPMAX = "nTempMax";
	public static final String NTEXT = "nText";
	public static final String DPHENOMENON = "dPhenomenon";
	public static final String DTEMPMIN = "dTempMin";
	public static final String DTEMPMAX = "dTempMax";
	public static final String DTEXT = "dText";

    private DatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		return db.delete(TABLE_NAME, selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		db.insert(TABLE_NAME, null, values);

		db.close();
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		return db.update(TABLE_NAME, values, selection, selectionArgs);
	}

	
    private static class DatabaseHelper extends SQLiteOpenHelper {

    	private static final String DATABASE_NAME = "weather.db";
    	
    	private static final int DATABASE_VERSION = 1;
    	
    	public DatabaseHelper(Context context) {
    		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	}

    	@Override
    	public void onCreate(SQLiteDatabase db) {
    		db.execSQL("CREATE TABLE " + TABLE_NAME + "("
    				+ ID + " INTEGER PRIMARY KEY,"
    				+ DATE + " TEXT,"
    				+ NPHENOMENON + " TEXT,"    				
    				+ NTEMPMAX + " INTEGER,"
    				+ NTEMPMIN + " INTEGER,"
    				+ NTEXT + " TEXT,"
    				+ DPHENOMENON + " TEXT,"
    				+ DTEMPMAX + " INTEGER,"
    				+ DTEMPMIN + " INTEGER,"    				
    				+ DTEXT + " TEXT"
    				+ ");");
    	}

    	@Override
    	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    		onCreate(db);
    	}
    	
    	
    }

}
