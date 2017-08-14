---
layout: post
title:  "Kotlin 中的 findViewById"
date:   2017-08-14 10:37:55 +0800

---

# Kotlin 中的 findViewById

## In Java
本文会记录下在Kotlin 中逐步优化绑定view 的过程，以熟悉Kotlin中的一些特性。

绑定view 在Android开发中，经常要用到。Java中这样实现：

``` Java
public class MainActivity extends Activity {

    private TextView tvHello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        tvHello = (TextView) findViewById(R.id.hello);
    }
}
```

对应的 layout.xml ：

``` xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >

    <TextView
        android:id="@+id/hello"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello Kotlin!"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</android.support.constraint.ConstraintLayout>



```


findViewById 是很枯燥的，即使有些  [butterknife](http://jakewharton.github.io/butterknife/) 可以简化工作，但是发现在kotlin 中可以完美的解决这个问题。


## In Kotlin

接下来将逐步的进行优化，首先先看以Java 的习惯，我们会怎么写:

``` Java
class MainActivity : Activity() {
    private var tvHello: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        tvHello = findViewById(R.id.hello) as TextView
    }
}
```

看起来和Java差不多。那我们开始第一步：

### Keyword lateinit

我们定义的 `tvHello` 默认是 null, 如果我们希望它可以延迟初始化，可以加上 `lateinit`

``` Java
class MainActivity : Activity() {
    private lateinit var tvHello: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        tvHello = findViewById(R.id.hello) as TextView
    }
}
```

### Extension

接下来我准备写一个bind方法，这里将用到 [extension](https://kotlinlang.org/docs/reference/extensions.html) 这个功能，来给Activity扩展。

你可以新建一个Kotlin文件，用来放 bind 方法:

``` Java

fun <T : View> Activity.bind(@IdRes res : Int) : T {
    @Suppress("UNCHECKED_CAST")
    return findViewById(res) as T
}
```

再看 Activity 中：

``` Java
class MainActivity : Activity() {
    private lateinit var tvHello: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        tvHello = bind(R.id.hello)
    }
}
```

[extension](https://kotlinlang.org/docs/reference/extensions.html) 这个功能，可以在其他文件中给一个class 扩展方法和属性。

### Lazy

还有改进的空间，我们希望 view 在赋值后，不在改变。但是 lateinit 不能和 val 一起使用。

所以来使用 `Lazy<T>` ，很简单。修改后

``` Java
fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return lazy { findViewById(res) as T }
}
```

在Activity 中只要这样写

``` Java
class MainActivity : Activity() {
    private val tvHello2 :TextView by bind(R.id.hello)
}

```


## 终极 Kotlin Android Extensions

其实上面的过程只是以简化 findViewById 为例，体现Kotlin 中的一些特性。如果回到问题本身，还有最终的方法，体现Kotlin对程序员的爱。

就是`Kotlin Android Extensions`, 是Kotlin Team写的一个插件。

来看疗效：

``` Java
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        hello.text = "AWESOME!"
    }
}
```

只要在 import 部分加上

    kotlinx.android.synthetic.main.activity_main.*

就可以了，hello 就是你layout的 TextView，直接拿来用。。。完了。

### 如何设置

在modlue的gradle文件里加上：

    apply plugin: 'kotlin-android-extensions'

如果找不到,检查下 project 的 gradle 文件，是否有 `maven.google.com` ，应该是这样的

    repositories {
        jcenter()
        maven { url 'https://maven.google.com' }
    }


## 引用&链接
[Improving findViewById with Kotlin](https://medium.com/@quiro91/improving-findviewbyid-with-kotlin-4cf2f8f779bb)

[Kick starting Android Development with Kotlin — Goodbye findViewById](https://android.jlelse.eu/kickstarting-android-development-with-kotlin-goodbye-findviewbyid-6df19e02f378)

[Kotlin Reference](https://kotlinlang.org/docs/reference)