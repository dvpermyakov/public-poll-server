package com.public.poll.files

import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions

class StorageServiceProviderImpl : StorageServiceProvider {
    private val storage = StorageOptions.newBuilder()
        .setProjectId(System.getenv("STORAGE_SERVICE_PROJECT_ID"))
        .build()
        .service

    override fun getStorage(): Storage = storage

    override fun getBucketName(): String = System.getenv("STORAGE_SERVICE_BUCKET_NAME")

    override fun getStorageUrl(name: String): String = "https://storage.googleapis.com/${getBucketName()}/$name"
}