package com.smparkworld.hiltbinderexample.sample.basic.from

import android.util.Log
import javax.inject.Inject

class FromSampleModelImpl @Inject constructor(
    private val testString: String
) : FromSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in FromSampleModelImpl class.")
    }
}