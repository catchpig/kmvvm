package com.catchpig.utils.logger

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.orhanobut.logger.LogStrategy
import java.io.File
import java.io.FileWriter
import java.io.IOException

class MvvmDishLogStrategy(private val handler: WriteHandler) : LogStrategy {
    companion object {
        private const val TAG = "MvvmDishLogStrategy"
    }

    override fun log(level: Int, tag: String?, message: String) {
        handler.sendMessage(handler.obtainMessage(level, message))
    }

    class WriteHandler(
        private val looper: Looper,
        private val folder: String,
        private val maxFileSize: Int = 10 * 1024 * 1024
    ) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            val content = msg.obj as String

            var fileWriter: FileWriter? = null
            val logFile = getLogFile(folder, "logs")

            try {
                fileWriter = FileWriter(logFile, true)

                writeLog(fileWriter, content)

                fileWriter.flush()
                fileWriter.close()
            } catch (e: IOException) {
                if (fileWriter != null) {
                    try {
                        fileWriter.flush()
                        fileWriter.close()
                    } catch (e1: IOException) { /* fail silently */
                    }
                }
            }
        }

        private fun writeLog(fileWriter: FileWriter, content: String) {
            fileWriter.append(content)
        }

        private fun getLogFile(folderName: String, fileName: String): File {
            val folder = File(folderName)
            if (!folder.exists()) {
                folder.mkdirs()
            }

            var newFileCount = 0
            var newFile: File
            var existingFile: File? = null
            newFile = File(folder, String.format("%s_%s.log", fileName, newFileCount))
            while (newFile.exists()) {
                existingFile = newFile
                newFileCount++
                newFile = File(folder, String.format("%s_%s.log", fileName, newFileCount))
            }

            if (existingFile != null) {
                if (existingFile.length() >= maxFileSize) {
                    return newFile
                }
                return existingFile
            }

            return newFile
        }
    }
}