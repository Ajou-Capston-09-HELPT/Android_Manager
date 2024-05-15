package com.ajou.helptmanager.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.auth.model.Gym
import com.ajou.helptmanager.databinding.ItemUserInfoBinding
import com.ajou.helptmanager.home.model.UserInfo
import com.ajou.helptmanager.home.view.fragment.SearchUserFragment

class UserInfoRVAdapter(val context: Context, var list: List<UserInfo>, val link: AdapterToFragment) :
 RecyclerView.Adapter<UserInfoRVAdapter.ViewHolder>() {

     inner class ViewHolder(val binding: ItemUserInfoBinding) :
             RecyclerView.ViewHolder(binding.root) {
                 fun bind(item: UserInfo) {
                     binding.name.text = item.name
                     if (item.ticket != null) {
                         binding.ticket.text = item.ticket
                         val period = item.startDate + "~" + item.endDate
                         binding.period.text = period
                     }

                     binding.item.setOnClickListener {
                         link.getSelectedItem(item)
                     }
                 }
             }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserInfoBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateList(newList: List<UserInfo>) {
        list = newList
        notifyDataSetChanged()
    }
}