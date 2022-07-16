package com.smparkworld.hiltbinderexample.sample.supported.generic.intoset

import android.util.Log
import com.smparkworld.hiltbinder.HiltSetBinds
import javax.inject.Inject

@HiltSetBinds
class SetGenericSampleModelImpl2 @Inject constructor(
    private val testString: String
) : SetGenericSampleModel<Int> {

    override fun printTestString(data: Int) {
        Log.d("Test!!", "TestString is `$testString` in SetGenericSampleModelImpl2 class. :: Generic type is <Int>")
    }
}