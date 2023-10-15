package com.smparkworld.hiltbinder_processor.kotlin.generator.hiltbinds

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSDeclaration
import com.smparkworld.hiltbinder.HiltBinds
import com.smparkworld.hiltbinder_processor.core.Consts
import com.smparkworld.hiltbinder_processor.core.Logger
import com.smparkworld.hiltbinder_processor.core.base.KotlinParameterMapper
import com.smparkworld.hiltbinder_processor.kotlin.extension.asClassName
import com.smparkworld.hiltbinder_processor.kotlin.extension.findAnnotation
import com.smparkworld.hiltbinder_processor.kotlin.extension.findAnnotationByParent
import com.smparkworld.hiltbinder_processor.kotlin.extension.findClassFromArguments
import com.smparkworld.hiltbinder_processor.kotlin.extension.findSuperClassDeclaration
import com.smparkworld.hiltbinder_processor.kotlin.extension.findValueFromArguments
import com.smparkworld.hiltbinder_processor.kotlin.extension.getGenericTypeName
import com.smparkworld.hiltbinder_processor.kotlin.model.HiltBindsKotlinParamsModel
import com.squareup.kotlinpoet.asTypeName
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.reflect.KClass

@AutoService(KotlinParameterMapper::class)
class HiltBindsKotlinParameterMapper : KotlinParameterMapper<HiltBindsKotlinParamsModel> {

    override fun getSupportedAnnotationType(): KClass<out Annotation> = HiltBinds::class

    override fun toParamsModel(
        env: SymbolProcessorEnvironment,
        target: KSDeclaration,
        logger: Logger
    ): HiltBindsKotlinParamsModel {
        val annotation = target.findAnnotation(getSupportedAnnotationType())
            ?: throw IllegalArgumentException("Not found annotation.")

        val moduleFileName = "${target}${Consts.MODULE_SUFFIX}"
        val declarationName = target.toString()
        val declarationPackageName = target.packageName.asString()

        val paramTo = annotation.findClassFromArguments(Consts.PARAM_TO)
        val paramFrom = annotation.findClassFromArguments(Consts.PARAM_FROM)
        val paramComponent = annotation.findClassFromArguments(Consts.PARAM_COMPONENT) ?: SingletonComponent::class.asTypeName()
        val paramCombined = annotation.findValueFromArguments<Boolean>(Consts.PARAM_COMBINED)
        val qualifier = target.findAnnotationByParent(Qualifier::class, ignores = listOf(Named::class))
        val scope = target.findAnnotationByParent(Scope::class)
        val namedValue = target.findAnnotation(Named::class)?.findValueFromArguments<String>(Consts.NAMED_PARAM)

        return when {
            (paramFrom != null && paramTo == null) -> {
                HiltBindsKotlinParamsModel(
                    moduleFileName = moduleFileName,
                    declarationName = declarationName,
                    declarationPackageName = declarationPackageName,
                    to = target.asClassName(),
                    from = paramFrom,
                    component = paramComponent,
                    qualifier = qualifier,
                    scope = scope,
                    namedValue = namedValue
                )
            }
            (paramFrom == null && paramTo != null) -> {
                val to = target.findSuperClassDeclaration(parent = paramTo)
                if (to == null) {
                    logger.error(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltBindsKotlinParamsModel(
                    moduleFileName = moduleFileName,
                    declarationName = declarationName,
                    declarationPackageName = declarationPackageName,
                    to = to.getGenericTypeName(isCombined = paramCombined, logger),
                    from = target.asClassName(),
                    component = paramComponent,
                    qualifier = qualifier,
                    scope = scope,
                    namedValue = namedValue
                )
            }
            (paramFrom == null && paramTo == null) -> {
                val to = target.findSuperClassDeclaration()
                if (to == null) {
                    logger.error(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                    throw IllegalStateException(Consts.ERROR_MSG_NOT_FOUND_SUPER)
                }

                HiltBindsKotlinParamsModel(
                    moduleFileName = moduleFileName,
                    declarationName = declarationName,
                    declarationPackageName = declarationPackageName,
                    to = to.getGenericTypeName(isCombined =  paramCombined, logger),
                    from = target.asClassName(),
                    component = paramComponent,
                    qualifier = qualifier,
                    scope = scope,
                    namedValue = namedValue
                )
            }
            else -> {
                logger.error(Consts.ERROR_MSG_SIGNED_TOGETHER)
                throw IllegalStateException(Consts.ERROR_MSG_SIGNED_TOGETHER)
            }
        }
    }
}