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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.databinding.FragmentAddEquipmentBinding
import com.ajou.helptmanager.home.adapter.EquipmentRVAdapter
import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.UserInfo

class AddEquipmentFragment : Fragment(), AdapterToFragment {
    private var _binding: FragmentAddEquipmentBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private lateinit var adapter : EquipmentRVAdapter
    private var list = emptyList<Equipment>()

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
        _binding = FragmentAddEquipmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = listOf<Equipment>(Equipment(0,"name",4,3,20),
            Equipment(1,"ame",4,3,20),
            Equipment(2,"name",4,3,20),
            Equipment(3,"nae",4,3,20)
        )

        adapter = EquipmentRVAdapter(mContext!!, list, "add", this)
        binding.equipRV.adapter = adapter
        binding.equipRV.layoutManager = LinearLayoutManager(mContext)

        binding.equip.setOnEditorActionListener { view, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH){
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)
                adapter.updateList(list.filter { it.equipmentName.contains(binding.equip.text)  })
                return@setOnEditorActionListener true
            }else return@setOnEditorActionListener false
        }

    }

    override fun getSelectedItem(data: UserInfo) {
    }

    override fun getSelectedItem(data: Equipment, position: Int) {
        Log.d("selectedItem in add",data.toString())

    }
}