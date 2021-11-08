package com.public.poll.handler.user

import com.public.poll.dto.UploadDto
import com.public.poll.dto.UserDto
import com.public.poll.files.FileUploader
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import java.io.InputStream

class UploadAvatarHandler(private val fileUploader: FileUploader) {
    fun handle(
        userDto: UserDto,
        inputStream: InputStream,
        fileName: String?
    ): CommonResponse {
        val imagePath = fileUploader.uploadFile(
            name = "${userDto.id}/$fileName",
            data = inputStream.readBytes(),
            contentType = "image"
        )
        return UploadDto(image = imagePath).toResponse()
    }
}