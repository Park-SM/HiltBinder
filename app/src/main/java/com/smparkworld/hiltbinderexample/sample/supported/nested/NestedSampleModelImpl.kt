package com.smparkworld.hiltbinderexample.sample.supported.nested

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

@HiltBinds
class NestedSampleModelImpl @Inject constructor(
    private val testString: String
) : NestedSampleModel.SampleModel.SampleModelInternal {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in NestedSampleModelImpl class.")
    }
}