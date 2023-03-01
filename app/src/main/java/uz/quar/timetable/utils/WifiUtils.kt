package uz.quar.timetable.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import java.util.*


@Suppress("DEPRECATION")
class WifiUtils(
    private val context: Context,
    private val networkSSID: String,
    private val password: String
) {
    private var wifiIsConnected = false
    private val wifiManager =
        context.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager

    fun checkConnection(): Boolean {
        val conMgr: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        return isWifiOn() && netInfo != null
    }

    private fun isWifiOn() = wifiManager.isWifiEnabled

    @SuppressLint("MissingPermission")
    fun connectToNetworkWPA(): Boolean {
        if (isWifiOn()) {
            wifiManager.isWifiEnabled = true
        } else return false
        try {
            val conf = WifiConfiguration()
            conf.SSID = "\"" + networkSSID + "\""
            conf.preSharedKey = "\"" + password + "\""
            conf.status = WifiConfiguration.Status.ENABLED
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
            wifiManager.addNetwork(conf)
            wifiManager.configuredNetworks.find { it.SSID != null && it.SSID == "\"" + networkSSID + "\"" }
                ?.let {
                    wifiManager.disconnect()
                    wifiManager.enableNetwork(it.networkId, true)
                    wifiManager.reconnect()
                    wifiIsConnected = true

                }
        } catch (ex: Exception) {
            wifiIsConnected = false

        }

        return wifiIsConnected
    }

}