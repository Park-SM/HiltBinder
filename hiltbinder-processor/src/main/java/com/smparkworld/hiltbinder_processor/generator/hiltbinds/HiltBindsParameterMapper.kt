package com.smparkworld.hiltbinder_processor.generator.hiltbinds

import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.extension.asClassName
import com.smparkworld.hiltbinder_processor.extension.error
import com.smparkworld.hiltbinder_processor.extension.getGenericTypeNames
import com.smparkworld.hiltbinder_processor.extension.getSuperTypeMirror
import com.smparkworld.hiltbinder_processor.model.HiltBindsParamsModel
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.inject.Qualifier
import javax.lang.model.element.Element

internal class HiltBindsParameterMapper : ParameterMapper<HiltBindsParamsModel> {

    override fun toParamsModel(env: ProcessingEnvironment, element: Element): HiltBindsParamsModel {
        val paramTo = AnnotationManager.getAnnotationValue<HiltBinds>(env, element, PARAM_TO)
        val paramFrom = AnnotationManager.getAnnotationValue<HiltBinds>(env, element, PARAM_FROM)
        val paramComponent = AnnotationManager.getAnnotationValue<HiltBinds>(env, element, PARAM_COMPONENT)
        val qualifier = AnnotationManager.getAnnotationByParentAnnotation(env, element, Qualifier::class, Named::class)
        val namedValue = AnnotationManager.getAnnotationValues<Named>(env, element)?.get(NAMED_PARAM) as? String

        return when {
            (paramFrom != null && paramTo == null) -> {
                HiltBindsParamsModel(
                    element.asClassName(env),
                    paramFrom.asClassName(env),
                    paramComponent,
                    qualifier,
                    namedValue
                )
            }
            (paramFrom == null && paramTo != null) -> {
                HiltBindsParamsModel(
                    paramTo.asClassName(env),
                    element.asClassName(env),
                    paramComponent,
                    qualifier,
                    namedValue
                )
            }
            (paramFrom == null && paramTo == null) -> {
                val to = element.getSuperTypeMirror()
                if (to == null) {
                    env.error(ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltBindsParamsModel(
                    to.getGenericTypeNames(env),
                    element.asClassName(env),
                    paramComponent,
                    qualifier,
                    namedValue
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
        private const val ERROR_MSG_NOT_FOUND_SUPER = "Super class not found."


        private const val PARAM_TO = "to"
        private const val PARAM_FROM = "from"
        private const val PARAM_COMPONENT = "component"

        private const val NAMED_PARAM = "value"
    }
}