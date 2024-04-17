package com.ajou.helptmanager.Membership

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R

class MembershipAdapter(private val membershipList: List<Membership>) :
    RecyclerView.Adapter<MembershipAdapter.MembershipViewHolder>() {

    class MembershipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textViewMembershipTitle)
        val price: TextView = view.findViewById(R.id.textViewMembershipPrice)
        val month_price: TextView = view.findViewById(R.id.textViewMembershipMonthPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembershipViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_membership, parent, false)
        return MembershipViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MembershipViewHolder, position: Int) {
        val membership = membershipList[position]
        holder.title.text = membership.title
        holder.price.text = membership.price
        holder.month_price.text = membership.month_price
    }

    override fun getItemCount() = membershipList.size
}
