package com.public.poll.files

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo

class FileUploaderImpl(private val storageServiceProvider: StorageServiceProvider) : FileUploader {

    override fun uploadFile(name: String, data: ByteArray): String {
        val storage = storageServiceProvider.getStorage()
        val bucketName = storageServiceProvider.getBucketName()
        val blobId = BlobId.of(bucketName, name)
        val blobInfo = BlobInfo.newBuilder(blobId).build()
        storage.create(blobInfo, data)
        return storageServiceProvider.getStorageUrl(name)
    }
}