package com.alvin.resepqu.Object;

public class ResepObject {

    public static final String TABLE_NAME = "resep";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JUDUL = "judul";
    public static final String COLUMN_DESKRIPSI = "deskripsi";
    public static final String COLUMN_CARA = "cara";
    public static final String COLUMN_IMG = "img";
    public static final String COLUMN_TIMESTAMP = "time";

    public static final String CREATE_TABLE =
            " CREATE TABLE " + TABLE_NAME + " ( "
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_JUDUL + " TEXT, "
                    + COLUMN_DESKRIPSI + " TEXT, "
                    + COLUMN_CARA + " TEXT, "
                    + COLUMN_IMG + " TEXT, "
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP "
                    + " ) ";

    public ResepObject(){

    }

    public ResepObject(int id, String judul, String deskripsi, String cara, String img, String time) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.cara = cara;
        this.img = img;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getCara() {
        return cara;
    }

    public void setCara(String cara) {
        this.cara = cara;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private int id;
    private String judul;
    private String deskripsi;
    private String cara;
    private String img;
    private String time;


}
