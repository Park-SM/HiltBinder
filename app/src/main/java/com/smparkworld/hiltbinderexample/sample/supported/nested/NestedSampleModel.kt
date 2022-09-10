package com.smparkworld.hiltbinderexample.sample.supported.nested

interface NestedSampleModel {

    interface SampleModel {

        interface SampleModelInternal {
            fun printTestString()
        }
    }
}