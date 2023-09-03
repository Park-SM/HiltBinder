package com.smparkworld.hiltbinder_processor.core

interface Logger {

    fun log(message: String)

    fun warn(message: String)

    fun error(message: String)
}