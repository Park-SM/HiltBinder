package com.smparkworld.hiltbinderexample.sample.supported.generic.nested

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

@HiltBinds
class NestedGenericSampleModelImpl @Inject constructor(
    private val testString: String
) : NestedGenericSampleModel<SampleParam<SampleParam<String>>> {

    override fun printTest(test: SampleParam<SampleParam<String>>) {
        Log.d("Test!!", "test is $test")
    }
}