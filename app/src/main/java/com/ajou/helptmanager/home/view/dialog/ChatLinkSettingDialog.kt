package com.ajou.helptmanager.home.view.dialog

import android.annotation.SuppressLint
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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.DialogSetChatLinkBinding
import com.ajou.helptmanager.getWindowSize
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.GymService
import com.ajou.helptmanager.setOnSingleClickListener
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

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

        getChatLink()

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
        binding.posBtn.setOnSingleClickListener {
//            viewModel.setChatLink(binding.link.text.toString())
            if(!binding.link.text.toString().startsWith("https://open.kakao.com/")){
                Toast.makeText(mContext, "오픈 카톡 채팅 링크가 아닙니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnSingleClickListener
            }
            putChatLink(binding.link.text.toString())
            Toast.makeText(mContext, "채팅 링크가 ${binding.posBtn.text}되었습니다.", Toast.LENGTH_SHORT).show()
            dialog?.dismiss()
        }

        binding.delBtn.setOnSingleClickListener {
            delChatLink()
            Toast.makeText(mContext, "채팅 링크가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            dialog?.dismiss()
        }

        binding.closeBtn.setOnSingleClickListener {
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
            val requestBody =
                jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val chatLinkDeferred = async{gymService.putChatLink(accessToken, gymId!!, requestBody) }
            val chatLinkResponse = chatLinkDeferred.await()
            if(chatLinkResponse.isSuccessful) {
                dialog?.dismiss()
            }else{
                Log.d("chatLinkResponse fail",chatLinkResponse.errorBody()?.string().toString())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getChatLink(){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = dataStore.getAccessToken().toString()
            val gymId = dataStore.getGymId()
            val chatLinkDeferred = async{gymService.getChatLink(accessToken, gymId!!) }
            val chatLinkResponse = chatLinkDeferred.await()
            if(chatLinkResponse.isSuccessful) {
                val chatLinkResponse = chatLinkResponse.body()?.string()
                val jsonObject = JsonParser.parseString(chatLinkResponse).asJsonObject
                val dataObject = jsonObject.getAsJsonObject("data")
                if (dataObject.get("chatLink").isJsonNull){
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.currentLink.text = "현재 링크 : 없음"
                        binding.posBtn.text = "등록"
                        binding.delBtn.visibility = View.GONE
                    }
                    return@launch
                }
                val chatLink = dataObject.get("chatLink").asString
                CoroutineScope(Dispatchers.Main).launch {
                    if(chatLink != null){
                        binding.currentLink.text = "현재 링크 : $chatLink"
                        binding.posBtn.text = "수정"
                        binding.delBtn.visibility = View.VISIBLE
                        binding.delBtn.isEnabled = true
                        binding.delBtn.setOnSingleClickListener {
                            delChatLink()
                            Toast.makeText(mContext, "채팅 링크가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            dialog?.dismiss()
                        }
                    }
                }
            }else{
                Log.d("chatLinkResponse fail",chatLinkResponse.errorBody()?.string().toString())
            }
        }
    }

    private fun delChatLink() {
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = dataStore.getAccessToken().toString()
            val gymId = dataStore.getGymId()
            val chatLinkDeferred = async { gymService.deleteChatLink(accessToken, gymId!!) }
            val chatLinkResponse = chatLinkDeferred.await()
            if (chatLinkResponse.isSuccessful) {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.currentLink.text = "현재 링크 : 없음"
                    binding.posBtn.text = "등록"
                    binding.delBtn.visibility = View.GONE
                }
            } else {
                Log.d("chatLinkResponse fail", chatLinkResponse.errorBody()?.string().toString())
            }
        }
    }
}