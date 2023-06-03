package com.smparkworld.hiltbinderexample.sample.basic.to.generic

interface ToGenericSampleModel<T> {

    fun printTestString(model: T)
}