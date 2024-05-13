package com.ajou.helptmanager.auth.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentSetBizInfoBinding
import com.ajou.helptmanager.getFileName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetBizInfoFragment : Fragment() {
    private var _binding: FragmentSetBizInfoBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private lateinit var viewModel: AuthInfoViewModel
    private val dataStore = UserDataStore()
    private var userName : String? = null
    private var kakaoId : String? = null
    private lateinit var launcher: ActivityResultLauncher<Intent>
//    private val memberService = RetrofitInstance.getInstance().create(MemberService::class.java)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intent = checkNotNull(result.data)
                val imageUri = intent.data // 갤러리에서 선택한 사진 받아옴
                if (imageUri != null) {
                    Log.d("check imageuri",imageUri.toString())
                    viewModel.setBizImg(imageUri)
                    val fileName = getFileName(imageUri!!, requireActivity())
                    binding.bizImg.text = fileName.toString()
                    binding.bizImg.setTextColor(resources.getColor(R.color.black))
                    binding.bizImgIcon.visibility = View.GONE
                    binding.bizImgIconRemove.visibility = View.VISIBLE
                    binding.bizImg.isSelected = true
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity())[AuthInfoViewModel::class.java]
        _binding = FragmentSetBizInfoBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            userName = dataStore.getUserName()
            kakaoId = dataStore.getKakaoId()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = resources.getStringArray(R.array.biz_division)
        val adapter =  object : ArrayAdapter<String>(requireContext(), R.layout.item_spinner, R.id.item) {
            override fun getCount(): Int {
                return super.getCount()-1
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)

                if (position == count) {
                    //마지막 포지션의 textView 를 힌트 용으로 사용합니다.
                    (view.findViewById<View>(R.id.item) as TextView).text = ""
                    //아이템의 마지막 값을 불러와 hint로 추가해 줍니다.
                    (view.findViewById<View>(R.id.item) as TextView).hint = getItem(count)
                }

                return view
            }
        }
        adapter.addAll(list.toMutableList())
        adapter.setDropDownViewResource(R.layout.item_spinner_drop_down)
        binding.division.adapter = adapter
        binding.division.setSelection(adapter.count)

        binding.division.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != list.size-1) {
                    viewModel.setDivisionInfo(binding.division.getItemAtPosition(p2).toString())
                    viewModel.setDone(true)
                    binding.division.setBackgroundResource(R.drawable.auth_spinner_selected)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.bizImg.setOnClickListener {
            val intent = Intent().also { intent ->
                intent.type = "image/"
                intent.action = Intent.ACTION_GET_CONTENT
            }
            launcher.launch(intent)
        }

        binding.bizImgIconRemove.setOnClickListener {
            viewModel.setBizImg(null)
            viewModel.setDone(false)

            binding.bizImgIconRemove.visibility = View.GONE
            binding.bizImgIcon.visibility = View.VISIBLE
            binding.bizImg.text = "이미지를 업로드해주세요"
            binding.bizImg.isSelected = false
            // TODO imageuri 제거
        }

        binding.bizNum.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(str: Editable?) {
                if(str!!.isNotEmpty()) {
                    viewModel.setBizNumInfo(str.toString().toInt())
                    viewModel.setDone(true)
                    binding.bizNum.isSelected = true
                } else {
                    viewModel.setDone(true)
                    viewModel.setBizNumInfo(null)
                    binding.bizNum.isSelected = false
                }
            }
        })

        binding.bizName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(str: Editable?) {
                if(str!!.isNotEmpty()) {
                    viewModel.setBizNameInfo(str.toString())
                    viewModel.setDone(true)
                    binding.bizName.isSelected = true
                } else {
                    viewModel.setDone(true)
                    viewModel.setBizNameInfo(null)
                    binding.bizName.isSelected = false
                }
            }
        })

        viewModel.done.observe(viewLifecycleOwner) {
            binding.nextBtn.isEnabled = viewModel.bizImg.value != null && viewModel.bizName.value != null && viewModel.bizNum.value != null && viewModel.division.value != null
        }

        viewModel.bizImg.observe(viewLifecycleOwner) {
            binding.nextBtn.isEnabled = viewModel.bizImg.value != null && viewModel.bizName.value != null && viewModel.bizNum.value != null && viewModel.division.value != null
        }

        binding.nextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_setBizInfoFragment_to_setGymInfoFragment)
        }
    }
}