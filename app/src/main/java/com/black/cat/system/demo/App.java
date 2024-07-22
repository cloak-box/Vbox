package com.black.cat.system.demo;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.black.cat.system.demo.ui.MainActivity;
import com.black.cat.vsystem.api.NotificationBuilder;
import com.black.cat.vsystem.api.Vlog;
import com.black.cat.vsystem.api.Vsystem;
import com.black.cat.vsystem.api.VsystemConfig;

public class App extends Application {
  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    Vlog.setEnableLog(true);
    Vsystem.doAttachBaseContext(this);
    Vsystem.setConfig(
        VsystemConfig.build(
            vsystemConfig -> {
              vsystemConfig.setAppHomeComponentName(
                  new ComponentName(getPackageName(), MainActivity.class.getName()));
              vsystemConfig.setEnableOaidHook(true);
              vsystemConfig.setEnableDaemonService(true);
              vsystemConfig.setNotification(
                  context -> {
                    String channelID = context.getPackageName() + ".vbox_core";
                    NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationChannel notificationChannel =
                        notificationManager.getNotificationChannel(channelID);
                    if (notificationChannel == null) {
                      NotificationChannel channel =
                          new NotificationChannel(
                              channelID, "保活防止系统冻结server进程", NotificationManager.IMPORTANCE_MIN);
                      notificationManager.createNotificationChannel(channel);
                    }
                    Notification.Builder builder =
                        new Notification.Builder(context, channelID) // 获取一个Notification构造器
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(
                                BitmapFactory.decodeResource(
                                    context.getResources(), R.mipmap.ic_launcher))
                            .setContentText("保活防止系统冻结server进程");
                    return builder.build();
                  });
              return null;
            }));
  }

  @Override
  public void onCreate() {
    super.onCreate();
    registerActivityLifecycleCallbacks(
        new ActivityLifecycleCallbacks() {
          @Override
          public void onActivityCreated(
              @NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            Vlog.d("App", "onActivityCreated " + activity.getComponentName());
          }

          @Override
          public void onActivityStarted(@NonNull Activity activity) {
            Vlog.d("App", "onActivityStarted " + activity.getComponentName());
          }

          @Override
          public void onActivityResumed(@NonNull Activity activity) {
            Vlog.d("App", "onActivityResumed " + activity.getComponentName());
          }

          @Override
          public void onActivityPaused(@NonNull Activity activity) {
            Vlog.d("App", "onActivityPaused " + activity.getComponentName());
          }

          @Override
          public void onActivityStopped(@NonNull Activity activity) {
            Vlog.d("App", "onActivityStopped " + activity.getComponentName());
          }

          @Override
          public void onActivitySaveInstanceState(
              @NonNull Activity activity, @NonNull Bundle outState) {
            Vlog.d("App", "onActivitySaveInstanceState " + activity.getComponentName());
          }

          @Override
          public void onActivityDestroyed(@NonNull Activity activity) {
            Vlog.d("App", "onActivityDestroyed " + activity.getComponentName());
          }
        });
  }
}
