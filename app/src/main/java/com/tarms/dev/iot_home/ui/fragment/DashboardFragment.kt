package com.tarms.dev.iot_home.ui.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.tarms.dev.iot_home.R
import com.tarms.dev.iot_home.data.Firm
import com.tarms.dev.iot_home.databinding.FragmentDashboardBinding
import com.tarms.dev.iot_home.repository.MyViewModel
import com.tarms.dev.iot_home.service.ClickEventListener
import com.tarms.dev.iot_home.utils.FirebaseUtil
import com.tarms.dev.iot_home.utils.Utils

class DashboardFragment : Fragment(), ClickEventListener {
    lateinit var myViewModel: MyViewModel
    lateinit var firm: Firm
    private val mRef =
        FirebaseDatabase.getInstance().reference.child(Utils.firmRef("mazharul_sabbir"))
    private lateinit var valueEventListener: ValueEventListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)


        val binding = DataBindingUtil.inflate<FragmentDashboardBinding>(
            inflater,
            R.layout.fragment_dashboard,
            container,
            false
        ).apply {
            this.lifecycleOwner = this@DashboardFragment
            this.firm = myViewModel
            this.clickHandler = this@DashboardFragment
        }

        return binding.root
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myViewModel.getCurrentData().observe(this, Observer {
            firm = it
        })

        FirebaseUtil.getFirmInfo {
            myViewModel.updateFirmData(it)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onViewClick(view: View) {
        val mRef = FirebaseDatabase.getInstance().reference.child(Utils.firmRef("mazharul_sabbir"))
            .child("1581694698821")
        when (view.id) {
            R.id.light -> firm.light.l_status = !firm.light.l_status!!

            R.id.pump -> firm.pump.p_status = !firm.pump.p_status!!

        }

        mRef.setValue(firm).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    context, "Changed!", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mRef.removeEventListener(valueEventListener)
    }

}
