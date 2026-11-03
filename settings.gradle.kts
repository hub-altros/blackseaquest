rootProject.name = "BlackSeaQuest"

// Включаем все модули проекта
include(":androidApp")
include(":shared")
include(":webApp")
//include(":desktopApp")
// include(":iosApp") // Раскомментируйте, если у вас есть iOS-модуль

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        // Зеркало для стабильной загрузки зависимостей (особенно полезно в РФ)
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
    }
}