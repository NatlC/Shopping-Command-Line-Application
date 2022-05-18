import java.util.ArrayList;

public class Cart
{
    private ArrayList<CartItem> items = new ArrayList<CartItem>();

    public Cart() {
        this.items = items;
    }

    public void addItem(CartItem item)
    {
        items.add(item);
    }
    public void removeItem(int item) {
        items.remove(item);
    }
    public ArrayList<CartItem> getItems() { return items;}

}



