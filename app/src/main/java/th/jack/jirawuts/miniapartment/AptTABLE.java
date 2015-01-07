package th.jack.jirawuts.miniapartment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Jirawut-Jack on 22/12/2014.
 */

public class AptTABLE {

    //Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSqLite, readSqLite;

    public static final String TABLE_APT = "aptTABLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_ELEC_RATE = "Elec_rate";
    public static final String COLUMN_WATER_RATE = "Water_rate";
    public static final String COLUMN_ELEC_MIN = "Elec_min";
    public static final String COLUMN_WATER_MIN = "Water_min";

    //Constructor
    public AptTABLE(Context context) {
        objMyOpenHelper = new MyOpenHelper(context);
        writeSqLite = objMyOpenHelper.getWritableDatabase();
        readSqLite = objMyOpenHelper.getWritableDatabase();
    }//Constructor


    //AddNewAPT
    public long addNewAPT(Context context, String strName,
                          String strAddress, String strPhone,
                          Double dblElecRate, Double dblWaterRate,
                          Double dblElecMin, Double dblWaterMin) {

        ContentValues objContentValues = new ContentValues();

        try {
            objContentValues.put(COLUMN_NAME, strName);
            objContentValues.put(COLUMN_ADDRESS, strAddress);
            objContentValues.put(COLUMN_PHONE, strPhone);
            objContentValues.put(COLUMN_ELEC_RATE, dblElecRate);
            objContentValues.put(COLUMN_WATER_RATE, dblWaterRate);
            objContentValues.put(COLUMN_ELEC_MIN, dblElecMin);
            objContentValues.put(COLUMN_WATER_MIN, dblWaterMin);

            return writeSqLite.insertOrThrow(TABLE_APT, null, objContentValues);

        } catch (Exception e) {
            Log.d("error", "Error from add APT => " + e.toString());
            Long lNull = null;
            return lNull;
        }

    } //AddNewAPT


    //deleteAPT
    public boolean deleteAPT() {
        try {
            writeSqLite.delete(TABLE_APT, null, null);
            return true;
        } catch (Exception e) {
            Log.d("error", "Error from delete APT => " + e.toString());
            return false;
        }

    }//deleteAPT


    //updateAPT
    public long updateAPT(Context context, String strName,
                          String strAddress, String strPhone,
                          Double dblElecRate, Double dblWaterRate,
                          Double dblElecMin, Double dblWaterMin) {

        Long lNull = null;

        try {

            if (this.deleteAPT() == true) {
                return this.addNewAPT(context, strName, strAddress, strPhone,
                        dblElecRate, dblWaterRate, dblElecMin, dblWaterMin);
            } else {
                return lNull;
            }

        } catch (Exception e) {
            Log.d("error", "Error from update APT => " + e.toString());
            return lNull;
        }

    }//updateAPT

    //searchApt
    public String[] searchApt(Integer intId) {

        String arrayApt[] = null;

        try {

            Cursor objCursor = readSqLite.query(TABLE_APT,
                    new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_ADDRESS, COLUMN_PHONE, COLUMN_ELEC_RATE, COLUMN_WATER_RATE, COLUMN_ELEC_MIN, COLUMN_WATER_MIN},
                    COLUMN_ID + "=?",
                    new String[] {String.valueOf(intId)},
                    null,null,null,null );

            if ( objCursor != null ) {

                if (objCursor.moveToFirst()) {
                    arrayApt = new String[objCursor.getColumnCount()];
                    arrayApt[0] = objCursor.getString(0);
                    arrayApt[1] = objCursor.getString(1);
                    arrayApt[2] = objCursor.getString(2);
                    arrayApt[3] = objCursor.getString(3);
                    arrayApt[4] = objCursor.getString(4);
                    arrayApt[5] = objCursor.getString(5);
                    arrayApt[6] = objCursor.getString(6);
                    arrayApt[7] = objCursor.getString(7);
                }

                objCursor.close();
                return arrayApt;
            }

        } catch (Exception e) {
            return null;
        }

        return arrayApt;

    }//searchApt


} //Main Class
