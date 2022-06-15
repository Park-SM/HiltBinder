package com.smparkworld.hiltbinder_processor.core.manager

import com.smparkworld.hiltbinder_processor.extension.log
import java.lang.StringBuilder
import javax.annotation.processing.ProcessingEnvironment

internal object PerformanceManager {

    private var startedAt: Long? = null
    private var completedAt: Long? = null

    fun startProcessing() {
        startedAt = System.currentTimeMillis()
        completedAt = null
    }

    fun stopProcessing() {
        completedAt = System.currentTimeMillis()
    }

    fun reportPerformance(env: ProcessingEnvironment, count: Int) {

        val startedAtInternal = startedAt
        val completedAtInternal = completedAt

        if (startedAtInternal != null && completedAtInternal != null) {
            val message = getReportMessage(completedAtInternal - startedAtInternal)
            if (message.isBlank().not()) {
                env.log("HiltBinds: processed $count classes, total processing time: $message")
            }
        }
    }

    private fun getReportMessage(elapsedMillis: Long): String {

        var remainder = elapsedMillis

        val hours = (remainder / HOUR).toInt()
        remainder %= HOUR

        val minutes = (remainder / MINUTE).toInt()
        remainder %= MINUTE

        val seconds = (remainder / SECOND).toInt()
        remainder %= SECOND

        val millis = (remainder % SECOND).toInt()

        return StringBuilder().run {
            if (hours > 0) append("${hours}h ")
            if (minutes > 0) append("${minutes}m ")
            if (seconds > 0) append("${seconds}s ")
            if (millis > 0) append("${millis}ms")
            toString()
        }
    }

    private const val HOUR = 1000 * 60 * 60
    private const val MINUTE = 1000 * 60
    private const val SECOND = 1000
}