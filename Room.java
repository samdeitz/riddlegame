import java.util.ArrayList;
import java.util.HashMap;

class Room {
    String n,d;
    boolean locked;
    private String N,E,S,W;
    ArrayList<Item> items = new ArrayList<>();

    private boolean visited = false;

    Room(String n, String d, boolean locked) {
        this.n = n;
        this.d = d;
        this.locked = locked;
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

    void setVisited() {
        this.visited = true;
    }

    static void getRooms(HashMap<String, Room> rooms) {

        Room entrance = new Room("entrance", "You find yourself in a room, there is an entrance to your north and an exit to your south"
                                                + "\nYou see the door has 4 keyholes and a plaque with writing beside it.", false);
        rooms.put(entrance.n, entrance);
        entrance.setExits("hall2", "blackroom","exit","hall1");
        Item plaque = new Item("plaque", "Welcome, ", false);
        entrance.items.add(plaque);


        Room hall1 = new Room("hall1", "You find yourself at a crossroads, three entrances, north, west, and south, "
                                + "\nleaving the main room to your east. Where will you go?", false);
        rooms.put(hall1.n, hall1);
        hall1.setExits("death1", "entrance","puzzle", "keyroom");


        Room death1 = new Room("death1", "You walk in the room and step on a pressure plate, caving in the roof", false);
        rooms.put(death1.n, death1);


        Room riddleroom = new Room("puzzle", "SLAM! The door shuts behind you(north)."
                                            + "\nYou find yourself in a room, with a number on the wall, and an engraving you cant quite see.", false);
        rooms.put(riddleroom.n, riddleroom);
        Item engraving = new Item("engraving", "Welcome, to open the door you must answer a riddle, but careful! Three wrong answers and you die."
                                                + "\nThe riddle is: ", false);
        riddleroom.items.add(engraving);
        riddleroom.setExits("", "", "", "");


        Room keyroom1 = new Room("keyroom", "You find yourself in a room, "
                                + "there is a mat in the middle and a marker in the corner and the exit door to your east.", false);
        rooms.put(keyroom1.n, keyroom1);
        keyroom1.setExits("", "hall1", "", "");
        Item mat = new Item("mat", "plain red shag carpet.", false);
        Item key1 = new Item("redkey", "Shiny red key, perhaps useful for opening doors.", true);
        Item marker = new Item("marker", "A whiteboard marker, coloured black", false);
        keyroom1.items.add(marker);
        keyroom1.items.add(mat);
        keyroom1.items.add(key1);


        Room keyroom3 = new Room("blackroom", "You are in a room, whiteboard on the wall opposite to the door, with a game of hangman written."
                                                + "\nand a button with the shape of a key beside the board. Write start to begin.", true);
        rooms.put(keyroom3.n, keyroom3);
        keyroom3.setExits("", "", "", "entrance");
        Item key3 = new Item("greenkey", "A shiny green key.", true);
        keyroom3.items.add(key3);



        Room hall2 = new Room("hall2", "The hallway in front of you split into 3 paths. Which way will you go?", false);
        rooms.put(hall2.n, hall2);
        // make room, add to hashmap, set exits, add items
        hall2.setExits("greenroom","redroom","entrance","twoDoorsRoom");
        
        Room keyroom4 = new Room("greenroom","You enter a dark room.   ", true);
        rooms.put(keyroom4.n, keyroom4);
        keyroom4.setExits("","","hall2","");
        Item key4 = new Item("bluekey", "A blue key", true);
        keyroom4.items.add(key4);

        
        Room lockedRoom = new Room("redroom", "This exit is locked. You need the first key to enter", true);
        rooms.put(lockedRoom.n, lockedRoom);
        lockedRoom.setExits("","leverRoom","keyroom2", "hall2");

        Room keyroom2 = new Room("keyroom2", "The room is well lighted. In front of you is a huge stone wall. \"wordle\" is engraved in big writing. There are five holes lined up horizontally. "
                                            + "The stones on the ground seem to be the perfect size for the holes. Each stone has a letter."
                                            + "Arrange the stones to find the word combination that solves the puzzle. Type in the five letter code...", false);
        rooms.put(keyroom2.n, keyroom2);
        keyroom2.setExits("","","","");
        Item key2 = new Item("blackkey", "A mat black key", true);
        keyroom2.items.add(key2);

        Room leverRoom = new Room("leverRoom", "Seems like this is a storage room. There is some food in the fridge you can eat. "
                                                + "You also see a lever piece on top of some boxes. Maybe you should pick it up. Might be useful...");
        rooms.put(leverRoom.n, leverRoom);
        leverRoom.setExits("","","","redroom");
        Item lever = new Item("lever", "cool lever", true);
        leverRoom.items.add(lever);




        Room twoDoorsRoom = new Room("twoDoorsRoom", "The room is empty except the two ominous doors. You don't know where they lead. Choose one...");
        rooms.put(twoDoorsRoom.n, twoDoorsRoom);
        twoDoorsRoom.setExits("","","","");

        Room wrong1 = new Room("wrong1", "You fell straight into a pit");
        rooms.put(wrong1.n, wrong1);

        Room right1 = new Room("right1", "This room has another two doors. Which will you enter?");
        rooms.put(right1.n, right1);
        right1.setExits("","wrong2","","right2");

        Room wrong2 = new Room("wrong2", "You fell into lava");
        rooms.put(wrong2.n, wrong2);

        Room right2 = new Room("right2", "This room is identical to the previous. Choose a door again.");
        rooms.put(right2.n, right2);

        Room wrong3 = new Room("wrong3", "A big bolder falls on you. You died.");
        rooms.put(wrong3.n, wrong3);
        
        Room torchRoom = new Room("torchRoom", "Finally, a different room. There is a torch on the wall, maybe it will be useful.");
        rooms.put(torchRoom.n, torchRoom);
        Item torch = new Item("torch", "A lit torch, brightens up the room.", false);
        torchRoom.items.add(torch);
    }   
}
