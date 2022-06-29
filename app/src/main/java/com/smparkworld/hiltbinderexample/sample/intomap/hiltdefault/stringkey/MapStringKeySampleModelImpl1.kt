package com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.stringkey

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import dagger.multibindings.StringKey
import javax.inject.Inject

@HiltMapBinds
@StringKey("model1")
class MapStringKeySampleModelImpl1 @Inject constructor(
    private val testString: String
) : MapStringKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapStringKeySampleModelImpl1 class.")
    }
}