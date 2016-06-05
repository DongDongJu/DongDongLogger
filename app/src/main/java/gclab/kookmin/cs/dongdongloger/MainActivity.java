package gclab.kookmin.cs.dongdongloger;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private double lat;
    private double longi;
    GPSListener gpsListener;
    LocationManager man;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button getLocationButton = (Button) findViewById(R.id.button);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationService();
            }
        });
        final Button startMapView = (Button) findViewById(R.id.button4);
        startMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntet = new Intent(getApplicationContext(),getOwnLocationActivity.class);
                mapIntet.putExtra("lat",lat);
                mapIntet.putExtra("longi",longi);
                startActivity(mapIntet);
            }
        });
        final Button saveButton = (Button) findViewById(R.id.button2);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent saveIntent = new Intent(getApplicationContext(),saveActivity.class);
                saveIntent.putExtra("longi",longi);
                saveIntent.putExtra("lat",lat);
                startActivity(saveIntent);
            }
        });
        final Button viewALlButton = (Button)findViewById(R.id.button3);
        viewALlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewALlIntent = new Intent(getApplicationContext(),viewAllActivity.class);
                startActivity(viewALlIntent);
            }
        });
    }

    private void getLocationService() {
        man= (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gpsListener= new GPSListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        man.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, gpsListener);
        String debu = "lat =" +lat + "longi " +longi;
        Toast.makeText(getApplicationContext(),debu,Toast.LENGTH_LONG).show();
    }
    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            longi = location.getLongitude();
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}
