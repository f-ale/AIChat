package com.francescoalessi.parla.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.regex.Pattern

@Entity
data class TextGenerationHost (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val ipAddress:String,
    val ipPort:String
)
fun TextGenerationHost.isValidHost(): Boolean {
    return this.ipAddress.isValidIP() && this.ipPort.isValidPort()
}

fun String.isValidIP(): Boolean {
    // Regular expression for IPv4
    val ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    // Regular expression for IPv6
    val ipv6Pattern = "([0-9a-fA-F]{1,4}:){7}([0-9a-fA-F]{1,4}|:)|(([0-9a-fA-F]{1,4}:){1,7}|:)|(([0-9a-fA-F]{1,4}:){1,6}:([0-9a-fA-F]{1,4}|:))|(([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2})|(([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3})|(([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4})|(([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5})|([0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6}))|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])"

    return Pattern.matches(ipv4Pattern, this) || Pattern.matches(ipv6Pattern, this)
}

fun String.isValidPort(): Boolean {
    return try {
        val portInt = this.toInt()
        portInt in 1..65535
    } catch (e: NumberFormatException) {
        false
    }
}