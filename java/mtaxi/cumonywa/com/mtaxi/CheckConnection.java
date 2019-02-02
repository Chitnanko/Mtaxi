package mtaxi.cumonywa.com.mtaxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CheckConnection extends AppCompatActivity{
    Button btn_TryAgain;
    @Override
    protected void onStart() {
        super.onStart();
        if(CheeckInternetConnection.getInstance(this).isOnline()){
           // Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Require Connection",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connectionfail);
        btn_TryAgain=(Button) findViewById(R.id.btn_tryAgain);
        btn_TryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheeckInternetConnection.getInstance(getApplicationContext()).isOnline()){
                    //Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Require Connection",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
