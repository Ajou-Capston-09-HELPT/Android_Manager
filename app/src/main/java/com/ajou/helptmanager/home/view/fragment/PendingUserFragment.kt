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
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.home.model.PendingUserInfo
import com.ajou.helptmanager.databinding.FragmentPendingUserBinding
import com.ajou.helptmanager.home.adapter.PendingUserInfoRVAdapter
import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.GymEquipment
import com.ajou.helptmanager.home.model.RegisteredUserInfo
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.GymAdmissionService
import com.ajou.helptmanager.network.api.MemberService
import com.ajou.helptmanager.setOnSingleClickListener
import kotlinx.coroutines.*

class PendingUserFragment : Fragment(), AdapterToFragment {
    private var _binding : FragmentPendingUserBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null
    private lateinit var viewModel : UserInfoViewModel
    private val memberService = RetrofitInstance.getInstance().create(MemberService::class.java)
    private val gymAdmissionService = RetrofitInstance.getInstance().create(GymAdmissionService::class.java)
    private lateinit var accessToken : String
    private val dataStore = UserDataStore()
    private var gymId : Int? = null
    private lateinit var adapter : PendingUserInfoRVAdapter
    private lateinit var callback: OnBackPressedCallback
    private var originList = emptyList<PendingUserInfo>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("backpressed","")
                viewModel.setCheck(true)
                viewModel.setPendingUserInfo(null)
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
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
        getAdmissionList()

        viewModel.update.observe(viewLifecycleOwner, Observer {
            if(viewModel.update.value == true) {
                getAdmissionList()
                viewModel.setUpdate(false)
            }
        })

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
                adapter.filterList(originList, binding.user.text.toString())
                return@setOnEditorActionListener true
            }else return@setOnEditorActionListener false
        }

        binding.removeBtn.setOnSingleClickListener {
            binding.user.setText("")
            adapter.updateList(originList)
        }

    }

    override fun getSelectedItem(data: PendingUserInfo?) {
        viewModel.setPendingUserInfo(data)
        viewModel.setCheck(true)
    }

    override fun getSelectedItem(data: RegisteredUserInfo?) {
    }

    override fun getSelectedItem(data: GymEquipment, position: Int) {
    }

    override fun getSelectedItem(data: Equipment, position: Int) {
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun getAdmissionList(){
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            gymId = dataStore.getGymId()!!

            val admissionDeferred = async { gymAdmissionService.getAllAdmissionUsers(accessToken,gymId!!) }
            val admissionResponse = admissionDeferred.await()
            if (admissionResponse.isSuccessful) {
                val results = admissionResponse.body()!!.data.map { body ->
                    async {
                        try {
                            val response = memberService.getOneMemberInfo(accessToken,body.memberId)
                            if (response.isSuccessful) {
                                PendingUserInfo(body.memberId, body.gymAdmissionId, body.userName, response.body()!!.data.gender, response.body()!!.data.height, response.body()!!.data.weight, response.body()!!.data.startDate, response.body()!!.data.endDate, response.body()!!.data.membershipId, response.body()!!.data.profileImage, response.body()!!.data.birthDate)
                            } else {
                                null
                            }
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.awaitAll().filterNotNull()

                withContext(Dispatchers.Main){
                    binding.loadingBar.hide()
                    originList = results
                    adapter.updateList(results)
                }
            }else{
                Log.d("admissionResponse faill",admissionResponse.errorBody()?.string().toString())
            }
        }
    }
}