package com.smparkworld.hiltbinder_processor.extension

import com.squareup.javapoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

internal fun Element.asClassName(env: ProcessingEnvironment): ClassName =
    env.getClassName(this)