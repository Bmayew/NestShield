package appstopper.mobile.cs.fsu.edu.appstopper;

import android.app.PendingIntent.getActivity
import android.arch.persistence.room.Room
import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Switch
import android.widget.TextView
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.coroutineContext

class WhitelistEntriesAdapter (private val myDataSet: ArrayList<WhitelistEntry>, val context: Context):
        RecyclerView.Adapter<WhitelistEntriesAdapter.MyViewHolder>(){
    val db = Room.databaseBuilder(context.applicationContext,
        AppDatabase::class.java, "Whitelist").build()

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
        holder.name.text = myDataSet[position].labelName

        holder.switch.isChecked = myDataSet[position].isWhitelisted
        holder.switch.setOnCheckedChangeListener{ view, isChecked ->
            if (isChecked) {
                myDataSet[position].isWhitelisted = true
                UpdateTask(context).execute(myDataSet[position])
            } else {
                myDataSet[position].isWhitelisted = false
                UpdateTask(context).execute(myDataSet[position])
            }
        }
    }
    override fun getItemCount() = myDataSet.size

    class UpdateTask (val context:Context): AsyncTask<WhitelistEntry, Any, Any>()
    {
        override fun doInBackground(vararg params: WhitelistEntry?) {
            Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "Whitelist").build().entryDao().updateEntry(params[0])
        }
    }
}