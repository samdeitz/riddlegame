
import java.util.ArrayList;
public class Player {
    int lives = 6;
    int food = 5;
    String name;
    ArrayList<Item> inventory = new ArrayList<>();
    ArrayList<Item> keys = new ArrayList<>();


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
        inventory.add(i);
        System.out.printf("Added item %s.%n", i.n);
    }

    Item getItem(String name) {
        for(Item i : inventory) {
            if (i.n.equals(name)) {
                return i;
            }
        }
        return null;
    } 



}
