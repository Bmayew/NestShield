package appstopper.mobile.cs.fsu.edu.appstopper;

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView

class WhitelistEntriesAdapter (private val myDataSet: ArrayList<String>):
        RecyclerView.Adapter<WhitelistEntriesAdapter.MyViewHolder>(){

    class MyViewHolder(val mView: View): RecyclerView.ViewHolder(mView) {
        val name = mView.findViewById(R.id.entries_item_name) as TextView
        val checkBox = mView.findViewById(R.id.entries_item_checkbox) as CheckBox
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): WhitelistEntriesAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.whitelist_entries_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = myDataSet[position]
        holder.checkBox.isChecked = true
    }
    override fun getItemCount() = myDataSet.size
}