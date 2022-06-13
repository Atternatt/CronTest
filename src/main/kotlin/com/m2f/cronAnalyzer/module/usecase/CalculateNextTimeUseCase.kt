package com.m2f.cronAnalyzer.module.usecase

import com.m2f.cronAnalyzer.module.model.CronJob
import com.m2f.cronAnalyzer.module.model.Time
import com.m2f.cronAnalyzer.module.usecase.failure.InvalidTimeInput
import com.m2f.cronAnalyzer.module.utils.Result
import com.m2f.cronAnalyzer.module.utils.Failure
import com.m2f.cronAnalyzer.module.utils.Success

fun interface CalculateNextTimeUseCase {
    /**
     * Calculates the output for a [CronJob]
     */
    fun calculate(input: CronJob, currentTime: String): Result<InvalidTimeInput, String>
}

fun calculateNextTime() = CalculateNextTimeUseCase { input, currentTime ->
    input.calculateNextTime(currentTime)
}

private fun CronJob.calculateNextTime(currentTime: String): Result<InvalidTimeInput, String> {
    val splittedTime = currentTime.split(":")
    val hasCorrectFormat = splittedTime.size == 2
    val hasCorrectTimeValues = try {
        val (h, m) = splittedTime
        h.toInt() in 0..23 &&
                m.toInt() in 0..59
    } catch (nfe: NumberFormatException) {
        false
    }
    if (!hasCorrectFormat || !hasCorrectTimeValues) return Failure(InvalidTimeInput)
    val (currentH, currentM) = splittedTime.map { it.toInt() }
    val currentTime = Time(hours = Time.ConcreteTime(currentH), minutes = Time.ConcreteTime(currentM))

    val rules = listOf(
        AnyHourAnyMinuteRule,
        AnyHourConcreteMinuteRule,
        ConcreteHourConcreteMinuteRule,
        ConcreteHourAnyMinuteRule
    )

    val result = rules
        .filter { it.isMatch(time) }
        .map { it.formattedTime(time, currentTime) }
        .first()



    return Success("$result - ${command.command}")
}

//region calculation rules
/**
 * Abstract representation for the calculation of a specific time comparison across
 * all possible time patterns
 */
sealed interface DayCalculationRule {
    fun isMatch(time: Time): Boolean
    fun formattedTime(time: Time, currentTime: Time): String
}

object AnyHourAnyMinuteRule : DayCalculationRule {
    override fun isMatch(time: Time): Boolean =
        time.hours is Time.AnyTime && time.minutes is Time.AnyTime

    override fun formattedTime(time: Time, currentTime: Time): String {
        require(currentTime.minutes is Time.ConcreteTime)
        require(currentTime.hours is Time.ConcreteTime)
        return "${currentTime.hours.time}:${currentTime.minutes.formatted()} ${Today.value}"
    }
}

object AnyHourConcreteMinuteRule : DayCalculationRule {
    override fun isMatch(time: Time): Boolean =
        time.hours is Time.AnyTime && time.minutes is Time.ConcreteTime

    override fun formattedTime(time: Time, currentTime: Time): String {
        require(time.minutes is Time.ConcreteTime)
        require(currentTime.minutes is Time.ConcreteTime)
        require(currentTime.hours is Time.ConcreteTime)

        return if (time.minutes.time < currentTime.minutes.time) {
            val h = (currentTime.hours.time + 1).mod(24)
            val day = if (currentTime.hours.time == 23) Tomorrow else Today
            "$h:${time.minutes.formatted()} ${day.value}"
        } else {
            "${currentTime.hours.time}:${time.minutes.formatted()} ${Today.value}"
        }
    }
}

object ConcreteHourConcreteMinuteRule : DayCalculationRule {
    override fun isMatch(time: Time): Boolean =
        time.hours is Time.ConcreteTime && time.minutes is Time.ConcreteTime

    override fun formattedTime(time: Time, currentTime: Time): String {
        require(time.minutes is Time.ConcreteTime)
        require(time.hours is Time.ConcreteTime)
        require(currentTime.minutes is Time.ConcreteTime)
        require(currentTime.hours is Time.ConcreteTime)
        val day =
            if ((time.hours.time * 60 + time.minutes.time) > (currentTime.hours.time * 60 + currentTime.minutes.time)) Today else Tomorrow
        return "${time.hours.time}:${time.minutes.formatted()} ${day.value}"
    }
}

object ConcreteHourAnyMinuteRule : DayCalculationRule {
    override fun isMatch(time: Time): Boolean =
        time.hours is Time.ConcreteTime && time.minutes is Time.AnyTime

    override fun formattedTime(time: Time, currentTime: Time): String {
        require(time.hours is Time.ConcreteTime)
        require(currentTime.minutes is Time.ConcreteTime)
        require(currentTime.hours is Time.ConcreteTime)
        return if (time.hours.time == currentTime.hours.time) {
            "${currentTime.hours.time}:${currentTime.minutes.formatted()} ${Today.value}"
        } else if (time.hours.time < currentTime.hours.time) {
            "${time.hours.time}:00 ${Tomorrow.value}"
        } else {
            "${time.hours.time}:00 ${Today.value}"
        }
    }
}
//endregion

private sealed class Day(val value: String)

private object Today : Day("today")
private object Tomorrow : Day("tomorrow")
