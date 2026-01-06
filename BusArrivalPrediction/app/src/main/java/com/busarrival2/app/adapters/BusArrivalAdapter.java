package com.busarrival2.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.busarrival2.app.R;
import com.busarrival2.app.models.BusRoute;
import com.busarrival2.app.models.BusStop;

import java.util.List;

/**
 * BusArrivalAdapter
 * RecyclerView adapter for displaying bus arrival predictions
 */
public class BusArrivalAdapter extends RecyclerView.Adapter<BusArrivalAdapter.ArrivalViewHolder> {

    private Context context;
    private BusRoute route;
    private BusStop stop;
    private List<Integer> arrivalMinutes;

    public BusArrivalAdapter(Context context, BusRoute route, BusStop stop, 
                            List<Integer> arrivalMinutes) {
        this.context = context;
        this.route = route;
        this.stop = stop;
        this.arrivalMinutes = arrivalMinutes;
    }

    @NonNull
    @Override
    public ArrivalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bus_arrival, 
                parent, false);
        return new ArrivalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArrivalViewHolder holder, int position) {
        int minutes = arrivalMinutes.get(position);

        holder.tvRouteName.setText(route.toString());
        holder.tvStopName.setText(stop.getStopName());
        holder.tvArrivalTime.setText(String.valueOf(minutes));
    }

    @Override
    public int getItemCount() {
        return arrivalMinutes.size();
    }

    /**
     * ViewHolder class for arrival items
     */
    static class ArrivalViewHolder extends RecyclerView.ViewHolder {
        TextView tvRouteName, tvStopName, tvArrivalTime;

        public ArrivalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvStopName = itemView.findViewById(R.id.tvStopName);
            tvArrivalTime = itemView.findViewById(R.id.tvArrivalTime);
        }
    }
}
