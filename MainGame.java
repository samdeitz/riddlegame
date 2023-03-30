
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainGame {
    static String currentRoom;
    static HashMap<String, Room> rooms = new HashMap<>();
    static ArrayList<Item> inventory = new ArrayList<>();
    public static void main(String[] args) {
        setup();
        Scanner sc = new Scanner(System.in);
        while(true) {
            Room r = rooms.get(currentRoom);
            System.out.print(r.d);
            System.out.print("Which direction would you like to go? ");
            String[] resp = sc.nextLine().split("[ ]+");
            for(String i : resp) {
                if(i.equals("read")) {
                    Item it = r.getItem("plaque"); //FIXME -- how to find which item to look for
                    System.out.print(it.d);
                    System.out.print("alkjsda");
                }
                else {
                    switch(i) {
                        case "n":
                        case "e":
                        case "s":
                        case "w":
                            currentRoom = r.getExits(i.charAt(0));
                    }
                }
            }
            //currentRoom = r.getExits();
        } 

    }

    static void setup() {
        currentRoom = "foyer";
        Room.getRooms(rooms);
    }
}
