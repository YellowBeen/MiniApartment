package th.jack.jirawuts.miniapartment;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.TaskStackBuilder;
import android.app.PendingIntent;


public class MainActivity extends ActionBarActivity {

    //Explicit
    private AptTABLE objAptTABLE;
    private RoomTABLE objRoomTABLE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create & Connect Database
        objAptTABLE = new AptTABLE(this);
        objRoomTABLE = new RoomTABLE(this);
    }

    //clickMeter
    public void clickMeter(View view) {
        Intent objIntent = new Intent(MainActivity.this, MeterActivity.class);
        startActivity(objIntent);
        finish();
    }//clickMeter

    //clickPrint
    public void clickPrint(View view) {
        Intent objIntent = new Intent(MainActivity.this, RentalPrintActivity.class);
        startActivity(objIntent);
        finish();
    }//clickPrint

    //clickApt
    public void clickApt(View view) {
        Intent objIntent = new Intent(MainActivity.this, AptActivity.class);
        startActivity(objIntent);
        finish();
    }

    //clickRoom
    public void clickRoom(View view) {
        Intent objIntent = new Intent(MainActivity.this, RoomActivity.class);
        startActivity(objIntent);
        finish();
    }//clickRoom


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
