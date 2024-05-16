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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentAddEquipmentBinding
import com.ajou.helptmanager.home.adapter.EquipmentRVAdapter
import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.GymEquipment
import com.ajou.helptmanager.home.model.UserInfo
import com.ajou.helptmanager.home.view.dialog.TrainSettingDialog
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.EquipmentService
import com.ajou.helptmanager.network.api.GymEquipmentService
import kotlinx.coroutines.*

class AddEquipmentFragment : Fragment(), AdapterToFragment {
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
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            gymId = dataStore.getGymId()
            val equipDeferred = async { equipmentService.getAllEquipments(accessToken!!) }
            val equipResponse = equipDeferred.await()
            if (equipResponse.isSuccessful) {
                Log.d("equipResponse",equipResponse.body()?.data.toString())
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

        binding.equip.setOnEditorActionListener { view, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH){
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)
                adapter.updateList(list.filter { it.equipmentName.contains(binding.equip.text)  })
                return@setOnEditorActionListener true
            }else return@setOnEditorActionListener false
        }

    }

    override fun getSelectedItem(userId: Int, admissionId: Int?) {
    }

    override fun getSelectedItem(data: GymEquipment, position: Int) {
    }

    override fun getSelectedItem(data: Equipment, position: Int) {
        var setting = listOf<Int>(data.customWeight, data.customCount, data.customSet)
        val dialog = TrainSettingDialog(setting) { value ->
            setting = value
            data.customSet = value[2]
            data.customCount = value[1]
            data.customWeight = value[0]
            Log.d("before data","data  $data  value $value")
            // TODO data랑 value 값 조합해서 기구 등록 api 던지기
            postEquipmentApi(data)

        }
        dialog.show(requireActivity().supportFragmentManager, "setting")
    }

    private fun postEquipmentApi(data: Equipment){
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("gymId",gymId.toString())
            val postEquipDeferred = async { gymEquipmentService.postGymEquipment(accessToken, data) }
            val postEquipResponse = postEquipDeferred.await()
            if (postEquipResponse.isSuccessful){
                Log.d("postEquipResponse","")
            }else {
                Log.d("postEquipResponse faill",postEquipResponse.errorBody()?.string().toString())
            }
        }
    }
}