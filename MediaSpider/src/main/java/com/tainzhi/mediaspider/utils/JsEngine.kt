package com.tainzhi.mediaspider.utils

import org.mozilla.javascript.Context
import org.mozilla.javascript.Function

/**
 * File:     JsEngine
 * Author:   tainzhi
 * Created:  2020/12/29 12:23
 * Mail:     QFQ61@qq.com
 * Description:
 */
object JsEngine {
    fun execJs(js: String, functionName: String, vararg params: String): String {
        val cx = Context.enter()
        val rhino = cx.apply {
            optimizationLevel = -1
        }
        try {
            val scope = rhino.initStandardObjects()
            rhino.evaluateString(scope, js, "script", 1, null)
            val f = scope.get(functionName, scope)
            if (f is Function) {
                val result = f.call(rhino, scope, scope, arrayOf(params))
                return Context.toString(result)
            } else {
                throw Exception("$functionName} is not js function")
            }
        } finally {
            Context.exit()
        }
    }
}
