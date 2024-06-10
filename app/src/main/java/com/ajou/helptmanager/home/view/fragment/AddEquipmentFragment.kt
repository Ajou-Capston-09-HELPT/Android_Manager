package com.ajou.helptmanager.home.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajou.helptmanager.*
import com.ajou.helptmanager.auth.model.Gym
import com.ajou.helptmanager.databinding.FragmentAddEquipmentBinding
import com.ajou.helptmanager.home.adapter.EquipmentRVAdapter
import com.ajou.helptmanager.home.model.*
import com.ajou.helptmanager.home.view.dialog.ChatLinkSettingDialog
import com.ajou.helptmanager.home.view.dialog.TrainSettingDialog
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.EquipmentService
import com.ajou.helptmanager.network.api.GymEquipmentService
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class AddEquipmentFragment : Fragment(), AdapterToFragment, DialogToFragment {
    private var _binding: FragmentAddEquipmentBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private lateinit var adapter : EquipmentRVAdapter
    private var list = emptyList<Equipment>()
    private val equipmentService = RetrofitInstance.getInstance().create(EquipmentService::class.java)
    private val gymEquipmentService = RetrofitInstance.getInstance().create(GymEquipmentService::class.java)
    private val dataStore = UserDataStore()
    private lateinit var accessToken: String
    private var gymId : Int? = null
    private lateinit var viewModel : UserInfoViewModel
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
        _binding = FragmentAddEquipmentBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            gymId = dataStore.getGymId()
            val equipDeferred = async { gymEquipmentService.getUnlinkedEquipments(accessToken,gymId!!) }
            val equipResponse = equipDeferred.await()
            if (equipResponse.isSuccessful) {
                list = equipResponse.body()?.data!!
                withContext(Dispatchers.Main){
                    adapter = EquipmentRVAdapter(mContext!!, list, "add", this@AddEquipmentFragment)
                    binding.equipRV.adapter = adapter
                    binding.equipRV.layoutManager = LinearLayoutManager(mContext)
                }
            }else{
                Log.d("equipResponse faill",equipResponse.errorBody()?.string().toString())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pressBackButton()
        pressHamburgerButton()
        pressHamburgerCloseButton()
        pressHamburgerHomeButton()
        pressHamburgerMembershipButton()
        pressHamburgerQrButton()
        pressHamburgerEquipmentButton()
        pressHamburegerUserButton()
        pressHamburegerNoticeButton()
        pressHamburgerChatButton()

        binding.equip.setOnEditorActionListener { view, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH){
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)
                adapter.updateList(list.filter { it.equipmentName.contains(binding.equip.text)  })
                return@setOnEditorActionListener true
            }else return@setOnEditorActionListener false
        }


        viewModel.equipment.observe(viewLifecycleOwner, Observer {
            if (viewModel.equipment.value != null) {
                if (viewModel.equipment.value!!.customWeight != 0 || viewModel.equipment.value!!.customCount != 0 || viewModel.equipment.value!!.customSet != 0) {
                    postEquipmentApi(viewModel.equipment.value!!)
                    viewModel.setEquipment(null)
                }
            }
        })
    }

    override fun getSelectedItem(data: PendingUserInfo?) {
    }

    override fun getSelectedItem(data: RegisteredUserInfo?) {
    }

    override fun getSelectedItem(data: GymEquipment, position: Int) {
    }

    override fun getSelectedItem(data: Equipment, position: Int) {
        var setting = listOf<Int>(data.customWeight, data.customCount, data.customSet)
        Log.d("setting in",setting.toString())
        viewModel.setEquipment(data)
        val dialog = TrainSettingDialog()
        dialog.show(requireActivity().supportFragmentManager, "setting")
    }

    private fun postEquipmentApi(data: Equipment){
        CoroutineScope(Dispatchers.IO).launch {
            val jsonObject = JsonObject().apply {
                addProperty("equipmentId", data.equipmentId)
                addProperty("gymId",gymId)
                addProperty("customCount",data.customCount)
                addProperty("customSet",data.customSet)
                addProperty("customWeight",data.customWeight)
            }
            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
            val postEquipDeferred = async { gymEquipmentService.postGymEquipment(accessToken, requestBody) }
            val postEquipResponse = postEquipDeferred.await()
            if (postEquipResponse.isSuccessful){
                viewModel.setEquipment(null)
                requireActivity().runOnUiThread{
                    Toast.makeText(mContext, "기구 등록이 완료되었습니다", Toast.LENGTH_SHORT).show()
                }
            }else {
                Log.d("postEquipResponse faill",postEquipResponse.errorBody()?.string().toString())
                requireActivity().runOnUiThread{
                    Toast.makeText(mContext, "기구 등록이 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getSetting(data: List<Int>) {

    }

    private fun pressBackButton(){
        binding.backBtn.setOnSingleClickListener {
            findNavController().popBackStack()
        }
    }

    private fun pressHamburgerButton(){
        binding.hamburger.setOnSingleClickListener {
            binding.drawerLayout.openDrawer(binding.drawer.drawer)
        }
    }

    private fun pressHamburgerCloseButton(){
        binding.drawer.closeBtn.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
        }
    }

    private fun pressHamburgerHomeButton(){
        binding.drawer.home.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_addEquipmentFragment_to_homeFragment)
        }
    }

    private fun pressHamburgerMembershipButton(){
        binding.drawer.ticket.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_addEquipmentFragment_to_membershipFragment)
        }
    }

    private fun pressHamburgerQrButton(){
        binding.drawer.qr.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_addEquipmentFragment_to_homeFragment)
        }
    }

    private fun pressHamburgerEquipmentButton(){
        binding.drawer.train.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_addEquipmentFragment_self)
        }
    }

    private fun pressHamburegerUserButton(){
        binding.drawer.user.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_addEquipmentFragment_to_searchUserFragment)
        }
    }

    private fun pressHamburegerNoticeButton(){
        binding.drawer.notice.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_addEquipmentFragment_to_noticeFragment)
        }
    }

    private fun pressHamburgerChatButton(){
        binding.drawer.chat.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            dialog = ChatLinkSettingDialog()
            dialog.show(requireActivity().supportFragmentManager, "link")
        }
    }



}