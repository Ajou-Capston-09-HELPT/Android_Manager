package com.ajou.helptmanager.auth.view

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ajou.helptmanager.R

class PendingFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.isFocusableInTouchMode= true
        view.requestFocus()
        view.setOnKeyListener{_, keyCode, event->
            if (keyCode == KeyEvent.KEYCODE_BACK&& event.action== KeyEvent.ACTION_UP) {
                // 뒤로가기 버튼 동작을 막음
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }
}