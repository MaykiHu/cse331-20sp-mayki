package setup;

import java.util.Random;

/** RandomHello selects and prints a random greeting. */
public class RandomHello {

    /**
     * Prints a random greeting to the console.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        RandomHello randomHello = new RandomHello();
        System.out.println(randomHello.getGreeting());
    }

    /** @return a greeting, randomly chosen from five possibilities */
    public String getGreeting() {
        String[] greetings = new String[5];
        greetings[0] = (new HelloWorld()).getGreeting();
        greetings[1] = (new HolaWorld()).getGreeting();
        greetings[2] = "Bonjour Monde";
        greetings[3] = "Hallo Welt";
        greetings[4] = "Ciao Mondo";
        Random randomGenerator = new Random();
        return greetings[randomGenerator.nextInt(5)];
    }
}