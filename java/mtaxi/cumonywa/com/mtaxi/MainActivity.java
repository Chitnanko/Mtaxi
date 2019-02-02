package mtaxi.cumonywa.com.mtaxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.InetAddress;

public class MainActivity extends BaseActivity {

    private Button btn_phone;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onStart() {
        super.onStart();
            if(user!=null){
                hideProgressDialog();
                Intent intent=new Intent(getApplicationContext(),CustomerMapActivity.class);
                startActivity(intent);
                finish();
            }else {
                hideProgressDialog();
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main1);
        setContentView(R.layout.activity_main);
        btn_phone = (Button) findViewById(R.id.button_phone_login);
        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhoneInfoActivity.class);
                startActivity(intent);
            }

        });


    }

    public boolean isInternetAvailable(){
        try{
            showProgressDialog();
            InetAddress ipAddr=InetAddress.getByName("google.com");
            return !ipAddr.equals("");
        }catch (Exception e){
            return false;
        }
    }
}
