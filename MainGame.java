
import java.util.HashMap;
import java.util.Scanner;

public class MainGame {
    static String currentRoom;
    static HashMap<String, Room> rooms = new HashMap<>();
    static boolean gameOver = false;
    static Player p = new Player("");
    static boolean hasTorch = false, hasLever = false, hasMarker = false, hasMat = false;
    static final Scanner sc = new Scanner(System.in);
            

    public static void main(String[] args) {
            setup();
            System.out.print("What is your name? ");
            p.name = sc.nextLine();
            System.out.printf("Welcome %s.%n", p.name);
            System.out.printf("%s%n%n", rooms.get(currentRoom).d);

            while(!gameOver) {
                Room r = rooms.get(currentRoom);
                checkGame();
                System.out.print("What will you do? ");
                String sen = sc.nextLine();
                sen = translateSen(sen);
                String word1 = sen.split(" ")[0];
                
                switch(word1) {
                    case "n": case "s": case "w": case "e": case "u": case "d":
                    case "north": case "south": case "west": case "east": case "up": case "down":
                        moveToRoom(word1.charAt(0));
                        break;
                    case "i": case "inventory":
                        showInventory();
                        break;
                    
                    case "pickup":
                        for(Item i : r.items) {
                            
                            // if item is what they input
                            if(i.n.equals(sen.split(" ")[1])) {

                                //add to inventory, remove from room
                                p.addItem(r.getItem(sen.split(" ")[1]));
                                r.removeItem(sen.split(" ")[1]);
                                
                                // special case items --> needed for task
                                if(i.n.equals("torch")) {
                                    hasTorch = true;
                                    currentRoom = "hall2";
                                    System.out.println(rooms.get(currentRoom).d);
                                }
                                if(i.n.equals("lever")) hasLever = true;
                                if(i.n.equals("marker")) hasMarker = true;
                                
                                if(i.n.equals("mat")) hasMat = true;
                                break;
                            }
                            
                        }
                        
                    case "read":

                        // if readable item is in room
                        for(Item i : r.items) {
                            if(i.n.equals(sen.split(" ")[1])) System.out.println(i.d);
                            else System.out.println("I don't understanc");
                        }
                        break;

                    case "search":
                        // if item is marked as hidden, give
                        for(Item i : r.items) {
                            if(i.hidden) {
                                p.addItem(i);
                                r.removeItem(i.n);
                                break;
                            }
                        }
                        break;

                    // case default:
                    //     System.out.println("I dont understand");
                    
                    case "use":
                        String word2 = sen.split(" ")[1];
                        Room reqRoom = rooms.get(currentRoom);

                        // if they try to use keys on doors
                        if(word2.equals("redkey") && reqRoom.n.equals("hall2")) {
                            rooms.get("redroom").locked = false;
                            System.out.println("You open the locked room, you find there are two directions, east and south.");
                        }

                        if(word2.equals("greenkey") && reqRoom.n.equals("hall2")) {
                            rooms.get("greenroom").locked = false;
                            System.out.println("You open the green room, you see nothing but darkness inside.");
                        }

                        if(word2.equals("blackkey") && reqRoom.n.equals("entrance")) {
                            rooms.get("blackroom").locked = false;
                            System.out.println("You open the black room, and find yourself in a game?");
                        }

                        // if they try to use lever to play last game
                        if(word2.equals("lever") && reqRoom.n.equals("greenroom")) {
                            System.out.println("You use the lever and bring the mechanism to life, revealing to you the gameshow like surroundings.");
                            playTrivia();
                        }

                    
                }
                //String[] command = sc.nextLine().split(" ");
                //currentRoom = r.getExits();
            
            }
        } 
    
    /**
     * translates words so input is correct
     * @param s word to be translated
     */
    static String translateSen(String s) {

        // make uppercase and replace
        s = s.toUpperCase();
        s = s.replaceAll("PICK UP", "PICKUP");
        s = s.replaceAll("OPEN", "USE");
        s = s.replaceAll("UNLOCK", "USE");
        s = s.replaceAll("LOOK", "SEARCH");
        s = s.replaceAll("GREEN KEY", "GREENKEY");
        s = s.replaceAll("RED KEY", "REDKEY");
        s = s.replaceAll("BLACK KEY", "BLACKKEY");
        return s.toLowerCase();
    }

    /**
     * move to room based on direction (n,e,s,w)
     * @param d direction to go
     */
    static void moveToRoom(char d) {

        // they go an impossible direciton
        if(rooms.get(currentRoom).getExits(d).equals("")) {
            System.out.println("You hit a wall and pass out.");
            p.removeLife();
            return;
        }

        //find new room
        String newRoom = rooms.get(currentRoom).getExits(d);
        System.out.println(newRoom);

                

        //if you try to enter a locked room
        if(newRoom.equals("redroom")){
            // if locked(dont have key1) print message and return
            if (rooms.get("redroom").locked) {
                System.out.println("This exit is locked. You need the red key to enter.\n");
                return;
            }
        }

        //if they enter the locked room
        if(newRoom.equals("blackroom")) {

            //check locked
            if(rooms.get("blackroom").locked) {
                System.out.println("This exit is locked. You need the black key to enter.\n");
                return;
            }
        }

        // if they try to enter final room
        if(newRoom.equals("greenroom")){

            // check locked
            if (rooms.get("greenroom").locked) {
                System.out.println("This exit is locked. You need the green key to enter.\n");
                return;
            }

            //if the room is lit up
            if(hasTorch){
                System.out.println("You use the torch to light up the room. There is a broken mechanism on the wall. Maybe you can find an item to fix it.");
            }
            else {
                System.out.println("You are in a dark room, perhpas you could find something to light it up.");
                newRoom = "hall2";
            }
        }


        // memory game doors
        if(newRoom.equals("wrong1") || newRoom.equals("wrong2") || newRoom.equals("wrong3")){
            System.out.println(rooms.get(newRoom).d);
            p.removeLife();
            newRoom = "twoDoorsRoom"; 
        }

        //die if enter death room
        if(rooms.get(newRoom).n.equals("death")) {
            System.out.println(rooms.get(newRoom).d + ". \nYou are now sent back the the previous room.");
            p.removeLife();
            return;
        }

        // if they try to exit, check if they have all four keys
        if(newRoom.equals("exit")) {
            if(p.keys.size() != 4) {
                System.out.println("You must get all 4 keys to exit this room.");
                return;
            }
            else {
                System.out.printf("CONGRADULATIONS YOU WON THE GAME!!! %nYou ended with %d lives. %nYou ended %s the mat.", p.lives, hasMat ? "with" : "without");
                System.exit(0);
            }
        }
        
        //change room
        currentRoom = newRoom;
        rooms.get(currentRoom).setVisited();
        System.out.printf("%s%n%n", rooms.get(currentRoom).d);
    }


