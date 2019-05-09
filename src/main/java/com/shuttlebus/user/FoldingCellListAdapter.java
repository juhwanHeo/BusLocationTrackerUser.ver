package com.shuttlebus.user;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.HashSet;

import androidx.annotation.NonNull;

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

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Item item = null;
        // get item for selected view
        try {
            item = getItem(position);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.arrived_relative_layout = cell.findViewById(R.id.arrived_relative_layout);
            viewHolder.price = cell.findViewById(R.id.title_price);
            viewHolder.time = cell.findViewById(R.id.title_time_label);
            viewHolder.date = cell.findViewById(R.id.title_date_label);
            viewHolder.startStation = cell.findViewById(R.id.title_from_address);
            viewHolder.endStation = cell.findViewById(R.id.title_to_address);
            viewHolder.progress = cell.findViewById(R.id.title_requests_count);
            viewHolder.content_date_text = cell.findViewById(R.id.content_date_text);
            viewHolder.content_progress_text = cell.findViewById(R.id.content_progress_text);
            viewHolder.content_time_text = cell.findViewById(R.id.content_time_text);
            viewHolder.content_lateTIme_Text = cell.findViewById(R.id.content_lateTIme_Text);
            viewHolder.content_startStation_text = cell.findViewById(R.id.content_startStation_text);
            viewHolder.content_endStation_text = cell.findViewById(R.id.content_endStation_text);
            viewHolder.content_distance_text = cell.findViewById(R.id.head_image_center_text);
            viewHolder.title_distance = cell.findViewById(R.id.title_distance);
            viewHolder.title_seekBar = cell.findViewById(R.id.title_seekBar);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
//            if (unfoldedIndexes.contains(position)) {
//                cell.unfold(true);
//            } else {
//                cell.unfold(true);
//            }
            viewHolder = (ViewHolder) cell.getTag();

        }


        if (null == item)
            return cell;

        // bind data from selected element to view through view holder

        viewHolder.content_startStation_text.setText(item.getStartStation());
        viewHolder.startStation.setText(item.getStartStation());
        viewHolder.content_endStation_text.setText(item.getEndStation());
        viewHolder.endStation.setText(item.getEndStation());

        viewHolder.content_progress_text.setText(item.getProgress() + "%");

        viewHolder.content_date_text.setText(item.getDate());
        viewHolder.content_time_text.setText(item.getTime());

        viewHolder.title_distance.setText(String.format("%.2fKm", item.getDistance()));
        viewHolder.content_distance_text.setText(String.format("%.2fKm", item.getDistance()));

        item.setSeekBar(viewHolder.title_seekBar);
        viewHolder.title_seekBar.setProgress(item.getProgress());

        if (position != 0) {
            /*
             * 버스가 전역에 도착 햇다면.
             */
            if (getItems(position - 1).getArrived()) {

                getItems(position - 1).getSeekBar().setVisibility(View.INVISIBLE);
                viewHolder.title_seekBar.setVisibility(View.VISIBLE);
            } else {
                viewHolder.title_seekBar.setVisibility(View.INVISIBLE);
            }
            /*
             * 0번째 리스트뷰 이면
             */
        } else {
            if (item.getArrived()) {
                viewHolder.title_seekBar.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.title_seekBar.setVisibility(View.VISIBLE);
            }
        }

        if (item.getArrived()) {
            cell.setClickable(false);
            cell.setEnabled(false);

            if (Build.VERSION.SDK_INT >= 24) {
                cell.setContextClickable(false);
            }
            viewHolder.arrived_relative_layout.setBackgroundColor(getContext().getResources().getColor(R.color.bgTitleLeft));
            viewHolder.title_seekBar.setProgress(100);
            viewHolder.progress.setText("");

        }
        else{
            viewHolder.progress.setText(item.getProgress() + "%");
            viewHolder.arrived_relative_layout.setBackgroundColor(getContext().getResources().getColor(R.color.notArrivedColor));
        }


        // 정류장 도착
        if (item.getArrived()) {
            viewHolder.content_lateTIme_Text.setText("도착");

        } else if (item.getLateTime() >= 1.5) {
            viewHolder.content_lateTIme_Text.setText((int) item.getLateTime() + "분");
        } else {
            viewHolder.content_lateTIme_Text.setText("곧도착");
        }

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
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

        TextView price;
        //        TextView pledgePrice;
        TextView startStation;
        TextView endStation;
        TextView progress;
        TextView date;
        TextView time;
        TextView content_date_text;
        TextView content_progress_text;
        TextView content_time_text;
        TextView content_lateTIme_Text;
        TextView content_startStation_text;
        TextView content_endStation_text;
        TextView content_distance_text;
        TextView title_distance;
        BubbleSeekBar title_seekBar;
    }
}
