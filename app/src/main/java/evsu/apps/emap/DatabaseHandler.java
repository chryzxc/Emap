package evsu.apps.emap;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;


import android.content.ContentValues;

import android.content.Context;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHandler  extends SQLiteOpenHelper {



    public DatabaseHandler(Context applicationcontext) {

        super(applicationcontext, "emap.db", null, 1);

    }



    //Creates Table

    @Override

    public void onCreate(SQLiteDatabase database) {

        String queryB,queryR,queryU,queryF;

        queryB = "CREATE TABLE buildings ( id INTEGER, building TEXT, description TEXT, latitude REAL, longitude REAL, image TEXT)";
        queryR = "CREATE TABLE rooms ( id INTEGER, room TEXT , floor TEXT, building TEXT, image TEXT)";
   //     queryF = "CREATE TABLE floorplan ( building TEXT, floor TEXT, image TEXT)";
        queryU = "CREATE TABLE updates ( db INTEGER , app INTEGER)";
        String sql = "INSERT INTO updates(db) VALUES('0')";
        database.execSQL(queryB);
        database.execSQL(queryR);
     //   database.execSQL(queryF);
        database.execSQL(queryU);
        database.execSQL(sql);

    }

    @Override

    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {

        String queryB,queryR,queryU,queryF;

        queryB = "DROP TABLE IF EXISTS buildings";
        queryR = "DROP TABLE IF EXISTS rooms";
   //     queryF = "DROP TABLE IF EXISTS floorplan";
        queryU = "DROP TABLE IF EXISTS updates";

        database.execSQL(queryB);
        database.execSQL(queryR);
    //    database.execSQL(queryF);
        database.execSQL(queryU);

        onCreate(database);

    }


    public void delete(){
        SQLiteDatabase database = this.getWritableDatabase();
        String queryB,queryR,queryU,queryF;

        queryB = "DROP TABLE IF EXISTS buildings";
        queryR = "DROP TABLE IF EXISTS rooms";
    //    queryF = "DROP TABLE IF EXISTS floorplan";
        queryU = "DROP TABLE IF EXISTS updates";

        database.execSQL(queryB);
        database.execSQL(queryR);
    //    database.execSQL(queryF);
        database.execSQL(queryU);
        database.close();
    }

    public void create(){
        SQLiteDatabase database = this.getWritableDatabase();
        String queryB,queryR,queryU,queryF;

        queryB = "CREATE TABLE buildings ( id INTEGER, building TEXT,description TEXT, latitude REAL, longitude REAL, image BLOB )";
        queryR = "CREATE TABLE rooms ( id INTEGER, room TEXT , floor TEXT, building TEXT, image TEXT)";
       // queryF = "CREATE TABLE floorplan ( building TEXT, floor TEXT, image TEXT)";
        queryU = "CREATE TABLE updates ( db INTEGER , app INTEGER)";
        String sql = "INSERT INTO updates(db) VALUES('0')";
        database.execSQL(queryB);
        database.execSQL(queryR);
    //    database.execSQL(queryF);
        database.execSQL(queryU);
        database.execSQL(sql);
        database.close();
    }

    /**

         * Inserts User into SQLite DB

         * @param queryValues

         */






    public void insertBuilding(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", queryValues.get("id"));
        values.put("building", queryValues.get("building"));
        values.put("description", queryValues.get("description"));
        values.put("latitude", queryValues.get("latitude"));
        values.put("longitude", queryValues.get("longitude"));
        values.put("image", queryValues.get("image"));
        database.insert("buildings", null, values);
        database.close();

    }
    // SEARCH building

    public Double getbuildingLat(){
        SQLiteDatabase database = this.getWritableDatabase();
        Double Lat = null;
        Cursor cursor = database.rawQuery("SELECT latitude FROM buildings WHERE building="+Search.searchName, null);
        if(cursor.moveToNext()){
            Lat = cursor.getDouble(0);
        }
        cursor.close();
        database.close();
        return Lat;
    }

    public Double getbuildingLon(){
        SQLiteDatabase database = this.getWritableDatabase();
        Double Lon = null;
        Cursor cursor = database.rawQuery("SELECT longitude FROM buildings WHERE building="+Search.searchName, null);
        if(cursor.moveToNext()){
            Lon = cursor.getDouble(0);
        }
        cursor.close();
        database.close();
        return Lon;
    }

    public String getImage(){
        SQLiteDatabase database = this.getWritableDatabase();
        String img = null;
        Cursor cursor = database.rawQuery("SELECT image FROM buildings WHERE building="+Search.searchName, null);
        if(cursor.moveToNext()){
            img = cursor.getString(0);
        }
        database.close();
        return img;
    }

    public String getroomimage2(){
        SQLiteDatabase database = this.getWritableDatabase();
        String img = null;
        Cursor cursor = database.rawQuery("SELECT image FROM rooms WHERE id="+"'"+ListViewAdapter.roomId+"'"+" and floor="+"'"+ListViewAdapter.floor+"'", null);
        if(cursor.moveToNext()){
            img = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return img;
    }

    public String getroomimage1(){
        SQLiteDatabase database = this.getWritableDatabase();
        String img = null;
        Cursor cursor = database.rawQuery("SELECT image FROM rooms WHERE building="+Search.searchName+" and floor="+"'"+Room.floor+"'", null);
        if(cursor.moveToNext()){
            img = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return img;
    }


    // SEARCH rooms


    public List<String> getRoomId() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT id FROM rooms ORDER BY room ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    public List<String> getRoomNames() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT room FROM rooms ORDER BY room ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }




    public List<String> getFloor() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT floor FROM rooms ORDER BY room ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    public List<String> getBuilding() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT building FROM rooms ORDER BY Room ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }


    public Double getbuildinglat(){
        SQLiteDatabase database = this.getWritableDatabase();
        Double Lat = null;
        Cursor cursor = database.rawQuery("SELECT latitude FROM buildings WHERE building="+"'"+ListViewAdapter.building+"'", null);
        if(cursor.moveToNext()){
            Lat = cursor.getDouble(0);
        }
        cursor.close();
        database.close();
        return Lat;
    }
    public Double getbuildinglon(){
        SQLiteDatabase database = this.getWritableDatabase();
        Double Lon = null;
        Cursor cursor = database.rawQuery("SELECT longitude FROM buildings WHERE building="+"'"+ListViewAdapter.building+"'", null);
        if(cursor.moveToNext()){
            Lon = cursor.getDouble(0);
        }
        cursor.close();
        database.close();
        return Lon;
    }





    public String getimage(){
        SQLiteDatabase database = this.getWritableDatabase();
        String img = null;
        Cursor cursor = database.rawQuery("SELECT image FROM buildings WHERE building="+"'"+ListViewAdapter.building+"'", null);
        if(cursor.moveToNext()){
            img = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return img;
    }

    public String getimage1(){
        SQLiteDatabase database = this.getWritableDatabase();
        String img = null;
        Cursor cursor = database.rawQuery("SELECT image FROM floorplan WHERE building="+"'"+ListViewAdapter.building+"'"+" and floor="+"'"+ListViewAdapter.floor+"'", null);
        if(cursor.moveToNext()){
            img = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return img;
    }


    public String getClickImage(){
        SQLiteDatabase database = this.getWritableDatabase();
        String img = null;
        Cursor cursor = database.rawQuery("SELECT image FROM buildings WHERE building="+"'"+MainActivity.clickimageName+"'", null);
        if(cursor.moveToNext()){
            img = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return img;
    }


    public List<String> getbuildingRoom() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT room FROM rooms WHERE building="+Search.searchName+" ORDER BY room ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }






    public void insertRoom(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", queryValues.get("id"));
        values.put("building", queryValues.get("building"));
        values.put("room", queryValues.get("room"));
        values.put("floor", queryValues.get("floor"));
        values.put("image", queryValues.get("image"));
        database.insert("rooms", null, values);
        database.close();

    }



    public void updateDb(){
        SQLiteDatabase database = this.getWritableDatabase();
     //   ContentValues values = new ContentValues();
      //  values.put("db", queryValues.get("db"));
      //  database.update("updates",values,"db",null);
        String sql = "UPDATE updates SET db="+MainActivity.dbUpdate;
        database.execSQL(sql);
        database.close();
    }

    public Integer getUpdate() {
        SQLiteDatabase database = this.getWritableDatabase();
        Integer value = null;
        Cursor cursor = database.rawQuery("SELECT db FROM updates", null);
        if(cursor.moveToNext()){
            value = cursor.getInt(0);
        }
        database.close();
        return value;
    }

    public List<String> getBuildingNames() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT building FROM buildings ORDER BY building ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }


    public String getBuildingDescription() {
        SQLiteDatabase database = this.getWritableDatabase();
        String desc = null;
        Cursor cursor = database.rawQuery("SELECT description FROM buildings WHERE building="+Search.searchName, null);
        if(cursor.moveToNext()){
            desc = cursor.getString(0);
        }
        database.close();
        return desc;
    }


    //Display building

    public List<String> getbName() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT building FROM buildings ORDER BY building ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;

    }

    public List<Double> getbLat() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<Double> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT latitude FROM buildings ORDER BY building ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getDouble(0));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }


    public List<Double> getbLon() {
        SQLiteDatabase database = this.getWritableDatabase();
        List<Double> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT longitude FROM buildings ORDER BY building ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getDouble(0));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }


    public int getBuildingCount() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT building FROM buildings", null);
        int count = cursor.getCount();
        cursor.close();
        database.close();
        return count;
    }







    // DIRECTION


    public Double getLat(){
        SQLiteDatabase database = this.getWritableDatabase();
        Double lat = null;
        Cursor cursor = database.rawQuery("SELECT latitude FROM buildings WHERE building="+"'"+Direction.buildingName+"'", null);
        if(cursor.moveToNext()){
            lat = cursor.getDouble(0);
        }
        database.close();
        return lat;
    }

    public Double getLon(){
        SQLiteDatabase database = this.getWritableDatabase();
        Double lon = null;
        Cursor cursor = database.rawQuery("SELECT longitude FROM buildings WHERE building="+"'"+Direction.buildingName+"'", null);
        if(cursor.moveToNext()){
            lon = cursor.getDouble(0);
        }
        database.close();
        return lon;
    }






    /**

         * Get list of Users from SQLite DB as Array List

         * @return

         */

    public ArrayList<HashMap<String, String>> getAllBuildings() {

        ArrayList<HashMap<String, String>> buildingList;
        buildingList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM buildings";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("building", cursor.getString(1));
                buildingList.add(map);

            } while (cursor.moveToNext());

        }

        database.close();
        return buildingList;

    }




    public ArrayList<HashMap<String, String>> getAllRooms() {

        ArrayList<HashMap<String, String>> roomList;
        roomList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  id,room,floor FROM rooms WHERE building="+Search.searchName+" ORDER BY room ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("room", cursor.getString(1));
                map.put("floor", cursor.getString(2));

                roomList.add(map);

            } while (cursor.moveToNext());

        }

        database.close();
        return roomList;

    }



}

