package com.moonkin.luoxiaodouctrl.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.moonkin.luoxiaodouctrl.R
import java.util.*

class MainFragment : Fragment() {
    val host = "broker.hivemq.com"
    val port = 1883
    val topic = "luoxiaodou_1"
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        activity?.findViewById<Button?>(R.id.btn_connect)?.setOnClickListener {
            viewModel.initMQTT(host,port, UUID.randomUUID().toString(), topic)
            viewModel.connect()
        }
        activity?.findViewById<Button?>(R.id.btn_publish)?.setOnClickListener {
            viewModel.publish("phone".toByteArray())
        }
        activity?.findViewById<Button?>(R.id.btn_left)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_LEFT)
        }
        activity?.findViewById<Button?>(R.id.btn_right)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_RIGHT)
        }
        activity?.findViewById<Button?>(R.id.btn_forward)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_FORWARD)
        }
        activity?.findViewById<Button?>(R.id.btn_backward)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_BACKWARD)
        }
        activity?.findViewById<Button?>(R.id.btn_headup)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_HEAD_UP)
        }
        activity?.findViewById<Button?>(R.id.btn_headup_step)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_HEAD_UP_STEP)
        }
        activity?.findViewById<Button?>(R.id.btn_headdown)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_HEAD_DOWN)
        }
        activity?.findViewById<Button?>(R.id.btn_headdown_step)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_HEAD_DOWN_STEP)
        }
        activity?.findViewById<Button?>(R.id.btn_headleft)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_HEAD_LEFT)
        }
        activity?.findViewById<Button?>(R.id.btn_headleft_step)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_HEAD_LEFT_STEP)
        }
        activity?.findViewById<Button?>(R.id.btn_headright)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_HEAD_RIGHT)
        }
        activity?.findViewById<Button?>(R.id.btn_headright_step)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_HEAD_RIGHT_STEP)
        }

        activity?.findViewById<Button?>(R.id.btn_handup)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_HAND_UP)
        }
        activity?.findViewById<Button?>(R.id.btn_handdown)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_HAND_DOWN)
        }
        activity?.findViewById<Button?>(R.id.btn_shoulderup)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_SHOULDER_UP)
        }
        activity?.findViewById<Button?>(R.id.btn_shoulderdown)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_SHOULDER_DOWN)
        }
        activity?.findViewById<Button?>(R.id.btn_reset)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_RESET)
        }
        activity?.findViewById<Button?>(R.id.btn_off)?.setOnClickListener {
            viewModel.command(MainViewModel.CMD_OFF)
        }
    }
    override fun onDestroy () {
        super.onDestroy()
        viewModel.disconnect()
    }
}