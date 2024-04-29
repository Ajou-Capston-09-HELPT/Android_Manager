package com.ajou.helptmanager.auth.view

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajou.helptmanager.R
import com.ajou.helptmanager.auth.adapter.SearchGymRVAdapter
import com.ajou.helptmanager.auth.model.Gym
import com.ajou.helptmanager.databinding.DialogSearchGymBinding
import com.skt.Tmap.TMapData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class SearchGymDialog(private val callback: (Gym) -> Unit) : DialogFragment() {
    private lateinit var binding: DialogSearchGymBinding
    private var mContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowManager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y
        params?.width = (deviceWidth * 0.6).toInt()
        params?.height = (deviceHeight * 0.6).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams

        setStyle(STYLE_NORMAL, R.style.CustomDialog) // 배경 transparent

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSearchGymBinding.inflate(inflater, container, false)
        isCancelable = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tmapData = TMapData(mContext)
        val link = AdapterToFragment()

        binding.gym.setOnEditorActionListener { view, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_SEARCH){
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)

                CoroutineScope(Dispatchers.IO).launch {
                    val data = tmapData.findAllPOI(binding.gym.text.toString())
                    Log.d("searched data",data.toString())
                    for(i in 0 until data.size) {
                        Log.d("iiii",data[i].newAddressList[0].fullAddressRoad.toString())
                        Log.d("iii",data[i].poiPoint.toString())
                        Log.d("iii",data[i].poiName.toString())

                    }
                    withContext(Dispatchers.Main){
                        var adapter : SearchGymRVAdapter = SearchGymRVAdapter(mContext!!, data, link) // TODO 서버 연결 후에는 listOf()로 변경 후, 통신 이후에 list를 채워주는 방식으로 LNG
                        binding.gymRv.adapter = adapter
                        binding.gymRv.layoutManager = LinearLayoutManager(mContext) // 위와 동일
                    }
                }

                return@setOnEditorActionListener true
            }else return@setOnEditorActionListener false
        }

//        binding.posBtn.setOnClickListener {
////            callback(data)
//            dialog?.dismiss()
//        }
//
//        binding.negBtn.setOnClickListener {
//            dialog?.dismiss()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
    }
    inner class AdapterToFragment {
        fun getSelectedItem(data : Gym) {
            Log.d("선택된 데이터", data.toString())
             callback(data)
            dialog?.dismiss()
        }
    }
}