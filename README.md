# Signal

A lightweight Android UI feedback library for displaying **toasts**, **snackbars**, **dialogs**, and
**loading overlays** with a unified, type-safe API.

---

## Why Signal

- **One API for everything** — toasts, snackbars, dialogs, and loading overlays all follow the same
  pattern
- **Up and running in minutes** — initialize once in your `Application`, then call
  `Signal.toast(activity, ...)` anywhere
- **A dialog in under a minute** — title, message, buttons, type, auto-dismiss, text input, and
  selection lists — all in one readable block
- **Loading that actually behaves** — indefinite, determinate with live progress updates,
  cancelable, back-press aware, simple or advanced — all covered
- **Beautiful out of the box** — every signal type ships with four semantic styles (Info, Success,
  Warning, Error) and smooth animations
- **Fully customizable** — override colors per signal type, per light/dark mode, down to individual
  elements like button text or icon tint
- **Queue-aware** — no more signals stomping on each other; choose independent or global sequential
  queuing
- **Lifecycle safe** — attaches to the exact activity you pass in and cleans up automatically when
  it's destroyed; no leaks, no stale views, no guessing which screen is "current"
- **Works everywhere** — drop it into XML-based layouts or Jetpack Compose with zero extra setup

## Installation

### Step 1: Add JitPack repository

Add it in your root `settings.gradle` or `build.gradle`:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
```

### Step 2: Add the dependency

```kotlin
dependencies {
    implementation("com.github.Unitx-in:signal:latest_release")
}
```

---

## Setup

Initialize Signal once in your `Application` class:

```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Signal.createCore(this) {
            setQueueStrategy(QueueStrategy.Independent)

            theme {
                light { snackBackground = Color.WHITE }
                dark { snackBackground = Color.BLACK }
            }

            snack {
                duration = 2500L
                dismissOnBackPress = true
                position = SnackPosition.Top
            }

            toast {
                position = ToastPosition.Bottom
            }
        }
    }
}
```

Register your Application class in `AndroidManifest.xml`:

```xml

<application android:name=".MyApp" />
```

> **Every `show` call requires the target `Activity`.** Signal attaches directly to the activity
> you pass in and tears down automatically when *that* activity is destroyed — it never guesses
> which screen is "current," so calling from `onCreate()`, `onStart()`, or `onResume()` all behave
> identically. See [Jetpack Compose](#jetpack-compose) below for how to obtain the `Activity` from
> a `@Composable`.

---

## Toast

### Basic

```kotlin
Signal.toast(this, "File saved")
```

### With options

**XML / View**

```kotlin
Signal.toast(this, "File saved") {
    type = ToastType.Success
    position = ToastPosition.Top
    duration = 3000L
    iconRes = R.drawable.ic_check
    iconPosition = IconPosition.Start
}
```

**Jetpack Compose**

```kotlin
val context = LocalContext.current
val activity = remember(context) { context.findActivity() }

Button(onClick = {
    activity?.let {
        Signal.toast(it, "File saved") {
            type = ToastType.Success
            position = ToastPosition.Top
        }
    }
}) { Text("Show Toast") }
```

### ToastConfig options

| Property            | Type            | Default  | Description                                         |
|---------------------|-----------------|----------|-----------------------------------------------------|
| `type`              | `ToastType`     | `Info`   | Visual style: `Info`, `Success`, `Warning`, `Error` |
| `position`          | `ToastPosition` | `Bottom` | `Top`, `Center`, `Bottom`                           |
| `duration`          | `Long`          | `2000`   | Display duration in ms                              |
| `dismissOnTap`      | `Boolean`       | `true`   | Dismiss when tapped                                 |
| `iconRes`           | `Int?`          | `null`   | Optional drawable icon                              |
| `iconPosition`      | `IconPosition`  | `Start`  | `Start`, `End`, `Top`, `Bottom`                     |
| `topOffset`         | `Int`           | `0`      | Extra offset from top edge in px                    |
| `bottomOffset`      | `Int`           | `0`      | Extra offset from bottom edge in px                 |
| `tag`               | `String?`       | `null`   | Prevents duplicate toasts with the same tag         |
| `onShown`           | `() -> Unit`    | `null`   | Called when toast appears                           |
| `onDismissed`       | `() -> Unit`    | `null`   | Called when toast is dismissed                      |
| `accessibilityText` | `String?`       | `null`   | Overrides the default accessibility description     |

---

## Snackbar

### Basic

```kotlin
Signal.snack(this, "Changes saved")
```

### With options

**XML / View**

```kotlin
Signal.snack(this, "Changes saved") {
    type = SnackType.Success
    position = SnackPosition.Bottom
    persistent = true
    action("Undo") { undoChanges() }
}
```

**Jetpack Compose**

```kotlin
val context = LocalContext.current
val activity = remember(context) { context.findActivity() }

