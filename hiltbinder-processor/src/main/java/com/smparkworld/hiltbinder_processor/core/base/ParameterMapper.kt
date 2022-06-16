package com.smparkworld.hiltbinder_processor.core.base

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

interface ParameterMapper<R : ParametersModel> {

    fun toParamsModel(env: ProcessingEnvironment, element: Element): R
}