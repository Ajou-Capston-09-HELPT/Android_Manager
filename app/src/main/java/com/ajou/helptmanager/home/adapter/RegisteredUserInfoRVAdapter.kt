package com.ajou.helptmanager.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.calDate
import com.ajou.helptmanager.databinding.ItemUserInfoBinding
import com.ajou.helptmanager.home.model.RegisteredUserInfo
import com.ajou.helptmanager.setOnSingleClickListener

class RegisteredUserInfoRVAdapter(val context: Context, var list: List<RegisteredUserInfo>, val link: AdapterToFragment) :
 RecyclerView.Adapter<RegisteredUserInfoRVAdapter.ViewHolder>() {

     inner class ViewHolder(val binding: ItemUserInfoBinding) :
             RecyclerView.ViewHolder(binding.root) {
                 fun bind(item: RegisteredUserInfo) {
                     if (item.startDate!= null && item.endDate!=null){
                         binding.ticket.visibility = View.VISIBLE
                         binding.name.text = item.userName
                         val month = calDate(item.startDate.toString(), item.endDate.toString())
                         val ticketName = "${month}개월 회원권"
                         binding.ticket.text = ticketName
//                         val period = item.startDate + "~" + item.endDate
                         binding.birth.text = item.birthDate

                         binding.item.setOnSingleClickListener {
                             link.getSelectedItem(item)
                         }
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

    fun updateList(newList: List<RegisteredUserInfo>) {
        list = newList
        notifyDataSetChanged()
    }
    fun filterList(originList: List<RegisteredUserInfo>,keyword:String){
        list = originList.filter { it.userName.contains(keyword)  }
        Log.d("after filtered Regist list",list.toString())
        notifyDataSetChanged()
    }
}