Button(onClick = {
    activity?.let {
        Signal.snack(it, "Changes saved") {
            type = SnackType.Success
            action("Undo") { undoChanges() }
        }
    }
}) { Text("Show Snack") }
```

### SnackConfig options

| Property             | Type            | Default  | Description                                                    |
|----------------------|-----------------|----------|----------------------------------------------------------------|
| `type`               | `SnackType`     | `Info`   | Visual style: `Info`, `Success`, `Warning`, `Error`            |
| `position`           | `SnackPosition` | `Bottom` | `Top` or `Bottom`                                              |
| `duration`           | `Long`          | `2500`   | Display duration in ms (ignored when `persistent` is true)     |
| `persistent`         | `Boolean`       | `false`  | Stays until explicitly dismissed; forces cancel button visible |
| `showCancelAction`   | `Boolean`       | `true`   | Shows the cancel (✕) button                                    |
| `dismissOnBackPress` | `Boolean`       | `false`  | Dismiss on back press                                          |
| `topOffset`          | `Int`           | `0`      | Extra offset from top edge in px                               |
| `bottomOffset`       | `Int`           | `0`      | Extra offset from bottom edge in px                            |
| `tag`                | `String?`       | `null`   | Prevents duplicate snackbars with the same tag                 |
| `onShown`            | `() -> Unit`    | `null`   | Called when snackbar appears                                   |
| `onDismissed`        | `() -> Unit`    | `null`   | Called when snackbar is dismissed                              |
| `accessibilityText`  | `String?`       | `null`   | Overrides the default accessibility description                |

---

## Dialog

### Basic

```kotlin
Signal.dialog(this) {
    title = "Delete file?"
    message = "This action cannot be undone."
    type = DialogType.Error
    positive("Delete") { deleteFile() }
    negative("Cancel")
}
```

**Jetpack Compose**

```kotlin
val context = LocalContext.current
val activity = remember(context) { context.findActivity() }

