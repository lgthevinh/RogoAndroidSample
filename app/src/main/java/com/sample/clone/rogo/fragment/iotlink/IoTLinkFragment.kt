package com.sample.clone.rogo.fragment.iotlink

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sample.clone.rogo.R
import rogo.iot.module.rogocore.sdk.SmartSdk
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.net.UnknownHostException

class IoTLinkFragment: Fragment() {
    private lateinit var udpSocket: DatagramSocket
    private lateinit var udpAddress: InetAddress
    private var udpPort: Int = 25405

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_iotlink, container, false)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishConnection()
        println("Established UDP connection")

        // Show IP address and port number
        val tvIpAddress = view.findViewById<TextView>(R.id.tv_ip_address)
        val tvPort = view.findViewById<TextView>(R.id.tv_port_number)
        val tvLog = view.findViewById<TextView>(R.id.tv_log)

        // Update tvLog with socket status every 1 second
        Thread {
            while (true) {
                Thread.sleep(1000)
                activity?.runOnUiThread {
                    tvLog.text = "Socket status: ${if (udpSocket.isClosed) "Closed" else "Open"}"
                }
            }
        }.start()

        tvIpAddress.text = getLocalIpAddress()
        tvPort.text = udpPort.toString()

        view.findViewById<Button>(R.id.btn_open).setOnClickListener {
            establishConnection()
        }

        view.findViewById<Button>(R.id.btn_close).setOnClickListener {
            udpSocket.close()
        }

        // display received message list


    }

//    override fun onDestroy() {
//        super.onDestroy()
//        udpSocket.close()
//    }

    @SuppressLint("DefaultLocale")
    private fun getLocalIpAddress(): String {
        try {
            val wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val dhcpInfo = wifiManager.dhcpInfo
            val ipAddress = dhcpInfo.ipAddress

            val ipString = String.format("%d.%d.%d.%d",
                (ipAddress and 0xff),
                (ipAddress shr 8 and 0xff),
                (ipAddress shr 16 and 0xff),
                (ipAddress shr 24 and 0xff))

            return ipString
        } catch (e: Exception) {
            return "Failed to get IP address"
        }
    }

    private fun establishConnection() {
        // Establish connection with IoT device
        try {
            udpAddress = getLocalIpAddress().let { InetAddress.getByName(it) }
            udpSocket = DatagramSocket(udpPort, udpAddress)

            // Start receiving UDP packets
            receiveUdpPackets()
        } catch (e: UnknownHostException) {
            println(e)
            Log.e("UdpFragment", "Unknown host", e)
        } catch (e: SocketException) {
            println(e)
            Log.e("UdpFragment", "Socket exception", e)
        }
    }

    private fun receiveUdpPackets() {
        Thread {
            while (true) {
                try {
                    val buffer = ByteArray(1024)
                    val packet = DatagramPacket(buffer, buffer.size)
                    udpSocket.receive(packet)

                    // Process received UDP packet
                    val message = String(packet.data, 0, packet.length)
                    println("Received UDP packet: $message")
                    handleMessage(message)
                } catch (e: IOException) {
                    Log.e("UdpFragment", "IOException", e)
                }
            }
        }.start()
    }

    private fun handleMessage(message: String) {
        // Handle received message
        val deviceId: String = message.split(":")[0]
        val elementId: String = message.split(":")[1]
        val powerMode: String = message.split(":")[2]

        SmartSdk.controlHandler().controlDevicePower(deviceId, elementId.toInt(), (powerMode.equals("1")), null)
    }
}