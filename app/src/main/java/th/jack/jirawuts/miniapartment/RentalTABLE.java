package th.jack.jirawuts.miniapartment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Jirawut-Jack on 28/12/2014.
 */
public class RentalTABLE {

    //Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String TABLE_RENTAL = "rentalTABLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NO = "Room_no";
    public static final String COLUMN_MONTH = "Month";
    public static final String COLUMN_YEAR = "Year";
    public static final String COLUMN_EMETER_START = "Elec_meter_start";
    public static final String COLUMN_EMETER_END = "Elec_meter_end";
    public static final String COLUMN_ELEC_UNIT = "Elec_unit";
    public static final String COLUMN_ELEC_AMT = "Elec_amt";
    public static final String COLUMN_WMETER_START = "Water_meter_start";
    public static final String COLUMN_WMETER_END = "Water_meter_end";
    public static final String COLUMN_WATER_UNIT = "Water_unit";
    public static final String COLUMN_WATER_AMT = "Water_amt";
    public static final String COLUMN_RENTAL = "Rental_amt";
    public static final String COLUMN_INTERNET = "Internet_amt";
    public static final String COLUMN_PHONE = "Phone_amt";
    public static final String COLUMN_OTH = "Other_amt";

    //Constructor
    public RentalTABLE(Context context) {
        objMyOpenHelper = new MyOpenHelper(context);
        writeSQLite = objMyOpenHelper.getWritableDatabase();
        readSQLite = objMyOpenHelper.getReadableDatabase();
    }//Constructor

    //addNewRental
    public long addNewRental(Context context, String strRoomNo, Integer intMonth, Integer intYear,
                            Double dblEMeterStart, Double dblEMeterEnd, Double dblElecUnit, Double dblElecAmt,
                            Double dblWMeterStart, Double dblWMeterEnd, Double dblWaterUnit, Double dblWaterAmt,
                            Double dblRental, Double dblInternet, Double dblPhone, Double dblOther) {

        try {
            ContentValues objContentValues = new ContentValues();

            objContentValues.put(COLUMN_NO, strRoomNo);
            objContentValues.put(COLUMN_MONTH, intMonth);
            objContentValues.put(COLUMN_YEAR, intYear);
            objContentValues.put(COLUMN_EMETER_START, dblEMeterStart);
            objContentValues.put(COLUMN_EMETER_END, dblEMeterEnd);
            objContentValues.put(COLUMN_ELEC_UNIT, dblElecUnit);
            objContentValues.put(COLUMN_ELEC_AMT, dblElecAmt);
            objContentValues.put(COLUMN_WMETER_START, dblWMeterStart);
            objContentValues.put(COLUMN_WMETER_END, dblWMeterEnd);
            objContentValues.put(COLUMN_WATER_UNIT, dblWaterUnit);
            objContentValues.put(COLUMN_WATER_AMT, dblWaterAmt);
            objContentValues.put(COLUMN_RENTAL, dblRental);
            objContentValues.put(COLUMN_INTERNET, dblInternet);
            objContentValues.put(COLUMN_PHONE, dblPhone);
            objContentValues.put(COLUMN_OTH, dblOther);

            objContentValues.put(COLUMN_PHONE, dblPhone);
            return writeSQLite.insertOrThrow(TABLE_RENTAL, null, objContentValues);
        } catch (Exception e) {
            Log.d("error", "Error from add Rental => " + e.toString());
            Long lNull = null;
            return lNull;
        }

    }//addNewRental


    //deleteRental
    public boolean deleteRental(Context context, String strRoomNo,
                               Integer intMonth, Integer intYear) {

        try {
            writeSQLite.delete(TABLE_RENTAL, COLUMN_NO + "=?" + " and " +
                            COLUMN_MONTH + "=?" + " and " + COLUMN_YEAR + "=?",
                    new String[]{String.valueOf(strRoomNo), String.valueOf(intMonth), String.valueOf(intYear)});
            return true;
        } catch (Exception e) {
            Log.d("error", "Error from delete Rental => " + e.toString());
            return false;
        }

    }//deleteRental


    //updateRental
    public long updateRental(Context context, String strRoomNo, Integer intMonth, Integer intYear,
                             Double dblEMeterStart, Double dblEMeterEnd, Double dblElecUnit, Double dblElecAmt,
                             Double dblWMeterStart, Double dblWMeterEnd, Double dblWaterUnit, Double dblWaterAmt,
                             Double dblRental, Double dblInternet, Double dblPhone, Double dblOther) {

        Long lNull = null;

        if (this.deleteRental(context, strRoomNo, intMonth, intYear) == true) {
            return this.addNewRental(context, strRoomNo, intMonth, intYear,
                    dblEMeterStart, dblEMeterEnd, dblElecUnit, dblElecAmt,
                    dblWMeterStart, dblWMeterEnd, dblWaterUnit, dblWaterAmt,
                    dblRental, dblInternet, dblPhone, dblOther);
        } else {
            return lNull;
        }

    }//updateRental


    //searchRental
    public String[] searchRental(Context context, String strRoomNo,
                                Integer intMonth, Integer intYear) {

        String arrayRental[] = null;

        try {

            Cursor objCursor = readSQLite.query(TABLE_RENTAL,
                    new String[]{COLUMN_ID, COLUMN_NO, COLUMN_MONTH, COLUMN_YEAR,
                            COLUMN_EMETER_START, COLUMN_EMETER_END, COLUMN_ELEC_UNIT, COLUMN_ELEC_AMT,
                            COLUMN_WMETER_START, COLUMN_WMETER_END, COLUMN_WATER_UNIT, COLUMN_WATER_AMT,
                            COLUMN_RENTAL, COLUMN_INTERNET, COLUMN_PHONE, COLUMN_OTH},
                    COLUMN_NO + "=?" + " and " + COLUMN_MONTH + "=?" + " and " + COLUMN_YEAR + "=?",
                    new String[]{String.valueOf(strRoomNo), String.valueOf(intMonth), String.valueOf(intYear)}
                    , null, null, null, null);

            if (objCursor != null) {
                if (objCursor.moveToFirst()) {
                    arrayRental = new String[objCursor.getColumnCount()];
                    arrayRental[0] = objCursor.getString(0);
                    arrayRental[1] = objCursor.getString(1);
                    arrayRental[2] = objCursor.getString(2);
                    arrayRental[3] = objCursor.getString(3);
                    arrayRental[4] = objCursor.getString(4);
                    arrayRental[5] = objCursor.getString(5);
                    arrayRental[6] = objCursor.getString(6);
                    arrayRental[7] = objCursor.getString(7);
                    arrayRental[8] = objCursor.getString(8);
                    arrayRental[9] = objCursor.getString(9);
                    arrayRental[10] = objCursor.getString(10);
                    arrayRental[11] = objCursor.getString(11);
                    arrayRental[12] = objCursor.getString(12);
                    arrayRental[13] = objCursor.getString(13);
                    arrayRental[14] = objCursor.getString(14);
                    arrayRental[15] = objCursor.getString(15);
                }
            }

            objCursor.close();
            return arrayRental;

        } catch (Exception e) {
            Log.d("error", "Error from Search Meter => " + e.toString());
            return null;
        }
    }//searchMeter


} //MainClass
