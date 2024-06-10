package com.ajou.helptmanager.home.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajou.helptmanager.R
import com.ajou.helptmanager.UserDataStore
import com.ajou.helptmanager.databinding.FragmentEntryLogBinding
import com.ajou.helptmanager.home.adapter.EntryLogRVAdapter
import com.ajou.helptmanager.home.model.EntryLog
import com.ajou.helptmanager.home.view.HomeActivity
import com.ajou.helptmanager.home.view.dialog.ChatLinkSettingDialog
import com.ajou.helptmanager.home.view.dialog.SelectDateDialog
import com.ajou.helptmanager.network.RetrofitInstance
import com.ajou.helptmanager.network.api.EntryLogService
import com.ajou.helptmanager.setOnSingleClickListener
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class EntryLogFragment : Fragment() {
    private var _binding : FragmentEntryLogBinding? = null
    private val binding get() = _binding!!
    private var mContext : Context? = null
    private lateinit var adapter : EntryLogRVAdapter
    private val dataStore = UserDataStore()
    private val entryLogService = RetrofitInstance.getInstance().create(EntryLogService::class.java)
    private var gymId : Int? = -1
    private var originList = emptyList<EntryLog>()

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
        _binding = FragmentEntryLogBinding.inflate(layoutInflater, container, false)
        val today = LocalDate.now()

        CoroutineScope(Dispatchers.IO).launch {
            val userName = dataStore.getUserName()
            withContext(Dispatchers.Main) {
                binding.drawer.name.text = userName
            }
        }
        getEntryLogs("",today)
        adapter = EntryLogRVAdapter(mContext!!, emptyList())

        binding.logRV.adapter = adapter
        binding.logRV.layoutManager = LinearLayoutManager(mContext)

        binding.drawer.entryLogIcon.setImageResource(R.drawable.home_entry_on)

        binding.drawer.entryLog.setTextColor(resources.getColor(R.color.primary))
        binding.drawer.entryLog.setTypeface(binding.drawer.entryLog.typeface, Typeface.BOLD)

        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        binding.date.text = today.format(format)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.user.setOnEditorActionListener { view, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH){
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)

                adapter.filterList(originList, binding.user.text.toString())
                return@setOnEditorActionListener true
            }else return@setOnEditorActionListener false
        }

        binding.removeBtn.setOnSingleClickListener {
            binding.user.setText("")
            adapter.updateList(originList)
        }
        binding.date.setOnSingleClickListener {
            val dialog = SelectDateDialog() { value ->
                val date = LocalDate.parse(value, DateTimeFormatter.ISO_DATE)

                binding.date.text = value

                binding.loadingBar.show()
                getEntryLogs("",date)
            }
            dialog.show(requireActivity().supportFragmentManager, "date")
        }

        binding.hamburger.setOnSingleClickListener {
            binding.drawerLayout.openDrawer(binding.drawer.drawer)
        }

        binding.drawer.closeBtn.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
        }
        binding.drawer.ticket.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_entryLogFragment_to_membershipFragment)
        }
        binding.drawer.qr.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_entryLogFragment_to_homeFragment)
        }
        binding.drawer.train.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_entryLogFragment_to_equipmentListFragment)
        }
        binding.drawer.user.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_entryLogFragment_to_searchUserFragment)
        }
        binding.drawer.notice.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_entryLogFragment_to_noticeFragment)
        }
        binding.drawer.chat.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_entryLogFragment_to_homeFragment)
        }
        binding.drawer.home.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_entryLogFragment_to_homeFragment)
        }

        binding.drawer.entryLog.setOnSingleClickListener {
            binding.drawerLayout.closeDrawer(binding.drawer.drawer)
            findNavController().navigate(R.id.action_entryLogFragment_self)
        }
    }

    private fun getEntryLogs(name: String,date:LocalDate){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = dataStore.getAccessToken()
            if (gymId == -1) gymId = dataStore.getGymId()
            val entryLogDeferred = async{ entryLogService.getEntryLogs(accessToken!!,name,gymId!!,date) }
            val entryLogResponse = entryLogDeferred.await()
            withContext(Dispatchers.Main){
                if (entryLogResponse.isSuccessful) {
                    val body = entryLogResponse.body()!!.data

                    originList = body
                    binding.loadingBar.hide()

                    if (body.isNotEmpty()) {
                        adapter.updateList(body)
                        binding.noDataTxt.visibility = View.GONE
                    }
                    else {
                        binding.noDataTxt.visibility = View.VISIBLE
                        binding.logRV.visibility = View.INVISIBLE
                    }
                } else {
                    Log.d("entryLogResponse fail",entryLogResponse.errorBody()?.string().toString())
                }
            }
        }
    }

}