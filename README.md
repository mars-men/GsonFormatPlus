GsonFormatPlus

这是一个根据JSONObject格式的字符串,自动生成实体类参数.**本插件只适用 Intellij IDEA 和 Android Studio 工具**，
基于 GsonFormat 二次定制化开发的idea json生成 Bean插件，
which is more flexible and convenient.

#### 待实现特性
- 支持实现特定接口类，继承父类功能
- 支持自定义属性类型，如java.math.BigDecimal
- 支持实体类序列化，mock生成对象
- 更多特性希望大家提交issues反馈

[English Readme](README_EN.md)

#### 1.安装方法
- 运行编译生成 GsonFormatPlus.zip 或者 直接在Idea的 Plugins -&gt; Marketplace 搜索安装
- IntelliJ IDEA File-&gt; Settings.. -&gt; Plugins -&gt; install plugin from disk..导入下载的 GsonFormatPlus.zip
- 重启 IntelliJ IDEA .

#### 2.打开方法

1.使用Generate的快捷键.  
快捷键:图中选中的部分

![gsonformatinsertpng](https://raw.githubusercontent.com/sun-men/Figurebed/master/2020/03/12-11-12-47-gsonformat-insert.png)

2.使用 GsonFommat 的快捷键.  
默认 option + s (mac), alt + s (win)  
通过以下方式修改快捷键:

![gsonformatkeymappng](https://raw.githubusercontent.com/sun-men/Figurebed/master/2020/03/12-11-13-43-gsonformat-keymap.png)

#### 3.使用截图

生成DTO

![gsonformatgeneratorgif](https://raw.githubusercontent.com/sun-men/Figurebed/master/2020/03/12-11-18-54-gsonformat-generator.gif)

生成注释

![screen](https://user-images.githubusercontent.com/1943377/101614565-1471f100-3a48-11eb-891b-e7da00766423.gif)

#### 4.设置说明

![gsonformatsettingpng](https://raw.githubusercontent.com/sun-men/Figurebed/master/2020/03/12-11-18-09-gsonformat-setting.png)

字段说明

| 模块  | 设置  | 是否默认 | 说明  |
| --- | --- | --- | --- |
| Convert Method | object/arrayFromData | 否   | Gson自定义生成对象 |
| Generate | virgo mode | 是   | virgo模式，生成代码之前可自定义调整字段 |
| Generate | generate comments | 否   | 是否生成注释 |
| Generate | split generate | 否   | 是否单独生成子类 |
| Bean | reuse bean | 否   | TODO |
| Field | name suffix | 是   | 生成类名后缀 |
| Field | field(private/public) | 是   | 字段私有/公开 |
| Field | name prefix | 是   | 生成字段名前缀 |
| Field | use serialized name | 是   | 使用序列化名，类字段为驼峰与添加json注解声明 |
| Field | use wrapper class | 是   | 使用包装类，int 转 Integer |
| Field | use lombok | 是   | 使用Lombok替代Getter和Setter |
| Field | use number key as map | 是   | 使用数字类型key替换为Map结构，待完善 |
| Convert library | jackson/fastjson | 是   | jackson/fastjson等转换注解 |

#### 5.版本信息

future features

- 提取静态内部类到根类
- 支持数字key映射Map字段

版本1.5.2 by wangzejun

- 支持字段注释
- 支持json5格式

版本1.5.1 by sun-men

- 设置默认为jackson注解
- 支持选用lombok注解
- 代码在新版本idea重构

版本1.5.0 - 3 years ago

- fix several bug
- fix unlock setting window size

版本1.4.0

- New: Support for autovalue
- New: Support for lombok
- New: Support for split generate class

版本1.2.2

- 支持 field 类型的修改.
- 支持快捷键打开 GsonFormat ,默认为 option+s(mac), alt+s(win)
- 支持 field 名称的修改.
- 支持添加 field 前缀.
- 支持多种转换库 (Gson,Jackjson,FastJson,LoganSquare).
- 支持 private 和 public 两种模式.
- 支持过滤父类已有属性.

the end
