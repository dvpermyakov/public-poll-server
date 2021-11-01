package com.public.poll.files

interface FileProvider {
    fun saveFile(path: String, data: ByteArray)
}