// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.jetbrains.vuejs.web

import com.intellij.javascript.web.lang.js.decorateWithSymbolType
import com.intellij.webSymbols.*

class VueSymbolsCodeCompletionItemCustomizer : WebSymbolCodeCompletionItemCustomizer {
  override fun customize(item: WebSymbolCodeCompletionItem,
                         framework: FrameworkId?, namespace: SymbolNamespace, kind: SymbolKind): WebSymbolCodeCompletionItem =
    if (namespace == WebSymbolsContainer.NAMESPACE_HTML && framework == VueFramework.ID)
      when (kind) {
        WebSymbol.KIND_HTML_ATTRIBUTES ->
          item.symbol
            ?.takeIf { it.kind == VueWebSymbolsAdditionalContextProvider.KIND_VUE_COMPONENT_PROPS || it.kind == WebSymbol.KIND_JS_EVENTS }
            ?.let { item.decorateWithSymbolType(it) }
          ?: item
        else -> item
      }
    else item
}