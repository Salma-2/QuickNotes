# App Shortcuts
* [Types of Shorcuts](#types-of-shortcuts)


## Types of Shortcuts
* [Static Shortcuts](#static-shortcuts)
are best for apps that link to content using a consistent structure throughout the lifetime of a user's interaction with the app
* [Dynamic Shortcuts](#dynamic-shortcuts)
are used for actions in apps that are context-sensitive.
For instance, if you build a game that allows the user to start from their current level on launch, you should update the shortcut frequently.
Using a dynamic shortcut allows the shortcut to be updated each time the user clears a level.
* [Pinned Shortcuts](#pinned-shortcuts)
are used for specific, user-driven actions. For example, a user might want to pin a specific website to the launcher.

## Static Shortcuts
### Add resources to the Manifest
- In `AndroidManifest.xml`, find an activity whose intent filters are set to the `android.intent.action.MAIN` action and the `android.intent.category.LAUNCHER` category. 
- Add a `<meta-data>` element to this activity that references the resource file where the app's shortcuts are defined
```xml
...
  <meta-data android:name="android.app.shortcuts"
             android:resource="@xml/shortcuts" /> 
...
```
### Create shortcuts file and Customize attribute values
- Create a new resource file: [`res/xml/shortcuts.xml`](https://github.com/Salma-2/QuickNotes/blob/master/app/src/main/res/xml/shortcuts.xml)

#### Inner elements
- Capabilities tell Google how the in-app functionality can be semantically accessed using BII (built-in intents), and enables voice support for your features

