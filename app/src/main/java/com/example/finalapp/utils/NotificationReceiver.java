package com.example.finalapp.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.finalapp.App;
import com.example.finalapp.Calendar.CalendarFrag;
import com.example.finalapp.MainActivity;
import com.example.finalapp.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String eventName = intent.getStringExtra("eventName");
        String eventLocation = intent.getStringExtra("eventLocation");
        int id = intent.getIntExtra("alarmId",0);
        if(!TextUtils.isEmpty(eventLocation)){
            Intent calIntent = new Intent(context, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,id,calIntent,0);

            Intent navIntent = new Intent(context, NavigationReceiver.class);
            navIntent.putExtra("location", eventLocation);
            PendingIntent pendingNavigation = PendingIntent.getBroadcast(context, id, navIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            @SuppressLint("NotificationTrampoline") NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentTitle(eventName+" in 10 minutes")
                    .setContentText("@ "+eventLocation.split(",")[0])
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(Color.BLUE)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .addAction(R.mipmap.ic_launcher,"Navigation",pendingNavigation);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(1, builder.build());
        }
        else{
            Intent calIntent = new Intent(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,id,calIntent,0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentTitle(eventName+" in 10 minutes")
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(1, builder.build());
        }
    }
}
