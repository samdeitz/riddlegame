
import java.util.HashMap;
import java.util.Scanner;

public class MainGame {
    static String currentRoom;
    static HashMap<String, Room> rooms = new HashMap<>();
    static boolean gameOver = false;
    static String name;
    static Player p = new Player(name);

    public static void main(String[] args) {
            setup();
            Scanner sc = new Scanner(System.in);
            System.out.print("What is your name? ");
            name = sc.nextLine();
            System.out.printf("Welcome %s.%n", p.name);
            while(!gameOver) {
                Room r = rooms.get(currentRoom);
                System.out.printf("%s%n%n", r.d);
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
                                System.out.printf("Picked up item %s.", i.n);
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
                    
                    // case "use":
                    //     String word2 = sen.split(" ")[1];
                    //     if(word2.equals("key")) {
                    //         for(Item i : p.keys) {

                    //         }
                    //     }

                    case default:
                        System.out.println("I dont understand.");
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
        return s.toLowerCase();
    }

    static void moveToRoom(char d) {

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


        //if you try to enter a locked room
        if(newRoom.equals("lockedRoom")){
            boolean locked = true;

            // find key1 in key list
            for(Item key : p.keys) {

                // unlock
                if(key.n.equals("key1")) {
                    locked = false;
                }
            }

            // if still locked(dont have key1) print message and return
            if (locked) {
                System.out.println("You cannot go any further you do not have the right key to enter this locked room.");
                return;
            }
        }

        
        if(newRoom.equals("keyroom4")){
            boolean dark = true;
            boolean haslever = false;

            for(Item item : p.inventory){
                if(item.n.equals("torch")){
                    dark = false;
                }
                
                if(item.n.equals("lever")){
                    haslever = true;
                }
            }
            if(!dark && !haslever){
                System.out.println("You use the torch to light up the room. There is a broken mechanism on the wall. Maybe you can find an item to fix it.");
            }
            if(!dark && haslever){
                System.out.println("");
            }
        }
        

        // they go an impossible direciton
        if(newRoom.equals("")) {
            System.out.println("You hit a wall and pass out.");
            return;
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
