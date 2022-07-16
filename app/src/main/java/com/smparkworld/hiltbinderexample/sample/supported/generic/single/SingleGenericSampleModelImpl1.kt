package com.smparkworld.hiltbinderexample.sample.supported.generic.single

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

@HiltBinds
class SingleGenericSampleModelImpl1 @Inject constructor(
    private val testString: String
) : SingleGenericSampleModel<Int> {

    override fun printTestString(data: Int) {
        Log.d("Test!!", "TestString is `$testString` in GenericSampleModelImpl1 class. :: Generic type is <Int>")
    }
}