package com.tugoapp.mobile.ui.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.OnListItemClickListener

class OrderHistoryListAdapter(private val context: Context,
                              private val list: ArrayList<String>,
                              private val cellClickListener: OnListItemClickListener
) : RecyclerView.Adapter<OrderHistoryListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        val iconIV: ImageView = view.findViewById(R.id.iconIV)
//        val titleTV: TextView = view.findViewById(R.id.titleTV)
//        val subtitleTV: TextView = view.findViewById(R.id.subtitleTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order_history,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
//        holder.iconIV.setImageDrawable(ContextCompat.getDrawable(context, data.icon))
//        holder.titleTV.text = data.title
//        holder.subtitleTV.text = data.subtitle

        holder.itemView.setOnClickListener {
            cellClickListener.onListItemClick(position)
        }
    }
}