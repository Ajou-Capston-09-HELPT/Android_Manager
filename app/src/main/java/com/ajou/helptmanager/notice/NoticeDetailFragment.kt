package com.ajou.helptmanager.notice

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.databinding.FragmentNoticeDetailBinding
import com.ajou.helptmanager.setOnSingleClickListener


class NoticeDetailFragment : Fragment() {

    private var _binding: FragmentNoticeDetailBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null

    private var title : String? = null
    private var content : String? = null
    private var date : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeDetailBinding.inflate(layoutInflater, container, false)


        arguments?.let {
            title = it.getString("title")
            content = it.getString("content")
            date = it.getString("createAt")
        }

        // Detail 내용 설정
        binding.noticeDetailTitleTV.text = title
        binding.noticeDetailContentTV.text = content
        binding.noticeDetailDateTV.text = date

        pressBackButton()


        return binding.root
    }

    private fun pressBackButton() {
        binding.noticeDetailToolbar.noticeDetailBackButton.setOnSingleClickListener {
            findNavController().popBackStack()
        }
    }

}