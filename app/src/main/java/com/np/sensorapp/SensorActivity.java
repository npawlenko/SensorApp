package com.np.sensorapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class SensorActivity extends AppCompatActivity {

    public static final String KEY_SUBTITLE_VISIBLE = "KEY_SUBTITLE_VISIBLE";

    public static final int[] SUPPORTED_SENSORS = new int[]{
            Sensor.TYPE_LIGHT,
            Sensor.TYPE_PROXIMITY,
            Sensor.TYPE_MAGNETIC_FIELD,
            Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED
    };

    private SensorManager sensorManager;
    private List<Sensor> sensorList;

    private RecyclerView recyclerView;
    private SensorAdapter sensorAdapter;

    private boolean subtitleVisible = false;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SUBTITLE_VISIBLE, subtitleVisible);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(KEY_SUBTITLE_VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.sensor_menu, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else  {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        String subtitle = getString(R.string.sensors_count, sensorList.size());
        if(!subtitleVisible) {
            subtitle = null;
        }
        getSupportActionBar().setSubtitle(subtitle);
    }

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



    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Sensor sensor;
        private View container;
        private TextView sensorTextView;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            itemView.setOnClickListener(this);

            container = itemView.findViewById(R.id.sensorContainer);
            sensorTextView = itemView.findViewById(R.id.sensorNameTextView);
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            sensorTextView.setText(sensor.getName());

            boolean supported = Arrays.stream(SUPPORTED_SENSORS)
                    .anyMatch(sensorType -> sensorType == sensor.getType());
            if(supported) {
                container.setBackgroundResource(R.color.supported_background);
            }
            else {
                container.setBackgroundResource(R.color.unsupported_background);
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent;
            switch(sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    intent = new Intent(getApplicationContext(), LocationActivity.class);
                    startActivity(intent);
                    break;
                default:
                    intent = new Intent(getApplicationContext(), SensorDetailsActivity.class);
                    intent.putExtra(SensorDetailsActivity.KEY_EXTRA_SENSOR_ID, sensor.getType());
                    startActivity(intent);
            }
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