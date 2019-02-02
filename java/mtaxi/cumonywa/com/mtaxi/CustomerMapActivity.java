package mtaxi.cumonywa.com.mtaxi;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import android.location.LocationListener;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
//import android.support.test.espresso.core.deps.guava.collect.MapMaker;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mtaxi.cumonywa.com.mtaxi.model.DriverLocation;
import mtaxi.cumonywa.com.mtaxi.model.History;

public class CustomerMapActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,RoutingListener,Adapter {

    private static int driverAccept=1;
    private static int driverReject=0;

    Location centerLocation=new Location("");
    Location selectLocation=new Location("");
    double maxDistance=20000;

    LatLng centLatLng=new LatLng( 22.1167125,95.13193359374995);

    public Uri driveruri;
    CService cService;
    public boolean bound=false;
    CustomerRequest customerRequest;
   // Intent intent;
    private static final int  startLocation=1;
    private static final int stopLocation=2;
    private  LatLng startLatLong,stopLatLong;
    private Marker startMarker,stopMarker;
    private Button btn_phone;
    private GoogleMap mMap, pMap;
    private Location mlocation;
    private LatLng pickupLocation;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private Marker pickupMarker;
    private LatLng latLng;
    private FloatingActionButton btnSearch;
    private GoogleApiClient mGoogleApiClient;
    private boolean locationGranted = false;
    private TextView select_start;
    private TextView select_end;
    private TextView trip_duration,trip_distance,trip_cost;
    private CardView trip_info;
    private FrameLayout frameLayout;
    String driverId = "";
    Driver driver;
    private List<Polyline> polylines= new ArrayList<>();
    //private Polyline polyline;
    private Button btnBook;

    private LinearLayout layout_booking;
    private CardView card_driver;
    private TextView txtName;
    private ImageView imgPhone;
    private ImageView imgDphoto;
    private MenuItem actionAbout;
    private String startAddress,stopAddress;

    private LinearLayout layout_driverInfo;

    private TextView profileName,profilePhone;
    double dis;
    double totalCost;
    String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    DatabaseReference cancelRequest=FirebaseDatabase.getInstance().getReference("cancelRequest");

    DatabaseReference activeJob=FirebaseDatabase.getInstance().getReference("activeJob");
    DatabaseReference operator=FirebaseDatabase.getInstance().getReference("operator");
    DatabaseReference rejectJob=FirebaseDatabase.getInstance().getReference("rejectJob");

    DatabaseReference availableRef=FirebaseDatabase.getInstance().getReference("driversAvailable");

    DatabaseReference finishJob=FirebaseDatabase.getInstance().getReference("finishJob");

    DatabaseReference history=FirebaseDatabase.getInstance().getReference("history");

    DatabaseReference driverLocationRef = FirebaseDatabase.getInstance().getReference("driverLocation");

    DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference()
            .child("Users").child("Drivers");
   // TextView tvResult;

    CutomDialog cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        actionAbout=(MenuItem)findViewById(R.id.action_settings);
        trip_duration=findViewById(R.id.trip_duration);
        trip_distance=findViewById(R.id.trip_distance);
        trip_cost=findViewById(R.id.trip_cost);
        trip_info=findViewById(R.id.trip_info);
        layout_booking=(LinearLayout)findViewById(R.id.layout_booking);
        card_driver=findViewById(R.id.card_driverInfo);
        txtName=(TextView)findViewById(R.id.txtName);
        imgPhone=(ImageView)findViewById(R.id.imgPhone);
        imgDphoto=(ImageView)findViewById(R.id.imgDphoto);
        layout_driverInfo=findViewById(R.id.layout_driverInfo);

