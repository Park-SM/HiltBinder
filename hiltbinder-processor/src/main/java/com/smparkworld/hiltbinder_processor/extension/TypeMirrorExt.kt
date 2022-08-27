package com.smparkworld.hiltbinder_processor.extension

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

internal fun TypeMirror.asElement(env: ProcessingEnvironment): Element =
    env.typeUtils.asElement(this)
