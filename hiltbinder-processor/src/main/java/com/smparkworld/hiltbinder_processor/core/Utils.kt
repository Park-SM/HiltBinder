package com.smparkworld.hiltbinder_processor.core

import com.squareup.javapoet.ClassName
import javax.lang.model.type.TypeMirror

internal object Utils {

    fun isObjectClass(type: TypeMirror): Boolean =
        type.toString() == ClassName.get(Object::class.java).canonicalName()
}
