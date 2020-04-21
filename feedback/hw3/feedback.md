### Code quality score: 3/3

### Mechanics: 3/3

#### General Feedback
Overall good code quality! Good work!
#### Specific Feedback
When selecting a greeting in RandomHello, the best style would use the length
of the array to specify the maximum value for the random integer generation:
String nextGreeting = greetings[rand.nextInt(greetings.length)];
Notice how this benefits us later on if we wanted to change the number of
possible greetings in the array.

Box.java:
In your add() method, you can simplify your conditional statements by combining your if-statements.
A better way to write ball comparison is to use Double.compare.  Check
the Java documentation of these methods to see how you might use them.