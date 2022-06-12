package com.smparkworld.hiltbinderexample.data.mapper

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

@HiltBinds(to = TestMapper::class)
class TestMapperImpl @Inject constructor(
    private val testString: String
) : TestMapper {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in Mapper")
    }
}