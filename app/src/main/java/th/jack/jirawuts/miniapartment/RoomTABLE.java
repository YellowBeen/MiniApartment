package th.jack.jirawuts.miniapartment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Jirawut-Jack on 23/12/2014.
 */
public class RoomTABLE {

    //Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String TABLE_ROOM = "roomTABLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NO = "Room_no";
    public static final String COLUMN_FNAME = "Fname";
    public static final String COLUMN_LNAME = "Lname";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_START_DATE = "Start_Date";
    public static final String COLUMN_RENTAL = "Rental_amt";
    public static final String COLUMN_INTERNET = "Internet_amt";
    public static final String COLUMN_ELEC_RATE = "Elec_rate";
    public static final String COLUMN_WATER_RATE = "Water_rate";
    public static final String COLUMN_ELEC_MIN = "Elec_min";
    public static final String COLUMN_WATER_MIN = "Water_min";
    public static final String COLUMN_EMETER_START = "Elec_meter_start";
    public static final String COLUMN_WMETER_START = "Water_meter_start";
    public static final String COLUMN_INACTIVE = "Inactive";

    //Constructor
    public RoomTABLE(Context context) {
        objMyOpenHelper = new MyOpenHelper(context);
        writeSQLite = objMyOpenHelper.getWritableDatabase();
        readSQLite = objMyOpenHelper.getReadableDatabase();
    } //Constructor

    //addNewRoom
    public long addNewRoom(Context context, String strRoomNo, String strFname,
                           String strLname, String strPhone, String strStartDate,
                           Double dblRental, Double dblInternet, Double dblELecRate,
                           Double dblWaterRate, Double dblElecMin, Double dblWaterMin,
                           Double dblEmeterStart, Double dblWmeterSTart) {

        try {
            ContentValues objContentValues = new ContentValues();

            objContentValues.put(COLUMN_NO, strRoomNo);
            objContentValues.put(COLUMN_FNAME, strFname);
            objContentValues.put(COLUMN_LNAME, strLname);
            objContentValues.put(COLUMN_PHONE, strPhone);
            objContentValues.put(COLUMN_START_DATE, strStartDate);
            objContentValues.put(COLUMN_RENTAL, dblRental);
            objContentValues.put(COLUMN_INTERNET, dblInternet);
            objContentValues.put(COLUMN_ELEC_RATE, dblELecRate);
            objContentValues.put(COLUMN_WATER_RATE, dblWaterRate);
            objContentValues.put(COLUMN_ELEC_MIN, dblElecMin);
            objContentValues.put(COLUMN_WATER_MIN, dblWaterMin);
            objContentValues.put(COLUMN_EMETER_START, dblEmeterStart);
            objContentValues.put(COLUMN_WMETER_START, dblWmeterSTart);
            objContentValues.put(COLUMN_INACTIVE, 0);

            return writeSQLite.insertOrThrow(TABLE_ROOM, null, objContentValues);
        } catch (Exception e) {
            Log.d("error", "Error from add Room => " + e.toString());
            Long lNull = null;
            return lNull;
        }

    }//addNewRoom


    //deleteRoom
    public boolean deleteRoom(Context context, String strRoomNo) {

        try {
            writeSQLite.delete(TABLE_ROOM, COLUMN_NO + "=?",
                    new String[]{String.valueOf(strRoomNo)});
            return true;
        } catch (Exception e) {
            return false;
        }

    }//deleteRoom


    //updateRoom
    public long updateRoom(Context context, String strRoomNo, String strFname,
                           String strLname, String strPhone, String strStartDate,
                           Double dblRental, Double dblInternet, Double dblELecRate,
                           Double dblWaterRate, Double dblElecMin, Double dblWaterMin,
                           Double dblEmeterStart, Double dblWmeterStart) {

        Long lNull = null;

        if (this.deleteRoom(context, strRoomNo) == true) {
            return this.addNewRoom(context, strRoomNo, strFname, strLname, strPhone,
                    strStartDate, dblRental, dblInternet, dblELecRate,
                    dblWaterRate, dblElecMin, dblWaterMin, dblEmeterStart,
                    dblWmeterStart);
        } else {
            return lNull;
        }
    }//updateRoom


