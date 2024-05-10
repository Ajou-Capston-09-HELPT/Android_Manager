package com.ajou.helptmanager.home.view.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajou.helptmanager.AdapterToFragment
import com.ajou.helptmanager.R
import com.ajou.helptmanager.databinding.FragmentEquipmentListBinding
import com.ajou.helptmanager.home.adapter.EquipmentRVAdapter
import com.ajou.helptmanager.home.model.Equipment
import com.ajou.helptmanager.home.model.UserInfo

class EquipmentListFragment : Fragment(), AdapterToFragment {
    private var _binding : FragmentEquipmentListBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null
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
        _binding = FragmentEquipmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = listOf<Equipment>(Equipment(0,"name",4,3,20),
            Equipment(1,"name",4,3,20),
            Equipment(2,"name",4,3,20),
            Equipment(3,"name",4,3,20)
        )

        adapter = EquipmentRVAdapter(mContext!!, list, "search", this)
        binding.equipRV.adapter = adapter
        binding.equipRV.layoutManager = LinearLayoutManager(mContext)

        binding.btn.setOnClickListener {
            findNavController().navigate(R.id.action_equipmentListFragment_to_addEquipmentFragment)
        }

        binding.drawer.trainIcon.setImageResource(R.drawable.menu_machine_on)

        binding.drawer.train.setTextColor(resources.getColor(R.color.primary))
        binding.drawer.train.setTypeface(binding.drawer.user.typeface, Typeface.BOLD)

        binding.hamburger.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.drawer.drawer)
        }
        binding.drawer.ticket.setOnClickListener {
            findNavController().navigate(R.id.action_equipmentListFragment_to_membershipFragment)
            // 이용권으로 이동
        }
        binding.drawer.qr.setOnClickListener {
            // TODO QR스캔으로 이동
        }
        binding.drawer.train.setOnClickListener {
            findNavController().navigate(R.id.action_equipmentListFragment_self)
        }
        binding.drawer.user.setOnClickListener {
            findNavController().navigate(R.id.action_equipmentListFragment_to_searchUserFragment)
        }
        binding.drawer.notice.setOnClickListener {
            // TODO 공지사항으로 이동
        }
        binding.drawer.chat.setOnClickListener {
            // TODO 채팅으로 이동
        }
        binding.drawer.home.setOnClickListener {
            findNavController().navigate(R.id.action_equipmentListFragment_to_homeFragment)
            // 메인화면으로 이동
        }
    }

    override fun getSelectedItem(data: UserInfo) {
    }

    override fun getSelectedItem(data: Equipment, position: Int) {
        val dialog = EquipmentEditBottomSheetFragment(){value ->
            list[position].defaultWeight = value[0]
            list[position].defaultCount = value[1]
            list[position].defaultSet = value[2]
            adapter.notifyItemChanged(position)
        }
        dialog.show(parentFragmentManager,dialog.tag)
    }
}