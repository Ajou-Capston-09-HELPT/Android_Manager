package com.ajou.helptmanager.auth.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.*
import com.ajou.helptmanager.databinding.FragmentSetGymInfoBinding
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.GymService
import com.ajou.helptmanager.home.model.GymAddress
import com.ajou.helptmanager.home.model.RegisterGymInfo
import com.skt.Tmap.TMapTapi
import kotlinx.coroutines.*


class SetGymInfoFragment : Fragment() {
    private var _binding : FragmentSetGymInfoBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private lateinit var viewModel: AuthInfoViewModel
    private var tmapTApi : TMapTapi? = null
    private lateinit var dialog : SearchGymDialog
    private val gymService = RetrofitInstance.getInstance().create(GymService::class.java)
    private val dataStore = UserDataStore()
    private var accessToken : String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTMap()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSetGymInfoBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AuthInfoViewModel::class.java]
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val resizeUri = resizeImage(viewModel.bizImg.value!!, mContext!!)
//        Log.d("resizeUri",resizeUri.toString()) // TODO 이미지 리사이징 작업 혹은 이미지 사이즈 제한 작업 필요
        val img = getMultipartFile(viewModel.bizImg.value!!, requireActivity(),"businessFile")
        Log.d(":imgUri",img.toString())
        var latitude : String? = null
        var longitude : String? = null

        binding.gymAddress.setOnSingleClickListener {
            dialog = SearchGymDialog() { value ->
                binding.gymAddress.text = value.address
                binding.gymAddress.isSelected = true
                latitude = value.latitude
                longitude = value.longitude
            }
            dialog.show(requireActivity().supportFragmentManager,"search")
        }

        binding.gymName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(str: Editable?) {
                if (str!!.isNotEmpty()) {
                    binding.gymName.isSelected = true
                    viewModel.setDone(true)
                }else {
                    binding.gymName.isSelected = false
                    viewModel.setDone(false)
                }

            }

        })

        binding.gymNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(str: Editable?) {
                if (str!!.isNotEmpty()) {
                    binding.gymNum.isSelected = true
                    viewModel.setDone(true)
                }else {
                    binding.gymNum.isSelected = false
                    viewModel.setDone(false)
                }
            }

        })

        binding.gymAddressDetail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(str: Editable?) {
                if (str!!.isNotEmpty()) {
                    binding.gymAddressDetail.isSelected = true
                    viewModel.setDone(true)
                }else {
                    binding.gymAddressDetail.isSelected = false
                    viewModel.setDone(false)
                }
            }

        })

        viewModel.done.observe(viewLifecycleOwner) {
            binding.nextBtn.isEnabled =
                binding.gymAddressDetail.text.isNotEmpty() && binding.gymName.text.isNotEmpty() && binding.gymNum.text.isNotEmpty() && binding.gymAddress.text.isNotEmpty()
        }

        binding.nextBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val address = GymAddress(binding.gymAddress.text.toString(), binding.gymAddressDetail.text.toString(), latitude!!, longitude!!)
                val gymInfo = RegisterGymInfo(address, binding.gymName.text.toString(), binding.gymNum.text.toString(), viewModel.bizNum.value.toString(), viewModel.division.value.toString(), viewModel.bizName.value.toString())
                dataStore.saveUserName(binding.gymName.text.toString())
                val registerDeferred = async{ gymService.register(accessToken!!,gymInfo, img) }
                val registerResponse = registerDeferred.await()
                if (registerResponse.isSuccessful) {
                    Log.d("registerResponse",registerResponse.toString())
                    dataStore.saveUserName(binding.gymName.text.toString())
                    withContext(Dispatchers.Main){
                        findNavController().navigate(R.id.action_setGymInfoFragment_to_pendingFragment)
                    }
                }else{
                    Log.d("registerResponse Fail",registerResponse.errorBody()?.string().toString())
                }
            }
        }
    }

    private fun initTMap() {
        tmapTApi = TMapTapi(mContext)
        tmapTApi!!.setSKTMapAuthentication(BuildConfig.TMAP_API_KEY)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::dialog.isInitialized){
            dialog.dismiss()
        }
    }
}