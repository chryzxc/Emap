package evsu.apps.emap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.geojson.Point;
import java.util.List;

public class Direction extends AppCompatActivity {
    static Integer code = 0;
    static String dstartingPointName,ddestinationPointName,buildingName,originName,destinationName;
    private Button directionButton;
    private ImageView backButton,originClear,destinationClear;
    private TextView directionText;
    private String imagename1, imagename2;
    private EditText originSearch, originSearch1,destinationSearch,destinationSearch1;
    public Point myLocation, evsuLocation, defaultstartingPoint;
    static Point dstartingPoint,ddestinationPoint = null;
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    private ListView originList, destinationList;
    ConnectionDetector cd;
    DatabaseHandler controller = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        originClear = findViewById(R.id.originClear);
        destinationClear = findViewById(R.id.destinationClear);
        backButton =  findViewById(R.id.backButtonD);
        originSearch = findViewById(R.id.originSearch);
        originSearch1 = findViewById(R.id.originSearch1);
        destinationSearch = findViewById(R.id.destinationSearch);
        destinationSearch1 = findViewById(R.id.destinationSearch1);
        directionButton = findViewById(R.id.directionButton);
        directionText = findViewById(R.id.directionText);

        originList= findViewById(R.id.originList);
        destinationList = findViewById(R.id.destinationList);
        dstartingPoint = null;
        ddestinationPoint = null;
        dstartingPointName = null;
        ddestinationPointName = null;
        cd = new ConnectionDetector(this);

        originSearch1.setFocusableInTouchMode(true);
        originSearch1.requestFocus();
        destinationSearch.setFocusableInTouchMode(true);
        destinationSearch.requestFocus();
        checkLocation();

