package use_case.community;


import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import data_access.DummyCommunityDataAccessObject;
import entities.Recipe;
import entities.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import use_case.community.input_data.CommunityPoseSelectionInputData;
import use_case.community.input_data.CommunityPublishInputData;
import use_case.community.input_data.CommunityRecipeSelectionInputData;
import use_case.community.output_data.CommunityLikedRecipesOutputData;
import use_case.community.output_data.CommunityRatingsOutputData;
import use_case.community.output_data.CommunitySelectedRecipeOutputData;


class CommunityInteractorTests {
    @Test
    void successPublish() {
        CommunityPublishInputData inputData = new CommunityPublishInputData("Grace",
                12, 3, "Nice food, won't try again!", "Pizza with Pizza",
                "https://media.istockphoto.com/id/1442417585/photo/person-getting-a-piece-of-cheesy-pepperoni-pizza.jpg?s=612x612&w=0&k=20&c=k60TjxKIOIxJpd4F4yLMVjsniB4W1BpEV4Mi_nb4uJU=");
        CommunityDataAccessInterface testDAO = new DummyCommunityDataAccessObject();

        // This creates a successPresenter that tests whether the test case is as we expect.
        CommunityOutputBoundary successPresenter = new CommunityOutputBoundary() {

            @Override
            public void prepareFailView(String error) {
                fail("Unexpected failure pathway");
            }

            @Override
            public void prepareViewRating(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void prepareRecipeSelection(CommunityLikedRecipesOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void prepareCommentWriting(CommunitySelectedRecipeOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void preparePublishSucc(CommunityRatingsOutputData response) {
                final int idx = response.getComments().size() - 1;
                assertEquals(response.getComments().size(), 3);
                assertEquals(response.getComments().get(idx), inputData.getComment());
                assertEquals((long) response.getStars().get(idx), inputData.getRating());
                assertEquals((long) response.getRecipeIds().get(idx), inputData.getRecipeID());
                assertEquals(response.getRecipeNames().get(idx), inputData.getRecipeName());
                assertEquals(response.getRecipeImageUrls().get(idx), inputData.getRecipeImageURL());
            }
        };

        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, successPresenter);
        interactor.publish(inputData);
    }

    @Test
    void viewCommunityDisplaysExistingRatings() {
        CommunityDataAccessInterface testDAO = new DummyCommunityDataAccessObject();
        final boolean[] viewRatingsCalled = {false};

        CommunityOutputBoundary presenter = new CommunityOutputBoundary() {
            @Override
            public void prepareFailView(String error) {
                fail("Unexpected failure pathway");
            }

            @Override
            public void prepareViewRating(CommunityRatingsOutputData response) {
                viewRatingsCalled[0] = true;
                assertEquals(List.of(1, 2), response.getRecipeIds());
                assertEquals(List.of("hamburger", "pizza"), response.getRecipeNames());
                assertEquals(List.of("yummy", "good"), response.getComments());
                assertEquals(List.of(4, 4), response.getStars());
                assertEquals(CommunityRatingsOutputData.PROMPT, response.getPrompt());
            }

            @Override
            public void prepareRecipeSelection(CommunityLikedRecipesOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void prepareCommentWriting(CommunitySelectedRecipeOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void preparePublishSucc(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }
        };

        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, presenter);
        interactor.viewCommunity();
        assertTrue(viewRatingsCalled[0]);
    }

    @Test
    void viewToPostRequiresLogin() {
        CommunityDataAccessInterface testDAO = new DummyCommunityDataAccessObject();
        final boolean[] failCalled = {false};

        CommunityOutputBoundary presenter = new CommunityOutputBoundary() {
            @Override
            public void prepareFailView(String error) {
                failCalled[0] = true;
                assertEquals("Log in to write reviews", error);
            }

            @Override
            public void prepareViewRating(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void prepareRecipeSelection(CommunityLikedRecipesOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void prepareCommentWriting(CommunitySelectedRecipeOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void preparePublishSucc(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }
        };

        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, presenter);
        interactor.viewToPost(new CommunityPoseSelectionInputData(false, "Guest"));
        assertTrue(failCalled[0]);
    }

    @Test
    void viewToPostShowsLikedRecipesForLoggedInUser() {
        CommunityDataAccessInterface testDAO = new DummyCommunityDataAccessObject();
        final boolean[] selectionCalled = {false};

        CommunityOutputBoundary presenter = new CommunityOutputBoundary() {
            @Override
            public void prepareFailView(String error) {
                fail("Unexpected failure pathway");
            }

            @Override
            public void prepareViewRating(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void prepareRecipeSelection(CommunityLikedRecipesOutputData response) {
                selectionCalled[0] = true;
                assertEquals(2, response.getRecipeIds().size());
                assertTrue(response.getRecipeIds().containsAll(List.of(1, 2)));
                assertTrue(response.getRecipeNames().containsAll(List.of("pizza", "hamburger")));
                assertEquals(response.getRecipeIds().size(), response.getRecipeNames().size());
                assertEquals(response.getRecipeIds().size(), response.getRecipeImages().size());
            }

            @Override
            public void prepareCommentWriting(CommunitySelectedRecipeOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void preparePublishSucc(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }
        };

        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, presenter);
        interactor.viewToPost(new CommunityPoseSelectionInputData(true, "Grace"));
        assertTrue(selectionCalled[0]);
    }

    @Test
    void selectRecipeReturnsSelectedRecipeDetails() {
        CommunityDataAccessInterface testDAO = new DummyCommunityDataAccessObject();
        final boolean[] commentWritingCalled = {false};

        CommunityOutputBoundary presenter = new CommunityOutputBoundary() {
            @Override
            public void prepareFailView(String error) {
                fail("Unexpected failure pathway");
            }

            @Override
            public void prepareViewRating(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void prepareRecipeSelection(CommunityLikedRecipesOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void prepareCommentWriting(CommunitySelectedRecipeOutputData response) {
                commentWritingCalled[0] = true;
                assertEquals(1, response.getSelectedRecipeId());
                assertEquals("Homemade Pizza", response.getSelectedRecipeName());
                assertEquals("https://example.com/pizza.jpg", response.getSelectedRecipeImageUrl());
            }

            @Override
            public void preparePublishSucc(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }
        };

        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, presenter);
        CommunityRecipeSelectionInputData inputData = new CommunityRecipeSelectionInputData(
                1,
                "Homemade Pizza",
                "https://example.com/pizza.jpg"
        );
        interactor.selectRecipe(inputData);
        assertTrue(commentWritingCalled[0]);
    }

    @Test
    void daoReturnsAllLikedRecipes() {
        DummyCommunityDataAccessObject dao = new DummyCommunityDataAccessObject();
        User user = new User("Grace", "pw", new ArrayList<>(), new ArrayList<>(), null);

        List<Recipe> likedRecipes = dao.getLikedRecipes(user);

        assertEquals(2, likedRecipes.size());
        assertTrue(likedRecipes.stream().map(Recipe::getRecipeName)
                .collect(Collectors.toSet())
                .containsAll(List.of("pizza", "hamburger")));
        assertTrue(likedRecipes.stream().allMatch(recipe ->
                recipe.getRecipeImage() != null && !recipe.getRecipeImage().isEmpty()));
    }
}
