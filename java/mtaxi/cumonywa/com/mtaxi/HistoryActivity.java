package mtaxi.cumonywa.com.mtaxi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mtaxi.cumonywa.com.mtaxi.controller.GetDriverInfo;
import mtaxi.cumonywa.com.mtaxi.model.History;

public class HistoryActivity extends BaseActivity {

    String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<History> historyList=new ArrayList<>();
    List<Driver> drivers=new ArrayList<>();

    List<ItemGenerator> sendList=new ArrayList<>();

    int count=0;

    History his;

    ItemGenerator itemGenerator;


    private ListView myList;
    private DataProvider adapter;
    DatabaseReference history= FirebaseDatabase.getInstance().getReference("history");
    DatabaseReference driverDb= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("History");

         myList = (ListView) findViewById(R.id.myList);

        history.child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Toast.makeText(getApplicationContext(),dataSnapshot.getChildrenCount()+"",Toast.LENGTH_SHORT).show();
                for (DataSnapshot entry:dataSnapshot.getChildren()) {

/*
                    String historyId=en.getValue();
                    Log.e("historyId:",dataSnapshot1.getKey());
                    historyIds.add(historyId);
*/

                    his= entry.getValue(History.class);


                    driverDb.child(his.getDriverId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           Driver driver=dataSnapshot.getValue(Driver.class);
                           // drivers.add(driver);


                            itemGenerator=new ItemGenerator(driver.getName(),driver.getCarType(),his.getPrice(), his.getStartAddress(),his.getStopAddress());
                          /*  sendList[count]=new String[]{driver.getName(),driver.getCarType(),
                                    his.getPrice(), his.getStartAddress(),his.getStopAddress()};*/

                            Log.e("history:",his.getDriverId().toString()+"$$$$"+his.getStartAddress()+"////////////////////////");

                            Log.e("driverData",driver.getName()+"////"+driver.getCarNo());

                          // sendList.add(new ItemGenerator("Nay ye","nnn","nnnn","mmmm","kkk"));

                            sendList.add(itemGenerator);

                            Log.e("Size",sendList.size()+"&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                            adapter = new DataProvider(getApplicationContext(), sendList);
                            myList.setAdapter(adapter);
                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });


        //Log.e("historySize",historyList.size()+"//////////////////////////////////////");
        //String []sendList=getResources().getStringArray(R.array.category_drawer);

        // sendList=new String[][]{{"11","12","13","14","15"},{"21","22","23","24","25"}};



    }
}
