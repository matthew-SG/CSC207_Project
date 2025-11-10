package entities;
import java.util.ArrayList;

public final class GroceryList {
    private final  int listId;
    private final String listName;
    private final ArrayList<Ingredient> items;

    public GroceryList(int listId, String listName, List<Ingredient> items) {
        this.listId = listId;
        this.listName = listName;
        this.items = items;
    }
    public int getListId() {
        return listId;
    }
    public String getListName() {
        return listName;
    }
    public List<Ingredient> getItems() {return items; }
    }