    /**
     * initilize hangman and check if they guess the word
     */
    static void playHangman() {

        // play hangman until they win
        while(true) {
            new Hangman();

            // if they guessed the word
            if(Hangman.guessed) {

                //add item to inventory
                p.addItem(rooms.get(currentRoom).getItem("greenkey"));
                rooms.get(currentRoom).removeItem("greenkey");
                System.out.println("You won the green key!");
                break;
            }
            else p.removeLife();
        }
    }

    /**
     * play the riddles game
     */
    static void playRiddles() {

        int wrong = 0;
        String[] riddles = {"Your first riddle: What kind of room has no doors or windows?", "Good, you passed the first riddle. Was that hard? Here is the second riddle: What gets wet while drying?",
                            "Too easy. Ready for the hardest one? The final riddle: What food is so funny that it can be a comedian?"};
        String[] ans = {"mushroom", "towel", "crackers"};

        // until they get all riddles right
        int i = 0;
        while(i < riddles.length) {

            //print riddle get answer
            System.out.print(riddles[i]);
            String answer = sc.nextLine().toLowerCase();

            // if answer corrosponds with correct answer, move on
            if(answer.equals(ans[i])) {
                i++;
                System.out.println("Good job! Heres your next question: ");
            }
            
            // add to wrong --> every multiple 3 wrong they lose a life
            else {
                wrong++;
                if(wrong %3 == 0 && wrong != 0) System.out.printf("WRONG! You now have %d lives.%n", p.lives);
                else System.out.println("Wrong!");
            }
        }
    }

    /**
     * play the trivia game
     */
    static void playTrivia(){
        String[] qs = {"What does \"HTTP\" stand for?", "Who discovered penicillin?", "What is the name of Batman's butler?", "What is the common name for dried plums?", "In which country Adolph Hitler was born?", "What genre of music did Taylor Swift start in?", "What's the name of the paradise warriors go to after death?", "Bill Gates is the founder of which company?", "The video game “Happy Feet” features what animals?","If there are six apples and you take away four, how many do you have?"};
        String[] ans = {"hypertext transfer protocol", "alexander fleming", "alfred", "prunes", "austria", "country", "valhalla", "microsoft", "penguins", "four"};
        int right = 0;

        // loop through each trivia --> need to get at least half right
        for(int i = 0; i < qs.length; i++) {
             
            // give question get answer
            System.out.println(qs[i]);
            String answer = sc.nextLine().toLowerCase();

            // if correct answer add to right
            if(answer.equals(ans[i])) {
                right++;
                System.out.printf("Good Job! That's %d right!!%nHeres your next question: %n", right);
            }
            else System.out.println("WRONG! Better luck next time.");
        }

        // if they got 5 or more right they get the key
        if(right >= qs.length/2) {
            System.out.println("You Win! You get the final blue key!");
            p.addItem(rooms.get(currentRoom).getItem("bluekey"));
            rooms.get(currentRoom).removeItem("bluekey");
        }
    }

    /**
     * check if game needs to be played based on room.
     */
    static void checkGame() {

        // if they play a the riddles
        if(currentRoom.equals("puzzle")) {
            // start the game
            playRiddles();

            System.out.println("Congrats, you answered all three riddles. Here is your reward.");

            // give reward marker - remove from room
            p.addItem(rooms.get(currentRoom).getItem("marker"));
            rooms.get(currentRoom).removeItem("marker");
            
            // set has marker, go back to hall1
            hasMarker = true;
            
            //TODO i luv him more(she doesnt)(she very much does not!!!!!!!! <3)(I VEEEERRRRYYYYY MUCCCHHHH DDDDDDDDDDOOOOOOOOO.)(NOEP)
        }

        // if they enter the hangman room play hangman
        if(currentRoom.equals("blackroom")) {
            if(hasMarker) {
                playHangman();
            }
            else {
                System.out.println("Perhaps you could use a marker to draw on it.");
            }
        }
    }

    /**
     * show contents of player inventory
     */
    static void showInventory() {
        // print thier inventory
        for(Item i : p.inventory) {
            System.out.printf("%s: %s  ", i.n, i.d);
        }
    }

    /**
     * setup game --> start at entrance, initialize rooms
     */
    static void setup() {
        // start
        currentRoom = "entrance";
        Room.getRooms(rooms);
    }
}