import java.io.File;
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
    public static String wordToGuess;
    public static int lives = 6;
    
    public static void main(String[] args) {
        init();
    }
    
    public static void init() {
        /*
        This initialization method loads a dictionary of words into
        String[] words for random selection later on. The method also
        chooses a random word for the user to begin guessing.
        */
        
        words = loadWordsFromFile();
        //System.out.println("\n\n\nWord count:\t" + words.length + "\nFirst word:\t" + words[0] + "\nLast word:\t" + words[words.length - 1]);
        
        Random r = new Random();
        wordToGuess = words[r.nextInt(words.length)];
        
        gameStatus = new String[wordToGuess.length()];
        for (int i = 0; i < gameStatus.length; i++) {
            gameStatus[i] = "_ ";
        }
        
        
        
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
    
}