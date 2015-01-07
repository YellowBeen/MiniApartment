package th.jack.jirawuts.miniapartment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jirawut-Jack on 22/12/2014.
 */


public class MyOpenHelper extends SQLiteOpenHelper {

    //Explicit
    private static final String DATABASE_NAME = "MiniApartment.db";
    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_APT_TABLE = "create table aptTABLE (_id integer primary key, "+" Name text, Address text, Phone text, Elec_rate numeric, Water_rate numeric, Elec_min numeric, Water_min numeric);";
    private static final String CREATE_ROOM_TABLE = "create table roomTABLE (_id integer primary key, "+" Room_no text, Fname text, Lname text, Phone text, Start_date text, Rental_amt numeric, Internet_amt numeric, Elec_rate numeric, Water_rate numeric, Elec_min numeric, Water_min numeric, Elec_meter_start numeric, Water_meter_start numeric, Inactive integer);";
    private static final String CREATE_METER_TABLE = "create table meterTABLE (_id integer primary key, "+" Room_no text, Month integer, Year numeric, Elec_meter numeric, Water_meter numeric, Phone_amt numeric, Other_amt numeric);";
    private static final String CREATE_RENTAL_TABLE = "create table rentalTABLE (_id integer primary key, "+" Room_no text, Month integer, Year numeric, Elec_meter_start numeric, Elec_meter_end numeric, Elec_unit numeric,Elec_amt numeric, Water_meter_start numeric, Water_meter_end numeric, Water_unit numeric, Water_amt numeric, Rental_amt numeric, Internet_amt numeric, Phone_amt numeric, Other_amt numeric);";

    public MyOpenHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_APT_TABLE);
        db.execSQL(CREATE_ROOM_TABLE);
        db.execSQL(CREATE_METER_TABLE);
        db.execSQL(CREATE_RENTAL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "aptTABLE");
        db.execSQL("DROP TABLE IF EXISTS " + "roomTABLE");
        db.execSQL("DROP TABLE IF EXISTS " + "meterTABLE");
        db.execSQL("DROP TABLE IF EXISTS " + "rentalTABLE");
        onCreate(db);
    }
}
