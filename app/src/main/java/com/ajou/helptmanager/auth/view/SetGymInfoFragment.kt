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
import com.ajou.helptmanager.BuildConfig
import com.ajou.helptmanager.R
import com.ajou.helptmanager.auth.adapter.SearchGymRVAdapter
import com.ajou.helptmanager.databinding.FragmentSetGymInfoBinding
import com.skt.Tmap.TMapData
import com.skt.Tmap.TMapTapi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SetGymInfoFragment : Fragment() {
    private var _binding : FragmentSetGymInfoBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private lateinit var viewModel: UserInfoViewModel
    private var tmapTApi : TMapTapi? = null
    private lateinit var dialog : SearchGymDialog

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
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gymAddress.setOnClickListener {
            dialog = SearchGymDialog() { value ->
                Log.d("data value",value.toString())
                binding.gymAddress.text = value.address
                binding.gymAddress.isSelected = true
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
            findNavController().navigate(R.id.action_setGymInfoFragment_to_pendingFragment)
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