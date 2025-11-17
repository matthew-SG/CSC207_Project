package data_access;

import use_case.grocery_list.GroceryRepository;
import entities.GroceryItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class JsonGroceryRepository implements GroceryRepository {

    private final File file;

    public JsonGroceryRepository(String path) {
        this.file = new File(path);
    }

    @Override
    public List<GroceryItem> load() {
        List<GroceryItem> list = new ArrayList<>();
        if (!file.exists()) return list;

        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            String text = new String(bytes, StandardCharsets.UTF_8);
            JSONArray arr = new JSONArray(text);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                String item = o.optString("item", "");
                String qty = o.optString("qty", "");
                String units = o.optString("units", "");
                list.add(new GroceryItem(item, qty, units));
            }
        } catch (Exception e) {
            System.out.println("Error loading grocery items: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void save(List<GroceryItem> items) {
        JSONArray arr = new JSONArray();
        for (GroceryItem g : items) {
            JSONObject o = new JSONObject();
            o.put("item", g.getItem());
            o.put("qty", g.getQty());
            o.put("units", g.getUnits());
            arr.put(o);
        }
        try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            w.write(arr.toString(2));
        } catch (Exception e) {
            System.out.println("Error saving grocery items: " + e.getMessage());
        }
    }
}
