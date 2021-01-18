package pl.edu.pb.runnerapp

import android.os.Bundle

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.util.Log

class MainActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CHECKUJE"," ON CREATE ACTIVIMAIN");
        setContentView(R.layout.activity_login)
    }
}