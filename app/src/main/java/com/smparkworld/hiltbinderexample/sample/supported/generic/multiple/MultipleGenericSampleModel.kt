package com.smparkworld.hiltbinderexample.sample.supported.generic.multiple

interface MultipleGenericSampleModel<T1, T2> {

    fun printTestString(data1: T1, data2: T2)
}