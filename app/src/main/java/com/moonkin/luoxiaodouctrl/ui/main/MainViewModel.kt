package com.moonkin.luoxiaodouctrl.ui.main

//import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    private var client: Mqtt3AsyncClient? = null
    private var mTopic:String = ""


    val TAG:String = "MQTT"
    companion object {
        val CMD_HEAD_UP =
            byteArrayOf(0xff.toByte(), 0x05, 0x0f, 0xf1.toByte(), 0x03, 0x00, 0xf2.toByte())
        val CMD_HEAD_UP_STEP = byteArrayOf(
            0xff.toByte(), 0x05, 0x13, 0xe2.toByte(), 0x03,
            0x99.toByte(), 0x78
        )
        val CMD_HEAD_DOWN =
            byteArrayOf(0xff.toByte(), 0x05, 0x04, 0xf1.toByte(), 0x03, 0xff.toByte(), 0x0d)
        val CMD_HEAD_DOWN_STEP = byteArrayOf(
            0xff.toByte(), 0x05, 0x06, 0xe2.toByte(), 0x13,
            0x99.toByte(), 0x68
        )
        val CMD_HEAD_LEFT = byteArrayOf(
            0xff.toByte(), 0x05, 0x07, 0xf2.toByte(), 0x03,
            0xbf.toByte(), 0x4e
        )
        val CMD_HEAD_LEFT_STEP = byteArrayOf(
            0xff.toByte(), 0x05, 0x0e,
            0xe1.toByte(), 0x13, 0x99.toByte(), 0x6b
        )
        val CMD_HEAD_RIGHT = byteArrayOf(
            0xff.toByte(), 0x05, 0x10, 0xf2.toByte(), 0x03, 0x40,
            0xb1.toByte()
        )
        val CMD_HEAD_RIGHT_STEP = byteArrayOf(
            0xff.toByte(), 0x05, 0x11,
            0xe1.toByte(), 0x03, 0x99.toByte(), 0x7b
        )
        val CMD_HAND_UP = byteArrayOf(
            0xff.toByte(), 0x05, 0x14,
            0xf5.toByte(), 0x03, 0xaa.toByte(), 0x5c
        )
        val CMD_HAND_DOWN = byteArrayOf(
            0xff.toByte(), 0x05, 0x1b, 0xf5.toByte(), 0x03, 0x00,
            0xf6.toByte()
        )
        val CMD_SHOULDER_UP = byteArrayOf(
            0xff.toByte(), 0x05, 0x20, 0xf3.toByte(), 0x03,
            0xbf.toByte(), 0x4f
        )
        val CMD_SHOULDER_DOWN = byteArrayOf(
            0xff.toByte(), 0x05, 0x22,
            0xf3.toByte(), 0x03, 0x15, 0xe5.toByte()
        )

        val CMD_LEFT: ByteArray = byteArrayOf(
            0xff.toByte(), 0x05, 0x07, 0xef.toByte(),
            0xb0.toByte(), 0x14, 0x4b
        )
        val CMD_RIGHT: ByteArray = byteArrayOf(
            0xff.toByte(), 0x05, 0x0d,
            0xef.toByte(), 0x0b, 0x14, 0xf0.toByte()
        )
        val CMD_FORWARD: ByteArray = byteArrayOf(
            0xff.toByte(), 0x05, 0x24,
            0xef.toByte(), 0xee.toByte(), 0x14, 0x15
        )
        val CMD_BACKWARD: ByteArray = byteArrayOf(
            0xff.toByte(), 0x05, 0x30,
            0xef.toByte(), 0x66, 0x14, 0x9d.toByte()
        )
        val CMD_RESET: ByteArray =
            byteArrayOf(0xff.toByte(), 0x04, 0x13, 0xf0.toByte(), 0xfc.toByte(), 0x0c)
        val CMD_OFF: ByteArray = byteArrayOf(
            0xff.toByte(), 0x04, 0x14, 0xa0.toByte(), 0x00,
            0xa0.toByte()
        )
    }
    // TODO: Implement the ViewModel
    //enum class Command { CMD_HEAD_UP, CMD_HEAD_UP_STEP, FORWARD, BACKWARD }
    fun initMQTT(host:String, port:Int,id:String, topic:String) {
        mTopic = topic
        client = MqttClient.builder()
            .useMqttVersion3()
            .identifier(id)
            .serverHost(host)
            .serverPort(port)
            .buildAsync()
    }
    fun connect() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                client!!.connectWith()
                    .simpleAuth()
                    .username("hnmsky")
                    .password("821122hh".toByteArray())
                    .applySimpleAuth()
                    .send()
                    .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                        if (throwable != null) {
                            // handle failure
                            Log.i(TAG, "connect fail")
                        } else {
                            // setup subscribes or start publishing
                            Log.i(TAG, "connect success")
                            client!!.subscribeWith()
                                .topicFilter(mTopic)
                                .callback { publish: Mqtt3Publish? -> Log.i(TAG,
                                    publish!!.payload.toString()
                                )}
                                .send()
                                .whenComplete { subAck: Mqtt3SubAck?, throwable: Throwable? ->
                                    if (throwable != null) {
                                        // Handle failure to subscribe
                                        Log.i(TAG, "sub fail")
                                    } else {
                                        Log.i(TAG, "sub success")
                                        // Handle successful subscription, e.g. logging or incrementing a metric
                                    }
                                }
                        }
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    fun publish(payload:ByteArray) {
        client!!.publishWith()
            .topic(mTopic)
            .payload(payload)
            .send()
            .whenComplete { publish: Mqtt3Publish?, throwable: Throwable? ->
                if (throwable != null) {
                    // handle failure to publish
                    Log.i(TAG, "push fail")
                } else {
                    Log.i(TAG, "push success")
                    // handle successful publish, e.g. logging or incrementing a metric
                }
            }
    }

    fun command(bytes:ByteArray) {
        publish(bytes)
    }
    fun disconnect() {
        client!!.disconnect()
    }
}