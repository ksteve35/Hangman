import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hangman {
    
    /*
    scan is a Scanner that reads user input once the game has started.
    words is a String[] dictionary containing 307,104 English words.
    guessedLetters is a String[] that tracks what letters the player has guessed.
    gameStatus is a series of underscores (_) that displays how close the user is to victory.
    wordToGuess is the randomly selected String from words that the user plays with.
    lives is how many errors the player is allowed to make, which is six to simulate
    a stickman's head, body, both arms, and both legs. Game Over occurs when lives hit zero.
    */
    
    public static Scanner scan = new Scanner(System.in);
    public static String[] words, guessedLetters, gameStatus;
    public static String wordToGuess, intro;
    public static int lives = 6;
    
    public static void main(String[] args) {
        boolean livesLeft = true;
        init();
        System.out.println(intro);
        
        while (livesLeft) {
            // Main game loop
            
            System.out.println("\n\n");
            for (int i = 0; i < gameStatus.length; i++) {
                System.out.print(gameStatus[i]);
            }
            System.out.println("\nIncorrectly guessed letters:");
            for (int i = 0; i < guessedLetters.length; i++) {
                if (guessedLetters[i] != null && !wordToGuess.contains(guessedLetters[i]))
                    System.out.print(guessedLetters[i] + "\t");
            }
            
            guessALetter();
            boolean allLettersGuessed = answerCheck();
            if (lives <= 0 || allLettersGuessed)
                livesLeft = false;
        }
        
        if (lives <= 0) {
            System.out.println("Game Over!\nSorry, but you guessed six incorrect letters. You lose.\nThe word was \"" + wordToGuess + "\".");
        } else {
            System.out.println("CONGRATULATIONS! You guessed the word! It was \"" + wordToGuess + "\".");
        }
    }
    
    public static void init() {
        /*
        This initialization method loads a dictionary of words into
        String[] words for random selection later on. The method also
        chooses a random word for the user to begin guessing.
        */
        
        words = loadWordsFromFile();
        // Debugging print statement
        //System.out.println("\n\n\nWord count:\t" + words.length + "\nFirst word:\t" + words[0] + "\nLast word:\t" + words[words.length - 1]);
        
        Random r = new Random();
        wordToGuess = words[r.nextInt(words.length)].toUpperCase();
        guessedLetters = new String[1];
        
        gameStatus = new String[wordToGuess.length()];
        for (int i = 0; i < gameStatus.length; i++) {
            gameStatus[i] = "_ ";
        }
        
        intro = "Welcome to Hangman! The rules are simple:\n"
                + "Below are blank lines that represent letters\n"
                + "in a word. Your goal is to guess the letters\n"
                + "that make up this word before you lose. You\n"
                + "can guess up to six (6) incorrect letters\n"
                + "before you cause a Game Over and lose the game.\n"
                + "Incorrectly guessed letters will appear on the\n"
                + "screen for reference, and correctly guessed\n"
                + "letters will replace the blank lines in the\n"
                + "mysterious word you're guessing. Good luck and\n"
                + "have fun!\n\n";
        
    }
    
    public static String[] loadWordsFromFile() {
        
        /*
        loadWordsFromFile is a method that uses a Scanner to read through
        the text file "words.txt" and loads each individual word in a String[]
        called retVal (short for returnValue), which gets returned in the init
        method to overwrite the global String[] words.
        */
        
        String[] retVal = new String[1];
        String fileName = "words.txt";
        Scanner fileText;
        int counter = 370104;
        
        System.out.println("Attempting to load " + fileName + " from the directory...");
        
        try {
            fileText = new Scanner(new File(System.getProperty("user.dir") + "\\" + fileName));
            retVal = new String[counter];
            
            for (int i = 0; i < counter; i++) {
                if (fileText.hasNextLine()) {
                    retVal[i] = new String(fileText.nextLine());
                }
            }
            
            System.out.println("\"" + fileName + "\" has been loaded.");
            fileText.close();
            
        } catch (Exception e) {
            System.out.println("Unable to load the file named \"" + fileName
                    + "\".\nPerhaps the file doesn\'t exist, or you added the file extension?");
            Logger.getLogger(Hangman.class.getName()).log(Level.SEVERE, null, e);
        }
        
        System.out.println("\n\n");
        return retVal;
    }
    
    public static void guessALetter() {
        /*
        Infinite loop method that only ends when the player inputs a single
        letter character as their guess that hasn't already been guessed.
        */
        
        while (true) {
            System.out.println("\n\nPlease guess a letter, then press [ENTER]:\t");
            String s = scan.nextLine().trim().toUpperCase();
            boolean exists = Arrays.asList(guessedLetters).contains(s);
            if (s.length() == 1 && s.matches("[A-Z]+") && !exists) {
                
                char c = s.charAt(0);
                boolean inWordToGuess = wordToGuess.contains(s);
                
                if (inWordToGuess) {
                    int[] indices = new int[wordToGuess.length()];
                    for (int i = 0; i < indices.length; i++) {
                        if (wordToGuess.charAt(i) == c) {
                            gameStatus[i] = c + " ";
                        }
                    }
                } else {
                    // If wordToGuess does NOT contain the guessed character,
                    // the player will lose a life.
                    lives--;
                }
                
                if (guessedLetters[0] != null) {
                    String[] alreadyGuessedLetters = new String[guessedLetters.length + 1];
                
                    /*
                    For loop copies String[] guessedLetters into a new String[]
                    alreadyGuessedLetters and adds the new guess to the new String[].
                    Then, alreadyGuessedLetters is copied as the new String[]
                    guessedLetters.
                    */

                    for (int i = 0; i < guessedLetters.length; i++) {
                        alreadyGuessedLetters[i] = new String(guessedLetters[i]);
                        if (i + 1 == guessedLetters.length)
                            alreadyGuessedLetters[i + 1] = new String(s);
                    }
                    guessedLetters = alreadyGuessedLetters.clone();

                    break;
                } else {
                    guessedLetters[0] = s;
                    break;
                }
                
            } else if (s.length() != 1) {
                System.out.println("Guess cannot be shorter or longer than a single character.");
            } else if (!s.matches("[A-Z]+")) {
                System.out.println("Guess must be a letter.");
            } else if (exists) {
                System.out.println("You have already guessed the letter '" + s + "'");
            } else {
                System.out.println("Something went wrong with your guess...");
            }
            
        }
    }

    public static boolean answerCheck() {
        for (int i = 0; i < gameStatus.length; i++) {
            if (gameStatus[i].charAt(0) == '_')
                return false;
        }
        return true;
    }
    
}