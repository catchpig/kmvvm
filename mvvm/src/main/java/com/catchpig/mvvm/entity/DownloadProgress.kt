package com.catchpig.mvvm.entity

data class DownloadProgress(
    /**
     * 当前文件读取长度
     */
    var readLength: Long,
    /**
     * 当前文件总长度
     */
    var countLength: Long,
    /**
     * 完成文件数
     */
    var completeCount: Int,
    /**
     * 总文件数
     */
    var totalCount: Int
)
