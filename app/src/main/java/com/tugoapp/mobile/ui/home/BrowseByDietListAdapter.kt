package com.tugoapp.mobile.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.orders.OrderHistoryListAdapter
import kotlinx.android.synthetic.main.fragment_history_orders.*

class BrowseByDietListAdapter(private val context: Context,
                              private val list: ArrayList<Int>,
                              private val cellClickListener: OnListItemClickListener
) : RecyclerView.Adapter<BrowseByDietListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imgBrowseByDiet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_browse_by_diet,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.image.setImageDrawable(ContextCompat.getDrawable(context, data))

        holder.itemView.setOnClickListener {
            cellClickListener.onListItemClick(position)
        }
    }
}