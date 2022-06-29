package com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.classkey

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import dagger.multibindings.ClassKey
import javax.inject.Inject

@HiltMapBinds
@ClassKey(MapClassKeySampleModelImpl1::class)
class MapClassKeySampleModelImpl1 @Inject constructor(
    private val testString: String
) : MapClassKeySampleModel {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapClassKeySampleModelImpl1 class.")
    }
}