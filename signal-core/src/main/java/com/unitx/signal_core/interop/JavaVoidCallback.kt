package com.unitx.signal_core.interop

/**
 * Java-friendly functional interface for zero-argument callbacks.
 *
 * Kotlin function types like `() -> Unit` compile to `kotlin.jvm.functions.Function0<Unit>`.
 * Since `Function0` is generic, `Unit` erases to `Object` in the bytecode, forcing Java
 * lambdas to explicitly `return null;` (or `Unit.INSTANCE`) even though there's nothing
 * meaningful to return.
 *
 * `JavaVoidCallback` is a non-generic, single-abstract-method interface whose method
 * returns `void` directly, so Java lambdas implementing it don't need a return statement.
 *
 * Used throughout Signal's config classes as a sibling overload alongside the
 * Kotlin-facing `() -> Unit` parameter — e.g. `onShown`, `onDismissed`, `onCancelled`
 * on [com.unitx.signal_core.contract.config.ToastConfig], [com.unitx.signal_core.contract.config.SnackConfig],
 * [com.unitx.signal_core.contract.config.LoadingConfig], [com.unitx.signal_core.contract.config.NotificationConfig],
 * and [com.unitx.signal_core.contract.config.dialog.DialogConfig].
 *
 * ## Kotlin
 * ```kotlin
 * toastConfig.onShown = { Log.d("Tag", "Toast shown") }
 * ```
 *
 * ## Java
 * ```java
 * toastConfig.onShown(() -> {
 *     Log.d("Tag", "Toast shown");
 * });
 * ```
 *
 * @see JavaUnitCallback for the value-receiving equivalent (e.g. `onInput`, `onSelected`).
 */
fun interface JavaVoidCallback {
    fun invoke()
}