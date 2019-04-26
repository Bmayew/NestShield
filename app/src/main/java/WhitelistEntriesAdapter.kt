package appstopper.mobile.cs.fsu.edu.appstopper;

import android.app.PendingIntent.getActivity
import android.arch.persistence.room.Room
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Switch
import android.widget.TextView

class WhitelistEntriesAdapter (private val myDataSet: List<String>, val context: Context):
        RecyclerView.Adapter<WhitelistEntriesAdapter.MyViewHolder>(){

    class MyViewHolder(val mView: View): RecyclerView.ViewHolder(mView) {
        val name = mView.findViewById(R.id.entries_item_name) as TextView
        val switch = mView.findViewById(R.id.entries_item_checkbox) as Switch
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): WhitelistEntriesAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.whitelist_entries_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = myDataSet[position]

        holder.switch.isChecked = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "Whitelist"
        ).build().entryDao().isWhitelisted(myDataSet[position])

        holder.switch.setOnCheckedChangeListener{ b, isChecked ->
            if (isChecked) {
                // Set database entry to true
            } else {
                // Set database entry to false
            }
        }
    }
    override fun getItemCount() = myDataSet.size
}