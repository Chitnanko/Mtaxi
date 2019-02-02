package mtaxi.cumonywa.com.mtaxi;

import android.net.Uri;

public interface Adapter {
    void updateUI();
    void setDriverId(String id);
    void addDatabaseListener();
    void showProgress();
    void hideProgress();
    void setDriver(Driver driver);
    void setUri(Uri driveruri);
}
