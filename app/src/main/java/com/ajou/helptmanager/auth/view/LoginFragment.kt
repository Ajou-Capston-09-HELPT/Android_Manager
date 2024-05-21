package com.ajou.helptmanager.auth.view

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.ajou.helpt.auth.view.LogOutDialog
import com.ajou.helptmanager.home.view.HomeActivity
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentLoginBinding
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.GymService
import com.ajou.helptmanager.network.api.ManagerService
import com.google.gson.JsonObject
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private val dataStore = UserDataStore()
    private val managerService = RetrofitInstance.getInstance().create(ManagerService::class.java)
    private val gymService = RetrofitInstance.getInstance().create(GymService::class.java)
    private lateinit var logOutDialog : LogOutDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            val gymStatus = dataStore.getGymStatus()
            if (gymStatus == "Pending"){
                Log.d("gymStatus","Pending")
                withContext(Dispatchers.Main){
                    findNavController().navigate(R.id.action_loginFragment_to_pendingFragment)
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView3.setOnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            startActivity(intent)
//            logOutDialog = LogOutDialog(mContext!!)
//            logOutDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            logOutDialog.show()
//            CoroutineScope(Dispatchers.IO).launch {
//                val token = dataStore.getAccessToken()
//                Log.d("userToken",token.toString())
//                dataStore.deleteAll()
//            }
        }
        binding.nextBtn.setOnClickListener {


            // 임시 토큰 발행용
            CoroutineScope(Dispatchers.IO).launch {
                UserDataStore().saveGymId(4)
            }
            callLoginApi("3443165759")



            /*
            // 카카오톡 설치 확인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(mContext!!)) {
                Log.d(ContentValues.TAG, "유저가 카카오톡 설치했음")
                // 카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(mContext!!) { token, error ->
                    // 로그인 실패 부분
                    if (error != null) {
                        Log.e(ContentValues.TAG, "로그인 실패 $error")
                        // 사용자가 취소
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        // 다른 오류
                        else {
                            Log.d(ContentValues.TAG, "${error.message}")
                            Log.d(ContentValues.TAG, "유저가 카카오톡으로 로그인하기 실패함")
                            UserApiClient.instance.loginWithKakaoAccount(
                                mContext!!,
                                callback = mCallback
                            ) // 카카오 이메일 로그인
                        }
                    }
                    // 로그인 성공 부분
                    else if (token != null) {
                        UserApiClient.instance.me { user, error ->
                            CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
                                Log.d(
                                    ContentValues.TAG,
                                    "${user?.id}"
                                ) // accessToken, user.id 추후에 보내면 됨.
                                Log.d(
                                    ContentValues.TAG,
                                    "${user?.kakaoAccount?.email} ${user?.kakaoAccount?.profile?.nickname}"
                                )

//                                dataStore.saveUserName(user?.kakaoAccount?.profile?.nickname.toString())
                                withContext(Dispatchers.Main) {
                                    Log.d("user Id",user?.id.toString())
                                    callLoginApi(user?.id.toString())
                                }
                            }
                        }
                        Log.e(ContentValues.TAG, "로그인 성공 ${token.accessToken}")
                        Log.d(
                            ContentValues.TAG,
                            "카카오톡 로그인 정보 가져옴 ${token.refreshToken} ${token.accessTokenExpiresAt} ${token.refreshTokenExpiresAt}"
                        )

                    }
                }
            } else {
                Log.d(ContentValues.TAG, "유저가 카카오톡 설치안가")
                UserApiClient.instance.loginWithKakaoAccount(
                    mContext!!,
                    callback = mCallback
                ) // 카카오 이메일 로그인
            }
            */
        }
    }

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
                dataStore.saveAccessToken(token.accessToken)
            }
            Log.i(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.d("exceptionHandler", exception.message.toString())
        showErrorDialog()
    }

    private fun showErrorDialog() {
        requireActivity().runOnUiThread {
            AlertDialog.Builder(mContext!!).apply {
                setTitle("Error")
                setMessage("Network request failed.")
                setPositiveButton("뒤로 가기") { dialog, _ ->
                    dialog.dismiss()
                    findNavController().popBackStack()
                }
                setCancelable(false)
                show()
            }
        }
    } // TODO 구체적인 오류 메세지로 변경하기

    private fun callLoginApi(id:String) {
        val jsonObject = JsonObject().apply {
            addProperty("kakaoId", id)
        }
        Log.d("kakaoid",id.toString())
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
        CoroutineScope(Dispatchers.IO).launch{
            val loginDeferred = async {managerService.login(requestBody) }
            val loginResponse = loginDeferred.await()
            if (loginResponse.isSuccessful) {
                val tokenBody = JSONObject(loginResponse.body()?.string())
                val accessToken = "Bearer "+tokenBody.getJSONObject("data").getString("accessToken")
                Log.d("tokenBody",tokenBody.toString())
                dataStore.saveAccessToken(accessToken)
                dataStore.saveRefreshToken("Bearer "+tokenBody.getJSONObject("data").getString("refreshToken"))
                val checkGymDeferred = async { gymService.getGymStatus(accessToken) }
                val checkGymResponse = checkGymDeferred.await()
                var hasGym = "null"
                if (checkGymResponse.isSuccessful) {
                    hasGym = JSONObject(checkGymResponse.body()?.string()).getJSONObject("data").getString("status")
                    if (hasGym == "Approved"){
                        val idDeferred = async { managerService.getGymId(accessToken) }
                        val idResponse = idDeferred.await()
                        if (idResponse.isSuccessful){
                            val idBody = JSONObject(idResponse.body()?.string())
                            Log.d("idBody success",idBody.toString())
                            dataStore.saveGymId(idBody.getJSONArray("data").getJSONObject(0).getString("gymId").toInt())
                            val infoDeferred = async { gymService.getGymInfo(accessToken, idBody.getJSONArray("data").getJSONObject(0).getString("gymId").toInt()) }
                            val infoResponse = infoDeferred.await()
                            if (infoResponse.isSuccessful){
                                Log.d("infoBody success",infoResponse.body().toString())
                                dataStore.saveUserName(infoResponse.body()!!.data.gymName)
                            }
                        }
                    }
                }else{
                    Log.d("checkGymResponse fail",checkGymResponse.errorBody()?.string().toString())
                }
                withContext(Dispatchers.Main){
                    when(hasGym){
                        "Unregistered" -> {
                            findNavController().navigate(R.id.action_loginFragment_to_setBizInfoFragment)
                        }
                        "Approved" -> {
                            val intent = Intent(mContext,HomeActivity::class.java)
                            startActivity(intent)
                        }
                        "Pending" -> {
                            findNavController().navigate(R.id.action_loginFragment_to_pendingFragment)
                        }
                        "Rejected" -> {
                            // ...
                        }
                    }
                }
            }else{
                val registerDeferred = async { managerService.register(requestBody) }
                val registerResponse = registerDeferred.await()
                if (registerResponse.isSuccessful) {
                    val tokenBody = JSONObject(registerResponse.body()?.string())
                    dataStore.saveAccessToken("Bearer "+tokenBody.getJSONObject("data").getString("accessToken"))
                    dataStore.saveRefreshToken("Bearer "+tokenBody.getJSONObject("data").getString("refreshToken"))
                    withContext(Dispatchers.Main){
                        findNavController().navigate(R.id.action_loginFragment_to_setBizInfoFragment)
                    }
                }else{
                    Log.d("registerResponse faill",registerResponse.errorBody()?.string().toString())
                }
            }
        }
    }
}