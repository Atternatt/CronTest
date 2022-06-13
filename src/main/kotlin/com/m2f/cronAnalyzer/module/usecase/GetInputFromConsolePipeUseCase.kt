package com.m2f.cronAnalyzer.module.usecase

fun interface GetInputFromConsolePipeUseCase {
    /**
     * Read all the lines of a file passed as a stream though the program
     * and will send each one thought a lambda.
     */
    fun withInput(onEachInput: (String) -> Unit)
}

fun getInputFromConsolePipeUseCase() = GetInputFromConsolePipeUseCase {
    while(true) {
        val line = readlnOrNull()
        if(line != null) {
            it(line)
        } else break
    }
}