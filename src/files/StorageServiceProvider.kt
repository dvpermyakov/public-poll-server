package com.public.poll.files

import com.google.cloud.storage.Storage

interface StorageServiceProvider {
    fun getStorage(): Storage
    fun getBucketName(): String
    fun getStorageUrl(name: String): String
}