package backgroundservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import syncdata.SyncDataToSAP;

public class SyncDataService extends Service {

    Context mContex;
    String sync_data = null;
    String rfq_docno = null;
    String fr_tehsil = "";
    String to_tehsil = "";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mContex = getApplicationContext();
    }

    //    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //  handleStart(intent, startId);

        Bundle bundle = intent.getExtras();

        fr_tehsil = bundle.getString("from_tehsil");
        to_tehsil = bundle.getString("to_tehsil");
        sync_data = intent.getStringExtra("sync_data");
        rfq_docno = intent.getStringExtra("rfq_docno");


        if (sync_data.equals("route_data")) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    new SyncDataToSAP().SyncRouteDataToSap(mContex, fr_tehsil, to_tehsil);

                }
            }).start();
        }

        if (sync_data.equals("rfq_data")) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    new SyncDataToSAP().SyncRfqDataToSap(mContex, rfq_docno);

                }
            }).start();
        }


        if (sync_data.equals("quote_data")) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    new SyncDataToSAP().SyncQuotationDataToSap(mContex, rfq_docno);

                }
            }).start();
        }


        stopSelf();
        return START_NOT_STICKY;
    }

}
