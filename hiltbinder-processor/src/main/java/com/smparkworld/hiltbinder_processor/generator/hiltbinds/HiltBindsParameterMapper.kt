package com.smparkworld.hiltbinder_processor.generator.hiltbinds

import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.extension.error
import com.smparkworld.hiltbinder_processor.extension.getSuperInterfaceElement
import com.smparkworld.hiltbinder_processor.model.HiltBindsParamsModel
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind

class HiltBindsParameterMapper : ParameterMapper<HiltBindsParamsModel> {

    override fun toParamsModel(env: ProcessingEnvironment, element: Element): HiltBindsParamsModel {
        val paramTo = AnnotationManager.getValueFromAnnotation<HiltBinds>(element, PARAM_TO)?.let {
            env.typeUtils.asElement(it)
        }
        val paramFrom = AnnotationManager.getValueFromAnnotation<HiltBinds>(element, PARAM_FROM)?.let {
            env.typeUtils.asElement(it)
        }
        val paramQualifier = AnnotationManager.getValueFromAnnotation<HiltBinds>(element, PARAM_QUALIFIER)?.let {
            env.typeUtils.asElement(it).takeIf { element -> element.kind == ElementKind.ANNOTATION_TYPE }
        }
        val paramComponent = AnnotationManager.getValueFromAnnotation<HiltBinds>(element, PARAM_COMPONENT)?.let {
            env.typeUtils.asElement(it)
        }
        return when {
            (paramFrom != null && paramTo == null) -> {
                HiltBindsParamsModel(
                    element,
                    paramFrom,
                    paramQualifier,
                    paramComponent
                )
            }
            (paramFrom == null && paramTo != null) -> {
                HiltBindsParamsModel(
                    paramTo,
                    element,
                    paramQualifier,
                    paramComponent
                )
            }
            (paramFrom == null && paramTo == null) -> {
                HiltBindsParamsModel(
                    env.getSuperInterfaceElement(element),
                    element,
                    paramQualifier,
                    paramComponent
                )
            }
            else -> {
                val errorMessage = "`to` and `from` cannot be signed together."
                env.error(errorMessage)
                throw IllegalStateException(errorMessage)
            }
        }
    }

    companion object {
        private const val PARAM_TO = "to"
        private const val PARAM_FROM = "from"
        private const val PARAM_QUALIFIER = "qualifier"
        private const val PARAM_COMPONENT = "component"
    }
}