import java.util.ArrayList;
import java.util.HashMap;

class Room {
    String n,d;
    private String N,E,S,W;
    static ArrayList<String> items = new ArrayList<>();
    private boolean visited = false;

    Room(String n, String d) {
        this.n = n;
        this.d = d;
    }

    private void setExits(String n, String e, String s, String w) {
        this.N = n;
        this.E = e;
        this.S = s;
        this.W = w;
    }

    String getExits(char c) {
        switch(c) {
            case 'n': return this.N;
            case 'e': return this.E;
            case 's': return this.S;
            case 'w': return this.W;
            default: return null;
        }
    }

    static void getRooms(HashMap<String, Room> rooms) {
        Room r1 = new Room("foyer", "you are in a room, door behind you, "
                            + "there is a plaque on furthest wall, though "
                            + "you cant quite see what it says, three doors, one north, one east and one west");
            r1.setExits("hangman","","","");
            rooms.put(r1.n, r1);
        Room r2 = new Room("hangman", "There is a paper on the wall, it looks like hangman. "
                                        + "You see there is a pencil, an eraser, and a counter for lives");
            r2.setExits("","","foyer","");
            rooms.put(r2.n, r2);
    }
}
