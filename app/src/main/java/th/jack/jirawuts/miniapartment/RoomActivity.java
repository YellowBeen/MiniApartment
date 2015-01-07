package th.jack.jirawuts.miniapartment;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class RoomActivity extends ActionBarActivity {

    //Explicit
    private RoomTABLE objRoomTABLE;
    private String arrayRoom[] = null;
    private Integer intPosition;

    private ListView lsvRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        //bindWidget
        bindWidget();

        registerForContextMenu(lsvRoom);

        //loadRoom
        loadRoom();

    }

    //loadRoom
    private void loadRoom() {

        objRoomTABLE = new RoomTABLE(RoomActivity.this);

        arrayRoom = objRoomTABLE.loadRoomList();

        if ( arrayRoom != null ) {
            ArrayAdapter<String> roomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, arrayRoom);
            lsvRoom.setAdapter(roomAdapter);
            lsvRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String strNo = (String) ((TextView) view).getText();
                    Intent objIntent = new Intent(RoomActivity.this, RoomDetailActivity.class);
                    objIntent.putExtra("RoomNo",strNo);
                    startActivity(objIntent);
                    finish();
                }
            });

        }


    }//loadRoom

    //bindWidget
    private void bindWidget() {
        lsvRoom = (ListView) findViewById(R.id.lsvRoom);
    }//bindWidget

    //clickNewRoom
    public void clickNewRoom(View view) {
        Intent objIntent = new Intent(RoomActivity.this, RoomDetailActivity.class);
        objIntent.putExtra("RoomNo","XXX");
        startActivity(objIntent);
        finish();
    }//clickNewRoom

    @Override
         public void onBackPressed() {
        Intent objIntent = new Intent(RoomActivity.this, MainActivity.class);
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

    //*** LongPress menu ***
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        intPosition = info.position;

        menu.setHeaderTitle("ตัวเลือกจัดการห้องพัก");
        menu.add(1, 1, 1, "ห้องว่าง (ไม่คิดค่าเช่า)");
        menu.add(1, 2, 2, "ลบห้องพัก");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Toast.makeText(RoomActivity.this, "ห้องว่าง" + arrayRoom[intPosition], Toast.LENGTH_SHORT).show();
                return true;
            case 2:
                Toast.makeText(RoomActivity.this, "ลบห้องพัก" + arrayRoom[intPosition], Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }

    }



}//MainClass
