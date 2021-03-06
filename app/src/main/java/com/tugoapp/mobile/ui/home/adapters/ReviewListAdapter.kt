package com.tugoapp.mobile.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.data.remote.model.response.ReviewModel
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReviewListAdapter(private val context: Context,
                        private val list: ArrayList<ReviewModel>,
                        private val cellClickListener: OnListItemClickListener
) : RecyclerView.Adapter<ReviewListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtUserName: TextView = view.findViewById(R.id.txtUserName)
        val txtUserMealPlan: TextView = view.findViewById(R.id.txtUsermealPlan)
        val txtReview: TextView = view.findViewById(R.id.txtReview)
        val txtRateMark: RatingBar = view.findViewById(R.id.rateMark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_review,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.txtUserName.text = data.userName
        holder.txtUserMealPlan.text = data.meals  + " meals per day for "  +  data.noOfDays +" days"
        holder.txtReview.text = data.reviewText
        holder.txtRateMark.rating = data.starRating?.toFloat()!!
        if(!data.addedOn.isNullOrBlank()) {
            val apiFormat = "dd/MM/yyyy"
            val sdfApiFormat = SimpleDateFormat(apiFormat)
            var calender = Calendar.getInstance()
            calender.timeInMillis = data?.addedOn?.toLong()!!
            holder.txtDate.text = sdfApiFormat.format(calender.time)
        }
        holder.itemView.setOnClickListener {
            cellClickListener.onListItemClick(position)
        }
    }
}