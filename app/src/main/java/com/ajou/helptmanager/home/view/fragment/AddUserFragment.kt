package com.ajou.helptmanager.home.view.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentAddUserBinding
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.GymAdmissionService
import com.ajou.helptmanager.network.api.MemberService
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import java.time.LocalDate
import java.util.*

class AddUserFragment : Fragment() {
    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private val TAG = AddUserFragment::class.java.simpleName
    private lateinit var viewModel : UserInfoViewModel
    private val gymAdmissionService = RetrofitInstance.getInstance().create(GymAdmissionService::class.java)
    private val memberService = RetrofitInstance.getInstance().create(MemberService::class.java)
    private lateinit var accessToken : String
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
        _binding = FragmentAddUserBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            val userInfoDeferred = async { memberService.getOneMemberInfo(accessToken, viewModel.userId.value!!) }
            val userInfoResponse = userInfoDeferred.await()
            if (userInfoResponse.isSuccessful){
                Log.d("userInfoResponse",userInfoResponse.body()?.data.toString())
                val body = userInfoResponse.body()!!.data
                binding.name.text = body.userName
                when(body.gender){
                    "MAN" -> binding.sex.text = "남성"
                    "WOMEN" -> binding.sex.text = "여성"
                }

//                Glide.with(mContext)
//                    .load(body.)
                binding.height.text = body.height.toString()
                binding.weight.text = body.weight.toString()
            }else{
                Log.d("userInfoResponse faill",userInfoResponse.errorBody()?.string().toString())
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val today: LocalDate = LocalDate.now()
        var endDate : LocalDate? = null

        binding.period.setOnClickListener {
            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                val period = "${today.year}/${today.monthValue}/${today.dayOfMonth} ~ ${year}/${month+1}/${day}"
                binding.period.text = period
                endDate = LocalDate.of(year,month+1,day)
                binding.nextBtn.isEnabled = endDate != null
            }
            DatePickerDialog(
                mContext!!,
                data,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.backBtn.setOnClickListener {
            Log.d("backbtn","")
            findNavController().navigate(R.id.action_addUserFragment_to_searchUserFragment)
        }

        binding.nextBtn.setOnClickListener {
            callUserApi("approve",endDate!!)
        }
        binding.removeBtn.setOnClickListener {
            callUserApi("reject",null)
      }
//        viewModel.setRegisteredUserInfo(null)
    }

    private fun callUserApi(type: String, endDate: LocalDate?){
        CoroutineScope(Dispatchers.IO).launch {
            when(type){
                "approve" -> {
                    val approveDeferred = async { gymAdmissionService.approveUser(accessToken,viewModel.admissionId.value!!,endDate!!) }
                    val approveResponse = approveDeferred.await()
                    if (approveResponse.isSuccessful){
                        Log.d("approveResponse ","")
                        withContext(Dispatchers.Main){
                            findNavController().popBackStack()
                        }
                    }else{
                        Log.d("approveResponse faill",approveResponse.errorBody()?.string().toString())
                    }
                }
                "reject" -> {
                    val rejectDeferred = async { gymAdmissionService.rejectUser(accessToken,viewModel.admissionId.value!!) }
                    val rejectResponse = rejectDeferred.await()
                    if (rejectResponse.isSuccessful) {
                        Log.d("rejectResponse ","")
                        withContext(Dispatchers.Main){
                            findNavController().popBackStack()
                        }
                    }else{
                        Log.d("rejectResponse faill",rejectResponse.errorBody()?.string().toString())
                    }
                }
            }
        }
    }
}