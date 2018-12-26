package com.wcoast.finic.SQlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wcoast.finic.model.ModelSubCategory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private String TAG = DataBaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "MLMDataB.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static String DB_PATH = "";
    private static final String TABLE_SUBCATEGORIES = "table_subcategories";
    private static final String SUB_CAT_CATID = "category_id";
    private static final String SUB_CAT_NAME = "sub_catName";
    private static final String SUB_CAT_ID = "sub_catId";
    private static final String SUB_CAT_LINK = "sub_catLink";
    private static final String SUB_CAT_IMAGE_PATH = "sub_catImagePath";
    private static final String SUB_CAT_DETAIL = "sub_catDetail";
    private final Context mContext;
    private boolean mNeedUpdate = false;
    private SQLiteDatabase sqLiteDatabase;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();


    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DATABASE_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DATABASE_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DATABASE_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        sqLiteDatabase = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return sqLiteDatabase != null;
    }

    @Override
    public synchronized void close() {
        if (sqLiteDatabase != null)
            sqLiteDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            mNeedUpdate = true;
        }
    }

    public List<ModelSubCategory> getAllSubCategories(int CatId) {
        ArrayList<ModelSubCategory> modelSubCategories = new ArrayList<ModelSubCategory>();
        sqLiteDatabase = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SUBCATEGORIES + " where " + SUB_CAT_CATID + " = " + CatId;
        Cursor c = sqLiteDatabase.rawQuery(selectQuery, null);
        Log.e(TAG, "getAllSubCategories: " + c.getCount());
        if (c.moveToFirst()) {
            do {
                ModelSubCategory scm = new ModelSubCategory();
                scm.setSubcatId(c.getInt(c.getColumnIndex(SUB_CAT_ID)));
                scm.setSubcatName(c.getString(c.getColumnIndex(SUB_CAT_NAME)));
                scm.setSubcatImage(c.getString(c.getColumnIndex(SUB_CAT_IMAGE_PATH)));

                if (!c.isNull(c.getColumnIndex(SUB_CAT_DETAIL))) {
                    scm.setSubcatDetail(c.getString(c.getColumnIndex(SUB_CAT_DETAIL)));
                } else scm.setSubcatDetail("");

                scm.setSubcatLink(c.getString(c.getColumnIndex(SUB_CAT_LINK)));
                modelSubCategories.add(scm);
            } while (c.moveToNext());
        }
        c.close();
        return modelSubCategories;
    }


}
