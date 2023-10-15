package com.smparkworld.hiltbinder_processor.kotlin.model

import com.google.devtools.ksp.symbol.KSAnnotation
import com.smparkworld.hiltbinder_processor.core.base.ParametersModel
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import kotlin.reflect.KClass

data class HiltBindsKotlinParamsModel(

    val moduleFileName: String,
    val declarationName: String,
    val declarationPackageName: String,

    val to: TypeName,
    val from: TypeName,
    val component: TypeName,
    val qualifier: KSAnnotation? = null,
    val scope: KSAnnotation? = null,
    val namedValue: String? = null

) : ParametersModel