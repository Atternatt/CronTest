
package com.m2f.cronAnalyzer.module.usecase

import com.m2f.cronAnalyzer.module.model.CronJob
import com.m2f.cronAnalyzer.module.usecase.failure.ParserFailure
import com.m2f.cronAnalyzer.module.utils.Result
import com.m2f.cronAnalyzer.module.utils.Failure
import com.m2f.cronAnalyzer.module.utils.Success
import com.m2f.cronAnalyzer.module.utils.isFailure
import kotlin.contracts.ExperimentalContracts

fun interface ParseCronJobUseCase {
    /**
     * Search and returns the [CronJob] inside the string
     */
    fun parseCronJob(input: String): Result<ParserFailure, CronJob>
}


context(ParseTimeUseCase, ParseCommandUseCase)
@OptIn(ExperimentalContracts::class)
fun parseCronJobUseCase() = ParseCronJobUseCase { input ->
    val time = parseTime(input)
    val command = parseCommand(input)

    if (time.isFailure()) time
    else if (command.isFailure()) command as Failure
    else {
        Success(
            CronJob(
                time = (time as Success).value,
                command = (command as Success).value
            )
        )
    }
}