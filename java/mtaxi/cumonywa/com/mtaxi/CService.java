package mtaxi.cumonywa.com.mtaxi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class CService extends Service {

    NotificationManager noti;
    ServiceCallbacks serviceCallbacks;
    CustomerRequest customerRequest;
    boolean isBook=false;
    String driverId;
    MyBinder binder=new MyBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(getApplicationContext(),"service oncreate",Toast.LENGTH_SHORT).show();
       // startForegroundService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void setServiceCallbacks(ServiceCallbacks serviceCallbacks){
        this.serviceCallbacks=serviceCallbacks;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public boolean getBook() {
        return isBook;
    }

    public void setBook(boolean book) {
        isBook = book;
    }

    public CustomerRequest getCustomerRequest() {
        return customerRequest;
    }

    public void setCustomerRequest(CustomerRequest customerRequest) {
        this.customerRequest = customerRequest;
    }

    private void startForegroundService(){
        Intent in=new Intent(getApplicationContext(),CustomerMapActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        noti=(NotificationManager)getSystemService(Service.NOTIFICATION_SERVICE);
        //Notification notification=new Notification();
        PendingIntent pendingIntent=PendingIntent.getService(getApplicationContext(),0,in,0);
        String body="Hello Welcome to M-Taxi";
        String title="Welcome";
        Notification n=new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("MTaxi")
                .setContentText("running")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        startForeground(5,n);

    }

    public void booking(){
        startForegroundService();
    }



    public void stopMService(){
        stopSelf();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();

    }

    public class MyBinder extends Binder {

        public CService getServiceSystem(){
            return CService.this;
        }
    }
}
