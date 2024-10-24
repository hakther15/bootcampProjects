# Module One final project

This application is a catalog of items that application users can lend to each other. The design builds off the previous project where you made an application that imports data on books and provides the ability to search on that data. Instead of reading the data from a hard-coded array, you'll read the data from text files. The dataset now also includes movies and tools. The data is in the `resources` folder in multiple files.

The [Requirements](#requirements) section later in this document describes the data format.

## Starting code

Begin by opening the project in IntelliJ and reviewing the starting code.

The starting code provides a menu-based user interface that handles user input, displays data, and calls to methods in the application.

The Module One final project contains these files:

* `App.java` - The *main* class of the application
* `model/CatalogItem.java` - An interface to implement specific methods on catalog items
* `util/FileStorageService.java` - A File I/O class for you to put your I/O logic in and use in your code
* `util/exception/FileStorageException.java` - A custom exception for the `FileStorageService` class

### `App.java`

The `App` class contains two primary methods:

* `initialize()` - This method is empty to start. You need to write `initialize()` so that it loads the application data. The [Requirement: Data transformation](#requirement-data-transformation) section describes this in greater detail.
* `run()` - This method contains the loop responsible for displaying the application menus and search results.

The `main()` method of the `App` class calls these methods when the application starts.

The `App` class also contains other methods that comprise the application UI. The method names are descriptive of their purpose. Feel free to browse this class. **These methods are complete, and you shouldn't modify them.**

### `model/CatalogItem.java`

As part of the requirements for this project, you'll define classes that implement the `CatalogItem` interface. The interface defines the methods that these classes must implement. Although you shouldn't change the existing method names, parameters, and return types, you can add constants or a `throws` clause to the methods.

### `util/FileStorageService.java`

You can use this class to define your File I/O code. There are two empty methods with recommended parameters, return types, and a `throws` clause. Feel free to change the code provided, but you shouldn't have to.

## Data files

You can find the data files for this application in the `src/main/resources/` directory.

There are two types of files:

* `members.dat` - Read this file first to determine the application users and the filenames for each user's items.
* `items-*.dat` - These files describe the items a particular user has available.


## Requirements

You must complete several requirements for class design, file I/O, and data transformation.

### Requirement: Class design

Create the following four classes in the `model` folder/package. Each class has a list of members and methods that it must implement:

* `Member`
    * `String firstName`
    * `String lastName`
    * `String toString()`

* `Book`
    * `String id`
    * `String title`
    * `String author`
    * `LocalDate publishDate`
    * `CatalogItem` interface methods
    * `String toString()`

* `Movie`
    * `String id`
    * `String name`
    * `String director`
    * `LocalDate releaseDate`
    * `CatalogItem` interface methods
    * `String toString()`

* `Tool`
    * `String id`
    * `String type`
    * `String manufacturer`
    * `int count`
    * `CatalogItem` interface methods
    * `String toString()`

Each class must have the appropriate constructor that uses the properties from the data files. The `id` field doesn't exist in the data file, so don't pass that into the constructor. See `registerItem()` in the following [Interface methods](#interface-methods) section for more information.

The `toString()` method for `Member` must be first name and last name separated by a space. You can use any format for the item `toString()` methods, but it must display all four fields. Remember that the UI uses the `toString()` method to display items in the UI, so readability is a concern. Here's an example format that you can use in your code or as inspiration:

```java
"* " + title + System.lineSeparator()
+ " - Written by: " + author + System.lineSeparator()
+ " - Published: " + publishDate + System.lineSeparator()
+ " - Id: " + id;
```

#### Implementation of interface methods

The `CatalogItem` interface defines four methods that the `Book`, `Movie`, and `Tool` classes must implement:

* `matchesName(String searchStr)`
    * Return a boolean indicating if the "name" field contains the search string `searchStr`. The match must be case-insensitive.
    * The "name" field for the classes are:
        * `Book.title`
        * `Movie.name`
        * `Tool.type`

* `matchesCreator(String searchStr)`
    * Return a boolean indicating if the "creator" field contains the search string `searchStr`. The match must be case-insensitive.
    * The "creator" fields for the classes are:
        * `Book.author`
        * `Movie.director`
        * `Tool.manufacturer`

* `matchesYear(int searchStr)`
    * Return a boolean indicating if the "date" field is in the year specified in the parameter `searchYear`.
    * The "date" field for the classes are:
        * `Book.publishDate`
        * `Movie.releaseDate`
        * `Tool` has no year field, so this method must only return `false`.

* `registerItem()`
    * This method assigns a unique ID to the `id` field. Use the code `UUID.randomUUID().toString()` to generate a **universally unique identifier**—or UUID—and assign it to the `id` field.
        * Note: this is a common technique for creating a unique ID in code. You might also hear this called a *globally unique identifier* or GUID.
    * Write a *created* message to a log file after creating an item. A good log entry includes the date, time, and object's properties. Consider separate log files, one for each type. You can write your log files to `src/main/resources/logs` in the project folder.
        * Hint: use the `toString()` method to print the properties to the log.
        * See the following [File I/O](#file-io) section for more information.

### Requirement: File I/O

The `FileStorageService` class provides two `static` methods for you to use without instantiating a `FileStorageService` object. To use them, call `FileStorageService.writeContentsToFile(...)` and `FileStorageService.readContentsOfFile(...)` with the appropriate parameters.

You must implement the appropriate logic to read and write from files and handle any issues that may arise.

### Requirement: Data transformation

The `initialize()` method in the `App` class is for setting up the data for the application. You can create other methods to assist with processing the data, but everything must occur from the call to `initialize()` and before the call to `run()`.

You'll store the data you import from the `items-*.dat` files in the collection `Map<String, List<CatalogItem>> catalog` defined near the top of the `App` class:
* The key must be the member's first and last name.
    * HINT: use `Member.toString()` to fill in the key name.
* The value is a `List` of `CatalogItem` objects owned by that member.

The `members.dat` file contains three fields separated by a pipe character—`|`. The fields are in the order of:

1) First name
2) Last name
3) Name of the file that contains their items

Example contents of `members.dat`:

```
Edward|Kenney|items-ekenney.dat
Elizabeth|McCreadie|items-emccreadie.dat
Tonya|Fishbie|items-tfishbie.dat
```

The corresponding `items-*` files have four fields separated by a pipe character—`|`. The fields are in the order of:

1) Item type—such as book, movie, or tool
2) The book title, movie name, or tool type
3) The book author, movie director, or tool manufacturer
4) The book publish date, movie release date, or number of tools that member has for lending

Example contents of an `items` file:

```
movie|Rogue One|Gareth Edwards|2016-12-16
movie|The Hitchhiker's Guide to the Galaxy|Garth Jennings|2005-04-28
book|Oh, the Places You'll Go!|Dr. Seuss|1990-01-22
tool|hammer|Craftsman|1
```

>Recommendation: Use the provided `FIELD_DELIMITER` constant when splitting.

Items may appear in any order in the file—for example, you might find a book listed between two movies. The fields are always in the same order.

Your code for processing the items must make sure to handle the following:

* Empty lines
* Lines that have too many or too few fields
* An item type that isn't "book", "movie", or "tool"

As you process each item, you must:

1) Instantiate it
2) Call its `registerItem()` method
3) Add it to the collection

### Unit tests - Challenge

Write unit tests for the `matchesName()`, `matchesCreator()`, and `matchesYear()` for `Book`, `Movie`, and `Tool`. Consider what conditions you need to test for in each class.

## Running the project

The project provides a basic menu interface to retrieve and print data. Option 1 allows you to print the entire catalog or just a particular user's catalog items. Option 2 has the search functions of the application. You can search by name, creator, and year.
