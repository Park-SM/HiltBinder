package com.smparkworld.hiltbinderexample.sample.intoset

import android.util.Log
import com.smparkworld.hiltbinder.HiltSetBinds
import javax.inject.Inject

@HiltSetBinds
class SetSampleModelImpl3 @Inject constructor(
    private val testString: String
) : SetSampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in SetSampleModelImpl3 class.")
    }
}