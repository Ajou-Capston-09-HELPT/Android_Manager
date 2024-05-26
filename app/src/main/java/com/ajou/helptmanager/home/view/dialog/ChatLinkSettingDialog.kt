package com.ajou.helptmanager.home.view.dialog

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.DialogSetChatLinkBinding
import com.ajou.helptmanager.getWindowSize
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.GymService
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.Serializable

class ChatLinkSettingDialog() : DialogFragment() {
    private var _binding: DialogSetChatLinkBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private lateinit var viewModel : UserInfoViewModel
    private val gymService = RetrofitInstance.getInstance().create(GymService::class.java)
    private val dataStore = UserDataStore()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialog) // 배경 transparent
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogSetChatLinkBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]
        isCancelable = true
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val point = getWindowSize(mContext!!)
        val deviceWidth = point.x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.link.setOnEditorActionListener { view, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    requireActivity().window.decorView.applicationWindowToken,
                    0
                )

                if (binding.posBtn.isEnabled) {
                    viewModel.setChatLink(binding.link.text.toString())
                    dialog?.dismiss()
                }

                return@setOnEditorActionListener true
            } else return@setOnEditorActionListener false
        }
        binding.link.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(str: Editable?) {
                binding.posBtn.isEnabled = str!!.isNotEmpty()
            }
        })
        binding.posBtn.setOnClickListener {
//            viewModel.setChatLink(binding.link.text.toString())
            putChatLink(binding.link.text.toString())
            dialog?.dismiss()
        }

        binding.closeBtn.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun putChatLink(url:String){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = dataStore.getAccessToken().toString()
            val gymId = dataStore.getGymId()
            val jsonObject = JsonObject().apply {
                addProperty("chatLink", url)
            }
            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
            val chatLinkDeferred = async{gymService.putChatLink(accessToken, gymId!!, requestBody) }
            val chatLinkResponse = chatLinkDeferred.await()
            if(chatLinkResponse.isSuccessful) {
                dialog?.dismiss()
            }else{
                Log.d("chatLinkResponse fail",chatLinkResponse.errorBody()?.string().toString())
            }
        }
    }

}