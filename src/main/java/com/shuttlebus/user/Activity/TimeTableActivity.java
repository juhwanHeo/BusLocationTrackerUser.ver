package com.shuttlebus.user.Activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shuttlebus.user.R;
import com.shuttlebus.user.Process.Scheduler;
import com.shuttlebus.user.Adapter.TimeTableListAdapter;

public class TimeTableActivity extends AppCompatActivity {
    private ListView theListView;
    private TimeTableListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        setAdapter();
    }

    private void setAdapter(){
        Scheduler sd = new Scheduler();
        theListView = findViewById(R.id.listVIew_time_table);
        mAdapter = new TimeTableListAdapter(this, sd.getTimeTable());

        // set elements to adapter
        theListView.setAdapter(mAdapter);

    }
}
