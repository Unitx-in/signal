# Signal

A lightweight Android UI feedback library for displaying **toasts**, **snackbars**, **dialogs**,
**loading overlays**, and **notifications** with a unified, type-safe API.

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
- **Pinterest-style notifications** — a lightweight "Saved to X" style banner with a leading icon
  and two-part text, distinct from toasts and snackbars

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
    implementation("com.github.unitx.in:signal:latest_release")
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
| `iconUrl`                    | `String?`    | `null`    | Remote icon URL, loaded async — takes precedence over `icon` if both are set   |
| `cancelable`                 | `Boolean`    | `false`   | Dismiss on outside tap or back press                                           |
| `horizontalMargin`           | `Int`        | `24`      | Margin from screen edges in dp                                                 |
| `autoDismiss`                | `Boolean`    | `false`   | Auto-dismiss after `autoDismissDuration`                                       |
| `autoDismissDuration`        | `Long`       | `4000`    | Duration in ms before auto-dismiss                                             |
| `dismissOnPositive`          | `Boolean`    | `true`    | Dismiss on positive button tap (only if all field validators pass)             |
| `dismissOnNegative`          | `Boolean`    | `true`    | Dismiss on negative button tap                                                 |
| `dismissOnNeutral`           | `Boolean`    | `true`    | Dismiss on neutral text tap                                                    |
| `onShown`                    | `() -> Unit` | `null`    | Called when dialog appears                                                     |
| `onDismissed`                | `() -> Unit` | `null`    | Called when dialog is dismissed                                                |
| `accessibilityText`          | `String?`    | `null`    | Overrides the default accessibility description                                |
| `showCloseButton`            | `Boolean`    | `true`    | Shows the dialog close button at the top right                                 |
| `disableIconColor`           | `Boolean`    | `false`   | Disables icon tinting — useful with `iconUrl` for full-color remote images     |
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
`prevent()` to keep the dialog open — useful for async work — then call `dismiss()` once ready.

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

> **Note:** this is separate from field validation below. `prevent()`/`dismiss()` control the
> dialog's dismissal after the positive callback *runs*. Field validators run *before* the positive
> callback runs at all — if any field fails validation, the callback never fires and the dialog
> stays open with error text shown.

---

### Fields — composing inputs, selections, and dropdowns

A dialog can hold any number of `input { }`, `selection { }`, and `dropdown { }` fields, in any
combination and any order. Each call **adds** a field — calling the same builder multiple times
stacks fields rather than overwriting the previous one. Fields render top-to-bottom in the exact
order they were declared.

```kotlin
Signal.dialog(this) {
    title = "New Task"
    input { hint = "Task title"; onInput = { title = it } }
    dropdown {
        placeholder = "Priority"
        options("Low", "Medium", "High")
        onSelected = { priority = it }
    }
    selection {
        label = "Type"
        mode = DialogSelectionMode.CHIP
        options("Bug", "Feature", "Chore")
        onSelected = { type = it }
    }
    input { hint = "Notes"; multiLine = true; onInput = { notes = it } }
    positive("Create") {}
    negative("Cancel")
}
```

All field callbacks (`onInput` / `onSelected`) fire once, in declared order, when the positive
button is tapped — provided every field's validator (if set) passes. See **Validation** below.

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

The first `input { }` field in the dialog auto-focuses and shows the keyboard when the dialog
opens; subsequent input fields do not.

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
| `validator`       | `(String) -> Boolean` | `null`            | Blocks submit until this returns `true`             |
| `validationError` | `String`              | `""`              | Error shown below the field when validation fails   |
| `onInput`         | `(String) -> Unit`    | `null`            | Called with the field value when positive is tapped |

> While typing, the field also shows a live error preview as soon as `validator` fails on non-empty
> input — but this is cosmetic only. The actual submit block happens on tap, described below.

### Selection

