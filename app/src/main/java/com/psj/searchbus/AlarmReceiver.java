package com.psj.searchbus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by psj on 2016/5/26.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //设置通知内容并在onReceive()这个函数执行时开启
        /*NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=new Notification(R.drawable.ic_launcher,"用电脑时间过长了！白痴！"
                ,System.currentTimeMillis());
        notification.setLatestEventInfo(context, "快去休息！！！",
                "一定保护眼睛,不然遗传给孩子，老婆跟别人跑啊。", null);
        notification.defaults = Notification.DEFAULT_ALL;
        manager.notify(1, notification);
*/

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("hehe")
                .setContentText("haha");
        // 设置通知的点击行为：这里启动一个 Activity
        intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        // 发送通知 id 需要在应用内唯一
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder.build());
        //再次开启LongRunningService这个服务，从而可以
        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
    }


}
