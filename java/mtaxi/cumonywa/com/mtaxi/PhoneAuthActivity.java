package mtaxi.cumonywa.com.mtaxi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.collection.LLRBNode;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends BaseActivity implements View.OnClickListener{

    Timer T;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private TextView txtEditPhone;
    private EditText verify_code,cuphone;
    private Button verify_btn,resend_btn,continue_btn;
    private TextView tvCount;
    String name,phone,email,code;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private Boolean isPhoneCode=false;
    String validph=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone);
        verify_code=(EditText)findViewById(R.id.edit_code);
        verify_btn=(Button) findViewById(R.id.btn_verify);
        resend_btn=(Button) findViewById(R.id.btn_resend);
        continue_btn=(Button) findViewById(R.id.btn_continue);
        cuphone=(EditText)findViewById(R.id.edit_phone);
        tvCount=(TextView)findViewById(R.id.tvCount);
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        email=intent.getStringExtra("mail");
        //phone=intent.getStringExtra("phone");
        txtEditPhone=findViewById(R.id.txtEditPhone);
        txtEditPhone.setOnClickListener(this);
        verify_btn.setOnClickListener(this);
        resend_btn.setOnClickListener(this);
        continue_btn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                //Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                showProgressDialog();
                verify_code.setText(credential.getSmsCode());
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
               // updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                //if(credential!=null)
                    signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                   // mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), e.getMessage().toString(),
                            Snackbar.LENGTH_LONG).show();
                    // [END_EXCLUDE]
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
               // Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                //updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    private void startPhoneNumberVerification(String phoneNumber) {

        Toast.makeText(getApplicationContext(),"Code Sending...",Toast.LENGTH_LONG).show();
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                70,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            FirebaseUser user = task.getResult().getUser();
                            writeNewUser();
                            hideProgressDialog();
                            Intent intent=new Intent(getApplicationContext(),CustomerMapActivity.class);
                            startActivity(intent);
                            finish();


                            // [END_EXCLUDE]
                        } else {
                            hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    private void writeNewUser() {
            User user=new User(phone,name,email);
            String userId=mAuth.getCurrentUser().getUid();
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userId);
            databaseReference.setValue(user);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_continue:
                TimeCount();
                if (isValidatePhone()){

                    String str=phone.substring(0,1);
                    if(Integer.parseInt(str)==0){
                        validph=phone.substring(1,11);
                    }
                    phone="+95"+validph;

                    //if(!mVerificationInProgress) {
                        startPhoneNumberVerification(phone);
                    //}
                    isPhoneCode=true;
                    LinearLayout layout_phone=findViewById(R.id.phone_layout);
                    layout_phone.setVisibility(View.GONE);
                    LinearLayout layout_code=findViewById(R.id.code_layout);
                    layout_code.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_verify:
                     code = verify_code.getText().toString();
                    if (TextUtils.isEmpty(code)) {
                        verify_code.setError("Cannot be empty.");
                        return;
                    }

                    verifyPhoneNumberWithCode(mVerificationId, code);
                    showProgressDialog();

                break;
            case R.id.txtEditPhone:
                LinearLayout layout_phone=findViewById(R.id.phone_layout);
                layout_phone.setVisibility(View.VISIBLE);
                LinearLayout layout_code=findViewById(R.id.code_layout);
                layout_code.setVisibility(View.GONE);
                break;
            case R.id.btn_resend:
                ////resendVerificationCode(cuphone.getText().toString(), mResendToken);
                resend_btn.setVisibility(View.GONE);
                TimeCount();
                break;
        }
    }

    private boolean isValidatePhone() {
        phone=cuphone.getText().toString();

        if (phone==null){
            cuphone.setError("Invalid");
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        //counter=0;
        tvCount.setVisibility(View.VISIBLE);
        tvCount.setText("Count  0");
        T.cancel();
        resend_btn.setVisibility(View.GONE);
        //super.onBackPressed();
        if(isPhoneCode){
            LinearLayout layout_code=findViewById(R.id.code_layout);
            layout_code.setVisibility(View.GONE);
            LinearLayout layout_phone=findViewById(R.id.phone_layout);
            layout_phone.setVisibility(View.VISIBLE);
            isPhoneCode=false;
        }else {
           //finish();
        }
    }
    public void TimeCount(){
        T= new Timer();
        tvCount.setVisibility(View.VISIBLE);
        T.scheduleAtFixedRate(new TimerTask() {
            int counter=1;
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        tvCount.setText("Count   " + counter);
                        if(counter==10){
                            resend_btn.setVisibility(View.VISIBLE);
                            T.cancel();
                            tvCount.setVisibility(View.GONE);
                        }
                        counter++;
                    }
                });
            }
        },1000,1000);
    }
}
