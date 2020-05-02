package com.public.poll

import io.ktor.application.*
import io.ktor.server.tomcat.EngineMain

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) = Unit