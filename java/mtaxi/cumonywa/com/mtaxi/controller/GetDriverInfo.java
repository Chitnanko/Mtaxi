package mtaxi.cumonywa.com.mtaxi.controller;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mtaxi.cumonywa.com.mtaxi.Driver;


public class GetDriverInfo {
    List<Driver> drivers=new ArrayList<>();
    Driver driver;

    public GetDriverInfo(){

    }


    DatabaseReference driverDb= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");

    public List<Driver> getDrivers() {
        driverDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    Driver driver=dataSnapshot1.getValue(Driver.class);
                    drivers.add(driver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return drivers;
    }

    public Driver getDriver(String driverId){

        driverDb.child(driverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driver=dataSnapshot.getValue(Driver.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return driver;
    }
}
