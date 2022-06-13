package com.m2f.cronAnalyzer.module.usecase

import com.m2f.cronAnalyzer.module.model.Time
import com.m2f.cronAnalyzer.module.usecase.failure.InvalidArgument
import com.m2f.cronAnalyzer.module.usecase.failure.InvalidFormat
import com.m2f.cronAnalyzer.module.usecase.failure.ParserFailure
import com.m2f.cronAnalyzer.module.utils.Result
import com.m2f.cronAnalyzer.module.utils.Failure
import com.m2f.cronAnalyzer.module.utils.Success

fun interface ParseTimeUseCase {
    /**
     * Searches and returns the [Time] representation inside a String
     */
    fun parseTime(input: String): Result<ParserFailure, Time>
}

@OptIn(ExperimentalStdlibApi::class)
fun parseTimeUseCase() = ParseTimeUseCase { input ->
    val regex = Regex("(\\d\\d?|\\*)( )((\\d\\d?|\\*))")

    val result = regex.matchAt(input, 0)?.value?.split(" ")

    if (result == null) {
        Failure(InvalidFormat)
    } else {
        val (minutes, hours) = result
        try {
            val hourFrame = hours.getTimeFrame(23)
            val minutesFrame = minutes.getTimeFrame(59)
            Success(Time(minutesFrame, hourFrame))
        } catch (nfe: NumberFormatException) {
            Failure(InvalidArgument)
        }
    }
}


private fun String.getTimeFrame(invalidThreshold: Int) =
    if (this == "*") {
        Time.AnyTime
    } else {
        if (toInt() > invalidThreshold) {
            throw NumberFormatException()
        } else {
            Time.ConcreteTime(toInt())
        }
    }