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
    a stickman's head, body, both arms, and both legs.
    Game Over occurs when lives hit zero or when all of gameStatus' underscores have
    been replaced with the proper letters.
    */
    
    public static Scanner scan = new Scanner(System.in);
    public static String[] words, guessedLetters, gameStatus;
    public static String wordToGuess, intro;
    public static int lives, streak = 0, highestStreak = 0;
    
    public static void main(String[] args) {
        boolean livesLeft = true;
        init(true);
        clearCMD();
        System.out.println(intro);
        
        while (livesLeft) {
            // Main game loop
            
            // Prints out the hangman ascii art
            printHangmanASCII();
            
            /*
            The following two for loops print out both the underscores/correctly
            guessed letters and the incorrectly guessed letters.
            */
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
            
            if (!livesLeft) {
                printHangmanASCII();

                if (lives <= 0) {
                    System.out.println("Game Over!\nSorry, but you guessed six incorrect letters. You lose.\nThe word was \"" + wordToGuess + "\".");
                    if (streak > 1)
                        System.out.println("This loss ended your winning streak of " + streak + " games won in a row.");
                    highestStreakChecker();
                    streak = 0;
                } else {
                    System.out.println("CONGRATULATIONS! You guessed the word! It was \"" + wordToGuess + "\".");
                    streak++;
                    if (streak > 1)
                        System.out.println("You're on a winning streak! You've won " + streak + " games in a row!");
                    highestStreakChecker();
                }
                
                System.out.println("Would you like to play again? Type [Y] to play again:");
                String playAgain = scan.nextLine().toUpperCase().trim();
                if (playAgain.equals("Y")) {
                    livesLeft = true;
                    init(false);
                    clearCMD();
                    System.out.println(intro);
                } else {
                    String outro = "It was fun playing with you! ";
                    switch (highestStreak) {
                        case 0:
                            if (streak == 1)
                                outro += "You're highest winning streak was 1 game won in row! Goodbye!";
                            else
                                outro += "Goodbye!";
                            break;
                        default:
                            outro += "You're highest winning streak was " + highestStreak + " games won in row! Goodbye!";
                    }
                    System.out.println(outro);
                }
            }
        }
    }
    
    public static void init(boolean b) {
        
        /*
        This initialization method loads a dictionary of words into
        String[] words for random selection later on. The method also
        chooses a random word for the user to begin guessing.
        Boolean b is false when the player has finished a game and has
        chosen to play again, and tells the program to not reload the
        dictionary into String[] words.
        */
        
        if (b) {
            clearCMD();
            words = loadWordsFromFile();
        }
        
        Random r = new Random();
        wordToGuess = words[r.nextInt(words.length)].toUpperCase();
        guessedLetters = new String[1];
        
        gameStatus = new String[wordToGuess.length()];
        for (int i = 0; i < gameStatus.length; i++) {
            gameStatus[i] = "_ ";
        }
        
        lives = 6;
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
        clearCMD();
        System.out.println(intro);
    }

    public static boolean answerCheck() {
        
        /*
        This is a helper method to figure out whether or not the game
        is over. If any of the letters from wordToGuess are still
        undiscovered, this method returns false, otherwise every letter
        has been discovered and this method returns true.
        */
        
        for (int i = 0; i < gameStatus.length; i++) {
            if (gameStatus[i].charAt(0) == '_')
                return false;
        }
        return true;
    }

    public static void printHangmanASCII() {
        
        /*
        ASCII art was used from chrishorton's GitHub post, found at
        https://gist.github.com/chrishorton/8510732aa9a80a03c829b09f12e20d9c
        */
        
        switch (lives) {
            case 0:
                System.out.println("\n  +---+\n"
                                + "  |   |\n"
                                + "  O   |\n"
                                + " /|\\  |\n"
                                + " / \\  |\n"
                                + "      |\n"
                                + "=========");
                break;
            case 1:
                System.out.println("\n  +---+\n"
                                + "  |   |\n"
                                + "  O   |\n"
                                + " /|\\  |\n"
                                + " /    |\n"
                                + "      |\n"
                                + "=========");
                break;
            case 2:
                System.out.println("\n  +---+\n"
                                + "  |   |\n"
                                + "  O   |\n"
                                + " /|\\  |\n"
                                + "      |\n"
                                + "      |\n"
                                + "=========");
                break;
            case 3:
                System.out.println("\n  +---+\n"
                                + "  |   |\n"
                                + "  O   |\n"
                                + " /|   |\n"
                                + "      |\n"
                                + "      |\n"
                                + "=========");
                break;
            case 4:
                System.out.println("\n  +---+\n"
                                + "  |   |\n"
                                + "  O   |\n"
                                + "  |   |\n"
                                + "      |\n"
                                + "      |\n"
                                + "=========");
                break;
            case 5:
                System.out.println("\n  +---+\n"
                                + "  |   |\n"
                                + "  O   |\n"
                                + "      |\n"
                                + "      |\n"
                                + "      |\n"
                                + "=========");
                break;
            case 6:
                System.out.println("\n  +---+\n"
                                + "  |   |\n"
                                + "      |\n"
                                + "      |\n"
                                + "      |\n"
                                + "      |\n"
                                + "=========");
                break;
        }
    }
    
    public static void clearCMD() {
        
        /*
        Simple helper method for clearing the Command Prompt if the game
        is being played there. Currently only works for Windows.
        */
        
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            Logger.getLogger(Hangman.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static void highestStreakChecker() {
        
        /*
        This method checks if the current winning streak is higher than
        the highestStreak AND that the player has a streak to begin with.
        This is to prevent printing "1 games won in a row!" since "games"
        is plural.
        */
        
        if (streak > highestStreak && streak > 1) {
            System.out.println("You hit a new highest streak of " + streak + " games won in a row!");
            highestStreak = streak;
        }
    }
    
}
