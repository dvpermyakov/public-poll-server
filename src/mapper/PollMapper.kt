package com.public.poll.mapper

import com.public.poll.dao.PollDao
import com.public.poll.dto.PollDto

class PollMapper {
    fun map(pollEntity: PollDao): PollDto {
        return PollDto(
            id = pollEntity.id.value.toString(),
            status = pollEntity.status,
            question = pollEntity.question,
            answers = pollEntity.answers.map { answerEntity ->
                answerEntity.text
            },
            participantRequired = pollEntity.participantsRequired,
            likeCount = pollEntity.likes.count(),
            dislikeCount = pollEntity.dislikes.count()
        )
    }
}