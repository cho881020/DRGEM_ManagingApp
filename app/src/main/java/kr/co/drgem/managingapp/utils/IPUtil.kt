package kr.co.drgem.managingapp.utils

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException

class IPUtil {

    companion object {
        fun getIpAddress(): String {
            var result = ""
            NetworkInterface.getNetworkInterfaces().iterator().forEach { networkInterface ->
                networkInterface.inetAddresses.iterator().forEach {
                    if (!it.isLoopbackAddress && isIPv4Address(it.hostAddress)) {
                        result = it.hostAddress
                    }
                }
            }
            return result
        }
        private fun isIPv4Address(address: String): Boolean {
            return if (address.isEmpty()) {
                false
            } else try {
                InetAddress.getByName(address) is Inet4Address
            } catch (exception: UnknownHostException) {
                false
            }
        }
    }

}