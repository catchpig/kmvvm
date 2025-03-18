package com.catchpig.utils.ext

import java.io.File

fun File.deleteAll() {
    deleteFile(this)
}

private fun deleteFile(file: File) {
    if (file.isFile) {
        file.delete()
    } else if (file.isDirectory) {
        file.listFiles()?.forEach {
            deleteFile(it)
        }
    }
}

/**
 * 删除当前文件下的子文件和子文件夹
 * @receiver File
 */
fun File.deleteChildFiles() {
    deleteChildFile(this)
}

private fun deleteChildFile(directory: File) {
    if (directory.exists() && directory.isDirectory) {
        val files: Array<File>? = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isFile) {
                    file.delete()
                } else if (file.isDirectory) {
                    deleteChildFile(file)
                }
            }
        }
    }
}