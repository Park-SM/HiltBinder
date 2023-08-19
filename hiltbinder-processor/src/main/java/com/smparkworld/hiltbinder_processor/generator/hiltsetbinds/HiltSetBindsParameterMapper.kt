package com.smparkworld.hiltbinder_processor.generator.hiltsetbinds

import com.smparkworld.hiltbinder.HiltSetBinds
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.extension.asClassName
import com.smparkworld.hiltbinder_processor.extension.findSuperTypeMirror
import com.smparkworld.hiltbinder_processor.extension.getGenericTypeNames
import com.smparkworld.hiltbinder_processor.extension.getSuperTypeMirror
import com.smparkworld.hiltbinder_processor.model.HiltSetBindsParamsModel
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import javax.lang.model.element.Element

internal class HiltSetBindsParameterMapper : ParameterMapper<HiltSetBindsParamsModel> {

    override fun toParamsModel(env: ProcessingEnvironment, element: Element, logger: Logger): HiltSetBindsParamsModel {
        val paramTo = AnnotationManager.getElementFromAnnotation<HiltSetBinds>(env, element, PARAM_TO)
        val paramFrom = AnnotationManager.getElementFromAnnotation<HiltSetBinds>(env, element, PARAM_FROM)
        val paramComponent = AnnotationManager.getElementFromAnnotation<HiltSetBinds>(env, element, PARAM_COMPONENT)
        val paramCombined =  AnnotationManager.getValuesFromAnnotation<HiltSetBinds>(env, element)?.get(PARAM_COMBINED) as? Boolean
        val qualifier = AnnotationManager.getAnnotationByParentAnnotation(env, element, Qualifier::class, Named::class)
        val scope = AnnotationManager.getAnnotationByParentAnnotation(env, element, Scope::class)
        val namedValue = AnnotationManager.getValuesFromAnnotation<Named>(env, element)?.get(NAMED_PARAM) as? String

        return when {
            (paramFrom != null && paramTo == null) -> {
                HiltSetBindsParamsModel(
                    element.asClassName(env),
                    paramFrom.asClassName(env),
                    paramComponent,
                    qualifier,
                    scope,
                    namedValue
                )
            }
            (paramFrom == null && paramTo != null) -> {
                val to = element.findSuperTypeMirror(paramTo)
                if (to == null) {
                    logger.error(ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltSetBindsParamsModel(
                    to.getGenericTypeNames(env, paramCombined),
                    element.asClassName(env),
                    paramComponent,
                    qualifier,
                    scope,
                    namedValue
                )
            }
            (paramFrom == null && paramTo == null) -> {
                val to = element.getSuperTypeMirror()
                if (to == null) {
                    logger.error(ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltSetBindsParamsModel(
                    to.getGenericTypeNames(env, paramCombined),
                    element.asClassName(env),
                    paramComponent,
                    qualifier,
                    scope,
                    namedValue
                )
            }
            else -> {
                logger.error(ERROR_MSG_SIGNED_TOGETHER)
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
        private const val PARAM_COMBINED = "combined"

        private const val NAMED_PARAM = "value"
    }
}