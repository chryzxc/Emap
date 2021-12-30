package evsu.apps.emap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Search extends AppCompatActivity implements View.OnClickListener {
    static Integer code= 0;
    static String searchName,itemValue;
    static String searchNameTab = null,searchRoom,searchName2,searchBuilding,searchImage;
    static Double searchLat,searchLong,searchTilt,searchBearing,searchZoom;
    private EditText roomSearch,buildingSearch;
    private ListView buildingList,roomList;
    private Button buildingButton,roomButton;
    static ImageButton backSButton;
    ArrayAdapter<String> adapterB,adapterR;
    static List<String> buildingRooms;
    static Integer popup;
    SimpleAdapter adapter11;


    // NEW ITO
    String[] buildingName,roomName;
    ArrayList<Merge> arraylist = new ArrayList<Merge>();
    ListViewAdapter adapter;
    DatabaseHandler controller = new DatabaseHandler(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        backSButton = findViewById(R.id.backSButton);
        buildingButton = findViewById(R.id.buildingButton);
        roomButton = findViewById(R.id.roomButton);
        roomSearch = findViewById(R.id.roomSearch);
        buildingSearch = findViewById(R.id.buildingSearch);
        roomList = findViewById(R.id.roomList);
        buildingList = findViewById(R.id.buildingList);
        buildingList.setTextFilterEnabled(true);
        buildingButton.setOnClickListener(this);
        roomButton.setOnClickListener(this);
        backSButton.setOnClickListener(this);

        ListViewAdapter.code = 0;
        buildingSearch.clearFocus();
        roomSearch.clearFocus();

        // database

        List<String> bldg = controller.getBuildingNames();
        List<String> rooms = controller.getRoomNames();
        List<String> building = controller.getBuilding();
        List<String> roomId = controller.getRoomId();
        List<String> floor = controller.getFloor();



        buildingRooms = rooms;

        // NEW ITN
        String[] newroomId = new String[roomId.size()];
        newroomId = roomId.toArray(newroomId);

        for (int x = 0; x < newroomId.length; x++)
        {
            Log.d("String is",(String)newroomId[x]);
        }


        String[] newroom = new String[rooms.size()];
        newroom = rooms.toArray(newroom);

        for (int x = 0; x < newroom.length; x++)
        {
            Log.d("String is",(String)newroom[x]);
        }

        String[] newbuilding = new String[building.size()];
        newbuilding = building.toArray(newbuilding);

        for (int x = 0; x < newbuilding.length; x++)
        {
            Log.d("String is",(String)newbuilding[x]);
        }

        String[] newfloor = new String[floor.size()];
        newfloor  = floor.toArray(newfloor );

        for (int x = 0; x < newfloor.length; x++)
        {
            Log.d("String is",(String)newbuilding[x]);
        }


        for (int x = 0; x<newbuilding.length; x++)
        {
            Merge merge = new Merge(newbuilding[x],newroom[x],newroomId[x],newfloor[x]);
            arraylist.add(merge);
        }
        adapter = new ListViewAdapter(this,arraylist);
        roomList.setAdapter(adapter);









        adapterB = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,bldg);
        buildingList.setAdapter(adapterB);

       buildingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {







               adapterB.getItem(position);
                itemValue = (String) buildingList.getItemAtPosition(position);
                searchName = "'"+itemValue+"'";
                searchNameTab = itemValue;
                searchLat = controller.getbuildingLat();
                searchLong = controller.getbuildingLon();
                searchImage = controller.getImage();
                code = 1;
                popup = 1;
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }

        });


       buildingSearch.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             Search.this.adapterB.getFilter().filter(charSequence);


           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });



        roomSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = roomSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        }


    @Override
    public void onBackPressed() {
        code = 0;
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }






    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buildingButton:
                if (roomSearch.getVisibility() == View.VISIBLE && roomList.getVisibility() == View.VISIBLE){
                    roomSearch.setVisibility(View.GONE);
                    buildingSearch.setVisibility(View.VISIBLE);
                    roomList.setVisibility(View.GONE);
                    buildingList.setVisibility(View.VISIBLE);
                    buildingButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_building,0,0,0);
                    roomButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_room_black,0,0,0);
                }
                break;
            case R.id.roomButton:
                if (buildingSearch.getVisibility() == View.VISIBLE && buildingList.getVisibility() == View.VISIBLE){
                    buildingSearch.setVisibility(View.GONE);
                    roomSearch.setVisibility(View.VISIBLE);
                    buildingList.setVisibility(View.GONE);
                    roomList.setVisibility(View.VISIBLE);
                    buildingButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_building_black,0,0,0);
                    roomButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_room,0,0,0);
                }
                break;
            case R.id.backSButton:
                code = 2;
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
                break;
            case R.id.buildingSearch:
                buildingSearch.requestFocus();
                break;
            case R.id.roomSearch:
                roomSearch.requestFocus();
                break;

        }
    }


}