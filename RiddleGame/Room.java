import java.util.HashMap;

class Room {
    String n,d;
    boolean locked;
    private String N,E,S,W;
    HashMap<String, Item> items = new HashMap<>();


    /**
     * create a room object
     * @param n name
     * @param d description
     * @param locked if the room is locked
     */
    Room(String n, String d, boolean locked) {
        this.n = n;
        this.d = d;
        this.locked = locked;
    }


    /**
     * sets room exits
     * @param n north exit
     * @param e east exit
     * @param s south exit
     * @param w west exit
     */
    private void setExits(String n, String e, String s, String w) {
        this.N = n;
        this.E = e;
        this.S = s;
        this.W = w;
    }


    /**
     * gets a new room based on room exits
     * @param c character for north east south or west
     * @return  new room
     */
    String getExits(char c) {
        switch(c) {
            case 'n': return this.N;
            case 'e': return this.E;
            case 's': return this.S;
            case 'w': return this.W;
            default: return "";
        }
    }

    /**
     * gets a needed item 
     * @param name name of item
     * @return returns wanted item
     */
    Item getItem(String name) {
        for(String s : items.keySet()) {
            if(s.equals(name)) {
                return items.get(s);
            }
        }
        return null;
    }

    /**
     * removes item from room
     * @param name name of item to ber
     */
    void removeItem(String name) {
        for(String s : items.keySet()) {
            if(s.equals(name)){
                items.remove(s);
                break;
            }
        }
    }


