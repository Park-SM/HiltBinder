package com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.intkey

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import dagger.multibindings.IntKey
import javax.inject.Inject

@HiltMapBinds
@IntKey(1)
class MapIntKeySampleModelImpl1 @Inject constructor(
    private val testString: String
) : MapIntKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapIntKeySampleModelImpl1 class.")
    }
}