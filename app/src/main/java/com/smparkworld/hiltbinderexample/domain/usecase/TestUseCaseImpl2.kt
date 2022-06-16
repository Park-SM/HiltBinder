package com.smparkworld.hiltbinderexample.domain.usecase

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinderexample.di.TestUseCaseQualifier2
import javax.inject.Inject

@HiltBinds(qualifier = TestUseCaseQualifier2::class)
class TestUseCaseImpl2 @Inject constructor(
    private val testString: String
) : TestUseCase {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in UseCase2.")
    }
}