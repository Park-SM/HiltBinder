package com.smparkworld.hiltbinderexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.smparkworld.hiltbinderexample.sample.basic.component.ComponentSampleModel
import com.smparkworld.hiltbinderexample.sample.basic.from.FromSampleModel
import com.smparkworld.hiltbinderexample.sample.basic.named.NamedSampleModel
import com.smparkworld.hiltbinderexample.sample.basic.qualifier.QualifierSampleModel
import com.smparkworld.hiltbinderexample.sample.basic.qualifier.SampleQualifier1
import com.smparkworld.hiltbinderexample.sample.basic.qualifier.SampleQualifier2
import com.smparkworld.hiltbinderexample.sample.basic.to.ToSampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.SampleType
import com.smparkworld.hiltbinderexample.sample.intomap.complexkey.MapComplexKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.complexkey.SampleMapComplexKey
import com.smparkworld.hiltbinderexample.sample.intomap.custom.MapCustomKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.classkey.MapClassKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.intkey.MapIntKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.longkey.MapLongKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.hiltdefault.stringkey.MapStringKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intoset.SetSampleModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var toSampleModel: ToSampleModel

    @Inject
    lateinit var fromSampleModel: FromSampleModel

    @Inject
    lateinit var componentSampleModel: ComponentSampleModel

    @Inject
    @SampleQualifier1
    lateinit var qualifierSampleModel1: QualifierSampleModel

    @Inject
    @SampleQualifier2
    lateinit var qualifierSampleModel2: QualifierSampleModel

    @Inject
    @Named("model1")
    lateinit var namedSampleModel1: NamedSampleModel

    @Inject
    @Named("model2")
    lateinit var namedSampleModel2: NamedSampleModel

    @Inject
    lateinit var sampleSet: @JvmSuppressWildcards Set<SetSampleModel>

    @Inject
    lateinit var intKeySampleMap: @JvmSuppressWildcards Map<Int, Provider<MapIntKeySampleModel>>

    @Inject
    lateinit var longKeySampleMap: @JvmSuppressWildcards Map<Long, Provider<MapLongKeySampleModel>>

    @Inject
    lateinit var stringKeySampleMap: @JvmSuppressWildcards Map<String, Provider<MapStringKeySampleModel>>

    @Inject
    lateinit var classKeySampleMap: @JvmSuppressWildcards Map<Class<*>, Provider<MapClassKeySampleModel>>

    @Inject
    lateinit var customKeySampleMap: @JvmSuppressWildcards Map<SampleType, Provider<MapCustomKeySampleModel>>

    @Inject
    lateinit var complexKeySampleMap: @JvmSuppressWildcards Map<SampleMapComplexKey, Provider<MapComplexKeySampleModel>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toSampleModel.printTestString()
        fromSampleModel.printTestString()
        componentSampleModel.printTestString()
        qualifierSampleModel1.printTestString()
        qualifierSampleModel2.printTestString()
        namedSampleModel1.printTestString()
        namedSampleModel2.printTestString()

        sampleSet.forEach {
            it.printTestString()
        }

        for ((k, v) in intKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in longKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in stringKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in classKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in customKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        for ((k, v) in complexKeySampleMap) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }
    }
}