
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
                System.out.printf("%s%n%n", rooms.get(currentRoom).d);
                System.out.print("What will you do? ");
                String sen = sc.nextLine();
                String word1 = sen[0];
                
                switch(word1) {
                    case "n": case "s": case "w": case "e": case "u": case "d":
                    case "north": case "south": case "west": case "east": case "up": case "down":
                        moveToRoom(word1.charAt(0));
                        break;
                    case "i": case "inventory":
                        showInventory();
                        break;
                    }

                //String[] command = sc.nextLine().split(" ");

                //currentRoom = r.getExits();
            }
        } 

    static void moveToRoom(char d) {
        String newRoom = rooms.get(currentRoom).getExits(d);
        System.out.println(newRoom);

        if(newRoom.equals("exit")) {
            if(p.keys.size() != 4) {
                System.out.println("You must get all 4 keys to exit this room.");
                newRoom = currentRoom;
            }
        }

        if(newRoom.equals("lockedRoom")){
            
        }

        if(newRoom.equals("")) {
            System.out.println("You hit a wall and pass out.");
            return;
        }
        currentRoom = newRoom;
        rooms.get(newRoom).setVisited();
    }

    static void showInventory() {
        for(Item i : p.inventory) {
            System.out.printf("%s: %s  ", i.n, i.d);
        }
    }

    static void setup() {
        currentRoom = "entrance";
        Room.getRooms(rooms);
    }
}
