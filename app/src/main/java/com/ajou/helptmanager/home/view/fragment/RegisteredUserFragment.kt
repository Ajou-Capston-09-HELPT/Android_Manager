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
import com.ajou.helptmanager.databinding.FragmentRegisteredUserBinding
import com.ajou.helptmanager.home.adapter.RegisteredUserInfoRVAdapter
import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.GymEquipment
import com.ajou.helptmanager.home.model.RegisteredUserInfo
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.MemberService
import kotlinx.coroutines.*

class RegisteredUserFragment : Fragment(), AdapterToFragment{
    private var _binding : FragmentRegisteredUserBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null
    private lateinit var viewModel : UserInfoViewModel
    private val TAG = RegisteredUserFragment::class.java.simpleName
    private val memberService = RetrofitInstance.getInstance().create(MemberService::class.java)
    private lateinit var accessToken : String
    private val dataStore = UserDataStore()
    private var gymId : Int? = null
    private lateinit var adapter : RegisteredUserInfoRVAdapter

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
        _binding = FragmentRegisteredUserBinding.inflate(layoutInflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            gymId = dataStore.getGymId()!!
            val registeredMemberDeferred = async { memberService.getRegisteredMembers(accessToken,gymId!!,"") }
            val registeredMemberResponse = registeredMemberDeferred.await()
            if (registeredMemberResponse.isSuccessful){
                val list = registeredMemberResponse.body()!!.data
                Log.d("list registered",list.toString())
                withContext(Dispatchers.Main){
                    adapter.updateList(list)
                }
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RegisteredUserInfoRVAdapter(mContext!!, emptyList(), this)

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
        viewModel.setUserId(userId)
        viewModel.setCheck(true)
    }

    override fun getSelectedItem(data: GymEquipment, position: Int) {
    }

    override fun getSelectedItem(data: Equipment, position: Int) {
    }
}