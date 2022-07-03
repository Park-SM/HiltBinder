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
import com.smparkworld.hiltbinderexample.sample.intomap.qualifier.QualifiedMapCustomKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.SampleKey
import com.smparkworld.hiltbinderexample.sample.intomap.named.NamedMapCustomKeySampleModel
import com.smparkworld.hiltbinderexample.sample.intomap.qualifier.SampleMapQualifier1
import com.smparkworld.hiltbinderexample.sample.intomap.qualifier.SampleMapQualifier2
import com.smparkworld.hiltbinderexample.sample.intoset.SetSampleModel
import com.smparkworld.hiltbinderexample.sample.intoset.named.NamedSetSampleModel
import com.smparkworld.hiltbinderexample.sample.intoset.qualifier.SampleSetQualifier1
import com.smparkworld.hiltbinderexample.sample.intoset.qualifier.SampleSetQualifier2
import com.smparkworld.hiltbinderexample.sample.intoset.qualifier.QualifiedSetSampleModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    ////////////////////////////////////////////
    // Basic usage
    @Inject
    lateinit var toSampleModel: ToSampleModel

    @Inject
    lateinit var fromSampleModel: FromSampleModel

    @Inject
    lateinit var componentSampleModel: ComponentSampleModel
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Basic usage - qualifier
    @Inject
    @SampleQualifier1
    lateinit var qualifierSampleModel1: QualifierSampleModel

    @Inject
    @SampleQualifier2
    lateinit var qualifierSampleModel2: QualifierSampleModel
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Basic usage - named
    @Inject
    @Named("model1")
    lateinit var namedSampleModel1: NamedSampleModel

    @Inject
    @Named("model2")
    lateinit var namedSampleModel2: NamedSampleModel
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Set Multibinding - basics
    @Inject
    lateinit var sampleSet: @JvmSuppressWildcards Set<SetSampleModel>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Set Multibinding - qualifier
    @Inject
    @SampleSetQualifier1
    lateinit var sampleQualifiedSet1: @JvmSuppressWildcards Set<QualifiedSetSampleModel>

    @Inject
    @SampleSetQualifier2
    lateinit var sampleQualifiedSet2: @JvmSuppressWildcards Set<QualifiedSetSampleModel>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Set Multibinding - named
    @Inject
    @Named("sampleNamedSet1")
    lateinit var sampleNamedSet1: @JvmSuppressWildcards Set<NamedSetSampleModel>

    @Inject
    @Named("sampleNamedSet2")
    lateinit var sampleNamedSet2: @JvmSuppressWildcards Set<NamedSetSampleModel>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Map Multibinding - basic (Using @IntKey, @LongKey, @StringKey, @ClassKey provided by Hilt)
    @Inject
    lateinit var intKeySampleMap: @JvmSuppressWildcards Map<Int, Provider<MapIntKeySampleModel>>

    @Inject
    lateinit var longKeySampleMap: @JvmSuppressWildcards Map<Long, Provider<MapLongKeySampleModel>>

    @Inject
    lateinit var stringKeySampleMap: @JvmSuppressWildcards Map<String, Provider<MapStringKeySampleModel>>

    @Inject
    lateinit var classKeySampleMap: @JvmSuppressWildcards Map<Class<*>, Provider<MapClassKeySampleModel>>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Map Multibinding - custom key
    @Inject
    lateinit var customKeySampleMap: @JvmSuppressWildcards Map<SampleType, Provider<MapCustomKeySampleModel>>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Map Multibinding - complex custom key
    @Inject
    lateinit var complexKeySampleMap: @JvmSuppressWildcards Map<SampleMapComplexKey, Provider<MapComplexKeySampleModel>>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Map Multibinding - qualifier
    @Inject
    @SampleMapQualifier1
    lateinit var qualifiedCustomKeySampleMap1: @JvmSuppressWildcards Map<SampleKey, Provider<QualifiedMapCustomKeySampleModel>>

    @Inject
    @SampleMapQualifier2
    lateinit var qualifiedCustomKeySampleMap2: @JvmSuppressWildcards Map<SampleKey, Provider<QualifiedMapCustomKeySampleModel>>
    ////////////////////////////////////////////


    ////////////////////////////////////////////
    // Map Multibinding - named
    @Inject
    @Named("sampleNamedMap1")
    lateinit var namedCustomKeySampleMap1: @JvmSuppressWildcards Map<SampleKey, Provider<NamedMapCustomKeySampleModel>>

    @Inject
    @Named("sampleNamedMap2")
    lateinit var namedCustomKeySampleMap2: @JvmSuppressWildcards Map<SampleKey, Provider<NamedMapCustomKeySampleModel>>
    ////////////////////////////////////////////


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

        Log.d("Test!!", "qualifier: SampleSetQualifier1")
        sampleQualifiedSet1.forEach {
            it.printTestString()
        }

        Log.d("Test!!", "qualifier: SampleSetQualifier2")
        sampleQualifiedSet2.forEach {
            it.printTestString()
        }

        Log.d("Test!!", "named: sampleNamedSet1")
        sampleNamedSet1.forEach {
            it.printTestString()
        }

        Log.d("Test!!", "named: sampleNamedSet2")
        sampleNamedSet2.forEach {
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

        Log.d("Test!!", "qualifier: SampleMapQualifier1")
        for ((k, v) in qualifiedCustomKeySampleMap1) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        Log.d("Test!!", "qualifier: SampleMapQualifier2")
        for ((k, v) in qualifiedCustomKeySampleMap2) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        Log.d("Test!!", "named: sampleNamedMap1")
        for ((k, v) in namedCustomKeySampleMap1) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }

        Log.d("Test!!", "named: sampleNamedMap2")
        for ((k, v) in namedCustomKeySampleMap2) {
            Log.d("Test!!", "key: $k")
            v.get().printTestString()
        }
    }
}