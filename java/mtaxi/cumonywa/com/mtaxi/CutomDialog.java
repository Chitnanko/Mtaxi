package mtaxi.cumonywa.com.mtaxi;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static mtaxi.cumonywa.com.mtaxi.R.color.colorPrimary;

public class CutomDialog extends DialogFragment {

    FirebaseUser auth=FirebaseAuth.getInstance().getCurrentUser();
    private  List<String> availableDriver;
    View v;
    ImageView driverPhoto;
    private Button btnPrevious, btnNext,btnSelect;
    private int count=0;
    URL imgUrl;
    String driverId;
    Adapter adapter;
    private TextView txtName,txtPhone,txtCarNo,txtCarType;
    Uri driveruri;
    private Driver driver=null;
    private Button btnClose;

    public CutomDialog(){

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.cutom_dialog,container, false);
        btnPrevious = (Button) v.findViewById(R.id.btnPrevious);
        btnSelect=(Button)v.findViewById(R.id.btnSelect);
        btnNext = (Button) v.findViewById(R.id.btnNext);
        driverPhoto=v.findViewById(R.id.dirverImg);
        txtPhone=(TextView)v.findViewById(R.id.driverPhone);
        txtName=(TextView)v.findViewById(R.id.txtDName);
        txtCarNo=(TextView)v.findViewById(R.id.txtDCarNo);
        txtCarType=(TextView)v.findViewById(R.id.txtDCarType);

        getDialog().setTitle("Driver found");

        Window window=getDialog().getWindow();
        //window.setBackgroundDrawableResource(R.color.layout_background);
        //setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogStyle);
        //setCancelable(false);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //driverPhoto.setImageDrawable();
                //driverPhoto.setImageDrawable(null);

                count=count+1;
                /*if(count > availableDriver.size()){
                    btnPrevious.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);
                    return;
                }*/

                if(count==(availableDriver.size()-1)){
                    btnNext.setVisibility(View.GONE);
                    btnPrevious.setVisibility(View.VISIBLE);
                }else if(count>=availableDriver.size()){
                    btnNext.setVisibility(View.GONE);
                    return;
                }

                driverId = availableDriver.get(count);
                adapter.showProgress();
                getDriverInfo(driverId);


            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnNext.setVisibility(View.VISIBLE);
                count=count-1;
               /* if(count<0){
                    count=0;
                    btnPrevious.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                    return;
                }*/

                if(count==0){
                    btnPrevious.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                }

                driverId = availableDriver.get(count);
                adapter.showProgress();
                getDriverInfo(driverId);

            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child("Drivers").child(driverId).child("customerRequest")
                        .child(driverId);

                if(customerRequest!=null){
                    driverRef.setValue(customerRequest);
                }
                adapter.updateUI();
                adapter.setDriverId(driverId);
                adapter.addDatabaseListener();
                adapter.setDriver(driver);
                adapter.setUri(driveruri);
                dismiss();

            }
        });



        return v;
    }

    private void getDriverInfo(final String driverId){


        DatabaseReference driverDetail = FirebaseDatabase.getInstance().getReference("Users").child("Drivers");
        driverDetail.child(driverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot!=null){

                         driver=dataSnapshot.getValue(Driver.class);


                             FirebaseStorage storage=FirebaseStorage.getInstance();
                             StorageReference storageReference=storage.getReference("images");
                             String imageName=driver.getPhone().toString().trim().substring(3)+".jpg";
                             StorageReference imgReference=storageReference.child(driverId).child(imageName);

                             //Toast.makeText(getContext(),driver.getName().toString()+driver.getPhone().toString(),Toast.LENGTH_LONG).show();



                             try {

                                 imgReference.getDownloadUrl()
                                         .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                             @Override
                                             public void onSuccess(Uri uri) {

                                                 if(isAdded()) {

                                                     Glide.with(getContext())
                                                             .load(uri.toString())
                                                             .apply(RequestOptions.circleCropTransform())
                                                             .into(driverPhoto);
                                                 }
                                                 //show(fmg,title);
                                                 adapter.hideProgress();
                                                 Log.e("error", "photo downloaded/////////////////////////////");

                                                 //hideProgressDialog();
                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Log.e("error message", e.getMessage());
                                         adapter.hideProgress();
                                         dismiss();
                                         //Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                                         Log.e("error", e.getMessage() + "/////////////////////////////////////");
                                         //hideProgressDialog();
                                     }
                                 });
                             }catch (Exception e){
                                 Log.e("error",e.getMessage());
                             }


                     String phone=driver.getPhone();
                     String str="0"+phone.substring(3);

                     txtName.setText(driver.getName().toString());
                     txtPhone.setText(str.toString());
                     txtCarNo.setText(driver.getCarNo().toString());
                     txtCarType.setText(driver.getCarType().toString());
                 }else {

                     Toast.makeText(getActivity(),"No driver available",Toast.LENGTH_SHORT).show();
                     dismiss();
                 }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    FragmentManager fmg;
    String title;
    CustomerRequest customerRequest;
    public void show(FragmentManager fmg, String title, List<String> availableDriver,CustomerRequest customerRequest) {
        this.customerRequest=customerRequest;
        this.availableDriver=availableDriver;
        this.fmg=fmg;
        this.title=title;

        show(fmg, title);


        driverId = availableDriver.get(0);
            //show(fmg, title);
           // adapter.showProgress();
            if (driverId != null) {
                getDriverInfo(driverId);

            }

    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }
    public void setUri(Uri driveruri){
        this.driveruri=driveruri;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            dismiss();
        }
    }
}
