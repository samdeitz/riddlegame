
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainGame {
    static String currentRoom;
    static HashMap<String, Room> rooms = new HashMap<>();
    public static void main(String[] args) {
            setup();
            Scanner sc = new Scanner(System.in);
        
            //currentRoom = r.getExits();
        } 

    static void setup() {
        currentRoom = "Entrance";
        Room.getRooms(rooms);
    }
}
