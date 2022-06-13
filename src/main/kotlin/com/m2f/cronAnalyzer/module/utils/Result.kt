package com.m2f.cronAnalyzer.module.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class Result<out L, out R>

class Failure<L>(val value: L) : Result<L, Nothing>()
class Success<R>(val value: R) : Result<Nothing, R>()

@ExperimentalContracts
inline fun <reified L, R> Result<L, R>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is Failure<L>)
        returns(false) implies (this@isFailure is Success<R>)
    }
    return this is Failure
}

@ExperimentalContracts
inline fun <reified L, reified R> Result<L, R>.isSucess(): Boolean {
    contract {
        returns(true) implies (this@isSucess is Success<R>)
        returns(false) implies (this@isSucess is Failure<L>)
    }
    return this is Success
}