        List<String> bldg = controller.getBuildingNames();

        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bldg);
        originList.setAdapter(adapter1);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bldg);
        destinationList.setAdapter(adapter2);

        destinationClear.setVisibility(View.GONE);
        originSearch.setClickable(false);
        destinationSearch.setClickable(false);
        originSearch.setClickable(false);
        originSearch.setFocusable(false);
        destinationSearch.setFocusable(false);





        originList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                adapter1.getItem(position);
                buildingName = (String) originList.getItemAtPosition(position);

          //      imagename1 = bldg.get(position);

                Double originLat = controller.getLat();
                Double originLon = controller.getLon();

                dstartingPoint = Point.fromLngLat(originLon, originLat);
                dstartingPointName = buildingName;
                originSearch.setText(dstartingPointName);
                originSearch1.clearFocus();
                destinationSearch1.clearFocus();
                originList.setVisibility(View.GONE);
            //    dstartingPointName = imagename1;
           //     dstartingPoint = Point.fromLngLat(longitude.get(position), latitude.get(position));
                originSearch1.setVisibility(View.GONE);
                originSearch.setVisibility(View.VISIBLE);
            //    originSearch.setText(imagename1);
                directionText.setVisibility(View.GONE);
                originClear.setVisibility(View.VISIBLE);
                originSearch1.setFocusable(false);
                destinationSearch1.setFocusable(false);
            }
        });




        destinationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                adapter2.getItem(position);
                buildingName = (String) destinationList.getItemAtPosition(position);
                Double destiLat = controller.getLat();
                Double destiLon = controller.getLon();
                ddestinationPoint = Point.fromLngLat(destiLon, destiLat);
                ddestinationPointName = buildingName;
                destinationSearch.setText(ddestinationPointName);


              //  imagename2 = bldg.get(position);
                destinationSearch1.clearFocus();
                originSearch1.clearFocus();
                destinationList.setVisibility(View.GONE);
             //   ddestinationPointName = imagename2;
              //  ddestinationPoint = Point.fromLngLat(longitude.get(position), latitude.get(position));
              //  destinationSearch.setText(imagename2);
                directionText.setVisibility(View.GONE);
                destinationSearch1.setVisibility(View.GONE);
                destinationClear.setVisibility(View.VISIBLE);
                destinationSearch.setVisibility(View.VISIBLE);
                originSearch1.setFocusable(false);
                destinationSearch1.setFocusable(false);

            }

        });


        originSearch1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    originList.setVisibility(View.VISIBLE);
                    directionText.setVisibility(View.VISIBLE);
                    directionText.setText("Choose origin");
                }else{
                    originList.setVisibility(View.GONE);
                    directionText.setVisibility(View.GONE);
                }
            }
        });

        destinationSearch1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    destinationList.setVisibility(View.VISIBLE);
                    directionText.setVisibility(View.VISIBLE);
                    directionText.setText("Choose destination");
                }else{
                    destinationList.setVisibility(View.GONE);
                    directionText.setVisibility(View.GONE);
                }
            }
        });




        originSearch1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (originList.getVisibility() == View.GONE) {
                }
                Direction.this.adapter1.getFilter().filter(charSequence);
                directionText.setText("Choose origin");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });




        originSearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originList.setVisibility(View.VISIBLE);
                directionText.setVisibility(View.VISIBLE);
                directionText.setText("Choose origin");
                originSearch1.setFocusableInTouchMode(true);
                originSearch1.requestFocus();
            }
        });




        destinationSearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destinationList.setVisibility(View.VISIBLE);
                directionText.setVisibility(View.VISIBLE);
                directionText.setText("Choose destination");
                destinationSearch1.setFocusableInTouchMode(true);
                destinationSearch1.requestFocus();
            }
        });


        destinationSearch1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (destinationList.getVisibility() == View.GONE) {

                }
                Direction.this.adapter2.getFilter().filter(charSequence);
                directionText.setText("Choose destination");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = 0;
                dstartingPoint = null;
                ddestinationPoint = null;
                dstartingPointName = null;
                ddestinationPointName = null;
                Intent intent = new Intent();
                setResult(0, intent);
                finish();
            }
        });


        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ddestinationPoint == null) {
                    Toast.makeText(Direction.this, "Choose destination point", Toast.LENGTH_SHORT).show();
                }
                if (dstartingPoint == null) {
                    Toast.makeText(Direction.this, "Choose starting point", Toast.LENGTH_SHORT).show();
                }

                if (dstartingPoint != null && ddestinationPoint != null) {
                    if(cd.isConnected()) {
                        code = 1;
                        Intent intent = new Intent();
                        setResult(0, intent);
                        finish();
                    }else{
                        buildAlertMessageNotConnected();
                    }
                }

            }
        });

        originClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originSearch.setVisibility(View.GONE);
                originSearch1.setVisibility(View.VISIBLE);
                originClear.setVisibility(View.GONE);
                originSearch1.setText("");
                dstartingPoint = null;
                destinationSearch1.setFocusable(false);
                originSearch1.setFocusable(false);


            }
        });

        destinationClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destinationSearch.setVisibility(View.GONE);
                destinationSearch1.setVisibility(View.VISIBLE);
                destinationClear.setVisibility(View.GONE);
                destinationSearch1.setText("");
                ddestinationPoint = null;
                destinationSearch1.setFocusable(false);
                originSearch1.setFocusable(false);

            }
        });


    }

    @Override
    public void onBackPressed() {
        code = 0;
        dstartingPoint = null;
        ddestinationPoint = null;
        dstartingPointName = null;
        ddestinationPointName = null;
        Intent intent = new Intent();
        setResult(0, intent);
        finish();
    }


    private  void  buildAlertMessageNotConnected(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You are offline");

        builder.setMessage("Wifi/Mobile data is off. Turn it on to continue.")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }
                );
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void checkLocation() {

        if (MainActivity.originLocation != null) {
            originSearch.setText("Current location");
            dstartingPointName = "Current location";
           dstartingPoint = Point.fromLngLat(MainActivity.locationlongitude, MainActivity.locationlatitude);
        } else {
            originSearch.setText("EVSU Main Gate");
            dstartingPointName = "Evsu main gate";
            dstartingPoint = Point.fromLngLat(124.997556, 11.240392);
        }
    }



}
