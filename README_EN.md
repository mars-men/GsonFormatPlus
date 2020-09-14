GsonFormatPlus

This is a plugin you can generate Json model from Json String.
**This Plugin is only for IntelliJ IDEA**.
which is base on [GsonFormat](https://github.com/zzz40500/GsonFormat) 
and more flexible and convenient. Welcome to issue and PR.

## Install
- Using IDE built-in plugin system on Windows:
  - <kbd>File</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "GsonFormatPlus"</kbd> > <kbd>Install Plugin</kbd>
- Using IDE built-in plugin system on MacOs:
  - <kbd>Preferences</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "GsonFormatPlus"</kbd> > <kbd>Install Plugin</kbd>
- Manually:
  - Download the [latest release](https://github.com/mars-men/GsonFormatPlus/releases) and install it manually using <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd>
  - From official jetbrains store from [download](https://plugins.jetbrains.com/plugin/14949-gsonformatplus/)
  

Restart IDE.

## Usage
### 1.Use IDE menu

![Generate.png](https://raw.githubusercontent.com/sun-men/Figurebed/master/2020/03/12-11-12-47-gsonformat-insert.png)

### 2.Use hotkey

Default **Option + s**(Mac), **Alt + s** (win)

You can change the hotkey via: 

![修改快捷键.png](https://raw.githubusercontent.com/sun-men/Figurebed/master/2020/03/12-11-13-43-gsonformat-keymap.png)

### 3.Use Demo

![gsonformatgeneratorgif](https://raw.githubusercontent.com/sun-men/Figurebed/master/2020/03/12-11-18-54-gsonformat-generator.gif)

### 4.Setting

![gsonformatsettingpng](https://raw.githubusercontent.com/sun-men/Figurebed/master/2020/03/12-11-18-09-gsonformat-setting.png)




SETTING

| module  | value  | default | description  |
| --- | --- | --- | --- |
| Convert Method | object/arrayFromData | false   | Gson convert method |
| Generate | virgo mode | true   | virgo mode |
| Generate | generate comments | false   | generate comments |
| Generate | split generate | false   | split generate sub class |
| Bean | reuse bean | false   | TODO |
| Field | name suffix | true   | class suffix |
| Field | field(private/public) | true   | access level |
| Field | name prefix | true   | field name prefix |
| Field | use serialized name | true  | use serialized name  |
| Field | use wrapper class | true  | use wrapper class，eq: int convert Integer |
| Field | use lombok | true  | use lombok replace Getter/Setter |
| Field | use number key as map | true | use number key as map，TODO |
| Convert library | jackson/fastjson | true | jackson/fastjson convert library |



## Version Info

1.5.5 by mars-men

- upload to jetbrain plugin center

1.5.2 by wangzejun

- support field comment
- support json5 format

1.5.1 by mars-men

- setting jackson annotation as default
- support lombok as option
- fix some bug

1.5.0 - 3 years ago by gsonformat

- fix several bug
- fix unlock setting window size

1.4.0

- New: Support for autovalue
- New: Support for lombok
- New: Support for split generate class

the end