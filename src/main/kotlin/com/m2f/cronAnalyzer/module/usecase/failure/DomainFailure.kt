package com.m2f.cronAnalyzer.module.usecase.failure

sealed interface DomainFailure

object InvalidTimeInput:DomainFailure

sealed interface ParserFailure: DomainFailure

object InvalidFormat: ParserFailure
object InvalidArgument: ParserFailure

