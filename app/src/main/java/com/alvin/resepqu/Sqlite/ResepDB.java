package com.alvin.resepqu.Sqlite;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alvin.resepqu.Object.ResepObject;

import java.util.ArrayList;
import java.util.List;

public class ResepDB extends SQLiteOpenHelper {

    // Database Version
    public static final int DATABSE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "resep_db";

    public ResepDB(Context context){
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table ResepObject
        db.execSQL(ResepObject.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" DROP TABLE IF EXISTS " + ResepObject.TABLE_NAME);

        onCreate(db);

    }

    public long insertResep(ResepObject resep){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ResepObject.COLUMN_JUDUL, resep.getJudul());
        values.put(ResepObject.COLUMN_DESKRIPSI, resep.getDeskripsi());
        values.put(ResepObject.COLUMN_CARA, resep.getCara());
        values.put(ResepObject.COLUMN_IMG, resep.getImg());

        long id = db.insert(ResepObject.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public ResepObject getResep(long id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ResepObject.TABLE_NAME,
                new String[]{ResepObject.COLUMN_ID, ResepObject.COLUMN_JUDUL,
                        ResepObject.COLUMN_DESKRIPSI, ResepObject.COLUMN_CARA,
                        ResepObject.COLUMN_IMG, ResepObject.COLUMN_TIMESTAMP},
                ResepObject.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null,null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        ResepObject resep = new ResepObject(
                cursor.getInt(cursor.getColumnIndex(ResepObject.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ResepObject.COLUMN_JUDUL)),
                cursor.getString(cursor.getColumnIndex(ResepObject.COLUMN_DESKRIPSI)),
                cursor.getString(cursor.getColumnIndex(ResepObject.COLUMN_CARA)),
                cursor.getString(cursor.getColumnIndex(ResepObject.COLUMN_IMG)),
                cursor.getString(cursor.getColumnIndex(ResepObject.COLUMN_TIMESTAMP))
        );

        cursor.close();

        return resep;
    }

    public List<ResepObject> getAllResep(){

        List<ResepObject> resepObjects = new ArrayList<>();
        resepObjects.clear();

        String selectQuery = " SELECT * FROM " + ResepObject.TABLE_NAME + " ORDER BY "
                + ResepObject.COLUMN_TIMESTAMP + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {

                ResepObject resep = new ResepObject();

                resep.setId(cursor.getInt(cursor.getColumnIndex(ResepObject.COLUMN_ID)));
                resep.setJudul(cursor.getString(cursor.getColumnIndex(ResepObject.COLUMN_JUDUL)));
                resep.setDeskripsi(cursor.getString(cursor.getColumnIndex(ResepObject.COLUMN_DESKRIPSI)));
                resep.setCara(cursor.getString(cursor.getColumnIndex(ResepObject.COLUMN_CARA)));
                resep.setImg(cursor.getString(cursor.getColumnIndex(ResepObject.COLUMN_IMG)));
                resep.setTime(cursor.getString(cursor.getColumnIndex(ResepObject.COLUMN_TIMESTAMP)));

                resepObjects.add(resep);

            } while (cursor.moveToNext());
        }

        db.close();

        return resepObjects;
    }

    public int getResepCount(){

        String countQuery = " SELECT * FROM " + ResepObject.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateResep(ResepObject resep){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ResepObject.COLUMN_JUDUL, resep.getJudul());
        values.put(ResepObject.COLUMN_DESKRIPSI, resep.getDeskripsi());
        values.put(ResepObject.COLUMN_CARA, resep.getCara());
        values.put(ResepObject.COLUMN_IMG, resep.getImg());

        return db.update(ResepObject.TABLE_NAME, values, ResepObject.COLUMN_ID
                + " =? ", new String[]{String.valueOf(resep.getId())} );
    }

    public void deleteResep(ResepObject resep){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ResepObject.TABLE_NAME, ResepObject.COLUMN_ID
                + " =? ", new String[]{String.valueOf(resep.getId())});
        db.close();
    }
}
