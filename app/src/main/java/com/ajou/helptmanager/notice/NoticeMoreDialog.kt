package com.ajou.helptmanager.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoticeMoreDialog(
    private val noticeId: Int,
    private val title: String,
    private val content: String,
    private val createAt: String,
    private val viewModel: NoticeViewModel): BottomSheetDialogFragment() {

    private val dataStore = UserDataStore()
    private lateinit var accessToken: String
    private var gymId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_notice_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        modifyClick()
        deleteClick()
    }

    private fun modifyClick() {
        view?.findViewById<TextView>(R.id.btnNoticeMoreModify)?.setOnClickListener {
            modifyNotice()
        }
    }

    private fun deleteClick() {
        view?.findViewById<TextView>(R.id.btnNoticeMoreDelete)?.setOnClickListener {
            deleteNotice()
        }
    }

    private fun modifyNotice() {
        // TODO 수정 Fragment 추가
        val bundle = Bundle().apply{
            putInt("noticeId", noticeId)
            putString("title", title)
            putString("content", content)
            putString("createAt", createAt)
        }
        findNavController().navigate(R.id.action_noticeFragment_to_noticeModifyFragment, bundle)
        dismiss()
    }

    private fun deleteNotice() {
        CoroutineScope(Dispatchers.IO).launch {
            accessToken = dataStore.getAccessToken().toString()
            gymId = dataStore.getGymId()
            viewModel.deleteNotice(accessToken, noticeId, gymId!!)
        }
        dismiss()

    }

}