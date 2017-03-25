package ua.hanasaka.tasksmilemolodykh.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * class for managing database
 *
 * Created by Oleksandr Molodykh on 23.03.2017.
 */
public class DB {
    String TAG = "myLogs";
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Context ctx;

    private static DB instance;

    /**
     * setting context in constructor
     *
     * @param ctx transferred context
     */
    private DB(Context ctx) {
        this.ctx = ctx;
    }

    public static synchronized DB getInstance(Context ctx){
        if(instance == null){
            instance = new DB(ctx);
        }
        return instance;
    }

    /**
     * @param cv ContentValues data to be added
     * @return long id
     */
    public long addTwit(ContentValues cv){
        long id = sqLiteDatabase.insert("twits", null, cv);
        Log.i(TAG, "id=="+id);
        return id;
    }


    /**
     * Getting required data of all users
     *
     * @return Cursor with data
     */
    public Cursor getAllUsersData() {
        return sqLiteDatabase.query("users", new String[]{"name", "nick"}, null, null, null, null, null);
    }

    /**
     * initializing required fields for work with database
     */
    public void open() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(ctx);
        }
        if (sqLiteDatabase == null) {
            sqLiteDatabase = dbHelper.getWritableDatabase();
        }
    }

    /**
     * get joined data of user twits from tables users and twits
     *
     * @param id id of user Who data is nedded
     * @return Cursor with data
     */
    public Cursor getTwits(long id){
        String table = "users as US inner join twits as TW on US.id = TW.user_id";
        String columns[] = { "US.id as id", "US.name as name", "US.nick as nick",
                "TW.body as body" };
        Cursor cursor = sqLiteDatabase.query(table, columns, "US.id="+id, null, null, null, "TW.id DESC");
        return cursor;
    }

    public long ifUserIsInDB(String nick){
        long id;
        Cursor cursor = sqLiteDatabase.query("users", new String[]{"id"}, "nick = '"+nick+"'", null, null, null, null);
        if(cursor.getCount()==0){
            return -1;
        }
        else{
            cursor.moveToFirst();
            id = cursor.getLong(cursor.getColumnIndex("id"));
        }
        return id;
    }

    /**
     * method for closing db
     */
    public void close() {
        if (dbHelper != null)
            dbHelper.close();
    }

    /**
     * helper class for work with db
     *
     * @author Oleksandr Molodykh
     */
    class DBHelper extends SQLiteOpenHelper {

        /**
         * constructor for creating db named myDB
         *
         * @param context transfered context
         */
        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        /**
         * calls only once
         *
         * @param db SQLiteDatabase db to work with
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "--- onCreate database ---");
            ContentValues cv = new ContentValues();
            // creating db with users
            db.execSQL("create table users ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "nick text" + ");");

            // creating db with twits
            db.execSQL("create table twits ("
                    + "id integer primary key autoincrement,"
                    + "body text,"
                    + "user_id integer" + ");");
            initStartDBData(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        /**
         * fill tables init data
         *
         * @param db object of SQLiteDatabase db
         */
        private void initStartDBData(SQLiteDatabase db){
            ContentValues cv = new ContentValues();
            cv.put("nick", "@qwerty");
            cv.put("name", "John Smith");
            db.insert("users", null, cv);
            cv.clear();
            cv.put("nick", "@sayhello");
            cv.put("name", "Nick Harrison");;
            db.insert("users", null, cv);
            cv.clear();
            cv.put("nick", "@smile");
            cv.put("name", "Jane Ford");
            db.insert("users", null, cv);
            cv.clear();
            cv.put("body", "hello, world! I have two friends - @sayhello and @smile! But I do not know" +
                    "this @man!");
            cv.put("user_id", 1);
            db.insert("twits", null, cv);
            cv.clear();
            cv.put("body", "I am cool!");
            cv.put("user_id", 2);
            db.insert("twits", null, cv);
            cv.clear();
            cv.put("body", "Hello @smile!");
            cv.put("user_id", 2);
            db.insert("twits", null, cv);
            cv.clear();
            cv.put("body", "hello, @qwerty! @sayhello is bad man!");
            cv.put("user_id", 3);
            db.insert("twits", null, cv);
        }
    }
}