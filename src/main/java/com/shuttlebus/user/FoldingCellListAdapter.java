package com.shuttlebus.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<Item> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public FoldingCellListAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // get item for selected view
        Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
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
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.unfold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();

        }


        if (null == item)
            return cell;

        // bind data from selected element to view through view holder

        viewHolder.content_startStation_text.setText(item.getStartStation());
        viewHolder.startStation.setText(item.getStartStation());
        viewHolder.content_endStation_text.setText(item.getEndStation());
        viewHolder.endStation.setText(item.getEndStation());
        viewHolder.progress.setText(item.getProgress() + "%");
        viewHolder.content_progress_text.setText(item.getProgress()+"%");
        viewHolder.content_date_text.setText(item.getDate());
        viewHolder.content_time_text.setText(item.getTime());
        viewHolder.title_distance.setText(String.format("%.2fKm",item.getDistance()));
        viewHolder.content_distance_text.setText(String.format("%.2fKm",item.getDistance()));
        if(item.getLateTime() >= 1.5){
            viewHolder.content_lateTIme_Text.setText((int)item.getLateTime()+"분");
        }
        else{
            viewHolder.content_lateTIme_Text.setText("곧 도착");
        }


        if(item.getArrived()){

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
    }
}
