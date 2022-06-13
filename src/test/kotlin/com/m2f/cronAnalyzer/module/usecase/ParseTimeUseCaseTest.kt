package com.m2f.cronAnalyzer.module.usecase

import com.m2f.cronAnalyzer.module.model.Time
import com.m2f.cronAnalyzer.module.usecase.failure.InvalidArgument
import com.m2f.cronAnalyzer.module.usecase.failure.InvalidFormat
import com.m2f.cronAnalyzer.module.utils.Success
import com.m2f.cronAnalyzer.module.utils.isFailure
import com.m2f.cronAnalyzer.module.utils.isSucess
import org.junit.Test
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class ParseTimeUseCaseTest {

    @Test
    fun `parser will emit a InvalidFormat for invalid time formats`() {
        val exampleBadTimeFormats = listOf(
            "+ * - /run_me_daily",
            "- 24 - /run_me_daily",
            "+ +  - /run_me_daily",
            " - /run_me_daily",
            "/run_me_daily",
            " 0 * - /run_me_daily",
            "244 * - /run_me_daily"
            )

        val sut = parseTimeUseCase()

        val result = exampleBadTimeFormats.all {
            val r = sut.parseTime(it)
            r.isFailure() && r.value == InvalidFormat
        }

        assert(result)
    }

    @Test
    fun `parser will emit the expected time for valid time formats`() {
        val exampleGoodTimeFormats = listOf(
            "0 * - /bin/run_me_daily" to Time(minutes = Time.ConcreteTime(0), hours = Time.AnyTime),
            "* * - /bin/run_me_daily" to Time(minutes = Time.AnyTime, hours = Time.AnyTime),
            "23 0  - /bin/run_me_daily" to Time(minutes = Time.ConcreteTime(23), hours = Time.ConcreteTime(0)),
            "59 23 - /bin/run_me_daily" to Time(minutes = Time.ConcreteTime(59), hours = Time.ConcreteTime(23)),
            "1 1 - /bin/run_me_daily" to Time(minutes = Time.ConcreteTime(1), hours = Time.ConcreteTime(1)),
            "0 0 - /bin/run_me_daily" to Time(minutes = Time.ConcreteTime(0), hours = Time.ConcreteTime(0)),
            "* 0 - /bin/run_me_daily" to Time(minutes = Time.AnyTime, hours = Time.ConcreteTime(0)),
        )

        val sut = parseTimeUseCase()

        val result = exampleGoodTimeFormats.all {
            val r = sut.parseTime(it.first)
            r.isSucess() && (r as Success).value == it.second
        }

        assert(result)
    }

    @Test
    fun `parser will emit a InvalidArgument if it cant find a valid time argument`() {
        val exampleBadTimeArguments = listOf(
            "60 * - /run_me_daily",
            "* 24 - /run_me_daily",
            "60 24 - /run_me_daily"

        )

        val sut = parseTimeUseCase()

        val result = exampleBadTimeArguments.all {
            val r = sut.parseTime(it)
            r.isFailure() && r.value== InvalidArgument
        }

        assert(result)
    }
}