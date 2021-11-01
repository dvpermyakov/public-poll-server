package com.public.poll.files

import java.io.File
import java.io.FileOutputStream

class FileProviderImpl : FileProvider {
    override fun saveFile(path: String, data: ByteArray) {
        val file = File("/Users/dmitriip/$path")
        val stream = FileOutputStream(file)
        stream.write(data)
        stream.close()
    }
}