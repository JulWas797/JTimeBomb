# JTimeBomb

[![Java CI with Gradle](https://github.com/JulWas797/JTimeBomb/actions/workflows/gradle.yml/badge.svg)](https://github.com/JulWas797/JTimeBomb/actions/workflows/gradle.yml)
![GitHub top language](https://img.shields.io/github/languages/top/JulWas797/JTimeBomb)

Protect your Java code with expiration dates.

### Why?

Imagine this situation — you give a trial/demo of a product to a customer, yet you don't want them to use it
indefinitely.
**That's where JTimeBomb comes in handy.** You can give an expiration date to the code, so it cannot be run after
a specified date has passed.

JTimeBomb can be used to easily include a proper time-bomb, instead of relying on a single utility method
(that can be easily removed with a bit of tinkering) or manually repeating the same code multiple times.

**This app works using pure bytecode transformations, providing way faster performance than standard annotation
processing.**


> **Only Java 21 (and beyond) is supported!** I advise against using legacy Java versions.

<details>
<summary>Original inspiration :-)</summary>

![Meme](https://i.programmerhumor.io/2023/08/programmerhumor-io-frontend-memes-testing-memes-b9efd7441d70e85.jpg)

</details>

### How?

Import the `jtimebomb-annotations` library (for instructions, look at the next section), and add the necessary
annotations to methods that you want to be time-bombed.

```java
import io.github.julwas797.jtimebomb.annotations.*;

class App {
    
    @TimeBomb("2025-05-13T14:00")
    public void doSomething() {
        // Code here :)
    }
}
```

The `@TimeBomb` annotations uses **ISO-8601** syntax for determining date.

After you've compiled the code run the `jtimebomb-processor` tool, to automatically transform methods, such that
they’re now time-bombed. This tool uses the following syntax:

`java -jar jtimebomb-processor.jar [input] [output]`

The processor also removes the annotation using bytecode manipulations, so it's harder to trace it. **It's also
recommended to obfuscate the code beforehand.** This greatly improves the effectiveness of this app. Some of the Java
bytecode obfuscate tools include
[dProtect](https://obfuscator.re/dprotect/) or [yGuard](https://www.yworks.com/products/yguard).

[//]: # (### Imports)

[//]: # ()
[//]: # (<details>)

[//]: # (<summary>Maven</summary>)

[//]: # ()
[//]: # (```xml)

[//]: # (<dependencies>)

[//]: # (    <!-- Other dependencies -->)

[//]: # (    <dependency>)

[//]: # (        <groupId>io.github.julwas797.jtimebomb</groupId>)

[//]: # (        <artifactId>jtimebomb-annotations</artifactId>)

[//]: # (        <version>latest</version>)

[//]: # (    </dependency>)

[//]: # (</dependencies>)

[//]: # (```)

[//]: # (</details>)

[//]: # ()
[//]: # (<details>)

[//]: # (<summary>Gradle &#40;Groovy&#41;</summary>)

[//]: # ()
[//]: # (```groovy)

[//]: # (dependencies {)

[//]: # (    // Other dependencies)

[//]: # (    implementation 'io.github.julwas797.jtimebomb:jtimebomb-annotations:LATEST')

[//]: # (})

[//]: # (```)

[//]: # (</details>)

[//]: # ()
[//]: # (<details>)

[//]: # (<summary>Gradle &#40;Kotlin&#41;</summary>)

[//]: # ()
[//]: # (```kotlin)

[//]: # (dependencies {)

[//]: # (    // Other dependencies)

[//]: # (    implementation&#40;"io.github.julwas797.jtimebomb:jtimebomb-annotations:LATEST"&#41;)

[//]: # (})

[//]: # (```)

[//]: # ()
[//]: # (</details>)

### Plans

[//]: # (- [ ] **Implement proper testing for the processor**)
- [x] ~~Obtain the date by an NTP server~~ (Users can define a custom `LocalDateTime` provider, and implement it using the `timeMethod` parameter)
- [ ] Make a Gradle plugin
- [ ] Implement other types of time-bombs
- [ ] Incremental time-bombing (calculate time-delta at the time of processing)

### License

This project is licensed under the MIT License. License doesn’t affect distributed JARs generated by the processor.

