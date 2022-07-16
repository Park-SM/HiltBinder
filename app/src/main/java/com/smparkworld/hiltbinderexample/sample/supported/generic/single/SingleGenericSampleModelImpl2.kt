package com.smparkworld.hiltbinderexample.sample.supported.generic.single

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

@HiltBinds
class SingleGenericSampleModelImpl2 @Inject constructor(
    private val testString: String
) : SingleGenericSampleModel<String> {

    override fun printTestString(model: String) {
        Log.d("Test!!", "TestString is `$testString` in GenericSampleModelImpl2 class. :: Generic type is <String>")
    }
}