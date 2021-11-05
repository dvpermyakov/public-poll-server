package com.public.poll.files

import java.io.File

interface FileProvider {
    fun saveFile(path: String, data: ByteArray)
    fun getFile(path: String): File?
}