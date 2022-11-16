package com.np.sensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private List<Sensor> sensorList;

    private RecyclerView recyclerView;
    private SensorAdapter sensorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if(sensorAdapter == null) {
            sensorAdapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(sensorAdapter);
        }
        else {
            sensorAdapter.notifyDataSetChanged();
        }
    }

    private class SensorHolder extends RecyclerView.ViewHolder {

        private Sensor sensor;
        private ImageView sensorImageView;
        private TextView sensorTextView;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            sensorImageView = itemView.findViewById(R.id.sensorIconImageView);
            sensorTextView = itemView.findViewById(R.id.sensorNameTextView);
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            sensorTextView.setText(sensor.getName());
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private List<Sensor> sensors;

        public SensorAdapter(List<Sensor> tasks) {
            this.sensors = tasks;
        }


        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new SensorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensors.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }
}