        btnBook=(Button)findViewById(R.id.btnBooking);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(btnBook.getText().toString().equals("Booking")) {
                    trip_info.setVisibility(View.GONE);

                    showProgressDialog();


                    availableRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        List<String> availableDriver=new ArrayList<>();

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                for (DataSnapshot entry: dataSnapshot.getChildren()) {
                                    //driverId = entry.getKey();
                                    String d=entry.getValue(String.class);
                                    availableDriver.add(d);
                                    //testLatLong=test.getValue();
                                }
                                hideProgressDialog();

                                if(availableDriver.size()>0) {
                                    FragmentManager fmg = getSupportFragmentManager();

                                    if (cd == null)
                                        cd = new CutomDialog();

                                    cd.setAdapter(CustomerMapActivity.this);
                                    customerRequest=customerRequest();
                                    cd.show(fmg, "MDriver is running", availableDriver, customerRequest);
                                }else {
                                    Toast.makeText(getApplication(),"No driver available",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                hideProgressDialog();
                                Toast.makeText(CustomerMapActivity.this, "No driver available.Try again later", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            hideProgressDialog();
                        }
                    });
                }else if(btnBook.getText().toString().trim().equals("Cancel Book")){

                    cService.setBook(false);

                    trip_info.setVisibility(View.VISIBLE);
                    card_driver.setVisibility(View.GONE);


                    activeJob.child(customerId).removeValue();

                    cancelRequest.child(customerId).setValue(driverId);

                    driverRef.child(driverId).child("customerRequest").removeValue();

                    driverId="";
                    cService.setDriverId(driverId);
                    cService.stopSelf();

                    btnBook.setText("Booking");


                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        select_start=(TextView)findViewById(R.id.select_start);
        select_end=(TextView)findViewById(R.id.select_end);

        /*select_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(startLocation);
            }
        });
        select_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(stopLocation);
            }
        });*/


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        buildGoogleAPIClient();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);


        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        profilePhone=view.findViewById(R.id.profile_phone);
        profileName=view.findViewById(R.id.proflie_name);

