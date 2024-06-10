package com.ajou.helptmanager.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.home.model.PendingUserInfo
import com.ajou.helptmanager.databinding.ItemUserInfoBinding
import com.ajou.helptmanager.home.model.UserInfo
import com.ajou.helptmanager.setOnSingleClickListener

class PendingUserInfoRVAdapter(val context: Context, var list: List<PendingUserInfo>, val link: AdapterToFragment) :
 RecyclerView.Adapter<PendingUserInfoRVAdapter.ViewHolder>() {

     inner class ViewHolder(val binding: ItemUserInfoBinding) :
             RecyclerView.ViewHolder(binding.root) {
                 fun bind(item: PendingUserInfo) {
                     binding.name.text = item.userName
                     binding.birth.text = item.birthDate

                     binding.item.setOnSingleClickListener {
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

    fun updateList(newList: List<PendingUserInfo>) {
        list = newList
        notifyDataSetChanged()
    }

    fun filterList(originList: List<PendingUserInfo>, keyword:String){
        list = originList.filter { it.userName.contains(keyword) }
        notifyDataSetChanged()
    }
}