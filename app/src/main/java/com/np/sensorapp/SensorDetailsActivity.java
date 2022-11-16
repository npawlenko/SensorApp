package com.np.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener2 {

    public static final String SENSOR_TAG = "sensor";
    public static final String KEY_EXTRA_SENSOR_ID = "KEY_EXTRA_SENSOR_ID";

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        sensorTextView = findViewById(R.id.sensor_label);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        int type = getIntent().getIntExtra(KEY_EXTRA_SENSOR_ID, -1);
        if(type != -1) {
            sensor = sensorManager.getDefaultSensor(type);
        }

        if(sensor == null) {
            sensorTextView.setText(R.string.missing_sensor);
        }
        else {
            sensorTextView.setText(getResources().getString(R.string.sensor_label, 0F));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];

        switch(sensorType) {
            case Sensor.TYPE_LIGHT:
                sensorTextView.setText(getResources().getString(R.string.light_sensor_label, currentValue));
                break;
            case Sensor.TYPE_PROXIMITY:
                sensorTextView.setText(getResources().getString(R.string.proximity_sensor_label, currentValue));
                break;
            default:
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(SENSOR_TAG, "Wywołana została metoda onAccuracyChanged z wartością " + i);
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {

    }
}