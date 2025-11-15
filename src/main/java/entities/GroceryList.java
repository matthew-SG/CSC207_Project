package entities;
import java.util.ArrayList;
import java.util.List;

public final class GroceryList {
    private final  int listId;
    private final String listName;
    private final ArrayList<Ingredient> items;

    public GroceryList(int listId, String listName, List<Ingredient> items) {
        this.listId = listId;
        this.listName = listName;
        this.items = new ArrayList<>(items);
    }
    public int getListId() {
        return listId;
    }
    public String getListName() {
        return listName;
    }
    public List<Ingredient> getItems() {return items; }
    }