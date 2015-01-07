package th.jack.jirawuts.miniapartment;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AptActivity extends ActionBarActivity {

    //Explicit
    private AptTABLE objAptTABLE;

    private EditText edtName;
    private EditText edtAddress;
    private EditText edtPhone;
    private EditText edtElecRate;
    private EditText edtWaterRate;
    private EditText edtElecMin;
    private EditText edtWaterMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apt);

        //bindWidget
        bindWidget();

        //loadApt
        loadApt();

    }

    //loadApt
    private void loadApt() {

        objAptTABLE = new AptTABLE(this);
        String[] arrayApt = objAptTABLE.searchApt(1);

        if (arrayApt != null) {
            edtName.setText(arrayApt[1]);
            edtAddress.setText(arrayApt[2]);
            edtPhone.setText(arrayApt[3]);
            edtElecRate.setText(arrayApt[4]);
            edtWaterRate.setText(arrayApt[5]);
            edtElecMin.setText(arrayApt[6]);
            edtWaterMin.setText(arrayApt[7]);
        }

    }//loadAPt

    public void saveApt(View view) {

        objAptTABLE = new AptTABLE(this);

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

        Double dblElecRate = Double.parseDouble(edtElecRate.getText().toString());
        Double dblWaterRate = Double.parseDouble(edtWaterRate.getText().toString());
        Double dblElecMin = Double.parseDouble(edtElecMin.getText().toString());
        Double dblWaterMin = Double.parseDouble(edtWaterMin.getText().toString());

        long updateApt = objAptTABLE.updateAPT(AptActivity.this, edtName.getText().toString().trim(),
                         edtAddress.getText().toString().trim(), edtPhone.getText().toString().trim(),
                         dblElecRate, dblWaterRate, dblElecMin, dblWaterMin);

        Toast.makeText(AptActivity.this, "บันทึกเรียบร้อย", Toast.LENGTH_SHORT).show();
    }

    //bindWidget
    private void bindWidget() {
        edtName = (EditText) findViewById(R.id.edtName);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtElecRate = (EditText) findViewById(R.id.edtElecRate);
        edtWaterRate = (EditText) findViewById(R.id.edtWaterRate);
        edtElecMin = (EditText) findViewById(R.id.edtElecMin);
        edtWaterMin = (EditText) findViewById(R.id.edtWaterMin);
    }//bindWidget


    @Override
    public void onBackPressed() {
        Intent objIntent = new Intent(AptActivity.this, MainActivity.class);
        startActivity(objIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apt, menu);
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
