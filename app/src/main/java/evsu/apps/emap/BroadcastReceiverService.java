package evsu.apps.emap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BroadcastReceiverService extends BroadcastReceiver {
    static int noOfTimes = 0;


    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub
        noOfTimes++;
        DatabaseHandler controller = new DatabaseHandler(context);
        Integer numvalue = controller.getUpdate();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post("http://design6500.com/building/get_update",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                System.out.println(response);
            //    Toast.makeText(context, numvalue.toString(), Toast.LENGTH_SHORT).show();
                try {

                    JSONArray array = new JSONArray(response);
                    JSONObject obj = array.getJSONObject(0);
                    System.out.println(obj.get("value"));
                    //System.out.println(obj.get("value"));
                    // database
                    if( obj.getInt("value") != numvalue){


                        final Intent intnt = new Intent(context, NotificationService.class);
                        intnt.putExtra("intntdata", "version "+obj.get("value")+".0");
                        context.startService(intnt);
                    }else{
                     //   Toast.makeText(context, "Sync not needed" , Toast.LENGTH_SHORT).show();
                   }
                      } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // TODO Auto-generated method stub
                if(statusCode == 404){
                 //   Toast.makeText(context, "404", Toast.LENGTH_SHORT).show();
                }else if(statusCode == 500){
               //     Toast.makeText(context, "500", Toast.LENGTH_SHORT).show();
                }else{
              //      Toast.makeText(context, "Error occured!", Toast.LENGTH_SHORT).show();
                }
            }
        });







    }

}
