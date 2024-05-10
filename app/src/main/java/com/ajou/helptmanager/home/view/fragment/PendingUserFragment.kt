package com.ajou.helptmanager.home.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.databinding.FragmentPendingUserBinding
import com.ajou.helptmanager.home.adapter.UserInfoRVAdapter
import com.ajou.helptmanager.home.model.UserInfo
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel

class PendingUserFragment : Fragment(), AdapterToFragment {
    private var _binding : FragmentPendingUserBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null
    private lateinit var viewModel : UserInfoViewModel
    private val TAG = PendingUserFragment::class.java.simpleName

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
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]
        _binding = FragmentPendingUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = listOf<UserInfo>(
            UserInfo("최윤서",null,null,null),
            UserInfo("최서",null,null,null),
            UserInfo("최윤",null,null,null),
            UserInfo("최윤서",null,null,null),
            UserInfo("윤서",null,null,null)
        )

        var adapter : UserInfoRVAdapter = UserInfoRVAdapter(mContext!!, list, this)

        binding.userRV.adapter = adapter
        binding.userRV.layoutManager = LinearLayoutManager(mContext)

        binding.user.setOnEditorActionListener { view, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH){
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)
                adapter.updateList(list.filter { it.name.contains(binding.user.text)  })
                return@setOnEditorActionListener true
            }else return@setOnEditorActionListener false
        }

    }

    override fun getSelectedItem(data: UserInfo) {
        viewModel.setUserInfo(data)
    }

}