Button(onClick = {
    activity?.let {
        Signal.dialog(it) {
            title = "Delete file?"
            message = "This action cannot be undone."
            type = DialogType.Error
            positive("Delete") { deleteFile() }
            negative("Cancel")
        }
    }
}) { Text("Show Dialog") }
```

### Dismiss programmatically

```kotlin
Signal.dismissDialog()
```

### Auto-dismiss

```kotlin
Signal.dialog(this) {
    title = "Session expiring"
    message = "Your session will expire in 2 minutes."
    type = DialogType.Action
    autoDismiss = true
    autoDismissDuration = 3000L
    positive("Stay logged in") { extendSession() }
}
```

### DialogConfig options

| Property                     | Type         | Default   | Description                                                                    |
|------------------------------|--------------|-----------|--------------------------------------------------------------------------------|
| `title`                      | `String`     | `""`      | Main heading                                                                   |
| `message`                    | `String`     | `""`      | Body text                                                                      |
| `type`                       | `DialogType` | `Default` | `Default`, `Positive`, `Action`, `Error`                                       |
| `header`                     | `String`     | `""`      | Header strip label — defaults to `type` label if blank                         |
| `icon`                       | `Int?`       | `null`    | Custom header icon — defaults to `type` icon if null                           |
| `cancelable`                 | `Boolean`    | `false`   | Dismiss on outside tap or back press                                           |
| `horizontalMargin`           | `Int`        | `24`      | Margin from screen edges in dp                                                 |
| `autoDismiss`                | `Boolean`    | `false`   | Auto-dismiss after `autoDismissDuration`                                       |
| `autoDismissDuration`        | `Long`       | `4000`    | Duration in ms before auto-dismiss                                             |
| `dismissOnPositive`          | `Boolean`    | `true`    | Dismiss on positive button tap                                                 |
| `dismissOnNegative`          | `Boolean`    | `true`    | Dismiss on negative button tap                                                 |
| `dismissOnNeutral`           | `Boolean`    | `true`    | Dismiss on neutral text tap                                                    |
| `onShown`                    | `() -> Unit` | `null`    | Called when dialog appears                                                     |
| `onDismissed`                | `() -> Unit` | `null`    | Called when dialog is dismissed                                                |
| `accessibilityText`          | `String?`    | `null`    | Overrides the default accessibility description                                |
| `showCloseButton`            | `Boolean`    | `true`    | Shows the dialog close button at the top right                                 |
| `secondaryButtonStrokeWidth` | `Int`        | `2`       | Stroke width of the outlined (negative) button in dp — set via `negative(...)` |

> **Back press:** non-cancelable dialogs consume the back press and stay open. Cancelable dialogs
> dismiss on back press, same as on outside tap.

### Button functions

| Function              | Description                    |
|-----------------------|--------------------------------|
| `positive(label) { }` | Primary filled button          |
| `negative(label) { }` | Secondary outlined button      |
| `neutral(label) { }`  | Text-only action below buttons |

### Button behavior — `DialogScope`

Each button callback runs with `DialogScope` as its receiver. By default, tapping a button dismisses
the dialog (governed by `dismissOnPositive` / `dismissOnNegative` / `dismissOnNeutral`). Call
`prevent()`
to keep the dialog open — useful for validation or async work — then call `dismiss()` once ready.

```kotlin
Signal.dialog(this) {
    title = "Submit form"
    positive("Submit") {
        prevent()
        viewModel.submit { success ->
            if (success) dismiss()
        }
    }
    negative("Cancel")
}
```

### Text input

Add one or more input fields to a dialog with `input { }`. Call it multiple times to stack fields
(e.g. username + password).

```kotlin
Signal.dialog(this) {
    title = "Rename file"
    input {
        hint = "File name"
        prefill = currentName
        maxLength = 50
        showCounter = true
        validator = { it.isNotBlank() }
        validationError = "Name cannot be empty"
        onInput = { newName -> renameFile(newName) }
    }
    positive("Rename") {}
    negative("Cancel")
}
```

Multiple fields:

```kotlin
Signal.dialog(this) {
    title = "Login"
    input { hint = "Username"; onInput = { username = it } }
    input {
        hint = "Password"
        password = true
        validator = { it.length >= 6 }
        validationError = "Min 6 characters"
        onInput = { password = it }
    }
    positive("Login") {}
}
```

The positive button is automatically disabled until all `validator` checks pass. `onInput` fires
with
the field's current value when positive is tapped.

#### DialogInputConfig options

| Property          | Type                  | Default           | Description                                         |
|-------------------|-----------------------|-------------------|-----------------------------------------------------|
| `hint`            | `String`              | `""`              | Hint text shown inside the field                    |
| `prefill`         | `String`              | `""`              | Pre-filled value                                    |
| `inputType`       | `Int`                 | `TYPE_CLASS_TEXT` | Android `InputType` flags                           |
| `maxLength`       | `Int?`                | `null`            | Max character length                                |
| `showCounter`     | `Boolean`             | `false`           | Shows character counter — requires `maxLength`      |
| `password`        | `Boolean`             | `false`           | Masks input with a visibility toggle                |
| `multiLine`       | `Boolean`             | `false`           | Expands the field to multi-line                     |
| `validator`       | `(String) -> Boolean` | `null`            | Disables positive button until this returns `true`  |
| `validationError` | `String`              | `""`              | Error shown below the field when validation fails   |
| `onInput`         | `(String) -> Unit`    | `null`            | Called with the field value when positive is tapped |

### Selection

Add a radio (single), checkbox (multi), or chip selection list with `selection { }`.

```kotlin
Signal.dialog(this) {
    title = "Sort by"
    selection {
        mode = DialogSelectionMode.SINGLE
        options("Name", "Date", "Size")
        preSelected = setOf("Name")
        onSelected = { selected -> applySort(selected.first()) }
    }
    positive("Apply") {}
    negative("Cancel")
}
```

```kotlin
Signal.dialog(this) {
    title = "Notify me about"
    selection {
        mode = DialogSelectionMode.MULTI
        options("Updates", "Offers", "News")
        preSelected = setOf("Updates")
        onSelected = { selected -> savePreferences(selected) }
    }
    positive("Save") {}
}
```

```kotlin
Signal.dialog(this) {
    title = "Filter by tags"
    selection {
        mode = DialogSelectionMode.CHIP
        options("Android", "iOS", "Web", "Backend")
        onSelected = { selected -> applyFilters(selected) }
    }
    positive("Filter") {}
}
```

#### DialogSelectionConfig options

| Property      | Type                          | Default      | Description                                                         |
|---------------|-------------------------------|--------------|---------------------------------------------------------------------|
| `mode`        | `DialogSelectionType`         | `SINGLE`     | `SINGLE` (radio), `MULTI` (checkbox), or `CHIP`                     |
| `options`     | `List<DialogSelectionOption>` | `[]`         | Selectable options — use `options(vararg labels)` for plain strings |
| `preSelected` | `Set<String>`                 | `emptySet()` | Option values selected by default                                   |
| `onSelected`  | `(Set<String>) -> Unit`       | `null`       | Called with selected values when positive is tapped                 |

---

## Loading

### Basic

```kotlin
Signal.loading(this)
```

### With options

**XML / View**

```kotlin
Signal.loading(this) {
    title = "Uploading..."
    cancelable = true
    onCancelled = { cancelUpload() }
}
```

**Jetpack Compose**

```kotlin
val context = LocalContext.current
val activity = remember(context) { context.findActivity() }