    //loadRoomList
    public String[] loadRoomList() {

        String arrayRoom[] = null;
        Integer arrayId = 0;

        try {
            Cursor objCursor = readSQLite.query(TABLE_ROOM, new String[]{COLUMN_NO}, null, null, null, null, null);

            if (objCursor != null) {
                arrayRoom = new String[objCursor.getCount()];
                while (objCursor.moveToNext()) {
                    arrayRoom[arrayId] = objCursor.getString(0);
                    arrayId = arrayId + 1;
                }
                return arrayRoom;
            } else {
                return arrayRoom;
            }
        } catch (Exception e) {
            Log.d("error", "Error from Load Room List => " + e.toString());
            return arrayRoom;
        }

    }//loadRoomList

    //loadRoomList
    public String[] loadRoomListRange(String strNoFrom, String strNoTo) {

        String arrayRoom[] = null;
        Integer arrayId = 0;

        try {
            Cursor objCursor = readSQLite.rawQuery("SELECT " + COLUMN_NO + " FROM " + TABLE_ROOM +
                               " WHERE " + COLUMN_NO + " BETWEEN " + strNoFrom + " AND " + strNoTo, null );

            if (objCursor != null) {
                arrayRoom = new String[objCursor.getCount()];
                while (objCursor.moveToNext()) {
                    arrayRoom[arrayId] = objCursor.getString(0);
                    arrayId = arrayId + 1;
                }
                return arrayRoom;
            } else {
                return arrayRoom;
            }
        } catch (Exception e) {
            Log.d("error", "Error from Load Room List Range => " + e.toString());
            return arrayRoom;
        }

    }//loadRoomList


    //searchRoom
    public String[] searchRoom(String strNo) {

        try {

            String arrayRoom[] = null; //ประกาศ Array

            Cursor objCursor = readSQLite.query(TABLE_ROOM,
                    new String[]{COLUMN_ID, COLUMN_NO, COLUMN_FNAME, COLUMN_LNAME, COLUMN_PHONE,
                            COLUMN_START_DATE, COLUMN_RENTAL, COLUMN_INTERNET, COLUMN_ELEC_RATE,
                            COLUMN_WATER_RATE, COLUMN_ELEC_MIN, COLUMN_WATER_MIN, COLUMN_EMETER_START, COLUMN_WMETER_START},
                    COLUMN_NO + "=?",
                    new String[]{String.valueOf(strNo)},
                    null, null, null, null);

            if (objCursor != null) {

                if (objCursor.moveToFirst()) {
                    arrayRoom = new String[objCursor.getColumnCount()];
                    arrayRoom[0] = objCursor.getString(0);
                    arrayRoom[1] = objCursor.getString(1);
                    arrayRoom[2] = objCursor.getString(2);
                    arrayRoom[3] = objCursor.getString(3);
                    arrayRoom[4] = objCursor.getString(4);
                    arrayRoom[5] = objCursor.getString(5);
                    arrayRoom[6] = objCursor.getString(6);
                    arrayRoom[7] = objCursor.getString(7);
                    arrayRoom[8] = objCursor.getString(8);
                    arrayRoom[9] = objCursor.getString(9);
                    arrayRoom[10] = objCursor.getString(10);
                    arrayRoom[11] = objCursor.getString(11);
                    arrayRoom[12] = objCursor.getString(12);
                    arrayRoom[13] = objCursor.getString(13);
                }

            }

            objCursor.close();
            return arrayRoom;

        } catch (Exception e) {
            Log.d("error", "Error from Search Room => " + e.toString());
            return null;
        }

    }//searchRoom


}//MainClass
