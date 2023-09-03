package com.smparkworld.hiltbinder_processor.java.generator.hiltbinds

import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinder_processor.core.Consts
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.java.extension.asClassName
import com.smparkworld.hiltbinder_processor.java.extension.findSuperTypeMirror
import com.smparkworld.hiltbinder_processor.java.extension.getGenericTypeNames
import com.smparkworld.hiltbinder_processor.java.extension.getSuperTypeMirror
import com.smparkworld.hiltbinder_processor.java.model.HiltBindsParamsModel
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import javax.lang.model.element.Element

internal class HiltBindsParameterMapper : ParameterMapper<HiltBindsParamsModel> {

    override fun toParamsModel(env: ProcessingEnvironment, element: Element, logger: Logger): HiltBindsParamsModel {
        val paramTo = AnnotationManager.getElementFromAnnotation<HiltBinds>(env, element, Consts.PARAM_TO)
        val paramFrom = AnnotationManager.getElementFromAnnotation<HiltBinds>(env, element, Consts.PARAM_FROM)
        val paramComponent = AnnotationManager.getElementFromAnnotation<HiltBinds>(env, element, Consts.PARAM_COMPONENT)
        val paramCombined = AnnotationManager.getValuesFromAnnotation<HiltBinds>(env, element)?.get(Consts.PARAM_COMBINED) as? Boolean
        val qualifier = AnnotationManager.getAnnotationByParentAnnotation(env, element, Qualifier::class, Named::class)
        val scope = AnnotationManager.getAnnotationByParentAnnotation(env, element, Scope::class)
        val namedValue = AnnotationManager.getValuesFromAnnotation<Named>(env, element)?.get(Consts.NAMED_PARAM) as? String

        return when {
            (paramFrom != null && paramTo == null) -> {
                HiltBindsParamsModel(
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
                    logger.error(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltBindsParamsModel(
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
                    logger.error(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltBindsParamsModel(
                    to.getGenericTypeNames(env, paramCombined),
                    element.asClassName(env),
                    paramComponent,
                    qualifier,
                    scope,
                    namedValue
                )
            }
            else -> {
                logger.error(Consts.ERROR_MSG_SIGNED_TOGETHER)
                throw IllegalStateException(Consts.ERROR_MSG_SIGNED_TOGETHER)
            }
        }
    }
}