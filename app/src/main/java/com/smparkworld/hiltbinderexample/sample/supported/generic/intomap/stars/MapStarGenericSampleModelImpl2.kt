package com.smparkworld.hiltbinderexample.sample.supported.generic.intomap.stars

import android.util.Log
import com.smparkworld.hiltbinder.HiltMapBinds
import dagger.multibindings.StringKey
import javax.inject.Inject

@HiltMapBinds(combined = true)
@StringKey("impl2")
class MapStarGenericSampleModelImpl2 @Inject constructor(
    private val testString: String
) : MapStarGenericSampleModel<String> {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapStarGenericSampleModelImpl2 class. :: Generic type is <String>")
    }
}

/* You can use below code that with `to` option.

open class BaseModel

@HiltMapBinds(
    to = MapStarGenericSampleModel::class,
    combined = true
)
@StringKey("impl2")
class MapStarGenericSampleModelImpl2 @Inject constructor(
    private val testString: String
) : BaseModel(), MapStarGenericSampleModel<String> {

    override fun printTestString() {
        Log.d("Test!!", "TestString is `$testString` in MapStarGenericSampleModelImpl2 class. :: Generic type is <String>")
    }
}
*/