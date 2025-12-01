package interface_adapter.likedRecipeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import interface_adapter.ViewManagerModel;
import interface_adapter.speech.SpeechService;
import interface_adapter.step_by_step.StepByStepController;
import interface_adapter.step_by_step.StepByStepPresenter;
import interface_adapter.step_by_step.StepByStepViewModel;

import use_case.likedRecipeList.LikedRecipeOutputBoundary;
import use_case.likedRecipeList.LikedRecipeOutputData;
import use_case.step_by_step.StepByStepInputData;
import use_case.step_by_step.StepByStepInteractor;

import view.StepByStepView;

import speechapi.SystemTTS;

public class LikedRecipeListPresenter implements LikedRecipeOutputBoundary {
    private final LikedRecipeListViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public LikedRecipeListPresenter(LikedRecipeListViewModel viewModel,
                                    ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareLikedRecipeView(LikedRecipeOutputData likedRecipeOutputData) {
        int[] idsArray = likedRecipeOutputData.getRecipeIds();
        String[] namesArray = likedRecipeOutputData.getRecipeNames();
        String[] imagesArray = likedRecipeOutputData.getRecipeImages();

        List<Integer> ids = new ArrayList<>();
        for (int id : idsArray) {
            ids.add(id);
        }
        List<String> names = Arrays.asList(namesArray);
        List<String> images = imagesArray != null
                ? Arrays.asList(imagesArray)
                : new ArrayList<>();

        List<List<String[]>> ingredients = likedRecipeOutputData.getRecipeIngredients();
        List<Map<String, Double>> nutrition = likedRecipeOutputData.getRecipeNutrition();

        viewModel.setRecipes(ids, names, ingredients, nutrition, images);
    }

    @Override
    public void prepareHandsfree(StepByStepInputData stepByStepInputData) {

        StepByStepViewModel stepVm = new StepByStepViewModel();

        StepByStepPresenter stepPresenter = new StepByStepPresenter(stepVm, viewManagerModel);

        StepByStepInteractor stepInteractor = new StepByStepInteractor(stepPresenter);

        StepByStepController stepController =
                new StepByStepController(stepInteractor, stepByStepInputData.instructions());

        SpeechService tts = new SystemTTS();

        SwingUtilities.invokeLater(() -> {
            StepByStepView stepView =
                    new StepByStepView(stepController, stepVm, tts);
            stepController.start();
            stepView.setLocationRelativeTo(null);
            stepView.setVisible(true);
        });
    }

    @Override
    public void prepareFailView(String error) {
        System.err.println("Liked recipes error: " + error);
    }
}