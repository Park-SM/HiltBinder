package com.smparkworld.hiltbinderexample.sample.supported.generic.intoset.stars

import android.util.Log
import com.smparkworld.hiltbinder.HiltSetBinds
import javax.inject.Inject

@HiltSetBinds(combined = true)
class SetStarGenericSampleModelImpl1 @Inject constructor(
    private val testString: String
) : SetStarGenericSampleModel<String> {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in SetStarGenericSampleModelImpl1 class. :: Generic type is <String>")
    }
}