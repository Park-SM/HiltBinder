package com.smparkworld.hiltbinderexample.sample.supported.generic.single

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

@HiltBinds
class SingleGenericSampleModelImpl3 @Inject constructor(
    private val testString: String
) : SingleGenericSampleModel<Any> {

    override fun printTestString(data: Any) {
        Log.d("Test!!", "TestString is `$testString` in GenericSampleModelImpl3 class. :: Generic type is <Any>")
    }
}