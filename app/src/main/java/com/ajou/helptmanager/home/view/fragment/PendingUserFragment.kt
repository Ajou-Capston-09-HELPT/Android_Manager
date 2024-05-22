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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.home.model.PendingUserInfo
import com.ajou.helptmanager.databinding.FragmentPendingUserBinding
import com.ajou.helptmanager.home.adapter.PendingUserInfoRVAdapter
import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.GymEquipment
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.GymAdmissionService
import kotlinx.coroutines.*

class PendingUserFragment : Fragment(), AdapterToFragment {
    private var _binding : FragmentPendingUserBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null
    private lateinit var viewModel : UserInfoViewModel
    private val gymAdmissionService = RetrofitInstance.getInstance().create(GymAdmissionService::class.java)
    private lateinit var accessToken : String
    private val dataStore = UserDataStore()
    private var gymId : Int? = null
    private lateinit var adapter : PendingUserInfoRVAdapter

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
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]
        _binding = FragmentPendingUserBinding.inflate(layoutInflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            gymId = dataStore.getGymId()!!

            val admissionDeferred = async { gymAdmissionService.getAllAdmissionUsers(accessToken,gymId!!) }
            val admissionResponse = admissionDeferred.await()
            if (admissionResponse.isSuccessful) {
                withContext(Dispatchers.Main){
                    binding.loadingBar.hide()
                    adapter.updateList(admissionResponse.body()!!.data)
                }
            }else{
                Log.d("admissionResponse faill",admissionResponse.errorBody()?.string().toString())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PendingUserInfoRVAdapter(mContext!!, emptyList(), this)

        binding.userRV.adapter = adapter
        binding.userRV.layoutManager = LinearLayoutManager(mContext)

        binding.user.setOnEditorActionListener { view, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH){
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)
                adapter.filterList(binding.user.text.toString())
                return@setOnEditorActionListener true
            }else return@setOnEditorActionListener false
        }

    }

    override fun getSelectedItem(userId: Int, admissionId: Int?) {
        viewModel.setAdmissionId(admissionId!!)
        viewModel.setUserId(userId)
        viewModel.setCheck(true)
    }

    override fun getSelectedItem(data: GymEquipment, position: Int) {
    }

    override fun getSelectedItem(data: Equipment, position: Int) {
    }

}