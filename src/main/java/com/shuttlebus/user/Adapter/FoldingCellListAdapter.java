package com.shuttlebus.user.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuttlebus.user.DB.Item;
import com.shuttlebus.user.R;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.HashSet;

import androidx.annotation.NonNull;

import static android.view.View.VISIBLE;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> items;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public FoldingCellListAdapter(Context context, ArrayList<Item> objects) {
        super(context, 0, objects);
        this.items = objects;
    }

    private Item getItems(int position) throws NullPointerException {
        return items.get(position);
    }
    private String getPreviousStation(int position){
        return items.get(position-1).getStartStation();
    }

    private String getNextStation(int position){
        return items.get(position).getEndStation();
    }



    @SuppressLint({"ViewHolder", "SetTextI18n", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Item item = getItem(position);

        // get item for selected view
        // if cell is exists - reuse it, if not - create the new one from resource
        ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        LayoutInflater vi = LayoutInflater.from(getContext());
        convertView = vi.inflate(R.layout.cell, parent, false);
        // binding view parts to view holder
        viewHolder.background_tv = convertView.findViewById(R.id.background_tv);
        viewHolder.arrived_relative_layout = convertView.findViewById(R.id.arrived_relative_layout);
        viewHolder.price = convertView.findViewById(R.id.title_price);
        viewHolder.time = convertView.findViewById(R.id.title_time_label);
        viewHolder.date = convertView.findViewById(R.id.title_date_label);
        viewHolder.startStation = convertView.findViewById(R.id.title_from_address);
        viewHolder.endStation = convertView.findViewById(R.id.title_to_address);
        viewHolder.progress = convertView.findViewById(R.id.title_requests_count);
//        viewHolder.content_date_text = convertView.findViewById(R.id.content_date_text);
        viewHolder.content_progress_text = convertView.findViewById(R.id.content_progress_text);
//        viewHolder.content_time_text = convertView.findViewById(R.id.content_time_text);
        viewHolder.content_lateTIme_Text = convertView.findViewById(R.id.content_lateTIme_Text);
        viewHolder.content_previousStation = convertView.findViewById(R.id.content_previousStation);
        viewHolder.content_nextStation = convertView.findViewById(R.id.content_nextStation);
        viewHolder.content_distance_text = convertView.findViewById(R.id.head_image_center_text);
        viewHolder.title_distance = convertView.findViewById(R.id.title_distance);
        viewHolder.title_seekBar = convertView.findViewById(R.id.title_seekBar);
        convertView.setTag(viewHolder);

        // bind data from selected element to view through view holder

        if (item != null) {
            viewHolder.startStation.setText(item.getStartStation());
            viewHolder.endStation.setText(item.getEndStation());
            viewHolder.content_progress_text.setText(item.getProgress() + "%");
            viewHolder.content_distance_text.setText(String.format("%.2fKm", item.getDistance()));
            item.setSeekBar(viewHolder.title_seekBar);
            viewHolder.title_seekBar.setProgress(item.getProgress());
        }
//        viewHolder.content_date_text.setText(item.getDate());
//        viewHolder.content_time_text.setText(item.getTime());



        if(position != 0){
            viewHolder.content_previousStation.setText(getPreviousStation(position));
            viewHolder.content_nextStation.setText(getNextStation(position));
        }else{
            viewHolder.content_previousStation.setText("없음");
            viewHolder.content_nextStation.setText(getNextStation(position));
        }

        /*
        현재 버스가 진행중인 부분만 SeekBar 표시
         */
        if (position != 0) {
            /*
             * 버스가 전역에 도착 햇다면.
             */
            if (getItems(position - 1).getArrived()) {

                getItems(position - 1).getSeekBar().setVisibility(View.INVISIBLE);
                viewHolder.title_seekBar.setVisibility(VISIBLE);
            } else {
                viewHolder.title_seekBar.setVisibility(View.INVISIBLE);
            }

            /*
             * 0번째 리스트뷰 이면
             */
        } else {
            if (item != null) {
                if (item.getArrived()) {
                    viewHolder.title_seekBar.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.title_seekBar.setVisibility(VISIBLE);
                }
            }
        }


        /*
         Color
         */
        if (item != null) {
            if (item.getArrived()) {
                convertView.findViewById(R.id.list_cell).setClickable(false);
                convertView.findViewById(R.id.list_cell).setEnabled(false);

                if (Build.VERSION.SDK_INT >= 24) {
                    convertView.setContextClickable(false);
                }
                viewHolder.arrived_relative_layout.setBackgroundColor(getContext().getResources().getColor(R.color.busArrivedColor));
                viewHolder.background_tv.setBackgroundColor(getContext().getResources().getColor(R.color.busArrivedColor));
                viewHolder.title_seekBar.setProgress(100);
                viewHolder.content_progress_text.setText("100%");
                viewHolder.progress.setText("");

            }
            else if(viewHolder.title_seekBar.getVisibility() == VISIBLE){
                viewHolder.progress.setText(item.getProgress() + "%");
                viewHolder.arrived_relative_layout.setBackgroundColor(getContext().getResources().getColor(R.color.currentRunning));
                viewHolder.background_tv.setBackgroundColor(getContext().getResources().getColor(R.color.currentRunning));

            }
            else {
                viewHolder.progress.setText(item.getProgress() + "%");
                viewHolder.arrived_relative_layout.setBackgroundColor(getContext().getResources().getColor(R.color.notArrivedColor));
                viewHolder.background_tv.setBackgroundColor(getContext().getResources().getColor(R.color.notArrivedColor));
            }
        }


        // 정류장 도착
        if (item != null) {
            if (item.getArrived()) {
                viewHolder.content_lateTIme_Text.setText("도착");
                viewHolder.title_distance.setText("도착");
            } else if (item.getLateTime() >= 1) {
                viewHolder.content_lateTIme_Text.setText((int) item.getLateTime() + "분");
                viewHolder.title_distance.setText((int)item.getLateTime()+"분");
            } else {
                viewHolder.content_lateTIme_Text.setText("곧 도착");
                viewHolder.title_distance.setText("곧 도착");
            }
        }

        return convertView;
    }


    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        RelativeLayout arrived_relative_layout;
        TextView background_tv;
        TextView price;
        //TextView pledgePrice;
        TextView startStation;
        TextView endStation;
        TextView progress;
        TextView date;
        TextView time;
//        TextView content_date_text;
        TextView content_progress_text;
//        TextView content_time_text;
        TextView content_lateTIme_Text;
        TextView content_previousStation;
        TextView content_nextStation;
        TextView content_distance_text;
        TextView title_distance;
        BubbleSeekBar title_seekBar;
    }
}
