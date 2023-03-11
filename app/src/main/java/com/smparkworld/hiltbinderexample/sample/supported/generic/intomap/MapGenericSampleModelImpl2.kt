package com.smparkworld.hiltbinderexample.sample.supported.generic.intomap

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinder.HiltSetBinds
import dagger.multibindings.StringKey
import javax.inject.Inject

@HiltMapBinds
@StringKey("impl2")
class MapGenericSampleModelImpl2 @Inject constructor(
    private val testString: String
) : MapGenericSampleModel<Int> {

    override fun printTestString(data: Int) {
        Log.d("Test!!", "TestString is `$testString` in MapGenericSampleModelImpl2 class. :: Generic type is <Int>")
    }
}