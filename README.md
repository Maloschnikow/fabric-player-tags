> [!IMPORTANT]
> This mod utilizes the Minecraft functionality of teams.
> If your server already relies on Minecraft's built-in team feature, it is not recommended to use this mod.

### Usage
`/playertag <player> <set|remove> [tag] <color>`

### Problems
- Pets adapt the team of their owner, if they aren't explicitly assigned to another team. This results in named pets also having the decorated tag of their owner.

### Development
#### Build and deploy

1. `./gradlew build`
2. Copy the generated `.jar` file from `build/libs` to the server's `mods` folder

#### [Update gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html#sec:upgrading_wrapper)
`./gradlew wrapper --gradle-version latest`
