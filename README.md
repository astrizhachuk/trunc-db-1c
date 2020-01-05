# Описание

Утилита для генерации SQL-скрипта очистки таблиц (TRUNCATE) базы данных 1С при клиент-серверном варианте установки.

На вход утилите поставляется либо файл, либо ответ веб-сервиса в формате `json` с описанием маппинга объектов 1С и базы данных, на выходе - sql-скрипт в выбранном формате вывода.

По умолчанию очищаются все таблицы документов и регистры накопления (в т.ч. вспомогательные таблицы). Дополнительно можно расширить набор таблиц для очистки, передав их через файл-конфигурации в формате `json`.

## Требуемое программное окружение

* java (не ниже 1.8)

## Запуск

Запуск в командной строке: `java -jar path/to/file.jar`

``` sh
usage: Truncate DB tables for 1C [-b <BASE>] [-c <CONFIG>] [-f <FILE>]
       [-h] [-o <FILE>] [-s <URL>] [-v]
 -b,--base <BASE>       base name of the 1c application
 -c,--config <CONFIG>   path to configuration file
 -f,--file <FILE>       path to metadata file
 -h,--help              print this message
 -o,--output <FILE>     path to output file, default ./output.sql
 -s,--s <URL>           url to metadata service, like
                        http://example.com:8080/metadata
 -v,--version           print the version of the application
```

### Примеры запуска

#### Очистка таблиц по данным файла с выводом в консосль

``` sh
java -jar с:/temp/trunc-db-1c-0.1.jar -f с:/files/input.json
```

#### Очистка таблиц по данным файла с выводом в файл

``` sh
java -jar с:/temp/trunc-db-1c-0.1.jar -f с:/files/input.json -o с:/files/output.sql
```

#### Очистка таблиц по данным файла c расширенными настройками

``` sh
java -jar с:/temp/trunc-db-1c-0.1.jar -f с:/files/input.json -c -o с:/files/config.json 
```

#### Очистка таблиц по данным веб-сервиса с выводом в файл

``` sh
java -jar с:/temp/trunc-db-1c-0.1.jar -s http://example.com:8080/metadata -b dev-user-1c-acc
```

### Форматы файлов

#### Файл с описанием маппинга

``` json
[
  {
    "tableName": "Справочник.АдресныеСокращения",
    "storageTableName": "_Reference56"
  },
  {
    "tableName": "Справочник.АдресныеСокращения.Изменения",
    "storageTableName": "_ReferenceChngR728"
  }
]
```

#### Файл с описанием настройки очистки таблиц

``` json
{
  "prototypes": [
    "Документ",
    "РегистрНакопления"
  ],
  "metaDataObjects": [
    "Справочник.Банки",
    "Документ.РеестрВнутреннихПеремещений",
    "Справочник.ВидыДеятельностиКонтрагентов",
    "РегистрНакопления.ОстатиНаСкладах"
  ]
}
```

> **prototypes** - очистка таблиц для всех объектов прототипа (менеджера)
> **metaDataObjects** - очистка конкретного объекта