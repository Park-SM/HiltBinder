package com.smparkworld.hiltbinder_processor.java.generator.hiltmapbinds

import com.smparkworld.hiltbinder.HiltMapBinds
import com.smparkworld.hiltbinder_processor.core.Consts
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.base.ParameterMapper
import com.smparkworld.hiltbinder_processor.core.manager.AnnotationManager
import com.smparkworld.hiltbinder_processor.java.extension.asClassName
import com.smparkworld.hiltbinder_processor.java.extension.findSuperTypeMirror
import com.smparkworld.hiltbinder_processor.java.extension.getGenericTypeNames
import com.smparkworld.hiltbinder_processor.java.extension.getSuperTypeMirror
import com.smparkworld.hiltbinder_processor.java.model.HiltMapBindsParamsModel
import dagger.MapKey
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import javax.lang.model.element.Element

internal class HiltMapBindsParameterMapper : ParameterMapper<HiltMapBindsParamsModel> {

    override fun toParamsModel(env: ProcessingEnvironment, element: Element, logger: Logger): HiltMapBindsParamsModel {
        val paramTo = AnnotationManager.getElementFromAnnotation<HiltMapBinds>(env, element, Consts.PARAM_TO)
        val paramFrom = AnnotationManager.getElementFromAnnotation<HiltMapBinds>(env, element, Consts.PARAM_FROM)
        val paramComponent = AnnotationManager.getElementFromAnnotation<HiltMapBinds>(env, element, Consts.PARAM_COMPONENT)
        val paramCombined = AnnotationManager.getValuesFromAnnotation<HiltMapBinds>(env, element)?.get(Consts.PARAM_COMBINED) as? Boolean
        val qualifier = AnnotationManager.getAnnotationByParentAnnotation(env, element, Qualifier::class, Named::class)
        val scope = AnnotationManager.getAnnotationByParentAnnotation(env, element, Scope::class)
        val namedValue = AnnotationManager.getValuesFromAnnotation<Named>(env, element)?.get(Consts.NAMED_PARAM) as? String

        val mapKey = AnnotationManager.getAnnotationByParentAnnotation(env, element, MapKey::class)
        val mapKeyParams = AnnotationManager.getValuesFromParentAnnotation(env, element, MapKey::class)

        if (mapKey == null || mapKeyParams == null) {
            logger.error(ERROR_MSG_NOT_FOUND_KEY)
            throw IllegalStateException(ERROR_MSG_NOT_FOUND_KEY)
        }
        if (mapKeyParams.isEmpty()) {
            logger.error(ERROR_MSG_PARAMS_EMPTY)
            throw IllegalArgumentException(ERROR_MSG_PARAMS_EMPTY)
        }

        return when {
            (paramFrom != null && paramTo == null) -> {
                HiltMapBindsParamsModel(
                    element.asClassName(env),
                    paramFrom.asClassName(env),
                    paramComponent,
                    qualifier,
                    scope,
                    namedValue,
                    mapKey,
                    mapKeyParams
                )
            }
            (paramFrom == null && paramTo != null) -> {
                val to = element.findSuperTypeMirror(paramTo)
                if (to == null) {
                    logger.error(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltMapBindsParamsModel(
                    to.getGenericTypeNames(env, paramCombined),
                    element.asClassName(env),
                    paramComponent,
                    qualifier,
                    scope,
                    namedValue,
                    mapKey,
                    mapKeyParams
                )
            }
            (paramFrom == null && paramTo == null) -> {
                val to = element.getSuperTypeMirror()
                if (to == null) {
                    logger.error(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltMapBindsParamsModel(
                    to.getGenericTypeNames(env, paramCombined),
                    element.asClassName(env),
                    paramComponent,
                    qualifier,
                    scope,
                    namedValue,
                    mapKey,
                    mapKeyParams
                )
            }
            else -> {
                logger.error(Consts.ERROR_MSG_SIGNED_TOGETHER)
                throw IllegalStateException(Consts.ERROR_MSG_SIGNED_TOGETHER)
            }
        }
    }

    companion object {
        private const val ERROR_MSG_NOT_FOUND_KEY = "@HiltMapBinds must have Key annotation."
        private const val ERROR_MSG_PARAMS_EMPTY = "key annotation must not be empty."
    }
}