    /**
     * initialize all the rooms for the game
     * @param rooms room hashmap to add rooms too
     */
    static void getRooms(HashMap<String, Room> rooms) {


        //ENTRANCE / EXIT --> Need 4 keys to leave
        Room entrance = new Room("entrance", "\nYou find yourself in a room, there is an entrance to your north and an exit to your south"
                                                + "\nYou see the door has 4 keyholes and a plaque with writing beside it.", false);
        rooms.put(entrance.n, entrance);
        entrance.setExits("hall2", "blackroom","exit","hall1");
        Item plaque = new Item("plaque", "Welcome, you have been locked in our game, and must find four keys to use the door to exit.\n"
                                        + "You must complete our series of minigames to get the needed keys to escape.\n"
                                        + "You must get each key in sequential order, and have 10 lives to do so, good luck!", false);
        entrance.items.put(plaque.n, plaque);



        // hallways connecting rooms
        Room hall1 = new Room("hall1", "\nYou find yourself at a crossroads, three entrances, north, west, and south, "
                                + "\nleaving the main room to your east. Where will you go?", false);
        rooms.put(hall1.n, hall1);
        hall1.setExits("death", "entrance","puzzle", "keyroom");

        Room hall2 = new Room("hall2", "\nThe hallway in front of you split into 3 paths. Which way will you go?", false);
        rooms.put(hall2.n, hall2);
        hall2.setExits("greenroom","redroom","entrance","twoDoorsRoom");


        // riddleroom --> door shuts behind you you must solve a riddle to exit
        Room riddleroom = new Room("puzzle", "\nSLAM! The door shuts behind you(north)."
                                            + "\nYou find yourself in a room, with a number on the wall, and an engraving you cant quite see.", false);
        rooms.put(riddleroom.n, riddleroom);
        Item engraving = new Item("engraving", "Welcome, to open the door you must answer three riddles, but careful! Three wrong answers and you lose a life.", false);
        riddleroom.items.put(engraving.n, engraving);
        riddleroom.setExits("hall1", "", "", "");

        Item marker = new Item("marker", "A whiteboard marker, coloured black", false);
        riddleroom.items.put(marker.n, marker);





        // KEYROOMS HOUSING EACH KEY
        Room keyroom1 = new Room("keyroom", "\nYou find yourself in a room. "
                                + "\nThere is a mat in the middle and the exit door to your east.\n", false);
        rooms.put(keyroom1.n, keyroom1);
        keyroom1.setExits("", "hall1", "", "");
        Item mat = new Item("mat", "plain red shag carpet.", false);
        Item key1 = new Item("redkey", "Shiny red key, perhaps useful for opening doors.", true);
        keyroom1.items.put(mat.n, mat);
        keyroom1.items.put(key1.n, key1);


        Room keyroom2 = new Room("keyroom2", "\nWelcome to the black key room. Play along with my questions to get your reward.\n", false);
        rooms.put(keyroom2.n, keyroom2);
        keyroom2.setExits("redroom","","","");
        Item key2 = new Item("blackkey", "A mat black key", false);
        keyroom2.items.put(key2.n, key2);


        Room keyroom3 = new Room("blackroom", "\nYou enter a room, you see a whiteboard with a game of hangman setup"
                                            + "\nacross from you, perhaps it could help you get the green key.\n", true);
        rooms.put(keyroom3.n, keyroom3);
        keyroom3.setExits("", "", "", "entrance");
        Item key3 = new Item("greenkey", "A shiny green key.", false);
        keyroom3.items.put(key3.n, key3);

        Room keyroom4 = new Room("greenroom","\nYou see the lighted podiums, the big projector booting up with the words 'trivia thingy' on the screen."
                                            + "\nPerhaps this gameshow you find yourself in could aid you in finding your last key.\n", true);
        rooms.put(keyroom4.n, keyroom4);
        keyroom4.setExits("","","hall2","");
        Item key4 = new Item("bluekey", "A blue key", false);
        keyroom4.items.put(key4.n, key4);

        Room exit = new Room("exit", "\nCongrats you won the game!", true);
        rooms.put(exit.n, exit);

        // Locked room blocking second key, must have first to enter 
        Room lockedRoom = new Room("redroom", "\nYou are in an dimly lighted room, a painting to your left, and two doors, to your east and south.\n", true);
        rooms.put(lockedRoom.n, lockedRoom);
        lockedRoom.setExits("","leverRoom","keyroom2", "hall2");

    
        // hidden items for final room
        Room leverRoom = new Room("leverRoom", "\nSeems like this is a storage room."
                                                + "\nYou also see a lever piece on top of some boxes. Maybe you should pick it up. Might be useful...\n", false);
        rooms.put(leverRoom.n, leverRoom);
        leverRoom.setExits("","","","redroom");
        Item lever = new Item("lever", "cool lever", true);
        leverRoom.items.put(lever.n, lever);

        Room torchRoom = new Room("torchRoom", "\nFinally, a different room. There is a torch on the wall, maybe it will be useful.\n", false);
        rooms.put(torchRoom.n, torchRoom);
        torchRoom.setExits("","","right2","");
        Item torch = new Item("torch", "A lit torch, brightens up the room.", false);
        torchRoom.items.put(torch.n, torch);



        // Death rooms 
        Room death = new Room("death", "\nYou walk in the room and step on a pressure plate, caving in the roof.\n", false);
        rooms.put(death.n, death);

        Room twoDoorsRoom = new Room("twoDoorsRoom", "\nThe room is empty except the two ominous doors. One to the North, one to the South. "
                                                    + "\nYou don't know where they lead. Choose one...\n",false);
        rooms.put(twoDoorsRoom.n, twoDoorsRoom);
        twoDoorsRoom.setExits("wrong1","hall2","right1","");

        Room wrong1 = new Room("wrong1", "\nYou fell straight into a pit\n",false);
        rooms.put(wrong1.n, wrong1);

        Room right1 = new Room("right1", "\nThis room has another two doors. One to the East, one to the West. Which will you enter?\n",false);
        rooms.put(right1.n, right1);
        right1.setExits("twoDoorsRoom","wrong2","","right2");

        Room wrong2 = new Room("wrong2", "\nYou fell into lava\n",false);
        rooms.put(wrong2.n, wrong2);

        Room right2 = new Room("right2", "\nThis room is identical to the first room. North or South, choose a door.\n", false);
        rooms.put(right2.n, right2);
        right2.setExits("torchRoom", "right1", "wrong3", "");

        Room wrong3 = new Room("wrong3", "\nA big bolder falls on you. You died.\n", false);
        rooms.put(wrong3.n, wrong3);
        
        
    }   
}