package com.catchpig.utils.ext

import java.io.File

fun File.deleteAll() {
    deleteFile(this)
}

private fun deleteFile(file: File) {
    if (file.isFile) {
        file.delete()
    } else if (file.isDirectory) {
        file.listFiles().forEach {
            deleteFile(it)
        }
    }
}