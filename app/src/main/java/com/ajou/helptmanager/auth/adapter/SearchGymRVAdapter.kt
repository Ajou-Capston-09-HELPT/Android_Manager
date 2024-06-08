package com.ajou.helptmanager.auth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajou.helptmanager.auth.model.Gym
import com.ajou.helptmanager.auth.view.SearchGymDialog
import com.ajou.helptmanager.databinding.ItemSearchGymBinding
import com.ajou.helptmanager.setOnSingleClickListener
import com.skt.Tmap.poi_item.TMapPOIItem

class SearchGymRVAdapter(val context: Context, var list : ArrayList<TMapPOIItem>, val link : SearchGymDialog.AdapterToFragment):RecyclerView.Adapter<SearchGymRVAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSearchGymBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: TMapPOIItem){
            binding.name.text = item.name
            binding.address.text = item.newAddressList[0].fullAddressRoad

            binding.item.setOnSingleClickListener {
                link.getSelectedItem(Gym(item.name, item.newAddressList[0].fullAddressRoad,null, item.poiPoint.latitude.toString(), item.poiPoint.longitude.toString()))
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchGymBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}