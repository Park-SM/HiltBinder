package com.smparkworld.hiltbinder_processor.generator.hiltsetbinds

import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.extension.error
import com.smparkworld.hiltbinder_processor.extension.getGenericTypes
import com.smparkworld.hiltbinder_processor.extension.getSuperInterfaceElement
import com.smparkworld.hiltbinder_processor.model.HiltSetBindsParamsModel
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.inject.Qualifier
import javax.lang.model.element.Element

internal class HiltSetBindsParameterMapper : ParameterMapper<HiltSetBindsParamsModel> {

    override fun toParamsModel(env: ProcessingEnvironment, element: Element): HiltSetBindsParamsModel {
        val paramTo = AnnotationManager.getAnnotationValue<HiltBinds>(env, element, PARAM_TO)
        val paramFrom = AnnotationManager.getAnnotationValue<HiltBinds>(env, element, PARAM_FROM)
        val paramComponent = AnnotationManager.getAnnotationValue<HiltBinds>(env, element, PARAM_COMPONENT)
        val qualifier = AnnotationManager.getAnnotationByParentAnnotation(env, element, Qualifier::class, Named::class)
        val namedValue = AnnotationManager.getAnnotationValues<Named>(env, element)?.get(NAMED_PARAM) as? String

        return when {
            (paramFrom != null && paramTo == null) -> {
                HiltSetBindsParamsModel(
                    element,
                    paramFrom,
                    paramComponent,
                    qualifier,
                    namedValue,
                    null
                )
            }
            (paramFrom == null && paramTo != null) -> {
                HiltSetBindsParamsModel(
                    paramTo,
                    element,
                    paramComponent,
                    qualifier,
                    namedValue,
                    null
                )
            }
            (paramFrom == null && paramTo == null) -> {
                HiltSetBindsParamsModel(
                    env.getSuperInterfaceElement(element),
                    element,
                    paramComponent,
                    qualifier,
                    namedValue,
                    element.getGenericTypes(env)
                )
            }
            else -> {
                env.error(ERROR_MSG_SIGNED_TOGETHER)
                throw IllegalStateException(ERROR_MSG_SIGNED_TOGETHER)
            }
        }
    }

    companion object {
        private const val ERROR_MSG_SIGNED_TOGETHER = "`to` and `from` cannot be signed together."

        private const val PARAM_TO = "to"
        private const val PARAM_FROM = "from"
        private const val PARAM_COMPONENT = "component"

        private const val NAMED_PARAM = "value"
    }
}