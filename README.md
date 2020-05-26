# touk-cinema

This is my solution to a TouK's recruitment assignment.

## Building, running, and testing

To build and run the program (assuming you have `sbt` installed):

```shell script
sbt run
```

To execute the test scenario (assuming you have `curl`):

```shell script
./end2end-test.sh
```

If you also have `jq` installed, this version of the test scenario script
pretty-prints responses:

```shell script
./end2end-test-jq.sh
```

The console output from running the test is
[included](end2end-test-transcript.txt).

I did not supply Docker files on the assumption that both `sbt` and `curl` work
consistently across platforms, but I can add Docker support if needed.

## Architecture

The project's organization is reflected in the directory tree:

1. Business logic is under the `domain` directory.
2. Functionality of serving requests, abstracted from the specific application
   layer technology, is under the `application` directory.
3. Integration layer with peripheral technologies is under the `infrastructure`
   directory.
   
There are three kinds of data structures which correspond to domain objects like
screenings and reservations:

* Entities, which represent those objects for the purpose of business logic.
* Models, which represent objects for the purpose of database storage.
* Payloads, which represent objects for the purpose of serialization and
  transfer.
  
The application code is implemented with `cats-effect`. It is organized in terms
of:

* Services, which handle requests related to a specific resource.
* Repositories, which abstract a set of instances of a resource.
* Validators, which validate request data.
* Providers, which provide other kinds of functionality.

HTTP is supported with `akka-http`. The route structure is described with
Routes, and endpoints are defined in Controllers.

The database is implemented as an in-memory database. This is because the only
Scala framework for working with databases I used is Slick, which I recommend
against ([my issues](https://github.com/slick/slick/issues/2085) on Github were
acknowledged but are still open). I didn't want to learn a new framework for
the purpose of this project. My in-memory implementation has some limitations
(it does not support concurrent requests, it doesn't check operations for
consistency, it is not optimized), but it suffices for this project.

I tried to write idiomatic functional Scala and only occasionally departed from
production grade standards (omitted Scaladocs for classes, used literals in
code, used Option.get) to complete the project within reasonable time. 

This version of the project uses a fixed clock and a constant secret generators
to allow for having deterministic tests.

A production version of this code would have more extensive Scaladocs across
the code.