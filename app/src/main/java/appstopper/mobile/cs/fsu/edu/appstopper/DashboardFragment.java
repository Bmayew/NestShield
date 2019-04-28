package appstopper.mobile.cs.fsu.edu.appstopper;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private static final String PREFS_NAME = "DevicePrefsFile"; // Name of shared preferences file
    private EntryViewModel entryViewModel;
    private static final String TAG = "DashboardFragment";
    private RecyclerView entriesView;
    private RecyclerView.LayoutManager viewManager;
    private static ArrayList<WhitelistEntry> entriesDataset = new ArrayList<>();
    private static EntryDao entryDao;
    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        RecyclerView entriesView = root.findViewById(R.id.entries_view);
        entryViewModel = ViewModelProviders.of(this).get(EntryViewModel.class);
        final WhiteListEntriesAdapter viewAdapter = new WhiteListEntriesAdapter(this.getContext(), entryViewModel);
        entryViewModel.getAllEntries().observe(this, new Observer<List<WhitelistEntry>>() {
            @Override
            public void onChanged(@Nullable final List<WhitelistEntry> entries) {
                // Update the cached copy of the words in the adapter.
                viewAdapter.setEntries(entries);
                viewAdapter.notifyDataSetChanged();
                Log.v("Database", "changed");
            }
        });
        entriesView.setAdapter(viewAdapter);
        entriesView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        //new GetEntryTask().execute(null, null, null);
        return root;
    }
}
