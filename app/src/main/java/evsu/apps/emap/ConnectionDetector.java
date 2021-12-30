package evsu.apps.emap;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

public class ConnectionDetector extends AsyncTask<String, Void ,Integer> {
    //  public class ConnectionDetector{

    Context context;

    public ConnectionDetector(Context context) {
        this.context = context;
    }

// ===== > Working

    //  public boolean isConnected() {
    //    ConnectivityManager cm = (ConnectivityManager) context
    //            .getSystemService(Service.CONNECTIVITY_SERVICE);
    //     if (cm!=null)
    //    {
    //         NetworkInfo info = cm.getActiveNetworkInfo();
    //        if (info!=null)
    ////          {
    //             if(info.getState() == NetworkInfo.State.CONNECTED)
    //             {
    //                 return true;
    //             }
    //         }
    //     }
//
    //      return false;
    //  }


    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasInternetAccess() {
        try {
            HttpURLConnection urlc = (HttpURLConnection)
                    (new URL("https://google.com")
                            .openConnection());
            urlc.setRequestProperty("User-Agent", "Android");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(5);
            urlc.connect();
            if (urlc.getResponseCode() == 204 &&
                    urlc.getContentLength() == 0) ;
            {
                return true;
            }

        } catch (IOException e) {
            return false;
        }

    }


    //   private  boolean hasInternet(){
    //      try {
    //           URL url = new URL("https://wwwgoogle.com");
    //           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestProperty("User-Agent","Android Application:1");
    //           connection.setRequestProperty("Connection","close");
    //          connection.setConnectTimeout(1000 * 30);
    //          connection.connect();
    //           if (connection.getResponseCode() == 200 || connection.getResponseCode() > 400)
    //           {
    //               return true;
    //         }
    //      }
    //     catch (Exception ex){
    //         return false;
    //     }
    //   return false;

    //  }
//

    public boolean Internet() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(20);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }


    // public  boolean isConnected(){
    //     boolean success = false;
    //      try {
    //          URL url = new URL("https://google.com");
    //          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    //           connection.setConnectTimeout(10000);
    //          connection.connect();
    //          success = connection.getResponseCode() == 200;
    //     }
    //     catch (IOException e) {
    //     e.printStackTrace();
    //     }
    //    return success;
    //   }
    @Override
    protected Integer doInBackground(String... params) {

        //    Integer result = 0;
        //      try{
        //          Socket socket = new Socket();
        //          SocketAddress socketAddress  = new InetSocketAddress("8.8.8.8",53);
        //          socket.connect(socketAddress,100);
        //       socket.close();
        //        result = 1;
        //        } catch (IOException e){
        //            result = 0;
        //        }
        ///     return result;
        //      }


        //  }
        Integer result = 0;
        try {
            HttpURLConnection urlc = (HttpURLConnection)
                    (new URL("https://google.com")
                            .openConnection());
            urlc.setRequestProperty("User-Agent", "Android");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(15);
            urlc.connect();
            if (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0) {
                result = 1;
            }
        } catch (IOException e) {
            result = 0;
        }

        return result;
    }


    //             try {
    //                   URL url = new URL("https://google.com");
    //                   HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    //                   connection.setRequestProperty("User-Agent","Android Application:1");
    //                  connection.setRequestProperty("Connection","close");
    //                  connection.setConnectTimeout(1000 * 30);
    //                  connection.connect();
    //                  if (connection.getResponseCode() == 200 || connection.getResponseCode() > 400)
    //                  {
    //                      return true;
    //               }
    //             }
    //           catch (Exception ex){
    //                        return false;
    //            }
    //   return false;


    @Override
    protected void onPostExecute(Integer result) {
        if (isConnected()) {
            if (result == 1) {
                Toast.makeText(context, "Internet available", Toast.LENGTH_SHORT).show();
            }
            if (result == 0) {
                Toast.makeText(context, "no internet connection", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(result);
    }


//  @Override
//  protected void onPostExecute(Integer result) {

//      if (isConnected()) {
//         if (result == 1) {
//             Toast.makeText(context, "Internet available", Toast.LENGTH_SHORT).show();
//         }
//         if (result == 0) {

//           Toast.makeText(context, "no internet connection", Toast.LENGTH_SHORT).show();
//        }
//    }
//    else {
//         Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show();
//     }
//     super.onPostExecute(result);
// }
}