Add a radio (single), checkbox (multi), or chip selection list with `selection { }`. An optional
`label` renders as a heading above the group — recommended whenever a dialog has more than one
selection group, so each is clearly identified.

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
        validator = { it.isNotEmpty() }
        validationError = "Select at least one option"
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

Multiple selection groups — of the same or different modes — can be stacked in one dialog:

```kotlin
Signal.dialog(this) {
    title = "Advanced Filters"
    selection {
        label = "Sort by"
        mode = DialogSelectionMode.SINGLE
        options("Newest", "Oldest", "A-Z")
        preSelected = setOf("Newest")
        onSelected = { sort -> applySort(sort.first()) }
    }
    selection {
        label = "Status"
        mode = DialogSelectionMode.MULTI
        options("Active", "Archived", "Draft")
        onSelected = { status -> applyStatus(status) }
    }
    positive("Apply") {}
}
```

#### DialogSelectionConfig options

| Property          | Type                          | Default      | Description                                                          |
|-------------------|-------------------------------|--------------|----------------------------------------------------------------------|
| `label`           | `String`                      | `""`         | Optional heading above this group — recommended when stacking groups |
| `mode`            | `DialogSelectionMode`         | `SINGLE`     | `SINGLE` (radio), `MULTI` (checkbox), or `CHIP`                      |
| `options`         | `List<DialogSelectionOption>` | `[]`         | Selectable options — use `options(vararg labels)` for plain strings  |
| `preSelected`     | `Set<String>`                 | `emptySet()` | Option values selected by default                                    |
| `validator`       | `(Set<String>) -> Boolean`    | `null`       | Blocks submit until this returns `true` for the current selection    |
| `validationError` | `String`                      | `""`         | Error shown below the group when validation fails                    |
| `onSelected`      | `(Set<String>) -> Unit`       | `null`       | Called with selected values when positive is tapped                  |

### Dropdown

Add a tappable field that opens a popup list for single-value selection with `dropdown { }`.

```kotlin
Signal.dialog(this) {
    title = "Choose Country"
    dropdown {
        placeholder = "Select a country"
        options("India", "USA", "UK", "Germany", "Japan")
        preSelected = "India"
        onSelected = { country -> setCountry(country) }
    }
    positive("Confirm") {}
    negative("Cancel")
}
```

Multiple dropdowns can be stacked, same as selections and inputs:

```kotlin
Signal.dialog(this) {
    title = "Location"
    dropdown {
        placeholder = "Select country"
        options("India", "USA", "UK")
        onSelected = { country -> setCountry(country) }
    }
    dropdown {
        placeholder = "Select state"
        options("Delhi", "UP", "Maharashtra")
        onSelected = { state -> setState(state) }
    }
    positive("Confirm") {}
}
```

The field's border and chevron follow the dialog's resolved theme, and highlight in the primary
color while the popup is open.

#### DialogDropdownConfig options

| Property                 | Type                          | Default              | Description                                                                      |
|--------------------------|-------------------------------|----------------------|----------------------------------------------------------------------------------|
| `placeholder`            | `String`                      | `"Select an option"` | Text shown in the field before a selection is made                               |
| `options`                | `List<DialogSelectionOption>` | `[]`                 | Options shown in the popup list — use `options(vararg labels)` for plain strings |
| `preSelected`            | `String?`                     | `null`               | Option value selected by default                                                 |
| `autoDismissOnSelection` | `Boolean`                     | `true`               | If `true`, the popup auto-closes shortly after a selection is tapped             |
| `validator`              | `(String?) -> Boolean`        | `null`               | Blocks submit until this returns `true` for the current value                    |
| `validationError`        | `String`                      | `""`                 | Error shown below the field when validation fails                                |
| `onSelected`             | `(String?) -> Unit`           | `null`               | Called with the selected value (or `null` if none) when positive is tapped       |

---

### Validation

Every field type — `input`, `selection`, and `dropdown` — supports an optional `validator` and
`validationError`. Validation runs when the positive button is tapped, **before** any field's
`onInput`/`onSelected` callback fires and before the dialog checks whether it should dismiss.

