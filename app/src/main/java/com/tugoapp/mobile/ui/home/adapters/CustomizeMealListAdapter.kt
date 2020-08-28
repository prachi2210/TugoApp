package com.tugoapp.mobile.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.CustomizeListModel
import com.tugoapp.mobile.ui.base.OnListItemClickListener

class CustomizeMealListAdapter(private val context: Context,
                               private val list: ArrayList<String>,
                               private val cellClickListener: OnListItemClickListener
) : RecyclerView.Adapter<CustomizeMealListAdapter.ViewHolder>() {


    var mSelectedPosition = -1

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_customize_list,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.name.text = data

        if(position == mSelectedPosition) {
            holder.name.setTextColor(context.getColor(R.color.colorPrimary))
        } else {
            holder.name.setTextColor(context.getColor(R.color.color999999))
        }

        holder.itemView.setOnClickListener {
            mSelectedPosition = position
            cellClickListener.onListItemClick(position)
            notifyDataSetChanged()
        }
    }
}