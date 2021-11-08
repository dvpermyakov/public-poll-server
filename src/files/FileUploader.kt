package com.public.poll.files

interface FileUploader {
    fun uploadFile(name: String, data: ByteArray): String
}