        profileName.setText(":ooooooooooo");
        profilePhone.setText("::::::::");


    }

    private void startActivity(int request_number){

        try {
           /* LatLng topRight=new LatLng(22.18958,95.093800);
            LatLng buttonLeft=new LatLng(22.057512,95.21270);

            LatLngBounds latLngBounds=new LatLngBounds(buttonLeft,topRight);*/

            PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
           // builder.setLatLngBounds(latLngBounds);

            startActivityForResult(builder.build(CustomerMapActivity.this),request_number);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e("i am error",e.getMessage().toString());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("i am error",e.getMessage().toString());
        }
    }

    Routing routing;
    private void createRoute(LatLng start,LatLng stop,int isDriverAccept){

        erasePolylines();
        if(start!=null && stop!=null){
            routing=new Routing.Builder().travelMode(AbstractRouting.TravelMode.WALKING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(start,stop)
                    .key("AIzaSyAPBDnAkMm_hwJzxyBpxxNVARBMzvKGs1U")
                    .build();
            routing.execute();

            if(isDriverAccept==driverAccept){
                mMap.clear();

                mMap.addMarker(new MarkerOptions().position(start).title("Start Here")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.passenger)));

                stopMarker=mMap.addMarker(new MarkerOptions().position(stop).title("Driver")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
            }


         /*mMap.addMarker(new MarkerOptions().position(startLatLong).title("Start Here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            stopMarker=mMap.addMarker(new MarkerOptions().position(stopLatLong).title("Stop Here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
*/

            showProgressDialog();
        }
        CameraPosition cameraPosition=new CameraPosition.Builder().target(start)
                .zoom(17).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        btnBook.setEnabled(true);
       // showDetailDialog();

    }

    /*private void showDetailDialog() {
        View view=getLayoutInflater().inflate(R.layout.bottom_sheet_layout,null);
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
       // BottomSheetBehavior bottomSheetBehavior=BottomSheetBehavior.from((View)view.getParent());
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);

        Log.i("&*&*&*",requestCode+"OK");

        if(requestCode==startLocation){
            if(resultCode== RESULT_OK){
                Place place=PlacePicker.getPlace(CustomerMapActivity.this,data);


                    startAddress = place.getAddress().toString();
                    startLatLong = place.getLatLng();

                    centerLocation.setLatitude(centLatLng.latitude);
                    centerLocation.setLongitude(centLatLng.longitude);

                    selectLocation.setLatitude(startLatLong.latitude);
                    selectLocation.setLongitude(startLatLong.longitude);

                    Log.e("distance:",selectLocation.distanceTo(centerLocation)+"//////////////////////");

                    if(selectLocation.distanceTo(centerLocation)>maxDistance){

                        Toast.makeText(getApplication(),"This location no available",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    select_start.setText(startAddress);

                    if (startMarker != null) {
                        startMarker.remove();
                    }

                    startMarker = mMap.addMarker(new MarkerOptions().position(startLatLong).title("Start Here")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.passenger)));

                    if (stopLatLong != null) {
                        createRoute(startLatLong, stopLatLong, driverReject);
                    }
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(startLatLong)
                            .zoom(17).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
        }else if(requestCode==stopLocation){
            if(resultCode== RESULT_OK) {
                Place place = PlacePicker.getPlace(CustomerMapActivity.this, data);


                    stopLatLong = place.getLatLng();
                    stopAddress = place.getAddress().toString();

                    centerLocation.setLatitude(centLatLng.latitude);
                    centerLocation.setLongitude(centLatLng.longitude);

                    selectLocation.setLatitude(stopLatLong.latitude);
                    selectLocation.setLongitude(stopLatLong.longitude);

                    if(selectLocation.distanceTo(centerLocation)>maxDistance){
                        Toast.makeText(getApplication(),"This location no available",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    select_end.setText(stopAddress);

                    if (stopMarker != null) {
                        stopMarker.remove();
                    }

                    stopMarker = mMap.addMarker(new MarkerOptions().position(stopLatLong).title("Stop Here")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop)));

                    if (startLatLong != null) {
                        createRoute(startLatLong, stopLatLong, driverReject);
                    }
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(stopLatLong)
                            .zoom(17).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            CService.MyBinder myBinder = (CService.MyBinder) service;
            cService = myBinder.getServiceSystem();
            bound=true;

           // Toast.makeText(getApplicationContext(),"Cservice connected",Toast.LENGTH_SHORT).show();

            if(cService!=null){
                //Toast.makeText(getApplicationContext(),"Cservice not null",Toast.LENGTH_SHORT).show();
                String dId=cService.getDriverId();
                if(cService.getBook()){
                    btnBook.setText("Cancel Book");
                    btnBook.setEnabled(true);
                    customerRequest=cService.getCustomerRequest();
                    LatLng start=new LatLng(Double.parseDouble(customerRequest.getStartLat()),
                            Double.parseDouble(customerRequest.getStartLng()));
                    LatLng stop=new LatLng(Double.parseDouble(customerRequest.getStopLat()),
                            Double.parseDouble(customerRequest.getStopLng()));
                    createRoute(start,stop,driverAccept);

                }
            }

        }



        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, CService.class);
        startService(intent);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
        databaseReference.child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if(user!=null){
                    profileName.setText(user.getUser_name());
                    profilePhone.setText(user.getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(mGoogleApiClient!=null)
            mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
        if(mGoogleApiClient!=null)
            mGoogleApiClient.disconnect();

        if(bound) {
            unbindService(connection);
            //mService.setCallbacks(null);
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog dialog = showDiaglogbox();
            dialog.show();
        }
    //this.moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,About.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            Intent intent=new Intent(getApplicationContext(),HistoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_feedback) {
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/email");
            String shareBody="Dear..........,"+" ";
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"nylcumy@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
            intent.putExtra(Intent.EXTRA_TEXT,shareBody);
            startActivity(Intent.createChooser(intent,"Send Feedback"));

        } else if (id == R.id.nav_share) {
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBody="Here is the share content body!";
            intent.putExtra(Intent.EXTRA_SUBJECT,"Subjects");
            intent.putExtra(Intent.EXTRA_TEXT,shareBody);
            startActivity(Intent.createChooser(intent,"Share via"));

        } else if (id == R.id.nav_help) {
            Intent intent=new Intent(getApplicationContext(),HelpActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private AlertDialog showDiaglogbox() {
        AlertDialog adl = new AlertDialog.Builder(this).setTitle("M-Taxi").setMessage("Are you sure to exit?")
                .setIcon(R.mipmap.ic_launcher).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //CustomerMapActivity n=new CustomerMapActivity();
                        moveTaskToBack(true);
                       // finish();
                        //System.exit(0);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        return adl;
    }

    private synchronized void buildGoogleAPIClient(){

        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,0,this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

       // updateLocationUI();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }

        mMap.setMyLocationEnabled(true);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        mGoogleApiClient.connect();
        //updateLocationUI();

    }



    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(CustomerMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(CustomerMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {

            Log.e("i am here.","/////////////////////jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj/");
            return;
        }

        try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    checkLocationPermission();

                    // mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    return;
                }
                getDeviceLocation();

        }catch (Exception e){
            Log.e("Exception:%s",e.getMessage());
        }
    }

    private void getDeviceLocation() {

        try {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    checkLocationPermission();
                    return;
                }
                Task<Location> locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        try {
                            if (task.isSuccessful()) {
                                Location location = task.getResult();
                                mlocation=location;
                                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
                                mMap.moveCamera(update);
                               // latLng=null;
                            }
                        }catch (Exception e){


                        }

                    }
                });
        }catch (Exception e){
            Log.e("Exception:%s",e.getMessage());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateLocationUI();
        //Toast.makeText(this,"connected", Toast.LENGTH_SHORT).show();
        /*Log.e("starting..........","client start connection ///////////////");*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Check your connection",Toast.LENGTH_SHORT).show();
        Log.e("connection failed","fail to connect map ///////////////////////////");
    }

    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for(Location location : locationResult.getLocations()){
                if(getApplicationContext()!=null){
                   // mlocation = location;

                    latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    //updateLocationUI();

                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }
            }
        }
    };


    private static final int[] COLORS=new int[]{R.color.colorPrimaryDark};


    @Override
    public void onRoutingFailure(RouteException e) {

        hideProgressDialog();

        if(e!=null){
            Toast.makeText(this,"Check your connection:",Toast.LENGTH_LONG).show();
            mMap.clear();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> routes, int shortestRouteIndex) {
        if(polylines.size()>0){
            for(Polyline poly:polylines){
                poly.remove();
            }
            polylines.clear();
        }
        for(int i=0;i<routes.size();i++){
            int colorIndex=i% COLORS.length;
            PolylineOptions polylineOptions=new PolylineOptions();
            polylineOptions.color(getResources().getColor(COLORS[colorIndex]));
            polylineOptions.width(10+i*3);
            polylineOptions.addAll(routes.get(i).getPoints());

            Polyline polyline=mMap.addPolyline(polylineOptions);
            polylines.add(polyline);


           // Toast.makeText(getApplicationContext(),"Route"+(i+1),)
            trip_info.setVisibility(View.VISIBLE);

           double  duration=((double)routes.get(i).getDurationValue())/360;
           String st=String.format("%.2f",duration);

            trip_duration.setText(st+" minutes");
            dis=((double)routes.get(i).getDistanceValue())/1000;
            String sdis=String.format("%.2f",dis);
            trip_distance.setText(sdis+" km");

            double takecost=2000;
            double postcost=dis*500;

            totalCost=takecost+postcost;
            Double d=new Double(totalCost);
            int total=d.intValue();
            String str=String.format(""+total);
            trip_cost.setText(str);
        }
        hideProgressDialog();
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void erasePolylines(){
        for(Polyline poly:polylines){
            poly.remove();
        }
        polylines.clear();
    }

    public CustomerRequest customerRequest(){

        //DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverId).child("customerRequest");
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setCustomerId(customerId);
        customerRequest.setStartLat(Double.toString(startLatLong.latitude));
        customerRequest.setStartLng(Double.toString(startLatLong.longitude));
        customerRequest.setStopLat(Double.toString(stopLatLong.latitude));
        customerRequest.setStopLng(Double.toString(stopLatLong.longitude));
        customerRequest.setStartAddress(startAddress);
        customerRequest.setStopAddress(stopAddress);
        customerRequest.setDistance(Double.toString(dis));
        customerRequest.setTotalCost(Double.toString(totalCost));
        return customerRequest;

    }


    public void SelectMyLocation(View view) {
        startActivity(startLocation);
    }

    public void SelectWhereLocation(View view) {
        startActivity(stopLocation);
    }

    @Override
    public void updateUI() {
        cService.setBook(true);
        btnBook.setText("Cancel Book");
        cService.booking();
        cService.setCustomerRequest(customerRequest);
    }

    @Override
    public void setDriverId(String id) {
        this.driverId=id;
        cService.setDriverId(driverId);
    }

    @Override
    public void addDatabaseListener() {

           operator.child(customerId).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   try {
                       boolean b = dataSnapshot.getValue(Boolean.class);
                       if (b) {
                           btnBook.setText("Booking");
                           card_driver.setVisibility(View.GONE);
                           Toast.makeText(getApplication(), "operator delete the job", Toast.LENGTH_SHORT).show();
                           // rejectJob.child(customerId).removeValue();

                           cService.setBook(false);
                           cService.setCustomerRequest(null);
                           cService.stopSelf();
                           btnBook.setEnabled(true);

                           operator.child(customerId).removeValue();
                           driverLocationRef.child(customerId).removeValue();
                       }
                   }catch (Exception e){
                       e.printStackTrace();
                   }


               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

            activeJob.child(customerId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String dId = dataSnapshot.getValue(String.class);
                    if (dId != null) {
                        if (driverId.equals(dId)) {
                            Toast.makeText(getApplicationContext(), "Driver accepted the job", Toast.LENGTH_SHORT).show();
                            //layout_booking.setVisibility(View.GONE);
                            trip_info.setVisibility(View.GONE);
                            card_driver.setVisibility(View.VISIBLE);
                            if (driver != null) {
                               // activeJob.child(customerId).removeValue();
                                txtName.setText(driver.getName());

                           /* Glide.with(getApplication())
                                    .load(driveruri.toString())
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(imgDphoto);*/

                                
                                driverLocationRef.child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        try {
                                            DriverLocation driverLocation = dataSnapshot.getValue(DriverLocation.class);
                                            LatLng temp = new LatLng(driverLocation.getDriverLat(), driverLocation.getDriverLng());
                                            driverLocationRef.removeValue();
                                            mMap.clear();
                                            createRoute(startLatLong, temp, driverAccept);
                                        }catch (Exception e){
                                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                FirebaseStorage storage=FirebaseStorage.getInstance();
                                StorageReference storageReference=storage.getReference("images");
                                String imageName=driver.getPhone().toString().trim().substring(3)+".jpg";
                                StorageReference imgReference=storageReference.child(driverId).child(imageName);


                                try {

                                    imgReference.getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {


                                                        Glide.with(getApplication())
                                                                .load(uri.toString())
                                                                .apply(RequestOptions.circleCropTransform())
                                                                .into(imgDphoto);

                                                    Log.e("error", "photo downloaded/////////////////////////////");

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("error message", e.getMessage());

                                            Log.e("error", e.getMessage() + "/////////////////////////////////////");

                                        }
                                    });
                                }catch (Exception e){
                                    Log.e("error",e.getMessage());
                                }


                                imgPhone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String str = driver.getPhone();
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:" + str.trim()));
                                        startActivity(intent);
                                    }
                                });

                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            rejectJob.child(customerId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String dId = dataSnapshot.getValue(String.class);

                    if (dId != null) {
                        if (dId.equals(driverId)) {
                            btnBook.setText("Booking");
                            Toast.makeText(getApplicationContext(), "Driver rejected the job", Toast.LENGTH_SHORT).show();
                            rejectJob.child(customerId).removeValue();

                            cService.setBook(false);
                            cService.setCustomerRequest(null);
                            cService.stopSelf();
                            btnBook.setEnabled(true);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            finishJob.child(customerId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String dId = dataSnapshot.getValue(String.class);

                    if (driverId.equals(dId)) {

                        card_driver.setVisibility(View.GONE);

                        finishJob.child(customerId).removeValue();
                        activeJob.child(customerId).removeValue();

                        history.child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                long historySzie = 0;
                                historySzie = dataSnapshot1.getChildrenCount();
                                History hist = new History();
                                hist.setDriverId(driverId);

                                hist.setStartLat(Double.toString(startLatLong.latitude));
                                hist.setStartLng(Double.toString(startLatLong.longitude));
                                hist.setStopLat(Double.toString(stopLatLong.latitude));
                                hist.setStopLng(Double.toString(stopLatLong.longitude));
                                hist.setPrice(Double.toString(totalCost));
                                hist.setStartAddress(startAddress);
                                hist.setStopAddress(stopAddress);

                                history.child(customerId).child("historyId:" + historySzie).setValue(hist);

                                Toast.makeText(getApplication(), "Trip finished", Toast.LENGTH_SHORT).show();
                                btnBook.setText("Booking");

                                select_start.setText("Select my location");
                                select_end.setText("Where are you going?");
                                //cService.setBook(false);
                                //cService.setDriverId("");
                                cService.stopSelf();
                                mMap.clear();

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

        }


    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {

        hideProgressDialog();
    }

    @Override
    public void setDriver(Driver driver) {
        this.driver=driver;
    }

    @Override
    public void setUri(Uri driveruri) {
        this.driveruri=driveruri;
    }



}