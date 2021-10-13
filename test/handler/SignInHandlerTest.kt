package handler

import com.public.poll.dto.ErrorDto
import com.public.poll.dto.TokenDto
import com.public.poll.dto.UserDto
import com.public.poll.handler.auth.SignInHandler
import com.public.poll.repositories.UserRepository
import com.public.poll.response.toResponse
import io.ktor.http.*
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test

class SignInHandlerTest {

    private val userRepository = mockk<UserRepository>()

    private val signInHandler = SignInHandler(
        userRepository = userRepository
    )

    @Test
    fun `empty token`() {
        val tokenDto = TokenDto("")
        val response = signInHandler.handle(tokenDto)

        assertEquals(
            response.status,
            HttpStatusCode.BadRequest
        )
        assertTrue(response.dto is ErrorDto)
    }

    @Test
    fun `response with user`() {
        val userDto = UserDto(
            id = "id",
            name = "username",
            email = "email@email.com"
        )
        every {
            userRepository.findUserByCredential(
                UserRepository.Credentials(
                    name = userDto.email,
                    password = "cGFzc3dvcmQ="
                )
            )
        } returns userDto

        val tokenDto = TokenDto("ZW1haWxAZW1haWwuY29tOmNHRnpjM2R2Y21RPQ==")
        val response = signInHandler.handle(tokenDto)

        assertEquals(
            response,
            userDto.toResponse()
        )
    }

    @Test
    fun `user not found`() {
        every { userRepository.findUserByCredential(any()) } returns (null)

        val tokenDto = TokenDto("ZW1haWxAZW1haWwuY29tOmNHRnpjM2R2Y21RPQ==")
        val response = signInHandler.handle(tokenDto)

        assertEquals(
            response.status,
            HttpStatusCode.BadRequest
        )
        assertTrue(response.dto is ErrorDto)
    }
}