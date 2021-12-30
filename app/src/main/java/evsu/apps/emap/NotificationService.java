package evsu.apps.emap;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationService extends Service {
    int numMessages = 0;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {


    }

    @Override
    public void onStart(Intent intent, int startId) {

        Intent resultIntent = new Intent(this, MainActivity.class);
   //     resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putString("zz","zz");
        resultIntent.putExtras(bundle);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyID = 9001;
        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("New database update available")
          //      .setContentText("You've received new messages.")
                .setSmallIcon(R.mipmap.ic_launcher_emap_round)
                .setBadgeIconType(R.mipmap.ic_launcher_emap_round);

        mNotifyBuilder.setContentIntent(resultPendingIntent);

        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        mNotifyBuilder.setDefaults(defaults);

    //    mNotifyBuilder.setContentText(intent.getStringExtra("intntdata"));

        mNotifyBuilder.setAutoCancel(true);


        mNotificationManager.notify(notifyID, mNotifyBuilder.build());

    }

    @Override
    public void onDestroy() {
     //   Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }
}
