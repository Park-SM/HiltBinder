package com.smparkworld.hiltbinderexample.sample.supported.generic.intoset.stars

import android.util.Log
import com.smparkworld.hiltbinder.HiltSetBinds
import javax.inject.Inject

@HiltSetBinds(combined = true)
class SetStarGenericSampleModelImpl2 @Inject constructor(
    private val testString: String
) : SetStarGenericSampleModel<Int> {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in SetStarGenericSampleModelImpl2 class. :: Generic type is <Int>")
    }
}

/* You can use below code that with `to` option.

open class BaseModel

@HiltSetBinds(
    to = SetStarGenericSampleModel::class,
    combined = true
)
class SetStarGenericSampleModelImpl2 @Inject constructor(
    private val testString: String
) : BaseModel(), SetStarGenericSampleModel<String> {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in SetStarGenericSampleModelImpl2 class. :: Generic type is <String>")
    }
}
*/