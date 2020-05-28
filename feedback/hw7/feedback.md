### Design: 1/3

Because we have applications for the graph ADT that do not deal with comparable
data, do not declare `Comparator`s for type parameters of graph. Instead, use
`Comparable` data types or define a `Comparator` where appropriate
at the application of the graph. Note: comparing `toString`s is not a very
viable comparison method either.

Your path-finding method would be better suited to be static. The constructor
does the same work that a static method could.

`ModelAPI` should not be declared as generic.

The `ModelAPI` should not expose any graph specific implementation details,
since ideally our graph implementation should be able to be swapped out for some
other backend, like a table of shortest paths, or a `Google Maps` client.

It is not good design for `CampusMap` to extend some sort of algorithm class.
Rather, it should use the class as a utility. We only want to extend when it is
a true subtype.

### Documentation & Specification (including JavaDoc): 2/3

Generic type variables should be documented in the class/interface comment.

### Code quality (code and internal comments including RI/AF when appropriate): 2/3

CampusMap is actually an ADT because it represents an abstract map. Therefore,
it should include a representation invariant, abstraction function, and
`checkRep`.

Your `initializeData` returns a direct reference to the same graph that you are
storing in the object. The client should not be able to modify the `CampusMap`.

### Testing (test suite quality & implementation): 2/3

Your test suite is lacking in coverage.  Here's a few ideas for interesting test
cases:
- Cyclic graphs with path finding.
- Paths where the difference between BFS and Dijkstra's algorithm is apparent.
- Graphs with multiple edges of varying costs between the same two nodes.

### Mechanics: 3/3

#### Overall Feedback

Review good design choices, especially when it comes to extending only when
something is a true subtype and when it comes to what should be generic and
what should be classified as an ADT.

#### More Details

None.
