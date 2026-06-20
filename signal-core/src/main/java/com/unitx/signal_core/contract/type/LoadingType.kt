package com.unitx.signal_core.contract.type

/**
 * Loading animation style.
 * Configured via [LoadingConfig.type].
 */
enum class LoadingType {
    /** Spinner runs indefinitely. No progress tracking. */
    Indefinite,

    /** Progress is tracked 0–100. Subtitle shows percentage + [LoadingConfig.progressMessage]. */
    Determinate
}