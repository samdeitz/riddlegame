import java.util.ArrayList;
import java.util.HashMap;

class Room {
    String n,d;
    private String N,E,S,W;
    ArrayList<Item> items = new ArrayList<>();

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

    Item getItem(String name) {
        for(Item i : items) {
            if(i.n.equals(name)) {
                return i;
            }
        }
        return null;

        
    }

    static void getRooms(HashMap<String, Room> rooms) {
        Room hall2 = new Room("hall2", "The hallway in front of you split into 3 paths. Which way will you go?");
        rooms.put(hall2.n, hall2);
        // make room, add to hashmap, set exits, add items
        hall2.setExits("keyroom4","lockedRoom","entrance","twoDoorsRoom");
        
        Room keyroom4 = new Room("?Room?","The room is too dark. Unable to see something. Could use a light source");
        rooms.put(keyroom4.n, keyroom4);
        keyroom4.setExits("","","hall2","");
        
        Room lockedRoom = new Room("Locked", "This exit is locked. You need the first key to enter");
        rooms.put(lockedRoom.n, lockedRoom);
        lockedRoom.setExits("","leverRoom","keyroom2", "hall2");

        Room keyroom2 = new Room("Keyroom 2", "The room is well lighted. In front of you is a huge stone wall. \"wordle\" is engraved in big writing. There are five holes lined up horizontally. The stones on the ground seem to be the perfect size for the holes. Each stone has a letter. Arrange the stones to find the word combination that solves the puzzle. Type in the five letter code...");
        rooms.put(keyroom2.n, keyroom2);
        keyroom2.setExits("","","","");

        Room leverRoom = new Room("Storage room", "Seems like this is a storage room. There is some food in the fridge you can eat. You also see a lever piece on top of some boxes. Maybe you should pick it up. Might be useful...");
        rooms.put(leverRoom.n, leverRoom);
        leverRoom.setExits("","","","lockedRoom");
        Item lever = new Item("lever", "cool lever");
        leverRoom.items.add(lever);

        Room twoDoorsRoom = new Room("Empty Room", "The room is empty except the two ominous doors. You don't know where they lead. Choose one...");
        rooms.put(twoDoorsRoom.n, twoDoorsRoom);
        twoDoorsRoom.setExits("","","","");

        Room wrong1 = new Room("Wrong choice", "You fell straight into a pit");
        rooms.put(wrong1.n, wrong1);

        Room right1 = new Room("Safe room", "This room has another two doors. Which will you enter?");
        rooms.put(right1.n, right1);
        right1.setExits("","wrong2","","right2");

        Room wrong2 = new Room("Wrong choice", "You fell into lava");
        rooms.put(wrong2.n, wrong2);

        Room right2 = new Room("Safe room", "This room is identical to the previous. Choose a door again.");
        rooms.put(right2.n, right2);

        Room wrong3 = new Room("Wrong choice", "A big bolder falls on you. You died.");
        rooms.put(wrong3.n, wrong3);
        
        Room torchRoom = new Room("Safe room", "Finally, a different room. There is a torch on the wall, maybe it will be useful.");
        rooms.put(torchRoom.n, torchRoom);
        Item torch = new Item("torch", "A lit torch, brightens up the room.");
        torchRoom.items.add(torch);
    }   
}
