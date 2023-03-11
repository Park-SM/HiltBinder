package com.smparkworld.hiltbinderexample.sample.supported.generic.intomap.stars

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import dagger.multibindings.StringKey
import javax.inject.Inject

@HiltMapBinds(combined = true)
@StringKey("impl1")
class MapStarGenericSampleModelImpl1 @Inject constructor(
    private val testString: String
) : MapStarGenericSampleModel<Int> {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapStarGenericSampleModelImpl1 class. :: Generic type is <Int>")
    }
}