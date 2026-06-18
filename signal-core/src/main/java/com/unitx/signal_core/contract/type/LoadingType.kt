package com.unitx.signal_core.contract.type

enum class LoadingType {
    /**
     * Spinner runs indefinitely. No progress tracking.
     */
    Indefinite,

    /**
     * Progress is tracked 0-100. Subtitle shows percentage + progressMessage.
     */
    Determinate
}