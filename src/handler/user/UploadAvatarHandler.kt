package com.public.poll.handler.user

import com.public.poll.dto.UploadDto
import com.public.poll.dto.UserDto
import com.public.poll.files.FileProvider
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import java.io.InputStream

class UploadAvatarHandler(private val fileProvider: FileProvider) {
    fun handle(
        userDto: UserDto,
        inputStream: InputStream
    ): CommonResponse {
        fileProvider.saveFile("avatar_${userDto.id}.jpg", inputStream.readAllBytes())
        return UploadDto(success = true).toResponse()
    }
}