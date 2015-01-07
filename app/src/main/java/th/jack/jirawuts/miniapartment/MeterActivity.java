package th.jack.jirawuts.miniapartment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import java.util.Calendar;

public class MeterActivity extends ActionBarActivity {

    //Explicit
    private RoomTABLE objRoomTABLE;
    private MeterTable objMeterTABLE;

    private ListView lsvRoom;
    private Spinner spnMonth, spnYear;
    private Integer intMonth, intYear;
    private String strMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);

        //bindWidget
        bindWidget();

        //setupSpinner
        setupSpinner();

        //loadRoom
        loadRoom();
    }

    private void setupSpinner() {

        final Calendar cal = Calendar.getInstance();
        intMonth = cal.get(Calendar.MONTH) + 1;
        intYear = cal.get(Calendar.YEAR);

        //Month Spinner
        String strMonthName[] = getResources().getStringArray(R.array.name);
        final String strMonthNo[] = getResources().getStringArray(R.array.number);


        strMonth = strMonthName[cal.get(Calendar.MONTH)];

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strMonthName);
        spnMonth.setAdapter(monthAdapter);

        spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intMonth = Integer.parseInt(strMonthNo[position]);
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
    }


    //loadRoom
    private void loadRoom() {
        String arrayRoom[] = null;
        objRoomTABLE = new RoomTABLE(MeterActivity.this);
        arrayRoom = objRoomTABLE.loadRoomList();

        if ( arrayRoom != null ) {
            ArrayAdapter<String> roomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, arrayRoom);
            lsvRoom.setAdapter(roomAdapter);

            lsvRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String strRoomNo = (String) ((TextView) view).getText();

                    showMeterDialog(strRoomNo, intMonth, intYear );

                }
            });
        }
    }//loadRoom


    //showMeterDialog
    private void showMeterDialog(final String strRoomNo, final Integer intMonth, final Integer intYear) {

        LayoutInflater layoutInflater = LayoutInflater.from(MeterActivity.this);
        View promptView = layoutInflater.inflate(R.layout.activity_meter_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MeterActivity.this);
        alertDialogBuilder.setView(promptView);

        final TextView txtEMeterStart = (TextView) promptView.findViewById(R.id.txtEMeterStart);
        final TextView txtWMeterStart = (TextView) promptView.findViewById(R.id.txtWMeterStart);
        final EditText edtElecMeter = (EditText) promptView.findViewById(R.id.edtElecMeter);
        final EditText edtWaterMeter = (EditText) promptView.findViewById(R.id.edtWaterMeter);
        final EditText edtPhone = (EditText) promptView.findViewById(R.id.edtPhone);
        final EditText edtOther = (EditText) promptView.findViewById(R.id.edtOther);

        objMeterTABLE = new MeterTable(MeterActivity.this);
        String arrayMeter[] = objMeterTABLE.searchMeter(MeterActivity.this,strRoomNo,intMonth, intYear);
        String arrayPreMeter[] = objMeterTABLE.searchPreMeter(MeterActivity.this, strRoomNo, intMonth, intYear);

        if ( arrayMeter != null ) {
            edtElecMeter.setText(arrayMeter[4]);
            edtWaterMeter.setText(arrayMeter[5]);
            edtPhone.setText(arrayMeter[6]);
            edtOther.setText(arrayMeter[7]);
        }

        if (arrayPreMeter != null) {
            String strText = "(ยอดก่อนหน้า = ";
            txtEMeterStart.setText(strText + arrayPreMeter[4] + ")");
            txtWMeterStart.setText(strText + arrayPreMeter[5] + ")");
        } else {
            objRoomTABLE = new RoomTABLE(MeterActivity.this);
            String arrayRoom[] = objRoomTABLE.searchRoom(strRoomNo);
            String strText = "(ยอดก่อนหน้า = ";
            txtEMeterStart.setText(strText + arrayRoom[12] + ")");
            txtWMeterStart.setText(strText + arrayRoom[13] + ")");
        }

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)

                .setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (edtElecMeter.getText().toString().equals("")) {
                            edtElecMeter.setText("0");
                        }

                        if (edtWaterMeter.getText().toString().equals("")) {
                            edtWaterMeter.setText("0");
                        }

                        if (edtPhone.getText().toString().equals("")) {
                            edtPhone.setText("0");
                        }

                        if (edtOther.getText().toString().equals("")) {
                            edtOther.setText("0");
                        }

                        Double dblElecMeter = Double.parseDouble(edtElecMeter.getText().toString());
                        Double dblWaterMeter = Double.parseDouble(edtWaterMeter.getText().toString());
                        Double dblPhone = Double.parseDouble(edtPhone.getText().toString());
                        Double dblOther = Double.parseDouble(edtOther.getText().toString());

                        long updateMeter = objMeterTABLE.updateMeter(MeterActivity.this, strRoomNo, intMonth, intYear,
                                dblElecMeter, dblWaterMeter, dblPhone, dblOther);

                        Toast.makeText(MeterActivity.this, "บันทึกเรียบร้อย", Toast.LENGTH_SHORT).show();

                    }
                })

                .setNegativeButton("ยกเลิก",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }//showMeterDialog

    //bindWidget
    private void bindWidget() {
        lsvRoom = (ListView) findViewById(R.id.lsvRoom);
        spnMonth = (Spinner) findViewById(R.id.spnMonth);
        spnYear = (Spinner) findViewById(R.id.spnYear);
    }//bindWidget


    @Override
    public void onBackPressed() {
        Intent objIntent = new Intent(MeterActivity.this, MainActivity.class);
        startActivity(objIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_room, menu);
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
