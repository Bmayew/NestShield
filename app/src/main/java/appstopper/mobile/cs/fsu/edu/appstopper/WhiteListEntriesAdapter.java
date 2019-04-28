package appstopper.mobile.cs.fsu.edu.appstopper;

import android.arch.lifecycle.ViewModelProviders;
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

import java.util.List;

public class WhiteListEntriesAdapter extends RecyclerView.Adapter<WhiteListEntriesAdapter.MyViewHolder> {
    private List<WhitelistEntry> myDataset; //Cached copy of entries
    private final LayoutInflater mInflater;
    private EntryViewModel mEntryViewModel;
    private Context mContext;

    WhiteListEntriesAdapter(Context context, EntryViewModel entryViewModel) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mEntryViewModel = entryViewModel;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.whitelist_entries_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        WhitelistEntry current = myDataset.get(position);
        holder.aSwitch.setTag(current.packageName);
        holder.name.setText(current.labelName);
        holder.aSwitch.setOnCheckedChangeListener(null);
        holder.aSwitch.setChecked(current.isWhitelisted);
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cView, boolean isChecked) {
                if (isChecked) {
                    mEntryViewModel.setWhitelisted("true", cView.getTag().toString());
                } else {
                    mEntryViewModel.setWhitelisted("false", cView.getTag().toString());
                }
            }
        });
    }
    void setEntries(List<WhitelistEntry> entries) {
        myDataset = entries;
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(myDataset != null)
            return myDataset.size();
        else
            return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final Switch aSwitch;
        private MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.entries_item_name);
            aSwitch = view.findViewById(R.id.entries_item_checkbox);;
        }
    }
}
