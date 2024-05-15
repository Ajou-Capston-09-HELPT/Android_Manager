<<<<<<<< HEAD:app/src/main/java/com/ajou/helptmanager/membership/view/MembershipAdapter.kt
package com.ajou.helptmanager.membership.view
========
package com.ajou.helptmanager.home.adapter
>>>>>>>> origin/develop:app/src/main/java/com/ajou/helptmanager/home/adapter/MembershipAdapter.kt


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.R
<<<<<<<< HEAD:app/src/main/java/com/ajou/helptmanager/membership/view/MembershipAdapter.kt
import com.ajou.helptmanager.membership.model.Membership
========
import com.ajou.helptmanager.home.model.Membership
>>>>>>>> origin/develop:app/src/main/java/com/ajou/helptmanager/home/adapter/MembershipAdapter.kt


class MembershipAdapter(val listener : OnItemClickListener): ListAdapter<Membership, MembershipAdapter.MembershipViewHolder>(
    MembershipDiffCallback
) {

    interface OnItemClickListener {
        fun onMoreButtonClicked(id: Int)
    }

    class MembershipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textViewMembershipTitle)
        val price: TextView = view.findViewById(R.id.textViewMembershipPrice)
        val month_price: TextView = view.findViewById(R.id.textViewMembershipMonthPrice)
        val moreButton: ImageView = view.findViewById(R.id.imageViewMore)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembershipViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_membership, parent, false)
        return MembershipViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MembershipViewHolder, position: Int) {
        val membership = getItem(position)
        holder.title.text = membership.day
        holder.price.text = membership.price
        holder.month_price.text = membership.month_price
        holder.moreButton.setOnClickListener{
            listener.onMoreButtonClicked(position)
        }


    }

    companion object MembershipDiffCallback : DiffUtil.ItemCallback<Membership>() {
        override fun areItemsTheSame(oldItem: Membership, newItem: Membership): Boolean {
            // 아이템 동일성 검사 코드
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Membership, newItem: Membership): Boolean {
            // 아이템 내용 동일성 검사 코드
            return oldItem == newItem
        }
    }



}