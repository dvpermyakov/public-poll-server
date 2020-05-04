package com.public.poll.handler.auth

import com.public.poll.dto.ErrorDto
import com.public.poll.dto.TokenDto
import com.public.poll.repositories.UserRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import java.util.*

class SignInHandler(
    private val userRepository: UserRepository
) {

    fun handle(tokenDto: TokenDto): CommonResponse {
        val decodedToken = try {
            String(Base64.getDecoder().decode(tokenDto.token))
        } catch (ignore: Exception) {
            return ErrorDto("Token can't be parsed").toResponse()
        }
        val tokenSlices = decodedToken.split(":")
        if (tokenSlices.size < 2) {
            return ErrorDto("Token has wrong format").toResponse()
        }
        val nameFromToken = tokenSlices[0]
        val passFromToken = tokenSlices[1]

        val userDto = userRepository.findUserByCredential(
            UserRepository.Credentials(
                name = nameFromToken,
                password = passFromToken
            )
        )
        return userDto?.toResponse() ?: ErrorDto("Can't find user").toResponse()
    }
}