package com.m2f.cronAnalyzer.module.usecase

import com.m2f.cronAnalyzer.module.usecase.failure.InvalidFormat
import com.m2f.cronAnalyzer.module.utils.Success
import com.m2f.cronAnalyzer.module.utils.isFailure
import com.m2f.cronAnalyzer.module.utils.isSucess
import kotlin.contracts.ExperimentalContracts
import kotlin.test.Test


@ExperimentalContracts
class ParseCommandUseCaseTest {

    @org.junit.Test
    fun `parser will emit a InvalidFormat for non bin commands`() {
        val command = "45 * - /run_me_daily"

        val sut = parserComandUseCase()

        val result = sut.parseCommand(command)

        assert(result.isFailure() && result.value == InvalidFormat)
    }


    @Test
    fun `parser will emit a Command if it has the correct format`() {
        val command = "/bin/run_random"
        val commandString = "45 * - $command"

        val sut = parserComandUseCase()

        val result = sut.parseCommand(commandString)

        assert(result.isSucess() && (result as Success).value.command == command)
    }
}