import java.util.HashMap;
import java.util.Scanner;

public class MainGame {
    static String currentRoom;
    static HashMap<String, Room> rooms = new HashMap<>();
    static Player p = new Player("");
    static boolean hasTorch = false, hasLever = false, hasMarker = false, hasMat = false, usedLever = false;
    static final Scanner sc = new Scanner(System.in);
            

    public static void main(String[] args) {
            setup();
            System.out.print("What is your name? ");
            p.name = sc.nextLine().trim();
            System.out.printf("Welcome %s.%nIf you type 'commands' you are given a list of commands.", p.name);
            System.out.printf("%s%n%n", makeItalic(rooms.get(currentRoom).d));

            while(!checkEnd()) {
                Room r = rooms.get(currentRoom);
                System.out.print("\nWhat will you do? ");
                String sen = sc.nextLine();
                sen = translateSen(sen);
                String word1 = sen.split(" ")[0];
                
                               
                switch(word1.toLowerCase()) {
                    case "n": case "s": case "w": case "e": case "u": case "d":
                    case "north": case "south": case "west": case "east": case "up": case "down":
                        moveToRoom(word1.charAt(0));
                        break;
                    case "i": case "inventory":
                        showInventory();
                        break;

                    case "commands":
                        showCommands();
                        break;
                    
                    case "pickup":
                        if(!(sen.split(" ").length > 1)) break;

                        for(String s : r.items.keySet()) {
                            
                            // if item is what they input
                            if(s.equals(sen.split(" ")[1])) {

                                //add to inventory, remove from room
                                p.addItem(r.getItem(sen.split(" ")[1]));
                                r.removeItem(sen.split(" ")[1]);
                                
                                // special case items --> needed for task
                                if(s.equals("torch")) {
                                    hasTorch = true;
                                    currentRoom = "hall2";
                                    System.out.println("You are sent back to the hallway");
                                }
                                if(s.equals("lever")) hasLever = true;
                                if(s.equals("marker")) hasMarker = true;
                                if(s.equals("mat")) hasMat = true;
                                break;
                            }
                            
                        }
                        break;
                        
                    case "read":

                        // if readable item is in room
                        if(r.n.equals("puzzle")) {
                            System.out.println(rooms.get("puzzle").getItem("engraving").d);
                        }
                        if(r.n.equals("entrance")) {
                            System.out.println(rooms.get("entrance").getItem("plaque").d);
                        }
                        break;

                    case "search":
                        // if item is marked as hidden, give
                        for(Item i : r.items.values()) {
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
                        if(!(sen.split(" ").length > 1)) break;
                        String word2 = sen.split(" ")[1];

                        // if they try to use keys on doors --> unlock if in right room
                        if(word2.equals("redkey") && r.n.equals("hall2") && rooms.get("redroom").locked && p.hasItem("redkey")) {
                            rooms.get("redroom").locked = false;
                            System.out.println(makeItalic("You open the locked room."));
                            System.out.println(makeItalic("You are back in the hallway."));
                        }

                        else if(word2.equals("greenkey") && r.n.equals("hall2") && rooms.get("greenroom").locked && p.hasItem("greenkey")) {
                            rooms.get("greenroom").locked = false;
                            System.out.println(makeItalic("You open the green room, you see nothing but darkness inside."));
                            System.out.println(makeItalic("You are back in the hallway."));

                        }

                        else if(word2.equals("blackkey") && r.n.equals("entrance") && rooms.get("blackroom").locked && p.hasItem("blackkey")) {
                            rooms.get("blackroom").locked = false;
                            System.out.println(makeItalic("You open the black room."));
                            System.out.println(makeItalic("You are back at the entrance."));

                        }

                        // if they try to use lever to play last game
                        else if(word2.equals("lever") && r.n.equals("greenroom") && hasLever && p.hasItem("lever")) {
                            System.out.println(makeItalic("You use the lever and bring the mechanism to life, revealing to you the gameshow like surroundings."));
                            System.out.printf("%s%n%n", makeItalic(rooms.get(currentRoom).d));
                            usedLever = true;
                            Item l = p.getItem("lever");
                            rooms.get("greenroom").items.put(l.n, l);
                            p.inventory.remove(l.n);
                            
                        }
                        else System.out.println("You cannot use that here.");
                        
                        break;

                    case "play":
                        checkGame();
                        break;

                    default:
                        System.out.println("I dont understand");

                    
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
            System.out.println(makeItalic("You hit a wall and pass out."));
            p.removeLife();
            return;
        }

        //find new room
        String newRoom = rooms.get(currentRoom).getExits(d);        

        //if you try to enter a locked room
        if(newRoom.equals("redroom")){
            // if locked(dont have key1) print message and return
            if (rooms.get("redroom").locked) {
                System.out.println("This exit is locked. Use the red key to enter.\n");
                return;
            }
        }

        //if they enter the locked room
        if(newRoom.equals("blackroom")) {

            //check locked
            if(rooms.get("blackroom").locked) {
                System.out.println("This exit is locked. Use the black key to enter.\n");
                return;
            }
        }

        // if they try to enter final room
        if(newRoom.equals("greenroom")){

            // check locked
            if (rooms.get("greenroom").locked) {
                System.out.println("This exit is locked. Use the green key to enter.\n");
                return;
            }

            //if the room is lit up
            if(hasTorch && !usedLever){
                System.out.println(makeItalic("The torch lights up the room. There is a broken mechanism on the wall. Maybe you can find an item to fix it."));
            }
            else if (!hasTorch) {
                System.out.println(makeItalic("You are in a dark room, perhpas you could find something to light it up."));
                newRoom = "hall2";
            }
        }


        // memory game doors
        if(newRoom.equals("wrong1") || newRoom.equals("wrong2") || newRoom.equals("wrong3")){
            System.out.println(rooms.get(newRoom).d);
            p.removeLife();
            System.out.println("You are at the beginning of the loop again. ");
            newRoom = "twoDoorsRoom"; 
        }

        //die if enter death room
        if(rooms.get(newRoom).n.equals("death")) {
            System.out.println(makeItalic(rooms.get(newRoom).d) + "\nYou are now sent back the the previous room.");
            p.removeLife();
            return;
        }

        // if they try to exit, check if they have all four keys
        if(newRoom.equals("exit")) {
            if(!(p.hasItem("redkey") && p.hasItem("blackkey") && p.hasItem("greenkey") && p.hasItem("bluekey"))) {
                System.out.println("You must have all 4 keys to leave.");
                return;
            }
        }


        // if key is gotten from room 
        if(rooms.get(newRoom).locked) {
            System.out.println("This room is no longer accessable.");
            return;
        }

        
        //change room
        currentRoom = newRoom;
        if(!currentRoom.equals("greenroom")) System.out.printf("%s%n", makeItalic(rooms.get(currentRoom).d));
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
                return;
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

            // replace off answerss
            answer = answer.replace("towels", "towel");
            answer = answer.replace("mushrooms", "mushroom");

            // if answer corrosponds with correct answer, move on
            if(answer.equals(ans[i])) {
                i++;
                System.out.println("Good job! Heres your next question: ");
            }
            
            // add to wrong --> every multiple 3 wrong they lose a life
            else {
                wrong++;
                if(wrong %3 == 0 && wrong != 0) {
                    p.removeLife();
                    System.out.printf("WRONG! You now have %d lives.%n", p.lives);
                }
                else System.out.println("Wrong!");
            }
        }
        System.out.println("Congrats, you answered all three riddles. Here is your reward.");

        // give reward marker - remove from room
        p.addItem(rooms.get(currentRoom).getItem("marker"));
        rooms.get(currentRoom).removeItem("marker");

      
        
        // set has marker, go back to hall1, lock room
        currentRoom = "hall1";
        System.out.println("You are back in the hallway.");
        hasMarker = true;
        rooms.get(currentRoom).locked = true;

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
            System.out.println("Heres your next question: ");
            System.out.println(qs[i]);
            String answer = sc.nextLine().toLowerCase();

            // replace off answers
            answer = answer.replaceAll("prune", "prunes");
            answer = answer.replaceAll("penguin", "penguins");
            answer = answer.replaceAll("4", "four");

            // if correct answer add to right
            if(answer.equals(ans[i])) {
                right++;
                System.out.printf("Good Job! That's %d right!!%n", right);
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
     * play story game in keyroom 2
     */
    static void playStory() {
        String[] qs = {"503 bricks on a plane and 1 falls off, how many are left?", "How do you put an elephant in a fridge?", "How do you put a giraffe in a fridge?", "The Lion King is having a birthday party, all the animals are there except for one, which one?", "Sally has to get across a large river with lots of alligators, they're dangerous, but she gets across safely, how?", "But Sally dies anyway, why?"};
            String[] ans = {"Did you guess 502? That's right!", "You open the door, put the elephant in, and close it.", "Don't forget you need to open the door, take the elephant out first, put the giraffe in, and close the door!", "The giraffe of course.", "No gators are in the waters, they're at the party!", "She gets hit by the falling brick."};
            
            // game
            for (int i = 0; i < qs.length; i++) {

                // prompt question and give answer
                System.out.println(makeItalic(qs[i]));
                sc.nextLine();
                System.out.println(makeItalic(ans[i]) + "\n");
            }

            // add reward for playing
            System.out.println("Thank you for playing along. Here is your reward: ");
            p.addItem(rooms.get(currentRoom).getItem("blackkey"));
            rooms.get(currentRoom).removeItem("blackkey");

            // lock and return to previous room
            rooms.get(currentRoom).locked = true;
            currentRoom = "redroom";
            System.out.print(makeItalic("You are sent back to the previous room.\n"));
    }

    /**
     * check if game needs to be played based on room.
     */
    static void checkGame() {
        
        // PUT KEYS IN KEY ARRAY
        //if theyre in room and used lever 
        if(currentRoom.equals("greenroom") && usedLever) {
            playTrivia();
        }

        // if they play a the riddles
        else if(currentRoom.equals("puzzle")) {
            // start the game
            playRiddles();
            
        }

        // if they enter the hangman room play hangman
        else if(currentRoom.equals("blackroom")) {
            
            // if they already won
            if(Hangman.guessed) {
                System.out.println("You already won this game.");
                return;
            }

            // if they have the marker to play
            if(hasMarker) {
                playHangman();
            }

            else System.out.println("Perhaps you could use a marker to draw on it.");
        }


        else if(currentRoom.equals("keyroom2")) {
            playStory();
            
        }

        else System.out.println("There is no game to play here.");
    }

    /**
     * show contents of player inventory
     */
    static void showInventory() {
        // print thier inventory

        if(p.inventory.size() == 0){
            System.out.println("Inventory Empty");
            return;
        }
        for(Item i : p.inventory.values()) {
            System.out.printf("%s: %s %n", i.n, i.d);
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

    /**
     * make strings italic for printing
     * @param s string to make italic
     * @return string surrounded with italic code
     */
    static String makeItalic(String s) {
        return "\033[3m" + s + "\033[0m";
    }

    static void showCommands() {
        System.out.print("\nUSE itemName - uses item\nPICKUP itemName - pickup an item\nREAD - reads info/instruction on an item"
                        +"\nSEARCH - searches a room for hidden items\nINVENTORY - prints your inventory contents\nN/E/S/W - direction commands\nPLAY - starts game\n");
    }

    /**
     * check if the game ended by death or exiting
     * @return returns boolean breaking while loop if true
     */
    static boolean checkEnd() {
        boolean b = false;
        if(p.lives <= 0) {
            System.out.println("YOU ARE OUT OF LIVES!");
            b =true;
        }

        if(rooms.get(currentRoom).n.equals("exit")) {
            System.out.printf("CONGRADULATIONS YOU WON THE GAME!!! %nYou ended with %d lives. %nYou ended %s the mat.", p.lives, hasMat ? "with" : "without");
            b = true;
        }
        return b;
    }
}