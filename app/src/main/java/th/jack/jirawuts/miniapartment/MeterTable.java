package th.jack.jirawuts.miniapartment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ListView;

import java.util.concurrent.ExecutionException;

/**
 * Created by Jirawut-Jack on 26/12/2014.
 */
public class MeterTable {

    //Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String TABLE_METER = "meterTABLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NO = "Room_no";
    public static final String COLUMN_MONTH = "Month";
    public static final String COLUMN_YEAR = "Year";
    public static final String COLUMN_ELEC_METER = "Elec_meter";
    public static final String COLUMN_WATER_METER = "Water_meter";
    public static final String COLUMN_PHONE = "Phone_amt";
    public static final String COLUMN_OTH = "Other_amt";

    //Constructor
    public MeterTable(Context context) {
        objMyOpenHelper = new MyOpenHelper(context);
        writeSQLite = objMyOpenHelper.getWritableDatabase();
        readSQLite = objMyOpenHelper.getReadableDatabase();
    }//Constructor


    //addNewMeter
    public long addNewMeter(Context context, String strRoomNo, Integer intMonth,
                            Integer intYear, Double dblElecMeter, Double dblWaterMeter,
                            Double dblPhone, Double dblOther) {

        try {
            ContentValues objContentValues = new ContentValues();

            objContentValues.put(COLUMN_NO, strRoomNo);
            objContentValues.put(COLUMN_MONTH, intMonth);
            objContentValues.put(COLUMN_YEAR, intYear);
            objContentValues.put(COLUMN_ELEC_METER, dblElecMeter);
            objContentValues.put(COLUMN_WATER_METER, dblWaterMeter);
            objContentValues.put(COLUMN_PHONE, dblPhone);
            objContentValues.put(COLUMN_OTH, dblOther);
            return writeSQLite.insertOrThrow(TABLE_METER, null, objContentValues);
        } catch (Exception e) {
            Log.d("error", "Error from add Meter => " + e.toString());
            Long lNull = null;
            return lNull;
        }

    }//addNewMeter


    //deleteMeter
    public boolean deleteMeter(Context context, String strRoomNo,
                               Integer intMonth, Integer intYear) {

        try {
            writeSQLite.delete(TABLE_METER, COLUMN_NO + "=?" + " and " +
                            COLUMN_MONTH + "=?" + " and " + COLUMN_YEAR + "=?",
                    new String[]{String.valueOf(strRoomNo), String.valueOf(intMonth), String.valueOf(intYear)});
            return true;
        } catch (Exception e) {
            Log.d("error", "Error from delete Meter => " + e.toString());
            return false;
        }

    }//deleteMeter


    //updateMeter
    public long updateMeter(Context context, String strRoomNo, Integer intMonth,
                            Integer intYear, Double dblElecMeter, Double dblWaterMeter,
                            Double dblPhone, Double dblOther) {

        Long lNull = null;

        if (this.deleteMeter(context, strRoomNo, intMonth, intYear) == true) {
            return this.addNewMeter(context, strRoomNo, intMonth, intYear,
                    dblElecMeter, dblWaterMeter, dblPhone, dblOther);
        } else {
            return lNull;
        }

    }//updateMeter


    //searchMeter
    public String[] searchMeter(Context context, String strRoomNo,
                                Integer intMonth, Integer intYear) {

        String arrayMeter[] = null;

        try {

            Cursor objCursor = readSQLite.query(TABLE_METER,
                    new String[]{COLUMN_ID, COLUMN_NO, COLUMN_MONTH, COLUMN_YEAR, COLUMN_ELEC_METER,
                            COLUMN_WATER_METER, COLUMN_PHONE, COLUMN_OTH},
                    COLUMN_NO + "=?" + " and " + COLUMN_MONTH + "=?" + " and " + COLUMN_YEAR + "=?",
                    new String[]{String.valueOf(strRoomNo), String.valueOf(intMonth), String.valueOf(intYear)}
                    , null, null, null, null);

            if (objCursor != null) {

                if (objCursor.moveToFirst()) {
                    arrayMeter = new String[objCursor.getColumnCount()];
                    arrayMeter[0] = objCursor.getString(0);
                    arrayMeter[1] = objCursor.getString(1);
                    arrayMeter[2] = objCursor.getString(2);
                    arrayMeter[3] = objCursor.getString(3);
                    arrayMeter[4] = objCursor.getString(4);
                    arrayMeter[5] = objCursor.getString(5);
                    arrayMeter[6] = objCursor.getString(6);
                    arrayMeter[7] = objCursor.getString(7);
                }
            }

            objCursor.close();
            return arrayMeter;

        } catch (Exception e) {
            Log.d("error", "Error from Search Meter => " + e.toString());
            return null;
        }
    }//searchMeter


    //searchPreMeter
    public String[] searchPreMeter (Context context, String strRoomNo,
                                    Integer intMonth, Integer intYear) {

        String arrayMeter[] = null;
        Integer intMax = 24;

        Integer intTMonth = intMonth, intTYear = intYear;

        for (int intC = 0; intC < intMax; intC++) {

            if (intTMonth != 1) {
                intTMonth = intTMonth - 1;
            } else {
                intTMonth = 12;
                intTYear = intTYear - 1;
            }

            arrayMeter =this.searchMeter(context, strRoomNo, intTMonth, intTYear);

            if ( arrayMeter != null) {
                intC = intMax;
            }
        }

        return arrayMeter;
    }//searchPreMeter


}//MainClass
