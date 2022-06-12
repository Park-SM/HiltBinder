package com.smparkworld.hiltbinderexample.data.repository

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import javax.inject.Inject

@HiltBinds(to = TestRepository::class)
class TestRepositoryImpl @Inject constructor(
    private val testString: String
) : TestRepository {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in Repository.")
    }
}