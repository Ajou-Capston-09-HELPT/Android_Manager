package com.ajou.helptmanager.home.view.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentEquipmentListBinding
import com.ajou.helptmanager.home.adapter.EquipmentRVAdapter
import com.ajou.helptmanager.home.adapter.GymEquipmentRVAdapter
import com.ajou.helptmanager.home.model.*
import com.ajou.helptmanager.home.view.dialog.ChatLinkSettingDialog
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.EquipmentService
import com.ajou.helptmanager.network.api.GymEquipmentService
import com.ajou.helptmanager.network.api.GymService
import com.ajou.helptmanager.setOnSingleClickListener
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
    private var userName : String? = null
    private val dataStore = UserDataStore()
    private lateinit var viewModel : UserInfoViewModel
    private var selectedTmp : Equipment? = null
    private lateinit var dialog : ChatLinkSettingDialog

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
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken()
            gymId = dataStore.getGymId()
            userName = dataStore.getUserName()
            Log.d("check list","accessToken $accessToken  gymId  $gymId")
            val equipDeferred = async { gymService.getAllGymEquipments(accessToken!!,gymId!!) }
            val equipResponse = equipDeferred.await()
            if (equipResponse.isSuccessful) {
                list = equipResponse.body()!!.data as MutableList<GymEquipment>
                Log.d("list",list.toString())
                withContext(Dispatchers.Main){
                    adapter.updateList(list)
                    binding.drawer.name.text = userName
                }
            }else{
                Log.d("equipResponse fail",equipResponse.errorBody()?.string().toString())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GymEquipmentRVAdapter(mContext!!, list, "search", this)
        binding.equipRV.adapter = adapter
        binding.equipRV.layoutManager = LinearLayoutManager(mContext)

        binding.btn.setOnSingleClickListener {
            findNavController().navigate(R.id.action_equipmentListFragment_to_addEquipmentFragment)
        }
        binding.backBtn.setOnSingleClickListener {
            findNavController().popBackStack()
        }

        viewModel.equipment.observe(viewLifecycleOwner, Observer {
            if(viewModel.equipment.value != null && viewModel.equipment.value != selectedTmp) {
//                Log.d("viewmodel is called","equipmentlistfragment ${viewModel.position.value}")
                if(viewModel.equipment.value!!.customWeight == -1 && viewModel.position.value != null) {
                    deleteEquipmentApi(viewModel.equipment.value!!.equipmentId)
                    val position = viewModel.position.value!!
                    list.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Log.d("viewModel is called","deleteapi called ${viewModel.position.value}")
                } else if (viewModel.equipment.value!!.customWeight != -1 && viewModel.position.value != null){

                    Log.d("viewModel is called","editapi called ${viewModel.position.value}")
                    val position = viewModel.position.value!!
                    list[position].customWeight = viewModel.equipment.value!!.customWeight
                    list[position].customCount = viewModel.equipment.value!!.customCount
                    list[position].customSet = viewModel.equipment.value!!.customSet
                    adapter.notifyItemChanged(position)
                                    val jsonObject = JsonObject().apply {
                    addProperty("customCount", viewModel.equipment.value!!.customCount)
                    addProperty("customSet",viewModel.equipment.value!!.customSet)
                    addProperty("customWeight",viewModel.equipment.value!!.customWeight)
                }
                val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
                editSettingApi(requestBody, viewModel.equipment.value!!.equipmentId)
                }
            }
        })
        binding.drawer.trainIcon.setImageResource(R.drawable.menu_machine_on)

        binding.drawer.train.setTextColor(resources.getColor(R.color.primary))
        binding.drawer.train.setTypeface(binding.drawer.user.typeface, Typeface.BOLD)

        binding.hamburger.setOnSingleClickListener {
            binding.drawerLayout.openDrawer(binding.drawer.drawer)
        }
        binding.drawer.ticket.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_equipmentListFragment_to_membershipFragment)
        }
        binding.drawer.qr.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_equipmentListFragment_to_homeFragment)
        }
        binding.drawer.train.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_equipmentListFragment_self)
        }
        binding.drawer.user.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_equipmentListFragment_to_searchUserFragment)
        }
        binding.drawer.notice.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_equipmentListFragment_to_noticeFragment)
        }
        binding.drawer.chat.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            dialog = ChatLinkSettingDialog()
            dialog.show(requireActivity().supportFragmentManager, "link")
        }
        binding.drawer.home.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_equipmentListFragment_to_homeFragment)
        }
        binding.drawer.entryLog.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_equipmentListFragment_to_entryLogFragment)
        }
    }

    override fun getSelectedItem(data: PendingUserInfo?) {
    }

    override fun getSelectedItem(data: RegisteredUserInfo?) {
    }

    override fun getSelectedItem(data: GymEquipment, position: Int) {
        val setting = listOf<Int>(data.customWeight, data.customCount, data.customSet)
        selectedTmp = Equipment(data.gymEquipmentId,data.equipmentName, data.customCount, data.customSet, data.customWeight)
        val tmp = selectedTmp
        viewModel.setEquipment(tmp!!, position)

        val dialog = EquipmentEditBottomSheetFragment()
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
                viewModel.setEquipment(null)
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
                viewModel.setEquipment(null)
            }else{
                Log.d("deleteResponse fail",deleteResponse.errorBody()?.string().toString())
            }
        }
    }
}