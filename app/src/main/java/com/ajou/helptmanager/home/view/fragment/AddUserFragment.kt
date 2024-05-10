package com.ajou.helptmanager.home.view.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.R
import com.ajou.helptmanager.databinding.FragmentAddUserBinding
import com.ajou.helptmanager.home.viewmodel.UserInfoViewModel
import java.time.LocalDate
import java.util.*

class AddUserFragment : Fragment() {
    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private val TAG = AddUserFragment::class.java.simpleName
    private lateinit var viewModel : UserInfoViewModel

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
        _binding = FragmentAddUserBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[UserInfoViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val today: LocalDate = LocalDate.now()

        binding.period.setOnClickListener {

            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                val period = "${today.year}/${today.monthValue}/${today.dayOfMonth} ~ ${year}/${month+1}/${day}"
                binding.period.text = period
            }
            DatePickerDialog(
                mContext!!,
                data,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addUserFragment_to_searchUserFragment)
        }

        binding.nextBtn.setOnClickListener {
            // TODO 회원추가 api 연동
        }
        binding.removeBtn.setOnClickListener {
            // TODO 승인거절 api 연동
      }
        viewModel.setUserInfo(null)
    }
}