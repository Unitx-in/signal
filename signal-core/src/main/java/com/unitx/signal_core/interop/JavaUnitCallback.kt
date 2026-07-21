package com.unitx.signal_core.interop

/**
 * Java-friendly functional interface for callbacks that receive a value.
 *
 * Kotlin function types like `(T) -> Unit` compile to `kotlin.jvm.functions.Function1<T, Unit>`.
 * Since `Function1` is generic, `Unit` erases to `Object` in the bytecode, forcing Java
 * lambdas to explicitly `return null;` (or `Unit.INSTANCE`) even though there's nothing
 * meaningful to return.
 *
 * `JavaUnitCallback<T>` is a non-generic, single-abstract-method interface whose method
 * returns `void` directly, so Java lambdas implementing it don't need a return statement.
 *
 * Used throughout Signal's config classes as a sibling overload alongside the
 * Kotlin-facing `(T) -> Unit` parameter — e.g. [com.unitx.signal_core.contract.config.dialog.DialogInputConfig.onInput].
 *
 * ## Kotlin
 * ```kotlin
 * inputConfig.onInput = { value -> Log.d("Tag", value) }
 * ```
 *
 * ## Java
 * ```java
 * inputConfig.onInput(value -> {
 *     Log.d("Tag", value);
 * });
 * ```
 *
 * @see JavaVoidCallback for the zero-argument equivalent (e.g. `onShown`, `onDismissed`).
 */
fun interface JavaUnitCallback<T> {
    fun invoke(value: T)
}