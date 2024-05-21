package com.ajou.helptmanager.home.view.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentEquipmentListBinding
import com.ajou.helptmanager.home.adapter.EquipmentRVAdapter
import com.ajou.helptmanager.home.adapter.GymEquipmentRVAdapter
import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.GymEquipment
import com.ajou.helptmanager.home.model.UserInfo
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.EquipmentService
import com.ajou.helptmanager.network.api.GymEquipmentService
import com.ajou.helptmanager.network.api.GymService
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class EquipmentListFragment : Fragment(), AdapterToFragment {
    private var _binding : FragmentEquipmentListBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null
    private lateinit var adapter : GymEquipmentRVAdapter
    private var list = mutableListOf<GymEquipment>()
    private val gymEquipmentService = RetrofitInstance.getInstance().create(GymEquipmentService::class.java)
    private val gymService = RetrofitInstance.getInstance().create(GymService::class.java)
    private var accessToken : String? = null
    private var gymId : Int? = null
    private val dataStore = UserDataStore()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEquipmentListBinding.inflate(layoutInflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken()
            gymId = dataStore.getGymId()
            Log.d("check list","accessToken $accessToken  gymId  $gymId")
            val equipDeferred = async { gymService.getAllGymEquipments(accessToken!!,gymId!!) }
            val equipResponse = equipDeferred.await()
            if (equipResponse.isSuccessful) {
                list = equipResponse.body()!!.data as MutableList<GymEquipment>
                Log.d("list",list.toString())
                withContext(Dispatchers.Main){
                    adapter.updateList(list)
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GymEquipmentRVAdapter(mContext!!, list, "search", this)
        binding.equipRV.adapter = adapter
        binding.equipRV.layoutManager = LinearLayoutManager(mContext)

        binding.btn.setOnClickListener {
            findNavController().navigate(R.id.action_equipmentListFragment_to_addEquipmentFragment)
        }

        binding.drawer.trainIcon.setImageResource(R.drawable.menu_machine_on)

        binding.drawer.train.setTextColor(resources.getColor(R.color.primary))
        binding.drawer.train.setTypeface(binding.drawer.user.typeface, Typeface.BOLD)

        binding.hamburger.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.drawer.drawer)
        }
        binding.drawer.ticket.setOnClickListener {
            findNavController().navigate(R.id.action_equipmentListFragment_to_membershipFragment)
            // 이용권으로 이동
        }
        binding.drawer.qr.setOnClickListener {
            // TODO QR스캔으로 이동
        }
        binding.drawer.train.setOnClickListener {
            findNavController().navigate(R.id.action_equipmentListFragment_self)
        }
        binding.drawer.user.setOnClickListener {
            findNavController().navigate(R.id.action_equipmentListFragment_to_searchUserFragment)
        }
        binding.drawer.notice.setOnClickListener {
            // TODO 공지사항으로 이동
        }
        binding.drawer.chat.setOnClickListener {
            // TODO 채팅으로 이동
        }
        binding.drawer.home.setOnClickListener {
            findNavController().navigate(R.id.action_equipmentListFragment_to_homeFragment)
            // 메인화면으로 이동
        }
    }

    override fun getSelectedItem(userId: Int, admissionId: Int?) {
    }

    override fun getSelectedItem(data: GymEquipment, position: Int) {
        val setting = listOf<Int>(data.customWeight, data.customCount, data.customSet)
        val dialog = EquipmentEditBottomSheetFragment(setting){value ->
            if(value[0]==-1){
                deleteEquipmentApi(data.gymEquipmentId)
                list.removeAt(position)
                adapter.notifyItemRemoved(position)
            }else{
                list[position].customWeight = value[0]
                list[position].customCount = value[1]
                list[position].customSet = value[2]
                adapter.notifyItemChanged(position)
                val jsonObject = JsonObject().apply {
                    addProperty("customCount", value[1])
                    addProperty("customSet",value[2])
                    addProperty("customWeight",value[0])
                }
                val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
                editSettingApi(requestBody,data.gymEquipmentId)
            }

        }
        dialog.show(parentFragmentManager,dialog.tag)
    }

    override fun getSelectedItem(data: Equipment, position: Int) {
    }

    private fun deleteEquipmentApi(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val deleteDeferred = async { gymEquipmentService.deleteGymEquipment(accessToken!!,id) }
            val deleteResponse = deleteDeferred.await()
            if(deleteResponse.isSuccessful){
                Log.d("deleteResponse success","")
            }else{
                Log.d("deleteResponse fail",deleteResponse.errorBody()?.string().toString())
            }
        }
    }

    private fun editSettingApi(body:RequestBody, id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val deleteDeferred = async { gymEquipmentService.editGymEquipmentSetting(accessToken!!,id, body) }
            val deleteResponse = deleteDeferred.await()
            if(deleteResponse.isSuccessful){
                Log.d("deleteResponse success","")
            }else{
                Log.d("deleteResponse fail",deleteResponse.errorBody()?.string().toString())
            }
        }
    }
}