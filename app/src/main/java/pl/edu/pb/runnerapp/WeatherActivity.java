package pl.edu.pb.runnerapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class WeatherActivity extends AppCompatActivity {

    static final String TAG = "blond";
    final String API = "";
    final String URL = "https://api.openweathermap.org/data/2.5/weather";

    final long minTime = 5000;
    final float minDistance = 1000;
    final int requestCode = 101;

    String locationProvider = LocationManager.GPS_PROVIDER;

    TextView nameOfCity, weatherState, temperature;
    ImageView weatherIcon, backButton;
    RelativeLayout cityFinder;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherState = findViewById(R.id.weatherCondition);
        temperature = findViewById(R.id.weatherTemperature);
        weatherIcon = findViewById(R.id.weatherIcon);
        cityFinder = findViewById(R.id.weatherCityFinder);
        nameOfCity = findViewById(R.id.weatherCityName);
        backButton = findViewById(R.id.weatherBackImage);
        cityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, CityFinder.class);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        if (city != null) {
            getWeatherForNewCity(city);
        } else {
            getWeatherForCurrentLocation();
        }
    }

    private void getWeatherForNewCity(String city) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("q", city);
        requestParams.put("appid", API);
        letsDoSomeNetworking(requestParams);
    }

    private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                RequestParams requestParams = new RequestParams();
                requestParams.put("lat", latitude);
                requestParams.put("lon", longitude);
                requestParams.put("appid", API);
                letsDoSomeNetworking(requestParams);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missingf permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
            return;
        }
        locationManager.requestLocationUpdates(locationProvider, minTime, minDistance, locationListener);
    }

    private void letsDoSomeNetworking(RequestParams requestParams) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(WeatherActivity.this, "Data get Success", Toast.LENGTH_SHORT).show();

                WeatherData weatherData = WeatherData.fromJson(response);
                updateUI(weatherData);
            }
        });
    }

    private void updateUI(WeatherData weatherData) {
        temperature.setText(weatherData.getTemperature());
        nameOfCity.setText(weatherData.getCity());
        weatherState.setText(weatherData.getWeatherType());
        int resourceId = getResources().getIdentifier(weatherData.getIcon(), "drawable", getPackageName());
        weatherIcon.setImageResource(resourceId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == this.requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(WeatherActivity.this, "Location get Succesfully", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            } else {
                Toast.makeText(WeatherActivity.this, "Turn on localization", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
              ;
            }
        }
    }
}
