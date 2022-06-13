package com.m2f.cronAnalyzer.module.di

import com.m2f.cronAnalyzer.module.usecase.*
import kotlin.contracts.ExperimentalContracts

/**
 * Prepares the dependency tree passing all the elements as implicit receives
 */
@OptIn(ExperimentalContracts::class)
fun withDependencies(onDependenciesReady: context(GetInputFromConsolePipeUseCase, CalculateNextTimeUseCase, ParseCronJobUseCase) () -> Unit) {
    with(parseTimeUseCase()) {
        with(parserComandUseCase()) {
            onDependenciesReady(
                getInputFromConsolePipeUseCase(),
                calculateNextTime(),
                parseCronJobUseCase())
        }
    }
}