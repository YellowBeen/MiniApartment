package th.jack.jirawuts.miniapartment;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class RoomDetailActivity extends ActionBarActivity {

    //Explicit
    private RoomTABLE objRoomTABLE;

    private EditText edtNo;
    private EditText edtFname;
    private EditText edtLname;
    private EditText edtPhone;
    private EditText edtDate;
    private EditText edtRental;
    private EditText edtInternet;
    private EditText edtElecRate;
    private EditText edtWaterRate;
    private EditText edtElecMin;
    private EditText edtWaterMin;
    private EditText edtEmeterStart;
    private EditText edtWmeterStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        //bindWidget
        bindWidget();

        //loadRoomDetail
        loadRoomDetail();

    }


    //loadRoomDetail
    private void loadRoomDetail() {
        String strNo = getIntent().getExtras().getString("RoomNo");

        if (strNo != "XXX") {
            objRoomTABLE = new RoomTABLE(RoomDetailActivity.this);
            String arrayRoom[] = objRoomTABLE.searchRoom(strNo);

            if (arrayRoom != null) {
                edtNo.setText(arrayRoom[1]);
                edtNo.setEnabled(false);
                edtFname.setText(arrayRoom[2]);
                edtLname.setText(arrayRoom[3]);
                edtPhone.setText(arrayRoom[4]);
                edtDate.setText(arrayRoom[5]);
                edtRental.setText(arrayRoom[6]);
                edtInternet.setText(arrayRoom[7]);
                edtElecRate.setText(arrayRoom[8]);
                edtWaterRate.setText(arrayRoom[9]);
                edtElecMin.setText(arrayRoom[10]);
                edtWaterMin.setText(arrayRoom[11]);
                edtEmeterStart.setText(arrayRoom[12]);
                edtWmeterStart.setText(arrayRoom[13]);
            } else {
                edtNo.setEnabled(true);
            }
        } else {
            edtNo.setEnabled(true);
        }

    }//loadRoomDetail


    //bindWidget
    private void bindWidget() {
        edtNo = (EditText) findViewById(R.id.edtNo);
        edtFname = (EditText) findViewById(R.id.edtFname);
        edtLname = (EditText) findViewById(R.id.edtLname);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtDate = (EditText) findViewById(R.id.edtDate);
        edtRental = (EditText) findViewById(R.id.edtRental);
        edtInternet = (EditText) findViewById(R.id.edtInternet);
        edtElecRate = (EditText) findViewById(R.id.edtElecRate);
        edtWaterRate = (EditText) findViewById(R.id.edtWaterRate);
        edtElecMin = (EditText) findViewById(R.id.edtElecMin);
        edtWaterMin = (EditText) findViewById(R.id.edtWaterMin);
        edtEmeterStart = (EditText) findViewById(R.id.edtEmeterStart);
        edtWmeterStart = (EditText) findViewById(R.id.edtWmeterStart);
    }//bindWidget


    public void saveRoom(View view) {
        objRoomTABLE = new RoomTABLE(RoomDetailActivity.this);

        if (edtRental.getText().toString().equals("")) {
            edtRental.setText("0");
        }

        if (edtInternet.getText().toString().equals("")) {
            edtInternet.setText("0");
        }

        if (edtElecRate.getText().toString().equals("")) {
            edtElecRate.setText("0");
        }

        if (edtWaterRate.getText().toString().equals("")) {
            edtWaterRate.setText("0");
        }

        if (edtElecMin.getText().toString().equals("")) {
            edtElecMin.setText("0");
        }

        if (edtWaterMin.getText().toString().equals("")) {
            edtWaterMin.setText("0");
        }

        if (edtEmeterStart.getText().toString().equals("")) {
            edtEmeterStart.setText("0");
        }

        if (edtWmeterStart.getText().toString().equals("")) {
            edtWmeterStart.setText("0");
        }

        Double dblRental = Double.parseDouble(edtRental.getText().toString());
        Double dblInternet = Double.parseDouble(edtInternet.getText().toString());
        Double dblElecRate = Double.parseDouble(edtElecRate.getText().toString());
        Double dblWaterRate = Double.parseDouble(edtWaterRate.getText().toString());
        Double dblElecMin = Double.parseDouble(edtElecMin.getText().toString());
        Double dblWaterMin = Double.parseDouble(edtWaterMin.getText().toString());
        Double dblEmeterStart = Double.parseDouble(edtEmeterStart.getText().toString());
        Double dblWmeterStart = Double.parseDouble(edtWmeterStart.getText().toString());

        long updateRoom = objRoomTABLE.updateRoom(RoomDetailActivity.this, edtNo.getText().toString().trim(),
                edtFname.getText().toString().trim(), edtLname.getText().toString().trim(), edtPhone.getText().toString().trim(),
                edtDate.getText().toString().trim(), dblRental, dblInternet, dblElecRate, dblWaterRate,
                dblElecMin, dblWaterMin, dblEmeterStart, dblWmeterStart);

        Toast.makeText(RoomDetailActivity.this, "บันทึกเรียบร้อย", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent objIntent = new Intent(RoomDetailActivity.this, RoomActivity.class);
        startActivity(objIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_room_detail, menu);
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
}
