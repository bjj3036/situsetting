package kr.hs.dgsw.situsetting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SettingDBHelper extends SQLiteOpenHelper {

    private String table = "setting";

    public SettingDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder b = new StringBuilder();
        b.append("create table ");
        b.append(table);
        b.append("(id integer, ");
        b.append("ringVolume integer,");
        b.append("musicVolume integer,");
        b.append("brightness integer,");
        b.append("ringerMode integer)");
        db.execSQL(b.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table " + table;
        db.execSQL(sql);
        onCreate(db);
    }

    public void setting(SettingSituation situation, SettingBean setting) {
        if (!isExists(situation.getId()))
            insert(situation.getId(), setting);
        else
            update(situation.getId(), setting);
    }

    public long insert(String id, SettingBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", Integer.valueOf(id));
        values.put("ringVolume", bean.getRingVolume());
        values.put("musicVolume", bean.getMusicVolume());
        values.put("brightness", bean.getBrightness());
        values.put("ringerMode", bean.getRingerMode());
        return db.insert(table, null, values);
    }

    @Nullable
    public SettingBean select(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(table, null, "id=?",
                new String[]{id}, null, null, null);
        if (!c.moveToNext())
            return null;
        SettingBean bean = new SettingBean();
        bean.setMusicVolume(c.getInt(c.getColumnIndex("musicVolume")));
        bean.setRingVolume(c.getInt(c.getColumnIndex("ringVolume")));
        bean.setBrightness(c.getInt(c.getColumnIndex("brightness")));
        bean.setRingerMode(c.getInt(c.getColumnIndex("ringerMode")));
        return bean;
    }

    public boolean isExists(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(table, null, "id=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (!c.moveToNext())
            return false;
        return true;
    }

    public int update(String id, SettingBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ringVolume", bean.getRingVolume());
        values.put("musicVolume", bean.getMusicVolume());
        values.put("brightness", bean.getBrightness());
        values.put("ringerMode", bean.getRingerMode());
        return db.update(table, values, "id=?", new String[]{id});
    }
}
