package com.smparkworld.hiltbinderexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smparkworld.hiltbinderexample.data.mapper.TestMapper
import com.smparkworld.hiltbinderexample.data.repository.TestRepository
import com.smparkworld.hiltbinderexample.di.TestUseCaseQualifier2
import com.smparkworld.hiltbinderexample.domain.usecase.TestUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @TestUseCaseQualifier2
    lateinit var testUseCase: TestUseCase

    @Inject
    lateinit var testRepository: TestRepository

    @Inject
    lateinit var testMapper: TestMapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testUseCase.printTestString()
        testRepository.printTestString()
        testMapper.printTestString()
    }
}