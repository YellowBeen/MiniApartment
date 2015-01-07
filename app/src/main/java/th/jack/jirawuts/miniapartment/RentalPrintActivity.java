package th.jack.jirawuts.miniapartment;

import android.app.AlertDialog;
import android.content.Intent;
import android.print.PrintManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import java.util.Calendar;
import android.content.Context;
import android.widget.Toast;

public class RentalPrintActivity extends ActionBarActivity {

    //Explicit
    private RoomTABLE objRoomTABLE;
    private MeterTable objMeterTABLE;
    private RentalTABLE objRentalTABLE;
    private AptTABLE objAptTABLE;

    private Spinner spnMonth, spnYear, spnRoomFrom, spnRoomTo;
    private Integer intMonth, intYear;
    private String strMonth, strRoomFrom, strRoomTo;
    private CheckBox chbAllRoom;

    private Double dblEMeterStart, dblEMeterEnd, dblEMeterUnit, dblElecAmt;
    private Double dblWMeterStart, dblWMeterEnd, dblWMeterUnit, dblWaterAmt;
    private Double dblElecRate, dblElecMin, dblWaterRate, dblWaterMin;
    private Double dblRental, dblInternet, dblPhone, dblOther;

    private String[] arrayRoom = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_print);

        //bindWidget
        bindWidget();

        //setupDateSpinner
        setupDateSpinner();

        //setupRoomSpinner
        setupRoomSpinner();
    }

    public void printRental(View view) {
        calRental();
        printDocument();
        Toast.makeText(RentalPrintActivity.this, "สั่งพิมพ์เรียบร้อย", Toast.LENGTH_SHORT).show();
    }

    public void printDocument() {
        PrintModule objPrintModule = new PrintModule(RentalPrintActivity.this,arrayRoom,intMonth,strMonth,intYear);

        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) + " Document";

        printManager.print(jobName, objPrintModule, null);
    }


    private void calRental() {
        objRoomTABLE = new RoomTABLE(RentalPrintActivity.this);
        objMeterTABLE = new MeterTable(RentalPrintActivity.this);
        objRentalTABLE = new RentalTABLE(RentalPrintActivity.this);
        objAptTABLE = new AptTABLE(RentalPrintActivity.this);

        String[] arrayRoomDetail = null;
        String[] arrayMeter = null;
        String[] arrayPreMeter = null;
        String[] arrayApt = null;

        if (chbAllRoom.isChecked()) {
            arrayRoom = objRoomTABLE.loadRoomList();
        } else {
            arrayRoom = objRoomTABLE.loadRoomListRange(strRoomFrom,strRoomTo);
        }

        if (arrayRoom != null) {

            for (int intI = 0; intI < arrayRoom.length; intI = intI + 1) {

                arrayApt = objAptTABLE.searchApt(1);
                arrayRoomDetail = objRoomTABLE.searchRoom(arrayRoom[intI]);
                arrayMeter = objMeterTABLE.searchMeter(RentalPrintActivity.this, arrayRoom[intI], intMonth, intYear);
                arrayPreMeter = objMeterTABLE.searchPreMeter(RentalPrintActivity.this, arrayRoom[intI], intMonth, intYear);

                dblRental = Double.parseDouble(arrayRoomDetail[6]);
                dblInternet = Double.parseDouble(arrayRoomDetail[7]);

                dblElecRate = Double.parseDouble(arrayRoomDetail[8]);
                if (dblElecRate == 0) {
                    dblElecRate = Double.parseDouble(arrayApt[4]);
                }

                dblWaterRate = Double.parseDouble(arrayRoomDetail[9]);
                if (dblWaterRate == 0) {
                    dblWaterRate = Double.parseDouble(arrayApt[5]);
                }

                dblElecMin = Double.parseDouble(arrayRoomDetail[10]);
                if (dblElecMin == 0) {
                    dblElecMin = Double.parseDouble(arrayApt[6]);
                }

                dblWaterMin = Double.parseDouble(arrayRoomDetail[11]);
                if (dblWaterMin == 0) {
                    dblWaterMin = Double.parseDouble(arrayApt[7]);
                }

                if (arrayPreMeter == null) {
                    dblEMeterStart = Double.parseDouble(arrayRoomDetail[12]);
                    dblWMeterStart = Double.parseDouble(arrayRoomDetail[13]);
                } else {
                    dblEMeterStart = Double.parseDouble(arrayPreMeter[4]);
                    dblWMeterStart = Double.parseDouble(arrayPreMeter[5]);
                }

                dblEMeterEnd = Double.parseDouble(arrayMeter[4]);
                dblWMeterEnd = Double.parseDouble(arrayMeter[5]);
                dblPhone = Double.parseDouble(arrayMeter[6]);
                dblOther = Double.parseDouble(arrayMeter[7]);

                dblEMeterUnit = dblEMeterEnd - dblEMeterStart;
                dblWMeterUnit = dblWMeterEnd - dblWMeterStart;

                dblElecAmt = dblEMeterUnit * dblElecRate;
                dblWaterAmt = dblWMeterUnit * dblWaterRate;

                if (dblElecAmt < dblElecMin) {
                    dblElecAmt = dblElecMin;
                }

                if (dblWaterAmt < dblWaterMin) {
                    dblWaterAmt = dblWaterMin;
                }

                long updateRental = objRentalTABLE.updateRental(RentalPrintActivity.this, arrayRoomDetail[1], intMonth, intYear,
                        dblEMeterStart, dblEMeterEnd, dblEMeterUnit, dblElecAmt,
                        dblWMeterStart, dblWMeterEnd, dblWMeterUnit, dblWaterAmt,
                        dblRental, dblInternet, dblPhone, dblOther);

            }

        }

    }

    public void onChbClick(View view) {

        if (chbAllRoom.isChecked()) {
            spnRoomFrom.setEnabled(false);
            spnRoomTo.setEnabled(false);
        } else {
            spnRoomFrom.setEnabled(true);
            spnRoomTo.setEnabled(true);
        }

    }

    //bindWidget
    private void bindWidget() {
        spnMonth = (Spinner) findViewById(R.id.spnMonth);
        spnYear = (Spinner) findViewById(R.id.spnYear);
        spnRoomFrom = (Spinner) findViewById(R.id.spnRoomFrom);
        spnRoomTo = (Spinner) findViewById(R.id.spnRoomTo);
        chbAllRoom = (CheckBox) findViewById(R.id.chbAllRoom);
    }//bindWidget

    //setupRoomSpinner
    private void setupRoomSpinner() {

        objRoomTABLE = new RoomTABLE(RentalPrintActivity.this);
        final String[] arrayRoom = objRoomTABLE.loadRoomList();

        if (arrayRoom != null) {
            ArrayAdapter<String> roomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayRoom);
            spnRoomFrom.setAdapter(roomAdapter);

            spnRoomFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    strRoomFrom = arrayRoom[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spnRoomTo.setAdapter(roomAdapter);

            spnRoomTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    strRoomTo = arrayRoom[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }//setupRoomSpinner

    //setupDateSpinner
    private void setupDateSpinner() {

        final Calendar cal = Calendar.getInstance();
        intMonth = cal.get(Calendar.MONTH) + 1;
        intYear = cal.get(Calendar.YEAR);

        //Month Spinner
        final String strMonthName[] = getResources().getStringArray(R.array.name);
        final String strMonthNo[] = getResources().getStringArray(R.array.number);

        strMonth = strMonthName[cal.get(Calendar.MONTH)];

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strMonthName);
        spnMonth.setAdapter(monthAdapter);

        spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intMonth = Integer.parseInt(strMonthNo[position]);
                strMonth = strMonthName[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Year Spinner
        final Integer arrayYear[] = new Integer[5];
        Integer intCYear = intYear;
        intCYear = intCYear - 2;

        for (int intY = 0; intY < 5; intY = intY + 1) {
            arrayYear[intY] = intCYear;
            intCYear = intCYear + 1;
        }

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, arrayYear);
        spnYear.setAdapter(yearAdapter);

        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intYear = arrayYear[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Set Default value
        Integer intMonthPost = monthAdapter.getPosition(strMonth);
        spnMonth.setSelection(intMonthPost);

        Integer intYearPost = yearAdapter.getPosition(intYear);
        spnYear.setSelection(intYearPost);
    }//setupDateSpinner

    @Override
    public void onBackPressed() {
        Intent objIntent = new Intent(RentalPrintActivity.this, MainActivity.class);
        startActivity(objIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rental_print, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}//MainClass
