package mtaxi.cumonywa.com.mtaxi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends BaseActivity {

    Button btnPhone,btnWeb;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        btnPhone=(Button)findViewById(R.id.btncallPhone);
        btnWeb=(Button)findViewById(R.id.btnWebsite);

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str="09975672803";
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+str.trim()));
                startActivity(intent);
            }
        });

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/M-Taxi-1912796648779115/"));
                    startActivity(intent);
                }catch (Exception e){
                    Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/M-Taxi-1912796648779115/"));
                }
            }
        });
    }
}
