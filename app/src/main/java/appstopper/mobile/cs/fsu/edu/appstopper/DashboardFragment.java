package appstopper.mobile.cs.fsu.edu.appstopper;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private static final String TAG = "DashboardFragment";
    private RecyclerView entriesView;
    private static RecyclerView.Adapter viewAdapter;
    private RecyclerView.LayoutManager viewManager;
    private static ArrayList<WhitelistEntry> entriesDataset = new ArrayList<>();
    private static EntryDao entryDao;
    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        entryDao = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "Whitelist").build().entryDao();

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        entriesView = (RecyclerView) root.findViewById(R.id.entries_view);
        viewManager = new LinearLayoutManager(root.getContext());
        viewAdapter = new WhiteListEntriesAdapter(entriesDataset, getActivity().getApplicationContext());

        entriesView.setLayoutManager(viewManager);
        entriesView.setAdapter(viewAdapter);

        new GetEntryTask().execute(null, null, null);
        return root;
    }

    private static class GetEntryTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... voids) {
            List<WhitelistEntry> temp = entryDao.getAll();
            entriesDataset = new ArrayList<>();
            for (WhitelistEntry i : temp) {
                if (i != null) {
                    Log.v("StopperService", "getEntry: " + i.packageName);
                    entriesDataset.add(i);
                }
            }
            return null;
        }
    }
}
