package com.group9.factormebud;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


/**
 * Created by NaveenJetty on 10/28/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    //The Android's default system path of application database.
    private static String DB_PATH = "/data/data/com.group9.factormebud/databases/";
    private static String DB_NAME = "Factor_Me.db";
    private SQLiteDatabase myDataBase;
    private final String tLvlID = "_Id";
    private final String tLvlName = "LevelName";
    private final String tHighScr = "HighScore";
    private final String tIsLckd = "IsLocked";
    private final String tIsLastPlayed = "IsLastPlayed";
    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites with our own database
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if(dbExist) {
            // Database already exists - no need to do anything
        }else{
            // getReadableDatabase will create an empty database in the system default path
            this.getReadableDatabase();
            try {
                copyDataBase();
            }catch(IOException e) {
                throw new Error("Error Copying database");
            }
        }

    }

    /**
     * check if the database exists are not
     * @return true if database exists, false if not
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }catch(SQLiteException e) {
            // Database is not present
        }
        if (checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies database from local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public ArrayList<ArrayList<Object>> getAllRowsAsArrays() {
        Cursor cursor;
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
        try
        {
            // ask the database object to create the cursor.
            cursor = myDataBase.rawQuery("select * from LEVEL",null);

            // move the cursor's pointer to position zero.
            cursor.moveToFirst();

            // if there is data after the current cursor position, add it
            // to the ArrayList.
            if (!cursor.isAfterLast())
            {
                do
                {
                    ArrayList<Object> dataList = new ArrayList<Object>();

                    dataList.add(cursor.getInt(0));
                    dataList.add(cursor.getString(1));
                    dataList.add(cursor.getInt(2));
                    dataList.add(cursor.getString(3));
                    dataList.add(cursor.getString(4));

                    dataArrays.add(dataList);
                }
                // move the cursor's pointer up one position.
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        // return the ArrayList that holds the data collected from
        // the database.
        return dataArrays;

    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db){

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public void updateScr(int curLvlId, int curLvlHScr )
    {
        Cursor cursor;
        String s = "Update LEVEL set Highscore = " + curLvlHScr + " where _id = " + curLvlId;
        cursor = myDataBase.rawQuery(s, null);
        cursor.moveToFirst();
        cursor.close();

    }

    public void unlckLvl(int nxtLvlId)
    {
        Cursor cursor;
        String st = "update LEVEL set isLocked = 'N' where _id = " + nxtLvlId;
        cursor = myDataBase.rawQuery(st, null);
        cursor.moveToFirst();
        cursor.close();

    }
}