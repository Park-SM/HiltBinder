package com.smparkworld.hiltbinderexample.sample.basic.from

import com.smparkworld.hiltbinder.HiltBinds

@HiltBinds(from = FromSampleModelImpl::class)
interface FromSampleModel {

    fun printTestString()
}