package com.publicicat.mismascotastres;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.function.DoubleBinaryOperator;

public class Db extends SQLiteOpenHelper {

    private final Context context;

    public Db (Context context) {
        super(context, DbConfig.DATABASE_NAME, null, DbConfig.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateTablaMascota = "CREATE TABLE IF NOT EXISTS " +
                DbConfig.TABLE_MASCOTA + " (" +
                DbConfig.TABLE_MASCOTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbConfig.TABLE_MASCOTA_NAME + " TEXT, " +
                DbConfig.TABLE_MASCOTA_DESC + " TEXT, " +
                DbConfig.TABLE_MASCOTA_EMAIL + " TEXT " +
                //DbConfig.TABLE_MASCOTA_PIC  + " INTEGER " +
                ")";

        String queryCreateTablaPicsMascota = "CREATE TABLE IF NOT EXISTS " +
                DbConfig.TABLE_PIC + "(" +
                DbConfig.TABLE_PIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbConfig.TABLE_PIC_MASCOTA_ID + " INTEGER, " +
                DbConfig.TABLE_PIC_PIC + " INTEGER, " +
                "FOREIGN KEY (" + DbConfig.TABLE_PIC_MASCOTA_ID +
                ") REFERENCES " + DbConfig.TABLE_MASCOTA + " (" +
                DbConfig.TABLE_MASCOTA_ID + ")" +
                ")";

        String queryCreateTablaLikesMascota = "CREATE TABLE IF NOT EXISTS " +
                DbConfig.TABLE_LIKES + "(" +
                DbConfig.TABLE_LIKES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbConfig.TABLE_LIKES_MASCOTA_ID + " INTEGER, " +
                DbConfig.TABLE_LIKES_PIC_ID + " INTEGER, " +
                DbConfig.TABLE_LIKES_NUM_VOTES + " INTEGER, " +
                "FOREIGN KEY (" + DbConfig.TABLE_LIKES_MASCOTA_ID +
                ") REFERENCES " + DbConfig.TABLE_MASCOTA + " (" +
                DbConfig.TABLE_MASCOTA_ID + "), " +
                "FOREIGN KEY (" + DbConfig.TABLE_LIKES_PIC_ID +
                ") REFERENCES " + DbConfig.TABLE_PIC +
                ")";
        db.execSQL(queryCreateTablaMascota);
        db.execSQL(queryCreateTablaLikesMascota);
        db.execSQL(queryCreateTablaPicsMascota);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbConfig.TABLE_MASCOTA);
        db.execSQL("DROP TABLE IF EXISTS " +  DbConfig.TABLE_LIKES);
        db.execSQL("DROP TABLE IF EXISTS " +  DbConfig.TABLE_PIC);
        onCreate(db);
    }

    //ARGUMENTS SQL FOR FRAGMENT ONE
    public ArrayList<Constructor> obtenerTodasLasMascotas() {
        ArrayList<Constructor> mascotas = new ArrayList<>();
        String query = "SELECT * FROM " + DbConfig.TABLE_PIC +
                " LEFT JOIN " + DbConfig.TABLE_MASCOTA +
                " ON " + DbConfig.TABLE_PIC + "." + DbConfig.TABLE_PIC_MASCOTA_ID + " = " +
                DbConfig.TABLE_MASCOTA + "." + DbConfig.TABLE_MASCOTA_ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor registros = db.rawQuery(query, null);

        while(registros.moveToNext()) {
            Constructor mascotaActual = new Constructor();
            /*mascotaActual.setId(registros.getInt(0));
            mascotaActual.setId(registros.getInt(1));
            mascotaActual.setName(registros.getString(1));
            mascotaActual.setDesc(registros.getString(2));
            mascotaActual.setEmail(registros.getString(3));
            mascotaActual.setPic(registros.getInt(4));*/

            mascotaActual.setPicId(registros.getInt(0));
            mascotaActual.setId(registros.getInt(1));
            mascotaActual.setPic(registros.getInt(2));
            mascotaActual.setName(registros.getString(4));

            String  queryVotes = "SELECT COUNT (" +
                    DbConfig.TABLE_LIKES_NUM_VOTES + ") as votes" +
                    " FROM " +DbConfig.TABLE_LIKES +
                    " WHERE " + DbConfig.TABLE_LIKES_MASCOTA_ID + " = " + mascotaActual.getId() +
                    " AND " + DbConfig.TABLE_LIKES_PIC_ID + " = " + mascotaActual.getPicId();

            Cursor registrosVotes = db.rawQuery(queryVotes, null);
            if ( registrosVotes.moveToNext()) {
                mascotaActual.setVotes(registrosVotes.getInt(0));
            } else {
                mascotaActual.setVotes(0);
            }

            mascotas.add(mascotaActual);
        }

        db.close();
        return mascotas;
    }

