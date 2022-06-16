package com.smparkworld.hiltbinderexample.domain.usecase

import android.util.Log
import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinderexample.di.TestUseCaseQualifier1
import javax.inject.Inject

@HiltBinds(qualifier = TestUseCaseQualifier1::class)
class TestUseCaseImpl1 @Inject constructor(
    private val testString: String
) : TestUseCase {

    override fun printTestString() {
        Log.d("Test!!", "TestString is $testString in UseCase1.")
    }
}