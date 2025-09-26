

class Item {
    String n, d;
    boolean hidden;

    /**
     * gets item
     * @param n name of item
     * @param d description of item
     * @param hidden whether the item is hidden or not
     */
    Item(String n, String d, boolean hidden) {
        this.n = n;
        this.d = d;
        this.hidden = hidden;
    }
}