
import java.util.HashMap;
import java.util.Scanner;

public class MainGame {
    static String currentRoom;
    static HashMap<String, Room> rooms = new HashMap<>();
    static boolean gameOver = false;
    static Player p = new Player("");
    static boolean hasTorch = false, hasLever = false, hasMarker = false;
    static final Scanner sc = new Scanner(System.in);
            

    public static void main(String[] args) {
            setup();
            System.out.print("What is your name? ");
            p.name = sc.nextLine();
            System.out.printf("Welcome %s.%n", p.name);


            while(!gameOver) {
                Room r = rooms.get(currentRoom);

                System.out.printf("%s%n%n", r.d);
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
                            if(i.n.equals(sen.split(" ")[1])) {
                                p.addItem(r.getItem(sen.split(" ")[1]));
                                r.removeItem(sen.split(" ")[1]);

                                System.out.printf("Picked up item %s.%n", i.n);
                            }
                            if(sen.split(" ")[1].equals("torch")) {
                                hasTorch = true;
                            }
                            if(sen.split(" ")[1].equals("lever")) {
                                hasLever = true;
                            }
                            if(sen.split(" ")[1].equals("marker")) {
                                hasMarker = true;
                            }
                        }
                        break;
                    case "read":

                        for(Item i : r.items) {
                            if(i.n.equals(sen.split(" ")[1])) System.out.println(i.d);
                        }
                        break;
                    case "search":
                        for(Item i : r.items) {
                            if(i.hidden) {
                                p.addItem(i);
                                r.removeItem(i.n);
                                System.out.printf("Found item %s!%n", i.n);
                                break;
                            }
                        }
                        break;

                    // case default:
                    //     System.out.println("I dont understand");
                    
                    case "use":
                        String word2 = sen.split(" ")[1];
                        Room reqRoom = rooms.get(currentRoom);
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
                        if(word2.equals("lever") && reqRoom.n.equals("greenroom")) {
                            System.out.println("You use the lever and bring the mechanism to life, revealing to you the gameshow like surroundings.");
                        }

                    
                }
                //String[] command = sc.nextLine().split(" ");
                //currentRoom = r.getExits();
            
            }
        } 

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

    static void moveToRoom(char d) {

        // they go an impossible direciton
        if(rooms.get(currentRoom).getExits(d).equals("")) {
            System.out.println("You hit a wall and pass out.");
            p.lives--;
            return;
        }

        //find new room
        String newRoom = rooms.get(currentRoom).getExits(d);
        System.out.println(newRoom);

        

        // if they try to exit, check if they have all four keys
        if(newRoom.equals("exit")) {
            if(p.keys.size() != 4) {
                System.out.println("You must get all 4 keys to exit this room.");
                return;
            }
        }

        // riddle room --> trapped until they get 3 right
        

        //if you try to enter a locked room
        if(newRoom.equals("redroom")){
            // if locked(dont have key1) print message and return
            if (rooms.get("redroom").locked) {
                System.out.println("This exit is locked. You need the red key to enter.\n");
                return;
            }
        }
        if(newRoom.equals("blackroom")) {
            if(rooms.get("blackroom").locked) {
                System.out.println("This exit is locked. You need the black key to enter.\n");
                return;
            }
            
        }

        // if they try to enter last room
        if(newRoom.equals("greenroom")){
            if (rooms.get("green").locked) {
                System.out.println("This exit is locked. You need the green key to enter.\n");
                return;
            }
            if(hasTorch){
                System.out.println("You use the torch to light up the room. There is a broken mechanism on the wall. Maybe you can find an item to fix it.");
            }
            else System.out.println("You are in a dark room, perhpas you could find something to light it up.");
        }

        
        if(rooms.get(newRoom).n.equals("death")) {
            p.lives--;
        }
        

        //change room
        currentRoom = newRoom;
        rooms.get(newRoom).setVisited();
    }

    static void playHangman() {
        while(true) {
            new Hangman();
            if(Hangman.guessed) {
                p.addItem(rooms.get(currentRoom).getItem("greenkey"));
                rooms.get(currentRoom).removeItem("greenkey");
                System.out.println("You won the green key!");
                break;
            }
            else {
                p.lives--;
                System.out.printf("You now have %d lives.", p.lives);
            }
        }
    }

    static void playRiddles() {
        int wrong = 0;
        String[] riddles = {"Your first riddle: What kind of room has no doors or windows?", "Good, you passed the first riddle. Was that hard? Here is the second riddle: What gets wet while drying?",
                            "Too easy. Ready for the hardest one? The final riddle: What food is so funny that it can be a comedian?"};
        String[] ans = {"mushroom", "towel", "crackers"};

        int i = 0;
        while(i < riddles.length) {
            boolean a = false;
            System.out.print(riddles[i]);
            String answer = sc.nextLine().toLowerCase();
            if(answer.equals(ans[i])) a = true;
            if(a) {
                i++;
                System.out.println("Good job! Heres your next question: ");
            }
            else {
                wrong++;
                if(wrong %3 == 0 && wrong != 0) System.out.printf("WRONG! You now have %d lives.%n", p.lives);
                else System.out.println("Wrong!");
               
            }
        }
        

    
    }

    static void playTrivia(){
        String[] qs = {"What does \"HTTP\" stand for?", "Who discovered penicillin?", "What is the name of Batman's butler?", "What is the common name for dried plums?", "In which country Adolph Hitler was born?", "What genre of music did Taylor Swift start in?", "What's the name of the paradise warriors go to after death?", "Bill Gates is the founder of which company?", "The video game “Happy Feet” features what animals?","If there are six apples and you take away four, how many do you have?"};
        String[] ans = {"hypertext transfer protocol", "alexander fleming", "alfred", "prunes", "austria", "country", "valhalla", "microsoft", "penguins", "four"};
        int right = 0;
        for(int i = 0; i < qs.length; i++) {
             
            System.out.println(qs[i]);
            String answer = sc.nextLine().toLowerCase();
            if(answer.equals(ans[i])) {
                right++;
                System.out.printf("Good Job! That's %d right!!%nHeres your next question: %n", right);
            }
            else System.out.println("WRONG! Better luck next time.");
        }
        if(right >= qs.length/2) {
            System.out.println("You Win! You get the final blue key!");
            p.addItem(rooms.get(currentRoom).getItem("bluekey"));
            rooms.get(currentRoom).removeItem("bluekey");
        }
    }

    static void checkGame() {
        if(currentRoom.equals("puzzle")) {
            // start the game
            playRiddles();

            System.out.println("Congrats, you answered all three riddles. Here is your reward.");

            // give reward marker - remove from room
            p.addItem(rooms.get(currentRoom).getItem("marker"));
            rooms.get(currentRoom).removeItem("marker");
            
            // set has marker, go back to hall1
            hasMarker = true;
            
            //i luv him more(she doesnt)(she very much does!!!!!!!! <3)
        }

        if(currentRoom.equals("greenroom")) {
            playTrivia();
        }

        if(currentRoom.equals("blackroom")) {
            if(hasMarker) {
                playHangman();
            }
            else {
                System.out.println("Perhaps you could use a marker to draw on it.");
            }
        }
    }

    static void showInventory() {
        // print thier inventory
        for(Item i : p.inventory) {
            System.out.printf("%s: %s  ", i.n, i.d);
        }
    }

    static void setup() {
        // start
        currentRoom = "entrance";
        Room.getRooms(rooms);
    }
}
