---
layout: post
title:  "Android Multi Flavors"
date:   2017-08-04 10:37:55 +0800

---
## 介绍
依靠gradle flavors。在构建apk时候，选择生成不同样式的apk。

可以实现的功能：

- 基于一套代码，生产多种样式apk。
- 临时版本，如某节日，某活动特定。
- 功能分支，如给某渠道特定功能。支持后续迭代，且不污染主分支。


## Who To Use
### 1. Flavor Dimensions


### 2. Flavors


### 3. Source Set (源集)

## More
Android Studio 按逻辑关系将每个模块的源代码和资源分组为源集。模块的 main/ 源集包括其所有构建变体共用的代码和资源。其他源集目录为可选项，在您配置新的构建变体时，Android Studio 不会自动为您创建这些目录。不过，创建类似于 main/ 的源集有助于让 Gradle 只应在构建特定应用版本时使用的文件和资源井然有序：

    src/main/

    此源集包括所有构建变体共用的代码和资源。

    src/<buildType>/

    创建此源集可加入特定构建类型专用的代码和资源。

    src/<productFlavor>/

    创建此源集可加入特定产品风味专用的代码和资源。

    src/<productFlavorBuildType>/

    创建此源集可加入特定构建变体专用的代码和资源。

例如，要生成应用的“完整调试”版本，构建系统需要合并来自以下源集的代码、设置和资源：

    src/fullDebug/（构建变体源集）
    src/debug/（构建类型源集）
    src/full/（产品风味源集）
    src/main/（主源集）

如果不同源集包含同一文件的不同版本，Gradle 将按以下优先顺序决定使用哪一个文件（左侧源集替换右侧源集的文件和设置）：

    构建变体 > 构建类型 > 产品风味 > 主源集 > 库依赖项






## 相关链接
[配置构建](https://developer.android.com/studio/build/index.html)


[Migrate to Android Plugin for Gradle 3.0.0](https://developer.android.com/studio/build/gradle-plugin-3-0-0-migration.html?utm_source=android-studio#variant_aware)

![image](https://developer.android.com/images/tools/studio/project-structure_2x.png)

