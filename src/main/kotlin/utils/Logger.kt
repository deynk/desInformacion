package com.example.utils

import org.slf4j.LoggerFactory

object Logger {
    private val logger = LoggerFactory.getLogger("Application")

    fun info(msg: String) = logger.info(msg)
    fun debug(msg: String) = logger.debug(msg)
    fun warn(msg: String) = logger.warn(msg)
    fun error(msg: String) = logger.error(msg)
    fun error(msg: String, t: Throwable) = logger.error(msg, t)
}