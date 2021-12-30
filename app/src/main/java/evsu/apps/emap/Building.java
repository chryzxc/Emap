package evsu.apps.emap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class Building extends AppCompatActivity implements View.OnClickListener {
    private ImageView buildingImage,backRButton,buildingBDescriptionImage;
    private TextView buildingBName,buildingBDescription;
    private TextView buildingLocation;
    private ImageButton backBButton;
    static  Integer pass = 0;
    static  Integer roomCode = 0;
    private Button buildingGetDirectionButton,buildingShowRoomButton;
    DatabaseHandler controller = new DatabaseHandler(this);

    static String room;
    ConnectionDetector cd;
 //   static List<String> Rooms;
    static ArrayList<HashMap<String, String>> Rooms;
    String desc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        buildingImage = findViewById(R.id.buildingView);
        backBButton = findViewById(R.id.backBButton);
        buildingBName = findViewById(R.id.buildingBName);
        buildingBDescription = findViewById(R.id.buildingBDescription);
        buildingBDescriptionImage = findViewById(R.id.buildingBDescriptionImage);

        buildingLocation = findViewById(R.id.buildingBLocation);
        buildingGetDirectionButton = findViewById(R.id.buildingGetDirectionButton);
        buildingShowRoomButton = findViewById(R.id.buildingShowRoomButton);
        cd = new ConnectionDetector(this);

        String buildingLat = Double.toString(Search.searchLat);
        String buildingLong = Double.toString(Search.searchLong);
        if (Search.searchImage != null) {
            Picasso.get().load("http://design6500.com/assets/public/img/building/"+Search.searchImage).placeholder(R.drawable.no_preview).error(R.drawable.no_preview).into(buildingImage);
        }else{
            buildingImage.setImageDrawable(getResources().getDrawable(R.drawable.no_preview));
        }
        buildingLocation.setText("" + buildingLat + " , " + buildingLong);
        buildingBName.setText(Search.searchNameTab);
        desc = controller.getBuildingDescription();


        backBButton.setOnClickListener(this);
        buildingGetDirectionButton.setOnClickListener(this);
        buildingShowRoomButton.setOnClickListener(this);
        desc = controller.getBuildingDescription();


       // Rooms = controller.getbuildingRoom();



        // try

        Rooms = controller.getAllRooms();


        if (Rooms.isEmpty() || Rooms == null){
            buildingShowRoomButton.setVisibility(View.GONE);

        }


        if (desc != "") {
            buildingBDescription.setText(desc);
        }else{
            buildingBDescription.setVisibility(View.GONE);
            buildingBDescriptionImage.setVisibility(View.GONE);
        }




    }

    @Override
    public void onBackPressed() {

        pass = 0;
        Intent intent = new Intent();
        setResult(2, intent);
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


 //   public void dim(Building building){
 //   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
 //       Window window = building.getWindow();
//        window.setStatusBarColor(ContextCompat.getColor(building,R.color.colorDim));
//    }
//    }

//    public void light(View view,Building building){
 //       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int flags = view.getSystemUiVisibility();
 //           flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
 //           view.setSystemUiVisibility(flags);
 //           building.getWindow().setStatusBarColor(ContextCompat.getColor(building,R.color.colorPrimary));
 //       }

 //   }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBButton:
                pass = 0;
                roomCode = 0;
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
                break;
            case R.id.buildingGetDirectionButton:
                if(cd.isConnected()) {
                    pass = 1;
                    MainActivity.d.performClick();
                    Intent dintent = new Intent();
                    setResult(2, dintent);
                    finish();
                }else{
                    buildAlertMessageNotConnected();
                }
                break;
            case R.id.buildingShowRoomButton:

                Intent roomIntent = new Intent(Building.this,Room.class);
                startActivityForResult(roomIntent, 0);
              //  roomCode = 2;


                break;

        }
    }
}