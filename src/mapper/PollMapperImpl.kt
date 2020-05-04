package com.public.poll.mapper

import com.public.poll.dao.PollAnswerDao
import com.public.poll.dao.PollDao
import com.public.poll.dto.AnswerDto
import com.public.poll.dto.PollDto

class PollMapperImpl : PollMapper {
    override fun map(pollEntity: PollDao): PollDto {
        return PollDto(
            id = pollEntity.id.value.toString(),
            status = pollEntity.status,
            question = pollEntity.question,
            answers = pollEntity.answers.map { answerEntity -> map(answerEntity) },
            engagementRequired = pollEntity.engagementRequired,
            engagementCount = pollEntity.engagementCount.count(),
            likeCount = pollEntity.likes.count(),
            dislikeCount = pollEntity.dislikes.count()
        )
    }

    override fun map(answerEntity: PollAnswerDao): AnswerDto {
        return AnswerDto(
            id = answerEntity.id.value.toString(),
            text = answerEntity.text
        )
    }
}