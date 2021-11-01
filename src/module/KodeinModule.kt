package com.public.poll.module

import com.public.poll.cache.Cache
import com.public.poll.cache.CacheImpl
import com.public.poll.client.HttpClientProvider
import com.public.poll.client.HttpClientProviderImpl
import com.public.poll.files.FileProvider
import com.public.poll.files.FileProviderImpl
import com.public.poll.handler.auth.SignInHandler
import com.public.poll.handler.auth.SignUpHandler
import com.public.poll.handler.poll.action.PollApproveHandler
import com.public.poll.handler.poll.action.PollEngageHandler
import com.public.poll.handler.poll.action.PollReportHandler
import com.public.poll.handler.poll.action.PollVoteHandler
import com.public.poll.handler.poll.crud.PollCreateHandler
import com.public.poll.handler.poll.crud.PollEditHandler
import com.public.poll.handler.poll.crud.PollGetHandler
import com.public.poll.handler.poll.crud.PollSearchHandler
import com.public.poll.handler.poll.list.PollFeedHandler
import com.public.poll.handler.poll.list.PollHistoryHandler
import com.public.poll.handler.poll.list.PollMyListHandler
import com.public.poll.mapper.PollMapper
import com.public.poll.mapper.PollMapperImpl
import com.public.poll.mapper.UserMapper
import com.public.poll.mapper.UserMapperImpl
import com.public.poll.repositories.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun kodeinModule() = Kodein {
    bind<HttpClientProvider>() with singleton { HttpClientProviderImpl() }
    bind<Cache>() with singleton { CacheImpl() }
    bind<FileProvider>() with singleton { FileProviderImpl() }

    bind<UserMapper>() with singleton { UserMapperImpl() }
    bind<PollMapper>() with singleton { PollMapperImpl() }

    bind<UserRepository>() with singleton { UserRepositoryImpl(instance()) }
    bind<PollRepository>() with singleton { PollRepositoryImpl(instance(), instance(), instance(), instance(), instance()) }
    bind<PollCollectionRepository>() with singleton { PollCollectionRepositoryImpl(instance()) }
    bind<PollSearchRepository>() with singleton { PollSearchRepositoryImpl(instance(), instance()) }
    bind<PollBrokerRepository>() with singleton { PollBrokerRepositoryImpl() }

    bind<SignInHandler>() with provider { SignInHandler(instance()) }
    bind<SignUpHandler>() with provider { SignUpHandler(instance()) }
    bind<PollEngageHandler>() with provider { PollEngageHandler(instance()) }
    bind<PollReportHandler>() with provider { PollReportHandler(instance()) }
    bind<PollVoteHandler>() with provider { PollVoteHandler(instance()) }
    bind<PollCreateHandler>() with provider { PollCreateHandler(instance()) }
    bind<PollEditHandler>() with provider { PollEditHandler(instance()) }
    bind<PollGetHandler>() with provider { PollGetHandler(instance()) }
    bind<PollFeedHandler>() with provider { PollFeedHandler(instance()) }
    bind<PollHistoryHandler>() with provider { PollHistoryHandler(instance()) }
    bind<PollMyListHandler>() with provider { PollMyListHandler(instance()) }
    bind<PollApproveHandler>() with provider { PollApproveHandler(instance()) }
    bind<PollSearchHandler>() with provider { PollSearchHandler(instance()) }
}