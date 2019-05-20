package com.shuttlebus.user.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.shuttlebus.user.R;
import com.shuttlebus.user.TimeTable.TimeTable;

import org.jetbrains.annotations.NotNull;

public class TimeTableListAdapter extends ArrayAdapter<TimeTable> {

    public TimeTableListAdapter(@NonNull Context context, TimeTable[] object) {
        super(context, 0, object);
    }

    @NotNull
    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TimeTable timeTable = getItem(position);
        LayoutInflater vi = LayoutInflater.from(getContext());
        convertView = vi.inflate(R.layout.item_time_table, parent, false);
        ViewHolder viewHolder = findViews(convertView);

       if (timeTable != null) {
                if (timeTable.getMinute() > 9) {
                    viewHolder.item_time_table.setText(timeTable.getHour() + "시 " + timeTable.getMinute() + "분");
                } else {
                    viewHolder.item_time_table.setText(timeTable.getHour() + "시 0" + timeTable.getMinute() + "분");
                }
            }

        return convertView;
    }

    private static class ViewHolder {
        TextView item_time_table;

    }

    private ViewHolder findViews(View convertView){
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.item_time_table = convertView.findViewById(R.id.item_time_table);

        return viewHolder;
    }
}