Button(onClick = {
    activity?.let {
        Signal.loading(it) {
            title = "Uploading..."
            cancelable = true
            onCancelled = { cancelUpload() }
        }
    }
}) { Text("Show Loading") }
```

### Determinate (with progress)

```kotlin
Signal.loading(this) {
    title = "Downloading"
    type = LoadingType.Determinate
    progress = 0
}

// Update progress from anywhere
Signal.updateProgress(42, "Downloading files...")
Signal.updateProgress(100)

// Dismiss when done
Signal.dismissLoading()
```

### Simple overlay (dots only)

```kotlin
Signal.loading(this) { simpleLoading = true }
```

### LoadingConfig options

| Property             | Type          | Default                   | Description                                                |
|----------------------|---------------|---------------------------|------------------------------------------------------------|
| `title`              | `String`      | `"Please wait a moment."` | Primary label below the animation                          |
| `subtitle`           | `String?`     | `null`                    | Secondary label below the title                            |
| `type`               | `LoadingType` | `Indefinite`              | `Indefinite` or `Determinate`                              |
| `progress`           | `Int`         | `0`                       | Initial progress (0–100), used with `Determinate`          |
| `progressMessage`    | `String?`     | `null`                    | Appended to the percentage, e.g. `"42% · Uploading files"` |
| `simpleLoading`      | `Boolean`     | `false`                   | Minimal dots-only overlay, no text or icon                 |
| `icon`               | `Int?`        | `null`                    | Drawable shown in the center of the animation ring         |
| `horizontalMargin`   | `Int`         | `12`                      | Margin from screen edges in dp                             |
| `cancelable`         | `Boolean`     | `false`                   | Dismiss on dim overlay tap, triggers `onCancelled`         |
| `dismissOnBackPress` | `Boolean`     | `false`                   | Dismiss on back press                                      |
| `onShown`            | `() -> Unit`  | `null`                    | Called when overlay appears                                |
| `onDismissed`        | `() -> Unit`  | `null`                    | Called when overlay is dismissed                           |
| `onCancelled`        | `() -> Unit`  | `null`                    | Called when user cancels via tap or back press             |
| `accessibilityText`  | `String?`     | `null`                    | Overrides the default accessibility description            |

---

## Jetpack Compose

Every `Signal.*` function needs an `Activity`. Inside a `@Composable`, `LocalContext.current` is
often a wrapped `Context` (not the `Activity` itself), so resolve it with a small extension:

```kotlin
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
```

Then in any composable:

```kotlin
@Composable
fun MyScreen() {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    Button(onClick = {
        activity?.let {
            Signal.toast(it, "Saved!")
        }
    }) { Text("Save") }
}
```

To show a signal once when a screen first appears (equivalent to `onResume` in the View system), use
`LaunchedEffect`:

```kotlin
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    LaunchedEffect(Unit) {
        activity?.let {
            Signal.dialog(it) {
                title = "Update required"
                message = "Please update to continue."
                cancelable = false
                positive("Update now") { /* open Play Store */ }
            }
        }
    }

    // rest of your screen content
}
```

Because Signal attaches to the `Activity` you pass in rather than inferring one, this works
correctly
regardless of exactly when Compose runs the effect — no timing dependency on lifecycle ordering.

---

## Theming

Customize colors per light/dark mode during initialization:

```kotlin
Signal.createCore(this) {
    theme {
        light {
            snackBackground = Color.WHITE
            snackTextColor = applicationContext.color(R.color.white)
            dialogPrimaryColor = Color.BLUE
        }
        dark {
            snackBackground = Color.BLACK
            snackTextColor = Color.WHITE
            dialogPrimaryColor = applicationContext.color(R.color.cyan)
        }
    }
}
```

Any property left `null` falls back to the library default. See `ColorScheme` for the full list of
customizable properties.

---

## Queue Strategy

Controls how signals are queued when multiple are triggered at once.

```kotlin
Signal.createCore(this) {
    setQueueStrategy(QueueStrategy.Independent) // default
}
```

| Strategy           | Behavior                                                                                   |
|--------------------|--------------------------------------------------------------------------------------------|
| `Independent`      | Each type (Toast, Snack, Dialog) has its own queue. A toast can appear alongside a dialog. |
| `GlobalSequential` | All types share one queue. Nothing shows until the previous signal is fully dismissed.     |

---

## Signal Types Reference

### DialogType

| Value      | Color  | Use for                                |
|------------|--------|----------------------------------------|
| `Default`  | Blue   | General information                    |
| `Positive` | Green  | Success confirmations                  |
| `Action`   | Yellow | Confirmations requiring attention      |
| `Error`    | Red    | Destructive actions or critical errors |

### SnackType / ToastType

| Value     | Color  | Use for                 |
|-----------|--------|-------------------------|
| `Info`    | Blue   | Neutral messages        |
| `Success` | Green  | Success or confirmation |
| `Warning` | Yellow | Caution messages        |
| `Error`   | Red    | Errors or failures      |

---

## Requirements

- **Min SDK:** 24
- **Compile SDK:** 36
- **Kotlin:** 1.9+
- **Min AGP:** 8.6.1 or higher (required by transitive AndroidX dependencies)

## Migrating from earlier versions

If you're upgrading from a version where `Signal.toast(...)`, `Signal.snack(...)`,
`Signal.dialog { }`,
and `Signal.loading { }` took no activity parameter, add the calling `Activity` (usually `this`, or
the
result of `findActivity()` in Compose) as the first argument to each call:

```kotlin
// Before
Signal.toast("Saved!")
Signal.dialog { title = "Confirm?" }

// After
Signal.toast(this, "Saved!")
Signal.dialog(this) { title = "Confirm?" }
```

`Signal.dismissDialog()`, `Signal.dismissLoading()`, and `Signal.updateProgress(...)` are
unchanged —
they act on whatever signal is already showing and don't need an activity.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Support

- Create an [Issue](https://github.com/Unitx-in/signal/issues)
- Email: developer@unitx.in
- You can contact me on the above email directly, if you have any problem using the library.

## Show your support

Give a ⭐️ if this project helped you!

---

Made with ❤️ by [Navneet/Unitx] (https://github.com/navneetLawania)