package com.smparkworld.hiltbinderexample.sample.supported.generic.single.stars

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

@HiltBinds(combined = true)
class StarSingleGenericSampleModelImpl @Inject constructor(
    private val testString: String
) : StarSingleGenericSampleModel<Int> {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in StarSingleGenericSampleModelImpl class. :: Generic type is <Int>")
    }
}