# UTIL

**Version:** 1.0

**Author:** SpinAndDrain

**Description:** API for modifying and handling Arrays, Strings and mathematical processes (and much more).

### Useful classes to start

* [Array](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/util/arrays/Array.java)
  	* [IntArray](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/util/arrays/IntArray.java)
    * [StringArray](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/util/arrays/StringArray.java)
* [AdvancedString](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/util/advanced/AdvancedString.java)
* [MathUtils](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/util/advanced/MathUtils.java)
* [Memorizer](https://github.com/SpinAndDrain/LibsCollection/blob/master/src/de/spinanddrain/util/holding/Memorizer.java)

### The Use

The following are just a few examples. There is much more what you can do!

Handle Strings:

````java
String string = "SpaaaaaaAaaaAaaAaaaaAaaaAaaaaAaaAAaaaaam";

AdvancedString astr = Utils.expand(string);

astr.removeSuperfluousChars();

System.out.println(astr);

// Output: Spam
````

````java
String[] list = {"This", "is", "a", "list"};

String binded = AdvancedString.bind(", ", list);

System.out.println(binded);

// Output: This, is, a, list
````

````java
String message = "Hello World!";

StringArray array = ArrayUtils.splitAndModify(message);

array.reverse();

message = ArrayUtils.recreate(array.toArray());

System.out.println(message);

// Output: !dlroW olleH
````

````java
String message = "Hello World!";

StringArray array = ArrayUtils.splitAndModify(message);

array.eliminate(e -> Utils.expand(e).containsAny("aeiou"));

message = ArrayUtils.recreate(array.toArray());

System.out.println(message);

// Output: Hll Wrld!
````

Handle Math:

````java
int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

// Cast the array to a long[], because the 'getHighestOf' method only
// takes long[] and double[] as argument
int highest = (int) MathUtils.getHighestOf(ArrayCaster.convertToLong(numbers));

System.out.println(highest);

// Output: 10
````

````java
System.out.println(MathUtils.isEven(14));

// Output: true
````

````java
// This returns a random number between '4' and '14'
int randomNumber = MathUtils.random(14, 4);

System.out.println(randomNumber);

// Output (in my case): 9
````

````java
int[] arr = {15, 2, 9, 0, -1};

int sum = MathUtils.sum(arr);

System.out.println(sum);

// Output: 25
````

Handle Arrays:

````java
int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		
IntArray array = ArrayUtils.modify(arr);

array.keep(e -> MathUtils.isEven(e));

System.out.println(ArrayUtils.toString(array));

// Output: [0, 2, 4, 6, 8, 10]
````

````java
int[] arr = {2, 1, 8, 0, 7, 5, 10, 6, 3, 9, 4};

IntArray array = ArrayUtils.modify(arr);

array.sort();

System.out.println(ArrayUtils.toString(array));

// Output: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
````

````java
int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

IntArray array = ArrayUtils.modify(arr);

array.shuffle();

System.out.println(ArrayUtils.toString(array));

// Output (in my case): [2, 6, 1, 0, 7, 5, 10, 3, 4, 8, 9]
````

````java
int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

IntArray array = ArrayUtils.modify(arr);

array.unshift(-1);
array.add(11);

System.out.println(ArrayUtils.toString(array));

// Output: [-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
````

````java
int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

IntArray array = ArrayUtils.modify(arr);

array.pop(2);
array.shift(2);

System.out.println(ArrayUtils.toString(array));

// Output: [2, 3, 4, 5, 6, 7, 8]
````

````java
int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

IntArray array = ArrayUtils.modify(arr);

array.forEach(e -> {
	System.out.println(e);
});

/*
 * Output:
 * 0
 * 1
 * 2
 * 3
 * 4
 * 5
 * 6
 * 7
 * 8
 * 9
 * 10
 */
````
