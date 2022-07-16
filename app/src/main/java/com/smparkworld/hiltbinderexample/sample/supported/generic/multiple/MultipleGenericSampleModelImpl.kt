package com.smparkworld.hiltbinderexample.sample.supported.generic.multiple

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

@HiltBinds
class MultipleGenericSampleModelImpl @Inject constructor(
    private val testString: String
) : MultipleGenericSampleModel<Int, Any> {

    override fun printTestString(data1: Int, data2: Any) {
        Log.d("Test!!", "TestString is `$testString` in GenericSampleModelImpl1 class. :: Generic type is <Int, Any>")
    }
}