# Perfect Hash Table Generator

This is an assignment I completed in my *Introduction to Data Structures in Object Oriented Java Programming* 
computer science course at Iowa State University during my 2018 Spring Semester.

---

## Project Overview

For this assignment, we were instructed to implement a perfect hash table construction algorithm. The algorithm generates a pair of tables, 
each containing random numbers. Keys are hashed with the values in each of these tables, giving
two integers, one for each table. These integers are then taken as node indices in a graph, with an edge between
them. So as long as the resultant graph, after hashing all of the keys—contains no cycles, the algorithm
is ready to generate the hash table as a function of the tables used in the graph generation and
a function g() on those tables.

---

## Classes and Interfaces

There are 6 classes in this project:
* `PerfectHashGenerator.java` - *A class that generates a perfect hash table*
* `CodeGenerator.java` - *A class that generates Java code for the perfect hash table implementation*
* `Graph.java` - *A graph used to generate a perfect hash table*
* `Visitable.java` – *An interface representing an entity that can be marked as visited or unvisited*
* `Vertex.java` – *An interface representing a vertex in the Perfect Hash Table graph*
* `Graph.java` – *An interface representing an edge in the Perfect Hash Table graph*

*Note: I will add a javadoc that includes more information about all of the methods and interfaces*

#### `PerfectHashGenerator.java`

This class contains the `main()` method which runs the entire program. The `main()` method takes *one* required and *two* optional
arguments. 
* The required argument contains the keys, one per line.
* The optional arguments to `main()` are a prefix and a seed. By default, the program will generate a 
class named `CHM92Hash` in a file named `CHM92Hash.java`. This makes it impossible to use multiple of 
these hash tables in the same program (without manually editing the files). 
  * The prefix makes the manual 
  edits unnecessary; a prefix of `foo` results in the class `fooCHM92Hash` and file `fooCHM92Hash.java`. 
  * The seed parameter allows you to get reproducible behavior from the random number generator by forcing a 
  particular seed *(useful for debugging)*. You cannot specify a seed without using a prefix.

The two `generate()` methods build graphs and run the code generator. 

`readWordFile()` reads the input. 
`mapping()` handles all of the algorithm steps through cycle testing.  

*Note: if the file contains any repeated keys, the resultant graph will always have a cycle, 
so the program will enter an infinite loop*.

#### `CodeGenerator.java`

Given two tables (`T1` and `T2`), a gArray (`g()`), and a modulus, a java file is written that implements the hash table. The code 
generator also takes the key list in order to write it to the output source file, but this isn’t strictly necessary
for the hash table to function. The methods which write code to the output file, the `begin()`, `array()`, `table()`, 
and `end()` methods, were already implemented for us by the instructors.

---

## Testing and Running the Program
To run the program as a whole, run the main method in the `PerfectHashGenerator.java` class. 
To test if the program is working properly, navigate to your output file (`*your_prefix*CHM92Hash.java`), uncomment the
`main()` method, compile, and run it. This should not produce any output. If it produces output, there is an error in the code.
In addition, try modifying one or more of the keys in a generated `KEY_LIST` array, recompile, and run it again. The code should
print output regarding hashing errors, one for each key changed.

---

*Disclaimer: I do not take credit for any of the code that was already given to us by the instructors. Everything
that I was assigned to implement is marked with a `TODO` comment inside each of the methods.
