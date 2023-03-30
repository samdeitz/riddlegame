
import java.util.HashMap;
import java.util.Scanner;

public class MainGame {
    static String currentRoom;
    static HashMap<String, Room> rooms = new HashMap<>();
    public static void main(String[] args) {
        setup();
        Scanner sc = new Scanner(System.in);
        while(true) {
            Room r = rooms.get(currentRoom);
            System.out.print(r.d);
            System.out.print("Which direction would you like to go? ");
            String resp = sc.nextLine();
            currentRoom = r.getExits(resp.charAt(0));
        } 

    }

    static void setup() {
        currentRoom = "foyer";
        Room.getRooms(rooms);
    }
}
