package com.tugoapp.mobile.ui.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.OrderModel
import com.tugoapp.mobile.ui.base.OnListItemClickListener

class OrderHistoryListAdapter(private val context: Context,
                              private val isHistoryScreen : Boolean,
                              private val list: ArrayList<OrderModel>,
                              private val cellClickListener: OnListItemClickListener
) : RecyclerView.Adapter<OrderHistoryListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgHistoryOrder: ImageView = view.findViewById(R.id.imgHistoryOrder)
        val txtOrderTitle: TextView = view.findViewById(R.id.txtOrderTitle)
        val txtOrderSubTitle: TextView = view.findViewById(R.id.txtOrderSubTitle)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        val txtPlanDetail: TextView = view.findViewById(R.id.txtPlanDetail)
        val txtExpiryDate: TextView = view.findViewById(R.id.txtExpiryDate)
       // val isOngoing: TextView = view.findViewById(R.id.txtExpiryDate)
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
        holder.txtOrderTitle.text = data.companyName
        holder.txtOrderSubTitle.text = data.planName
        holder.txtPrice.text = data.price + " AED"
      //  holder.txtPlanDetail.text = data.subtitle
        if(isHistoryScreen) {
            holder.txtExpiryDate.text = String.format(context.getString(R.string.txt_order_expired_on),data.expiredOn)
        } else {
            holder.txtExpiryDate.text = String.format(context.getString(R.string.txt_order_started_on),data.startedOn)
        }

        Glide.with(context)
                .load(data.companyLogo)
                .centerCrop()
                .into(holder.imgHistoryOrder)

        holder.itemView.setOnClickListener {
            cellClickListener.onListItemClick(position)
        }
    }
}