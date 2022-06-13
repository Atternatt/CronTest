package com.m2f.cronAnalyzer.module.usecase

import com.m2f.cronAnalyzer.module.di.withDependencies
import com.m2f.cronAnalyzer.module.utils.Success
import com.m2f.cronAnalyzer.module.utils.isSucess
import org.junit.Test
import kotlin.contracts.ExperimentalContracts
import kotlin.test.assertEquals

class CalculateNextTimeUseCaseTest {

    @OptIn(ExperimentalContracts::class)
    @Test
    fun `Test expected ouputs for data set`() {
        val currentTime = "16:10"
        val commands = listOf(
            "30 1 /bin/run_me_daily" to "1:30 tomorrow - /bin/run_me_daily",
            "45 * /bin/run_me_hourly" to "16:45 today - /bin/run_me_hourly",
            "* * /bin/run_me_every_minute" to "16:10 today - /bin/run_me_every_minute",
            "* 19 /bin/run_me_sixty_times" to "19:00 today - /bin/run_me_sixty_times",
            "30 17 /bin/run_me_daily" to "17:30 today - /bin/run_me_daily",
            "9 * /bin/run_me_hourly" to "17:09 today - /bin/run_me_hourly",
            "* 15 /bin/run_me_sixty_times" to "15:00 tomorrow - /bin/run_me_sixty_times",
        )

        withDependencies {
            commands.forEach { (command, output) ->
                val cronJob = parseCronJob(command)
                assert(cronJob.isSucess())
                val result = calculate((cronJob as Success).value, currentTime)
                assert(result.isSucess())
                assertEquals((result as Success).value, output)
            }
        }
    }

    @OptIn(ExperimentalContracts::class)
    @Test
    fun `Test expected outputs for an edge case`() {
        val currentTime = "23:10"
        val commands = listOf(
            "9 * /bin/run_me_hourly" to "0:09 tomorrow - /bin/run_me_hourly"
        )

        withDependencies {
            commands.forEach { (command, output) ->
                val cronJob = parseCronJob(command)
                assert(cronJob.isSucess())
                val result = calculate((cronJob as Success).value, currentTime)
                assert(result.isSucess())
                assertEquals((result as Success).value, output)
            }
        }
    }
}