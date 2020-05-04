package com.public.poll.mapper

import com.public.poll.dao.PollAnswerDao
import com.public.poll.dao.PollDao
import com.public.poll.dto.AnswerDto
import com.public.poll.dto.PollDto

interface PollMapper {
    fun map(pollEntity: PollDao): PollDto
    fun map(answerEntity: PollAnswerDao): AnswerDto
}