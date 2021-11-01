package com.public.poll.handler.user

import com.public.poll.files.FileProvider
import java.io.File

class RetrieveAvatarHandler(private val fileProvider: FileProvider) {

    fun handle(userId: String): File {
        return fileProvider.getFile("avatar_$userId.jpg")
    }

}