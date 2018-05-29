package  com.jeremy.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class MyPlugin implements Plugin<Project> {

    void apply(Project project) {
        System.out.println("========================");
        System.out.println("hello my first gradle plugin!");
        System.out.println("========================");
        //在Demo（引用工程）中可以执行 ./gradlew MyPluginTask
        project.task('MyPluginTask') << {
            println "This is a gradle task defined in MyGradlePlugin"
        }

        project.extensions.create('myArgs', MyGradlePluginExtension)
        project.myArgs.extensions.create('nestArgs', MyNestPluginExtension)
        project.task('myCustomTask', type: MyCustomTask)

    }
}

class MyGradlePluginExtension {
    def message = "From MyGradlePluginExtention"
    def sender = "MyGradlePlugin"
}

class MyNestPluginExtension {
    def receiver = "Jenny Shi"
    def email = "jennyshi@qq.com"
}
