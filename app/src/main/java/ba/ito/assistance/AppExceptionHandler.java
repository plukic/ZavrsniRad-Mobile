package ba.ito.assistance;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

import ba.ito.assistance.ui.splash_screen.SplashActivity;
import timber.log.Timber;

public class AppExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String RESTART="assisto_app_carsh_restart_ito_ba";
    private Thread.UncaughtExceptionHandler crashlyticsHandler;
   private Activity activity;

   public AppExceptionHandler(Thread.UncaughtExceptionHandler crashlyticsHandler, Activity activity) {
       this.crashlyticsHandler = crashlyticsHandler;
       this.activity=activity;
   }


   @Override
   public void uncaughtException(Thread t, Throwable e) {
       Timber.e(e);
       crashlyticsHandler.uncaughtException(t,e);
       Intent intent = activity.getIntent();
       if(intent.hasExtra(RESTART) && intent.getBooleanExtra(RESTART,false)){
           System.exit(0);
           return;
       }

       intent.putExtra(RESTART,true);
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       activity.startActivity(intent);
       System.exit(0);

 }

    private boolean isSameException(Throwable oldThrowable,Throwable newThrowable) {
        return oldThrowable.getMessage().equals(newThrowable.getMessage());
    }

    private void killThisProcess() {
       android.os.Process.killProcess(android.os.Process.myPid());
       System.exit(10);
   }


}
