package com.sample.clone.rogo.rface

import android.util.Base64
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.URL

object RFaceServiceHandler {
    private val DEFAULT_SERVICE_PORT = 2248

    private var serviceIp: String = ""

    // Get local IP address in order to get the subnet
    private fun getLocalIpAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            for (intf in interfaces) {
                val addrs = intf.inetAddresses
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress && addr is Inet4Address) {
                        return addr.hostAddress // Return local IP
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    private fun isDeviceAlive(ip: String): Boolean {
        return try {
            val address = InetAddress.getByName(ip)
            return address.isReachable(100)
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }

    private fun isServiceAlive(ip: String): Boolean {
        // Check if service is alive
        return try {
            val url = "http://$ip:$DEFAULT_SERVICE_PORT/ping"
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 100
            connection.readTimeout = 100
            connection.requestMethod = "GET"

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)

                json.optString("message") == "Pong! RFace Service is here!"
            } else {
                false
            }
        } catch (timeout: java.net.SocketTimeoutException) {
            false
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }

    }

    fun scanRFaceService(): Boolean {
        try {
            val localIp = getLocalIpAddress()
            if (localIp != null) {
                val subnet = localIp.substring(0, localIp.lastIndexOf("."))
                for (i in 0..255) {
                    val host = "$subnet.$i"
                    println("Scanning $host ${isServiceAlive(host)}")
                    if (isServiceAlive(host)) {
                        serviceIp = host
                        return true
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return false
    }



    fun registerFace(image: ByteArray, name: String, callback: (Boolean, String) -> Unit): Pair<Boolean, String> {
        // convert image to base64
        try {
            val base64Image = Base64.encodeToString(image, Base64.NO_WRAP)
            val url = "http://$serviceIp:$DEFAULT_SERVICE_PORT/api/register_face"
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val json = JSONObject()
            json.put("image", base64Image)
            json.put("name", name)

            connection.outputStream.write(json.toString().toByteArray())

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                callback(true, "Face registered successfully")
                return Pair(true, "Face registered successfully")
            } else {
                callback(false, json.optString("message"))
                return Pair(false, json.optString("message"))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return Pair(false, ex.message ?: "Unknown error")
        }

    }
}