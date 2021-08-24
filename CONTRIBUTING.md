# Contributing to Thumbprint

The Design Systems team welcomes contributions from all developers at Thumbtack. These contributions can range from small bug reports to brand new components and initiatives.

Here are a few ways to get started:

## File a bug or request a feature

Providing feedback is the easiest way to contribute to Thumbprint. You can do this by [creating an issue on GitHub](https://github.com/thumbtack/thumbprint-android/issues).

If you're a Thumbtack employee, you can also post on [#design-systems](https://thumbtack.slack.com/messages/C7FLM0ZGU/details/) for quick help.

## Contribute code to Thumbprint

There are two ways to contribute code back to Thumbprint:

1. **Tackle open GitHub issues:** Issues labeled as “[good first issue](https://github.com/thumbtack/thumbprint-android/issues?q=is%3Aopen+is%3Aissue+label%3A%22good+first+issue%22)” or “[help wanted](https://github.com/thumbtack/thumbprint-android/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22)” are perfect for contributors that want to tackle small tasks.
2. **Propose and create a new component:** Creating a component allows contributors to dive-deep into component API design, testing, accessibility, and documentation. Please [create a GitHub issue](https://github.com/thumbtack/thumbprint-android/issues) to propose a new component. If the component is a good candidate for Thumbprint, we’ll schedule a kick-off meeting to discuss next steps.

### Submitting a pull request

You can create a pull request using the standard `gh pr create` command. Here are a few things to keep in mind when creating a pull request:

- **Tests:** Our suite of tests will run automatically on the creation of a pr but you can also run them on your local branch by running `./gradlew check`.

- **Creating a local maven .AAR:** If you want to test your changes locally before creating a PR, you can publish your .AAR locally by running `./gradlew publishReleasePublicationToMavenLocal`. Make sure to add the maven local repo to the application that is importing thumbprint so it is fetched from there and not JitPack. If you are a Thumbtack employee add it to BaseModulePlugin like this:
```
BaseModulePlugin.apply(...) {
   project.repositories{
      // Add here
      mavenLocal {
         content {
            includeGroup("com.github.thumbtack")
         }
      }
   }
}
```

If you're having issues, try changing the version number to one that isn't currently used by JitPack and importing that version locally.

## Releasing a new version of Thumbprint

This will be done by a member of the Thumbtack Android team when code has been merged and is ready for release.

---

As always, reach out to [#design-systems](https://thumbtack.slack.com/messages/C7FLM0ZGU/details/) (internal to Thumbtack employees) or [create an issue](https://github.com/thumbtack/thumbprint-android/issues) if you have questions or feedback.