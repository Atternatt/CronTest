package com.m2f.cronAnalyzer.module.model


/**
 * 'hour:minute' representation for a [CronJob]
 */
data class Time(val minutes: TimeFrame, val hours: TimeFrame) {

    //TimeFrame definition is inside Times definition because they have an aggregation relationship

    /**
     * ADT (Algebraic data type) representation for the time frame
     */
    sealed interface TimeFrame

    object AnyTime: TimeFrame

    data class ConcreteTime(val time: Int): TimeFrame {
        fun formatted(): String = String.format("%02d", time)
    }

}

