package com.smparkworld.hiltbinderexample.sample.supported.generic.intomap

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import dagger.multibindings.StringKey
import javax.inject.Inject

@HiltMapBinds
@StringKey("impl3")
class MapGenericSampleModelImpl3 @Inject constructor(
    private val testString: String
) : MapGenericSampleModel<String> {

    override fun printTestString(data: String) {
        Log.d("Test!!", "TestString is `$testString` in MapGenericSampleModelImpl3 class. :: Generic type is <String>")
    }
}