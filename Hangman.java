import java.util.Scanner;
import java.util.ArrayList;

public class Hangman {
    static boolean guessed = false;
    
    /**
     * show what the person has guessed
     * @param arr the array containing the hidden word
     */
    void showWord(String[] arr) {
        System.out.println();
        // print the array of guessed
        for(String s : arr) {
            System.out.print(s + " ");
        }
    }


    /**
     * show all letters that have been guessed
     * @param arr the array of guessed letters
     */
    void showGuessedLetters(ArrayList<String> arr) {
        System.out.println();
        // print each letter
        for(String l : arr) {
            System.out.print(l + " ");
        }
    }


    /**
     * check if they guessed the word
     * @param a answer
     * @param g guesses
     */
    void checkGuessedWord(String a, String[] g) {
        // make the array of correct guesses a string
        String s = "";
        for(int i = 0; i < g.length; i++) {
            s += g[i];
        }
        // if they are equal --> win
        if(a.equals(s)) guessed = true;
    }

    Hangman() {
        Scanner sc = new Scanner(System.in);
        int guesses = 12;
        String answer ="words";

        // create hidden word array
        String[] hiddenWord = new String[answer.length()];
        for(int i = 0 ; i < hiddenWord.length; i++) {
            hiddenWord[i] = "_";
        }
        ArrayList<String> letters = new ArrayList<>();


        while(!guessed) {

            System.out.printf("%nGuesses: %d%n", guesses);
            showWord(hiddenWord);
            showGuessedLetters(letters);

            // if they have 0 lives, break
            if(guesses <= 0) {
                System.out.printf("%nYOU LOSE!%n");
                break;
            }
            

            // take guess
            System.out.print("\nWhat is your guess? ");

            String guess = sc.next();


            // if they guess with one letter 
            if(guess.equals(answer)) {
                guessed = true;
                for(int i = 0; i < hiddenWord.length; i++) {
                    hiddenWord[i] = String.valueOf(guess.charAt(i));
                }
            }
            if(!guess.matches("[A-Za-z]{1}")) continue;

            // if they guessed before
            if(letters.contains(guess)) {
                System.out.println("You already guessed this.");
                continue;
            }
            letters.add(guess);
            
            // if the guess is in the answer, find index of the letter
            if(answer.contains(guess)) {
                for(int i = 0; i < answer.length(); i++) {

                    // if index of letter, put in array at that index
                    if(String.valueOf(answer.charAt(i)).equals(guess)) {
                        hiddenWord[i] = guess.toLowerCase();
                    }
                }
            }
            guesses--;  
            checkGuessedWord(answer, hiddenWord);
        }
        showWord(hiddenWord);

        if(guessed) {
            System.out.println("\nYOU WIN!");
            
        }
    }
}
