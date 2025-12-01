package interface_adapter.likedRecipeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LikedRecipeListViewModel {

    private final LikedRecipeListState state = new LikedRecipeListState();
    private final List<Runnable> listeners = new ArrayList<>();

    public LikedRecipeListState getState() {
        return state;
    }

    public void setRecipes(List<Integer> ids,
                           List<String> names,
                           List<List<String[]>> ingredients,
                           List<Map<String, Double>> nutrition,
                           List<String> images) {
        state.setRecipeIds(ids);
        state.setRecipeNames(names);
        state.setRecipeIngredients(ingredients);
        state.setRecipeNutrition(nutrition);
        state.setRecipeImages(images);
        notifyListeners();
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable r : listeners) {
            r.run();
        }
    }
}