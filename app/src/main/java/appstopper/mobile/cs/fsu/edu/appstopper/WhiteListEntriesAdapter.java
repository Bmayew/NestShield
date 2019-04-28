package appstopper.mobile.cs.fsu.edu.appstopper;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WhiteListEntriesAdapter extends RecyclerView.Adapter<WhiteListEntriesAdapter.MyViewHolder> {
    private static ArrayList<WhitelistEntry> myDataset;
    private static Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Switch aSwitch;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.entries_item_name);
            aSwitch = (Switch) view.findViewById(R.id.entries_item_checkbox);;
        }
    }

    public WhiteListEntriesAdapter(ArrayList<WhitelistEntry> mList, Context cntxt) {
        myDataset = mList;
        context = cntxt;
    }
    @Override
    public WhiteListEntriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.whitelist_entries_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(myDataset.get(holder.getAdapterPosition()).packageName);

        holder.aSwitch.setChecked(myDataset.get(holder.getAdapterPosition()).isWhitelisted);

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cView, boolean isChecked) {
                if (isChecked) {
                    Log.v("StopperService", myDataset.get(holder.getAdapterPosition()).packageName + "checked");
                    myDataset.get(holder.getAdapterPosition()).isWhitelisted = true;
                    new UpdateTask().execute(myDataset.get(holder.getAdapterPosition()));
                } else {
                    Log.v("StopperService", myDataset.get(holder.getAdapterPosition()).packageName + "checked");
                    myDataset.get(holder.getAdapterPosition()).isWhitelisted = true;
                    new UpdateTask().execute(myDataset.get(holder.getAdapterPosition()));
                }
            }


        });
    }

    public static class UpdateTask extends AsyncTask<WhitelistEntry, Void, Void> {
        protected Void doInBackground(WhitelistEntry... entries) {
            Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "Whitelist").build().entryDao().updateEntry(entries[0]);
            return null;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}
