package com.public.poll.handler.auth

import com.public.poll.dto.ErrorDto
import com.public.poll.dto.TokenDto
import com.public.poll.repositories.UserRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SignUpHandler(
    private val userRepository: UserRepository
) {

    fun handle(tokenDto: TokenDto): CommonResponse {
        val decodedToken = try {
            String(Base64.getDecoder().decode(tokenDto.token))
        } catch (ignore: Exception) {
            return ErrorDto("Token can't be parsed").toResponse(HttpStatusCode.BadRequest)
        }
        val tokenSlices = decodedToken.split(":")
        if (tokenSlices.size < 3) {
            return ErrorDto("Token has wrong format").toResponse(HttpStatusCode.BadRequest)
        }
        val nameFromToken = tokenSlices[0]
        val passFromToken = tokenSlices[1]
        val emailFromToken = tokenSlices[2]

        return transaction {
            if (userRepository.findUserByEmail(emailFromToken) == null) {
                userRepository.createUser(
                    name = nameFromToken,
                    pass = passFromToken,
                    email = emailFromToken
                ).toResponse(HttpStatusCode.Created)
            } else {
                ErrorDto("User with $emailFromToken already exists").toResponse()
            }
        }
    }
}