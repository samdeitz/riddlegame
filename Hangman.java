
import java.util.Scanner;
import java.util.ArrayList;

public class Hangman {
    static boolean guessed = false;

    static void showWord(String[] arr) {
        System.out.println();
        for(String s : arr) {
            System.out.print(s + " ");
        }
    }

    static void showGuessedLetters(ArrayList<String> arr) {
        System.out.println();
        for(String l : arr) {
            System.out.print(l + " ");
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String answer ="hello";
        String[] hiddenWord = new String[answer.length()];
        for(int i = 0 ; i < hiddenWord.length; i++) {
            hiddenWord[i] = "_";
        }
        ArrayList<String> letters = new ArrayList<>();

        while(!guessed) {
            
            showWord(hiddenWord);
            showGuessedLetters(letters);
            System.out.print("\nWhat is your guess? ");
            String guess;
            
            guess = sc.next();
            if(guess.length() > 1) continue;
            if(letters.contains(guess)) {
                System.out.println("You already guessed this.");
                continue;
            }
            letters.add(guess);
            

            if(answer.contains(guess)) {
                for(int i = 0; i < answer.length(); i++) {
                    if(String.valueOf(answer.charAt(i)).equals(guess)) {
                        hiddenWord[i] = guess;
                    }
                }
            }
        }


    }
}