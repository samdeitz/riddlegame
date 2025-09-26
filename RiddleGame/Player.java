
import java.util.HashMap;
public class Player {
    int lives = 10;
    int food = 5;
    String name;
    HashMap<String, Item> inventory = new HashMap<>();


    /**
     * creates player with name
     * @param name player name
     */
    Player(String name) {
        this.name = name;
    }

    /**
     * get rid of life
     */
    void removeLife() {
        this.lives--;
        System.out.printf("You now have %d lives.%n", this.lives);
    }

    /**
     * add item to player inventory
     * @param i item to be added to inventory
     */
    void addItem(Item i) {
        inventory.put(i.n, i);
        System.out.printf("Added item %s.%n", i.n);
    }

    /**
     * get an item from player inventory
     * @param name name of desired item
     * @return returns desired item
     */
    Item getItem(String name) {
        for(String s : inventory.keySet()) {
            if (s.equals(name)) {
                return inventory.get(s);
            }
        }
        return null;
    } 

    /**
     * checks if player is in posession of item
     * @param s name of desired item
     * @return true or false indicating if the player has an item
     */
    boolean hasItem(String s) {
        boolean b = false;
        if(inventory.containsKey(s)) {
            b = true;
        }
        return b;
    }



}
