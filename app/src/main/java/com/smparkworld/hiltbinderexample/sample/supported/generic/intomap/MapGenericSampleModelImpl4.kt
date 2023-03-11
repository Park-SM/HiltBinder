package com.smparkworld.hiltbinderexample.sample.supported.generic.intomap

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import dagger.multibindings.StringKey
import javax.inject.Inject

@HiltMapBinds
@StringKey("impl4")
class MapGenericSampleModelImpl4 @Inject constructor(
    private val testString: String
) : MapGenericSampleModel<String> {

    override fun printTestString(data: String) {
        Log.d("Test!!", "TestString is `$testString` in MapGenericSampleModelImpl4 class. :: Generic type is <String>")
    }
}