package evsu.apps.emap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Room extends AppCompatActivity {
 PhotoView roomView;
 Spinner roomSpinner;
 ImageButton backRButton;
 static String room,floor;
 TextView roomText;

 DatabaseHandler controller = new DatabaseHandler(this);

 String image = null;
    SimpleAdapter adapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        roomView = findViewById(R.id.roomView);
        roomSpinner = findViewById(R.id.roomSpinner);
        backRButton = findViewById(R.id.backRButton);
        roomText = findViewById(R.id.roomText);





        if(MainActivity.roomCode == 1) {


            // Set the Room Array list in ListView
            adapter = new SimpleAdapter(this, Building.Rooms, R.layout.view_rooms, new String[] {
                    "id", "room" }, new int[] { R.id.room_Id, R.id.room_Name });


            roomSpinner.setVisibility(View.VISIBLE);
        //    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Building.Rooms);
            roomSpinner.setAdapter(adapter);
            roomSpinner.setPrompt("Select room");
            room = roomSpinner.getSelectedItem().toString();


            image = controller.getroomimage1();
            Toast.makeText(this,image,Toast.LENGTH_SHORT).show();

            if (image != null) {
                Picasso.get().load("http://design6500.com/assets/public/img/floorplan/"+image).placeholder(R.drawable.no_preview).error(R.drawable.no_preview).into(roomView);
            }else{
                roomView.setImageDrawable(getResources().getDrawable(R.drawable.no_preview));
            }

            roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        HashMap<String, String> map = Building.Rooms.get(i);
                        floor = map.get("floor");
                        room = roomSpinner.getSelectedItem().toString();

                        image = controller.getroomimage1();
                        Toast.makeText(Room.this, image, Toast.LENGTH_SHORT).show();

                        if (image != null) {
                            Picasso.get().load("http://design6500.com/assets/public/img/floorplan/"+image).placeholder(R.drawable.no_preview).error(R.drawable.no_preview).into(roomView);
                        }else{
                            roomView.setImageDrawable(getResources().getDrawable(R.drawable.no_preview));
                        }




                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });




        }
        if (MainActivity.roomCode == 2){
            roomText.setVisibility(View.VISIBLE);
            roomText.setText(ListViewAdapter.room);
            room = ListViewAdapter.room;

            image = controller.getroomimage2();

            if (image != null) {
                Picasso.get().load("http://design6500.com/assets/public/img/floorplan/" + image).placeholder(R.drawable.no_preview).error(R.drawable.no_preview).into(roomView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {


                    }
                });
                // buildingView.setImageBitmap(image);
            } else {
               roomView.setImageDrawable(getResources().getDrawable(R.drawable.no_preview));
            }
        }

        backRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.roomCode == 1){
                    roomSpinner.setVisibility(View.GONE);
                    Intent intent = new Intent();
                    setResult(5, intent);
                    finish();
                }
                if (MainActivity.roomCode == 2){
                    Intent intent = new Intent();
                    setResult(6, intent);
                    finish();
                }



            }
        });




    }


    @Override
    public void onBackPressed() {

        if(MainActivity.roomCode == 1){
            roomSpinner.setVisibility(View.GONE);
            Intent intent = new Intent();
            setResult(5, intent);
            finish();
        }
        if (MainActivity.roomCode == 2){
            Intent intent = new Intent();
            setResult(6, intent);
            finish();
        }
    }


}




//      roomView.setOnClickListener(new View.OnClickListener() {
//           @Override
//          public void onClick(View view) {
//              AlertDialog.Builder mBuilder = new AlertDialog.Builder(Room.this);
//            View mView = getLayoutInflater().inflate(R.layout.custom,null);
//              PhotoView photoView = mView.findViewById(R.id.imageViewaa);
//             photoView.setImageBitmap(image);
//             mBuilder.setView(view);
//             AlertDialog mDialog = mBuilder.create();
//              mDialog.show();
//          }
//     });
//   mAttacher = new PhotoViewAttacher(roomView);
//  mAttacher.setZoomable(true);

//   mAttacher.update();