package com.catchpig.utils.ext

import java.security.MessageDigest

/**
 *
 * @author TLi2
 **/

/**
 * md5加密
 */
fun String.md5():String{
    val instance: MessageDigest = MessageDigest.getInstance("MD5")
    //对字符串加密，返回字节数组
    val digest:ByteArray = instance.digest(this.toByteArray())
    var sb = StringBuffer()
    for (b in digest) {
        //获取低八位有效值
        var i :Int = b.toInt() and 0xff
        //将整数转化为16进制
        var hexString = Integer.toHexString(i)
        if (hexString.length < 2) {
            //如果是一位的话，补0
            hexString = "0" + hexString
        }
        sb.append(hexString)
    }
    return sb.toString()
}

/**
 * md5加密
 */
fun ByteArray.md5():String{
    val instance: MessageDigest = MessageDigest.getInstance("MD5")
    //对字符串加密，返回字节数组
    val digest:ByteArray = instance.digest(this)
    var sb = StringBuffer()
    for (b in digest) {
        //获取低八位有效值
        var i :Int = b.toInt() and 0xff
        //将整数转化为16进制
        var hexString = Integer.toHexString(i)
        if (hexString.length < 2) {
            //如果是一位的话，补0
            hexString = "0$hexString"
        }
        sb.append(hexString)
    }
    return sb.toString()
}