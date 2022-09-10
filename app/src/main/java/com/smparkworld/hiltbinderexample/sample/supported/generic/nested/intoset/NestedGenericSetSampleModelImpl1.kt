package com.smparkworld.hiltbinderexample.sample.supported.generic.nested.intoset

import android.util.Log
import com.smparkworld.hiltbinder.HiltSetBinds
import com.smparkworld.hiltbinderexample.sample.supported.generic.nested.NestedGenericSampleModel
import com.smparkworld.hiltbinderexample.sample.supported.generic.nested.SampleParam
import javax.inject.Inject

@HiltSetBinds
class NestedGenericSetSampleModelImpl1 @Inject constructor(
    private val testString: String
) : NestedGenericSampleModel<SampleParam<SampleParam<String>>> {

    override fun printTest(test: SampleParam<SampleParam<String>>) {
        Log.d("Test!!", "TestString is `$testString` in NestedGenericSetSampleModelImpl1 class. :: $test")
    }
}