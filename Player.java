
import java.util.ArrayList;
public class Player {
    int lives = 5;
    int food = 5;
    final String name;
    ArrayList<Item> inventory = new ArrayList<>();
    ArrayList<Item> keys = new ArrayList<>();


    Player(String name) {
        this.name = name;
    }

    void removeLife() {
        this.lives--;
        System.out.printf("You now have %d lives.%n", this.lives);
    }

    void removeFood() {
        this.food--;
        System.out.printf("You now have %d hunger.", this.food);
    }

    void addItem(Item i) {
        inventory.add(i);
        
    }



}
