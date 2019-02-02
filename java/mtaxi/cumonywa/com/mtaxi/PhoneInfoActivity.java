package mtaxi.cumonywa.com.mtaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneInfoActivity extends BaseActivity {

    private EditText cuname,cugmail;//,cuphone
    private Button next;
    String name,email;//,phone

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phnone_info);
        cuname=(EditText)findViewById(R.id.edit_Name);
        //cuphone=(EditText)findViewById(R.id.edit_phone);
        cugmail=(EditText)findViewById(R.id.edit_mail);
        next=(Button)findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = cuname.getText().toString();
                //phone=cuphone.getText().toString();
                email = cugmail.getText().toString();
                if(name.equals("")){
                    cuname.setError("Enter your name");
                }
                else if (!isValidEmailID(email)) {
                    cugmail.setError("Invalid email");
                }
                else {

                    Intent intent = new Intent(getBaseContext(), PhoneAuthActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("mail", email);
                    Log.i("###########", name + "****" + email);
                    startActivity(intent);
                    //finish();
                }
            }
        });
    }
    private boolean isValidEmailID(String email) {
        String PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