    public void insertarMascota(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DbConfig.TABLE_MASCOTA, null, contentValues);
        db.close();
    }

    public void insertarPicsMascota(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DbConfig.TABLE_PIC, null, contentValues);
        db.close();
    }

    public void insertarVoteMascota(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DbConfig.TABLE_LIKES, null, contentValues);
        db.close();
    }

    public int obtenerVotesMascota(Constructor mascota) {
        int votes = 0;

        String query = "SELECT COUNT (" +
                DbConfig.TABLE_LIKES_NUM_VOTES + ")" +
                " FROM " + DbConfig.TABLE_LIKES +
                " WHERE " + DbConfig.TABLE_LIKES_MASCOTA_ID + " = " + mascota.getId() +
                " AND " + DbConfig.TABLE_LIKES_PIC_ID + " = " + mascota.getPicId();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor registros = db.rawQuery(query, null);

        while (registros.moveToNext()){
            votes = registros.getInt(0);
        }
        db.close();
        return votes;
    }

    //ARGUMENTS SQL FOR FRAGMENT TWO
        public ArrayList<Constructor> obtenerUnaSolaMascota() {
            ArrayList<Constructor> mascota = new ArrayList<>();
            String query = "SELECT * FROM " + DbConfig.TABLE_PIC +
                    " LEFT JOIN " + DbConfig.TABLE_MASCOTA +
                    " ON " + DbConfig.TABLE_PIC + "." + DbConfig.TABLE_PIC_MASCOTA_ID + " = " +
                    DbConfig.TABLE_MASCOTA + "." + DbConfig.TABLE_MASCOTA_ID +
                    " WHERE " + DbConfig.TABLE_PIC + "." + DbConfig.TABLE_PIC_MASCOTA_ID + " = " + 3;
            SQLiteDatabase dbUnaMascota = this.getWritableDatabase();
            Cursor registros = dbUnaMascota.rawQuery(query, null);

            while(registros.moveToNext()) {
                Constructor mascotaActual = new Constructor();
            /*mascotaActual.setId(registros.getInt(0));
            mascotaActual.setId(registros.getInt(1));
            mascotaActual.setName(registros.getString(1));
            mascotaActual.setDesc(registros.getString(2));
            mascotaActual.setEmail(registros.getString(3));
            mascotaActual.setPic(registros.getInt(4));*/

                mascotaActual.setPicId(registros.getInt(0));
                mascotaActual.setId(registros.getInt(1));
                mascotaActual.setPic(registros.getInt(2));
                mascotaActual.setName(registros.getString(4));

                String  queryVotes = "SELECT COUNT (" +
                        DbConfig.TABLE_LIKES_NUM_VOTES + ") as votes" +
                        " FROM " +DbConfig.TABLE_LIKES +
                        " WHERE " + DbConfig.TABLE_LIKES_MASCOTA_ID + " = " + mascotaActual.getId() +
                        " AND " + DbConfig.TABLE_LIKES_PIC_ID + " = " + mascotaActual.getPicId();

                Cursor registrosVotes = dbUnaMascota.rawQuery(queryVotes, null);
                if ( registrosVotes.moveToNext()) {
                    mascotaActual.setVotes(registrosVotes.getInt(0));
                } else {
                    mascotaActual.setVotes(0);
                }

                mascota.add(mascotaActual);
            }

            dbUnaMascota.close();
            return mascota;
        }

    public int obtenerVotesUnaMascota(Constructor mascota) {
        int votes = 0;

        String query = "SELECT COUNT (" +
                DbConfig.TABLE_LIKES_NUM_VOTES + ")" +
                " FROM " + DbConfig.TABLE_LIKES +
                " WHERE " + DbConfig.TABLE_LIKES_MASCOTA_ID + " = " + mascota.getId() +
                " AND " + DbConfig.TABLE_LIKES_PIC_ID + " = " + mascota.getPicId();

        SQLiteDatabase dbUnaMascota = this.getReadableDatabase();
        Cursor registros = dbUnaMascota.rawQuery(query, null);

        while (registros.moveToNext()){
            votes = registros.getInt(0);
        }
        dbUnaMascota.close();
        return votes;
    }


}
