### Design: 2/3

### Documentation & Specification (including JavaDoc): 3/3

### Code quality (code and internal comments including RI/AF): 1/3

### Testing (test suite quality & implementation): 3/3

### Mechanics: 3/3

#### Overall Feedback

-1: Your list nodes should return a set of nodes instead of a string representation of the nodes, because returning a string would require clients to process the string in order to get information about the nodes. same for the listChildren. 

-1: You should not make your Node implement a Comparable interface. While it is true that you will need to compare nodes, doing it in graph itself is not necessarily the best design, and Comparable is not the only way to define comparison on a datatype. 

-1: Your edge class should override equals and hashcode methods. Any elements of Collection type objects should probably be overriding equals and hashCode. 



#### More Details

None.

