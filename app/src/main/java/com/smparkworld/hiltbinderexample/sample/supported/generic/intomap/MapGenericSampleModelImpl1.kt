package com.smparkworld.hiltbinderexample.sample.supported.generic.intomap

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import dagger.multibindings.StringKey
import javax.inject.Inject

@HiltMapBinds
@StringKey("impl1")
class MapGenericSampleModelImpl1 @Inject constructor(
    private val testString: String
) : MapGenericSampleModel<Int> {

    override fun printTestString(data: Int) {
        Log.d("Test!!", "TestString is `$testString` in MapGenericSampleModelImpl1 class. :: Generic type is <Int>")
    }
}