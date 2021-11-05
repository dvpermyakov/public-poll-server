package com.public.poll.files

import java.io.File

class FileProviderImpl : FileProvider {

    override fun saveFile(path: String, data: ByteArray) {
        val file = File("$DEFAULT_FILE_SYSTEM_PATH$path")
        file.outputStream().use { stream ->
            stream.write(data)
        }
    }

    override fun getFile(path: String): File {
        return File("$DEFAULT_FILE_SYSTEM_PATH$path")
    }

    companion object {
        private const val DEFAULT_FILE_SYSTEM_PATH = "/server_poll_fs/"
    }
}