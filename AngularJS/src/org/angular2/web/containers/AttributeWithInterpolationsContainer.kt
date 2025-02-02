// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.angular2.web.containers

import com.intellij.model.Pointer
import com.intellij.util.containers.Stack
import com.intellij.webSymbols.*
import com.intellij.webSymbols.patterns.ComplexPatternOptions
import com.intellij.webSymbols.patterns.WebSymbolsPattern
import com.intellij.webSymbols.patterns.WebSymbolsPatternFactory
import com.intellij.webSymbols.patterns.WebSymbolsPatternItemsProvider
import com.intellij.webSymbols.utils.match
import org.angular2.Angular2Framework
import org.angular2.web.Angular2WebSymbolsAdditionalContextProvider

object AttributeWithInterpolationsContainer : WebSymbolsContainer {

  override fun createPointer(): Pointer<out WebSymbolsContainer> =
    Pointer.hardPointer(this)

  override fun getModificationCount(): Long = 0

  override fun getSymbols(namespace: SymbolNamespace?,
                          kind: SymbolKind,
                          name: String?,
                          params: WebSymbolsNameMatchQueryParams,
                          context: Stack<WebSymbolsContainer>): List<WebSymbolsContainer> =
    if (namespace == WebSymbolsContainer.NAMESPACE_HTML
        && kind == WebSymbol.KIND_HTML_ATTRIBUTES
        && name != null) {
      AttributeWithInterpolationsSymbol.match(name, context, params)
    }
    else emptyList()

  private object AttributeWithInterpolationsSymbol : WebSymbol {

    override val origin: WebSymbolsContainer.Origin =
      WebSymbolsContainer.OriginData(Angular2Framework.ID)

    override val namespace: SymbolNamespace
      get() =
        WebSymbolsContainer.NAMESPACE_HTML

    override val kind: SymbolKind
      get() =
        WebSymbol.KIND_HTML_ATTRIBUTES

    override fun createPointer(): Pointer<out WebSymbol> =
      Pointer.hardPointer(this)

    override val pattern: WebSymbolsPattern = WebSymbolsPatternFactory.createComplexPattern(
      ComplexPatternOptions(
        null, false, true,
        WebSymbol.Priority.HIGHEST, null, false, false,
        PropertiesProvider),
      false,
      WebSymbolsPatternFactory.createItemReferencePlaceholder(null))

  }

  private object PropertiesProvider : WebSymbolsPatternItemsProvider {
    override fun getSymbolTypes(context: WebSymbol?): Set<WebSymbol.SymbolType> = setOf(
      WebSymbol.SymbolType(WebSymbolsContainer.NAMESPACE_JS, WebSymbol.KIND_JS_PROPERTIES),
      WebSymbol.SymbolType(WebSymbolsContainer.NAMESPACE_JS, Angular2WebSymbolsAdditionalContextProvider.KIND_NG_DIRECTIVE_INPUTS)
    )

    override val delegate: WebSymbol? get() = null

    override fun codeCompletion(name: String,
                                position: Int,
                                contextStack: Stack<WebSymbolsContainer>,
                                registry: WebSymbolsRegistry): List<WebSymbolCodeCompletionItem> =
      emptyList()

    override fun matchName(name: String,
                           contextStack: Stack<WebSymbolsContainer>,
                           registry: WebSymbolsRegistry): List<WebSymbol> =
      registry.runNameMatchQuery(
        listOf(WebSymbolsContainer.NAMESPACE_JS, WebSymbol.KIND_JS_PROPERTIES, name),
        context = contextStack) +
      registry.runNameMatchQuery(
        listOf(WebSymbolsContainer.NAMESPACE_JS, Angular2WebSymbolsAdditionalContextProvider.KIND_NG_DIRECTIVE_INPUTS, name),
        context = contextStack)

  }
}