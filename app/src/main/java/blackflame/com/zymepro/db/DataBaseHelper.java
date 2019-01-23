package blackflame.com.zymepro.db;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;
import blackflame.com.zymepro.ui.document.model.ImageTableHelper;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper implements SQLiteDatabase.CursorFactory{
  // Database Version
  private static final int DATABASE_VERSION = 3;
  // Database Name
  private static final String DATABASE_NAME = "ZymePro";
  // Table Names
  private static final String IMAGETABLE = "TABLE_IMAGE_PRO";
  private static final String PATHIMAGE = "TABLE_IMAGE_PATH";

  // column names
  private static final String KEY_NAME = "image_name";
  private static final String KEY_DATA = "image_data";
  private static final String KEY_TYPE="image_type";
  private static final String KEY_PATH="image_path";
  private static final String KEY_LAST_MODIFIED="last_modified";
  private static final String KEY_CREATION_DATE="creation_date";
  // Common column names
  private static final String KEY_ID = "id";
  Context con;
  Cursor c;


  // CREATE TABLE STATEMENT FOR IMAGE TABLE
  // Table create statement
  private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + IMAGETABLE + "("+
      KEY_ID + " TEXT," +
      KEY_NAME + " TEXT," +
      KEY_TYPE + " TEXT," +
      KEY_DATA+ " BLOB," +
      KEY_PATH+ " TEXT" +
      " )";

  private static final String CREATE_TABLE_PATH = "CREATE TABLE " + PATHIMAGE + "("+
      KEY_ID + " TEXT," +
      KEY_NAME + " TEXT," +
      KEY_TYPE + " TEXT," +
      KEY_CREATION_DATE + " TEXT," +
      KEY_LAST_MODIFIED + " TEXT," +
      KEY_PATH+ " TEXT" +
      " )";




  public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);

  }
  public DataBaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);

  }

  @Override
  public void onCreate(SQLiteDatabase db) {

    db.execSQL(CREATE_TABLE_PATH);


  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + IMAGETABLE);
//        onCreate(db);
//db
    // Log.e(TAG, "onUpgrade: "+"Call" );

    if (newVersion>oldVersion){

      db.execSQL(CREATE_TABLE_PATH);

    }
  }



  @Override
  public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
    //Log.d("SQL", query.toString());
    return null;
  }
  // CODE FOR INSERT  IMAGE RELATED DATA
  public void insertImage( ImageTableHelper helper) throws SQLiteException {
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues cv = new  ContentValues();
    cv.put(KEY_NAME,    helper.getName());
    cv.put(KEY_TYPE,helper.getType());
    cv.put(KEY_LAST_MODIFIED,helper.getLast_modified());
    cv.put(KEY_CREATION_DATE,helper.getCreation_date());
    cv.put(KEY_PATH,helper.getPath());
    cv.put(KEY_ID, helper.getId());
    Log.e(TAG, "insertImage: name"+helper.getName());

    long i=database.insert(PATHIMAGE, null, cv );
    Log.e(TAG, "insertImage: Insert"+i );
  }
  public ArrayList<ImageTableHelper> getImageData() {
    ArrayList<ImageTableHelper> data = new ArrayList<>();
    try {
      String documentName="Registration Certificate";

      String selectQuery = "SELECT  * FROM " + PATHIMAGE;// +" where "+KEY_TYPE+" = "+documentName;
      SQLiteDatabase db = this.getReadableDatabase();
      c = db.rawQuery(selectQuery, null);
      Log.e(TAG, "getImageData:Count " + c.getCount());
      if (c.moveToFirst()) {
        do {

          ImageTableHelper tableHelper = new ImageTableHelper();
          // Log.e(TAG, "getImageData:Inside " + c.getColumnIndex("oid"));

          tableHelper.setCreation_date(c.getString(c.getColumnIndex(KEY_CREATION_DATE)));
          tableHelper.setName(c.getString(c.getColumnIndex(KEY_NAME)));
          tableHelper.setType(c.getString(c.getColumnIndex(KEY_TYPE)));
          tableHelper.setLast_modified(c.getString(c.getColumnIndex(KEY_LAST_MODIFIED)));
          tableHelper.setPath(c.getString(c.getColumnIndex(KEY_PATH)));
          Log.e(TAG, "getImageData: name" + c.getString(c.getColumnIndex(KEY_PATH)));

          //   if (DATABASE_VERSION!=1){
          tableHelper.setId(c.getString(c.getColumnIndex(KEY_ID)));
          Log.e(TAG, "getImageData: key_id" + c.getString(c.getColumnIndex(KEY_ID)));
          Log.e(TAG, "getImageData: " + "NAME" + c.getString(c.getColumnIndex(KEY_NAME)) + "Id" + c.getString(c.getColumnIndex(KEY_ID)));
          // }


          data.add(tableHelper);
          Log.e(TAG, "getImageData:size "+data.size());

        } while (c.moveToNext());
      }
      db.close();
    }

    catch (Exception ex){

      ex.printStackTrace();
      Log.e(TAG, "getImageData: "+ex.getCause() );
    }

    return data;
  }
  public void deleteDocument(String id){
    SQLiteDatabase db=this.getWritableDatabase();
    Log.e(TAG, "deleteDocument: id"+id );

    //db.execSQL("DELETE FROM TABLE_IMAGE_PRO WHERE image_data = "+filedata);
    long a=  db.delete(PATHIMAGE,"id = ?", new String[]{id});

  }


}

