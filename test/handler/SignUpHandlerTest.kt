package handler

import com.public.poll.dto.ErrorDto
import com.public.poll.dto.TokenDto
import com.public.poll.dto.UserDto
import com.public.poll.handler.auth.SignUpHandler
import com.public.poll.repositories.UserRepository
import io.ktor.http.*
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SignUpHandlerTest {

    private val userRepository = mockk<UserRepository>()

    private val signUpHandler = SignUpHandler(
        userRepository = userRepository
    )

    @Test
    fun `empty token`() {
        val tokenDto = TokenDto("")
        val response = signUpHandler.handle(tokenDto)

        assertEquals(
            response.status,
            HttpStatusCode.BadRequest
        )
        assertTrue(response.dto is ErrorDto)
    }

    @Test
    fun `user already exist`() {
        val userDto = UserDto(
            id = "id",
            name = "username",
            email = "email@email.com"
        )
        every {
            userRepository.findUserByEmail(userDto.email)
        } returns userDto

        val tokenDto = TokenDto("dXNlcm5hbWU6Y0dGemMzZHZjbVE9OmVtYWlsQGVtYWlsLmNvbQ==")
        val response = signUpHandler.handle(tokenDto)

        assertEquals(
            response.status,
            HttpStatusCode.BadRequest
        )
        assertTrue(response.dto is ErrorDto)
    }

    @Test
    fun `successful user creation`() {
        val userDto = UserDto(
            id = "id",
            name = "username",
            email = "email@email.com"
        )
        every {
            userRepository.findUserByEmail(userDto.email)
        } returns null

        every {
            userRepository.createUser(userDto.name, any(), userDto.email)
        } returns userDto

        val tokenDto = TokenDto("dXNlcm5hbWU6Y0dGemMzZHZjbVE9OmVtYWlsQGVtYWlsLmNvbQ==")
        val response = signUpHandler.handle(tokenDto)

        assertEquals(
            response.status,
            HttpStatusCode.Created
        )
    }
}