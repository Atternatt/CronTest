package com.m2f.cronAnalyzer.module.usecase

import com.m2f.cronAnalyzer.module.model.*
import com.m2f.cronAnalyzer.module.usecase.failure.InvalidFormat
import com.m2f.cronAnalyzer.module.usecase.failure.ParserFailure
import com.m2f.cronAnalyzer.module.utils.Result
import com.m2f.cronAnalyzer.module.utils.Failure
import com.m2f.cronAnalyzer.module.utils.Success
import kotlin.contracts.ExperimentalContracts

fun interface ParseCommandUseCase {
    /**
     * Search and returns the [Command] inside the string. Is tearches for patterns that contains
     * '/bin/' as heading.
     */
    fun parseCommand(string: String): Result<ParserFailure, Command>
}

@ExperimentalContracts
fun parserComandUseCase() = ParseCommandUseCase { stringCommand ->
    val result = Regex("/bin/([a-z].+)")
    if (result.containsMatchIn(stringCommand)) Success(Command(result.findAll(stringCommand).first().value))
    else Failure(InvalidFormat)
}
