package com.smparkworld.hiltbinderexample.sample.supported.generic.nested.intomap

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinderexample.sample.supported.generic.nested.NestedGenericSampleModel
import com.smparkworld.hiltbinderexample.sample.supported.generic.nested.SampleParam
import dagger.multibindings.StringKey
import javax.inject.Inject

@HiltMapBinds
@StringKey("NestedGenericMapSampleModel1")
class NestedGenericMapSampleModelImpl1 @Inject constructor(
    private val testString: String
) : NestedGenericSampleModel<SampleParam<SampleParam<String>>> {

    override fun printTest(test: SampleParam<SampleParam<String>>) {
        Log.d("Test!!", "TestString is `$testString` in NestedGenericMapSampleModelImpl1 class. :: $test")
    }
}