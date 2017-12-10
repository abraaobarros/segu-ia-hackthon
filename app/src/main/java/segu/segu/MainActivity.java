package segu.segu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LocationListener {


    protected LocationManager locationManager;
    String provider;
    Location actualLocation;

    @BindView(R.id.taxes)
    TextView velocity;

    @BindView(R.id.activate)
    Button activate;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.total)
    TextView total;

    @BindView(R.id.total_description)
    TextView total_description;

    @BindView(R.id.credit_int)
    TextView credit;

    double tx = 0.0012;

    ArrayList<Dado> dados = new ArrayList<>();
    double ttl = 100.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), false);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(provider, 500, 1, this);
            actualLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            velocity.setText(String.format("R$%.2f/h",tx * 60));
        }else{
            checkLocationPermission();
        }

        dados = new IAReaderStream().read(this);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("aqui")
                        .setMessage("aqui")
                        .setPositiveButton("aqui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 20000, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    double getTx(){
        for (Dado dado:dados){
            if (dado.isNear(actualLocation)){
                if(dado.isBetweenTime(new Date())){
                    return dado.tarifa;
                }
            }
        }
        return tx;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onLocationChanged(Location location) {
        actualLocation = location;
        tx = 10*3.6/100;
        velocity.setText(String.format("R$ %.4f/min",getTx()));
        hideLoading();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @OnClick(R.id.create_car_button)
    public void openMAp(){
        Intent intent = new Intent(this, CreateCarActivity.class);
//        intent.putExtra("lat",actualLocation.getLatitude());
//        intent.putExtra("lng",actualLocation.getLongitude());
        startActivity(intent);
    }

    ProgressDialog progressDialog;
    void showLoading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Carregando sua localização");
        progressDialog.show();
    }

    boolean isActive = true;

    @SuppressLint("NewApi")
    @OnClick(R.id.activate)
    public void onClickActivate(){
        if (isActive){
            activate.setText("Desativar Seguro");
            activate.setBackgroundColor(getColor(R.color.colorAccent));
            total_description.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            startCounterTime();
            isActive = false;
        }else{
            activate.setText("Ativar Seguro");
            activate.setBackgroundColor(getColor(R.color.colorPrimaryDark));
            total.setVisibility(View.INVISIBLE);
            total_description.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            stopTime();
            isActive = true;
        }
    }

    int count;
    Timer T = new Timer();
    public void startCounterTime(){
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        total.setText(String.format("R$ %.4f ",count*getTx()/60));
                        total.setVisibility(View.VISIBLE);
                    }
                });
                count++;
            }
        }, 1000, 1000);
    }
    public void stopTime(){
        T.cancel();
        T= new Timer();
        ttl -= count*getTx()/60;
        count = 0;
        credit.setText(String.format("Creditos: R$ %.2f ",ttl));
    }


    void hideLoading(){
        if(progressDialog!=null)
            progressDialog.hide();
    }
}
