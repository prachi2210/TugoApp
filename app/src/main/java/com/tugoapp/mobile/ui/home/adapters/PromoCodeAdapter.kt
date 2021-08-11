package com.tugoapp.mobile.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.home.Data
import kotlinx.android.synthetic.main.rv_promo_code.view.*

class PromoCodeAdapter(
    val context: Context,
    private val dataItem: ArrayList<Data>,
    val planId: String
) : androidx.recyclerview.widget.RecyclerView.Adapter<PromoCodeAdapter.RVHolder>() {

    var selectedPromoCode: SelectedPromoCode? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVHolder {
        return RVHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_promo_code, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataItem.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.bind(dataItem[position])
    }

    inner class RVHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(dataItem: Data) {

            itemView.promoDescription.text = dataItem.discription
            itemView.btnPromoCode.text = dataItem.promoCode

            if (dataItem.mealId == planId) {
                itemView.tvPromoApply.setTextColor(context.getColor(R.color.colorPrimary))
                itemView.tvPromoApply.text = context.getString(R.string.apply)
                itemView.tvPromoApply.setOnClickListener {
                    selectedPromoCode?.onClick(dataItem)
                }

            } else {
                itemView.tvPromoApply.setTextColor(context.getColor(R.color.flat_red_1))
                itemView.tvPromoApply.text = context.getString(R.string.promo_code_unavailable)
            }
        }

    }

    interface SelectedPromoCode {
        fun onClick(dataItem: Data)
    }

}