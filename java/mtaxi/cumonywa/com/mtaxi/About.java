package mtaxi.cumonywa.com.mtaxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class About extends Activity {

    MenuItem actionSetting;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        actionSetting=(MenuItem)findViewById(R.id.action_settings);
    }
}
