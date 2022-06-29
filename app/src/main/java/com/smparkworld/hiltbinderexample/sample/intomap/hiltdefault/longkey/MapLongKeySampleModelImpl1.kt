package com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.longkey

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import dagger.multibindings.LongKey
import javax.inject.Inject

@HiltMapBinds
@LongKey(1L)
class MapLongKeySampleModelImpl1 @Inject constructor(
    private val testString: String
) : MapLongKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapLongKeySampleModelImpl1 class.")
    }
}