- If **any** field's validator returns `false`, the dialog does not dismiss and no field callbacks
  fire — not just the failing field's, none of them.
- **Every** field is validated on each tap, not just up to the first failure — so if three fields
  are invalid at once, all three show their error simultaneously.
- Once all validators pass, every field's callback fires in the dialog's declared field order,
  then the dialog dismisses (subject to `dismissOnPositive`).

```kotlin
Signal.dialog(this) {
    title = "New Task"
    input {
        hint = "Task title"
        validator = { it.isNotBlank() }
        validationError = "Title required"
        onInput = { title = it }
    }
    dropdown {
        placeholder = "Priority"
        options("Low", "Medium", "High")
        validator = { it != null }
        validationError = "Priority required"
        onSelected = { priority = it }
    }
    selection {
        label = "Type"
        mode = DialogSelectionMode.CHIP
        options("Bug", "Feature", "Chore")
        validator = { it.isNotEmpty() }
        validationError = "Pick a type"
        onSelected = { type = it }
    }
    positive("Create Task") {}
    negative("Discard")
}
```

Tapping **Create Task** with the title empty, no priority chosen, and no type picked shows all
three errors at once and keeps the dialog open. Fixing one field and tapping again re-validates
only against current state — no need to re-trigger the others if they already pass.

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

## Notification

A Pinterest-style ephemeral banner for two-part messages like "Saved to **Board name**", separate
from the toast system.

### Basic

```kotlin
Signal.notif(this) {
    message = "Saved to"
    highlight = "Men fashion casual outfits"
    iconRes = R.drawable.ic_board_thumb
}
```

**Jetpack Compose**

```kotlin
val context = LocalContext.current
val activity = remember(context) { context.findActivity() }

Button(onClick = {
    activity?.let {
        Signal.notif(it) {
            message = "Saved to"
            highlight = "Men fashion casual outfits"
            iconRes = R.drawable.ic_board_thumb
        }
    }
}) { Text("Show Notification") }
```

### With a remote icon

```kotlin
Signal.notif(this) {
    message = "Added to"
    highlight = "Travel wishlist"
    iconUrl = "https://example.com/board-thumb.jpg"
    duration = 3000L
}
```

### NotificationConfig options

| Property             | Type                   | Default | Description                                                                 |
|----------------------|------------------------|---------|-----------------------------------------------------------------------------|
| `message`            | `String`               | `""`    | Leading, regular-weight text (e.g. "Saved to")                              |
| `highlight`          | `String`               | `""`    | Trailing, bold-weight text (e.g. board or item name)                        |
| `duration`           | `Long`                 | `2500`  | Display duration in ms                                                      |
| `position`           | `NotificationPosition` | `Top`   | Screen position of the notification                                         |
| `iconRes`            | `Int?`                 | `null`  | Drawable shown in the leading icon slot                                     |
| `iconUrl`            | `String?`              | `null`  | Remote image URL for the icon slot — takes precedence over `iconRes` if set |
| `dismissOnTap`       | `Boolean`              | `true`  | Dismiss immediately when tapped                                             |
| `topOffset`          | `Int`                  | `0`     | Extra offset from top edge in px — applied when `position` is `Top`         |
| `bottomOffset`       | `Int`                  | `0`     | Extra offset from bottom edge in px — applied when `position` is `Bottom`   |
| `dismissOnBackPress` | `Boolean`              | `false` | Dismiss on back press                                                       |
| `tag`                | `String?`              | `null`  | Prevents duplicate notifications with the same tag from queuing             |
| `onShown`            | `() -> Unit`           | `null`  | Called when the notification appears                                        |
| `onDismissed`        | `() -> Unit`           | `null`  | Called when the notification is dismissed                                   |
| `accessibilityText`  | `String?`              | `null`  | Overrides the default accessibility description                             |

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