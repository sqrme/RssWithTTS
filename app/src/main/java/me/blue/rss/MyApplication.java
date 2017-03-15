package me.blue.rss;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Created by blue on 2017/3/9.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //get wifi state..
            NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //get wnet state..
            NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected()&& wifiNetworkInfo.isAvailable()){
                AppLog.d("Rss","wifi is conneted...");
                return true;
            }
            if(dataNetworkInfo.isConnected()&& dataNetworkInfo.isAvailable()) {
                AppLog.d("Rss", "data net is conneted...");
                return true;
            }
                return false;
        } else {
            Network[] networks = connectivityManager.getAllNetworks();
            for (Network network : networks
                    ) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                if (networkInfo.isConnected()&&networkInfo.isAvailable()){
                    AppLog.d("Rss",networkInfo.getTypeName()+" is connected...");
                    return true;
                }
            }
            return false;
        }
        /*
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null){
            return networkInfo.isAvailable();
        }*/
    }

}
