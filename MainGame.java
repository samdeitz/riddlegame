
import java.util.HashMap;
import java.util.Scanner;

public class MainGame {
    static String currentRoom;
    static HashMap<String, Room> rooms = new HashMap<>();
    static boolean gameOver = false;
    static Player p = new Player("");
    static boolean hasTorch = false, hasLever = false, hasMarker = false;

    public static void main(String[] args) {
            setup();
            Scanner sc = new Scanner(System.in);
            System.out.print("What is your name? ");
            p.name = sc.nextLine();
            System.out.printf("Welcome %s.%n", p.name);


            while(!gameOver) {
                Room r = rooms.get(currentRoom);
                if(!r.locked) System.out.printf("%s%n%n", r.d);
                else System.out.printf("Welcome to the %s.%n%n", r.n);
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
                                System.out.printf("Found item %s!%n", i.n);
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

        if(rooms.get(newRoom).locked) {
            System.out.println(rooms.get(newRoom).d);
            return;
        }

        // if they try to exit, check if they have all four keys
        if(newRoom.equals("exit")) {
            if(p.keys.size() != 4) {
                System.out.println("You must get all 4 keys to exit this room.");
                return;
            }
        }

        if(newRoom.equals("puzzle")) {

            //riddle code


            // if solve all riddle back to old room
            return;
            //i luv him more(she doesnt)

        }
        

        //if you try to enter a locked room
        if(newRoom.equals("redroom")){
            // if locked(dont have key1) print message and return
            if (rooms.get("redroom").locked) {
                System.out.println(rooms.get("blackroom").d);
                return;
            }
        }
        if(newRoom.equals("blackroom")) {
            if(rooms.get("blackroom").locked) {
                System.out.println(rooms.get("blackroom").d);
                return;
            }
            if(hasMarker) System.out.println("You enter a room, you see a whiteboard with a game of hangman setup across from you, perhaps it could help you get the green key.");
            else {
                System.out.println("You see a whiteboard, perhaps you could use a marker to draw on it.");
            }
        }

        // if they try to enter last room
        if(newRoom.equals("greenroom")){
            if (rooms.get("green").locked) {
                System.out.println(rooms.get("blackroom").d);
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
