package use_case.community;


import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import data_access.DummyCommunityDataAccessObject;
import entities.Rating;
import entities.Recipe;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import use_case.community.input_data.CommunityLikeRecipeInputData;
import use_case.community.input_data.CommunityPoseSelectionInputData;
import use_case.community.input_data.CommunityPublishInputData;
import use_case.community.input_data.CommunityRecipeSelectionInputData;
import use_case.community.output_data.CommunityLikedRecipesOutputData;
import use_case.community.output_data.CommunityRatingsOutputData;
import use_case.community.output_data.CommunitySelectedRecipeOutputData;


public class CommunityInteractorTests {

    // Stub implementation of CommunityUserRecipeDataAccessInterface for testing
    private static class StubUserRecipeDAO implements CommunityUserRecipeDataAccessInterface {
        private final Map<String, List<Recipe>> savedRecipes = new HashMap<>();

        @Override
        public List<Recipe> getLikedRecipesForUser(String username) {
            return savedRecipes.getOrDefault(username, new ArrayList<>());
        }

        @Override
        public void saveRecipeToUser(String username, Recipe recipe) {
            savedRecipes.computeIfAbsent(username, k -> new ArrayList<>()).add(recipe);
        }

        @Override
        public Optional<Recipe> getCurrentUserLikedRecipe(int recipeId) {
            return Optional.empty();
        }

        @Override
        public String getCurrentUsername() {
            return null;
        }

        public List<Recipe> getSavedRecipesForUser(String username) {
            return savedRecipes.getOrDefault(username, new ArrayList<>());
        }
    }

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

            @Override
            public void prepareAddSucc(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }
        };

        StubUserRecipeDAO stubUserRecipeDAO = new StubUserRecipeDAO();
        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, successPresenter, stubUserRecipeDAO);
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

            @Override
            public void prepareAddSucc(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }
        };

        StubUserRecipeDAO stubUserRecipeDAO = new StubUserRecipeDAO();
        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, presenter, stubUserRecipeDAO);
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
            public void prepareAddSucc(CommunityRatingsOutputData response) {
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

        StubUserRecipeDAO stubUserRecipeDAO = new StubUserRecipeDAO();
        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, presenter, stubUserRecipeDAO);
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
            public void prepareAddSucc(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }

            @Override
            public void preparePublishSucc(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }
        };

        StubUserRecipeDAO stubUserRecipeDAO = new StubUserRecipeDAO();
        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, presenter, stubUserRecipeDAO);
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
            public void prepareAddSucc(CommunityRatingsOutputData response) {
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

        StubUserRecipeDAO stubUserRecipeDAO = new StubUserRecipeDAO();
        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, presenter, stubUserRecipeDAO);
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

        List<Recipe> likedRecipes = dao.getLikedRecipes("Grace");

        assertEquals(2, likedRecipes.size());
        assertTrue(likedRecipes.stream().map(Recipe::getRecipeName)
                .collect(Collectors.toSet())
                .containsAll(List.of("pizza", "hamburger")));
        assertTrue(likedRecipes.stream().allMatch(recipe ->
                recipe.getRecipeImage() != null && !recipe.getRecipeImage().isEmpty()));
    }

    @Test
    void likeRecipeSuccessAddsRecipeToUser() {
        // Create DAO with a rating that has a detailed recipe
        DummyCommunityDataAccessObject testDAO = new DummyCommunityDataAccessObject();
        // Set a detailed recipe on an existing rating
        Rating ratingWithRecipe = testDAO.getCurrentRatings().get(0);
        Recipe detailedRecipe = new Recipe(ratingWithRecipe.getRecipeId(), ratingWithRecipe.getRecipeName(),
                ratingWithRecipe.getRecipeImageUrl(), new ArrayList<>(), "american", new HashMap<>());
        ratingWithRecipe.setDetailedRecipe(detailedRecipe);

        final boolean[] addSuccCalled = {false};
        StubUserRecipeDAO stubUserRecipeDAO = new StubUserRecipeDAO();

        CommunityOutputBoundary presenter = new CommunityOutputBoundary() {
            @Override
            public void prepareFailView(String error) {
                fail("Unexpected failure pathway: " + error);
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

            @Override
            public void prepareAddSucc(CommunityRatingsOutputData response) {
                addSuccCalled[0] = true;
                assertNotNull(response);
            }
        };

        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, presenter, stubUserRecipeDAO);
        CommunityLikeRecipeInputData inputData = new CommunityLikeRecipeInputData("Grace", ratingWithRecipe.getRatingId());
        interactor.likeRecipe(inputData);

        assertTrue(addSuccCalled[0]);
        List<Recipe> savedRecipes = stubUserRecipeDAO.getSavedRecipesForUser("Grace");
        assertEquals(1, savedRecipes.size());
        assertEquals(detailedRecipe.getRecipeId(), savedRecipes.get(0).getRecipeId());
    }

    @Test
    void likeRecipeFailsWhenRatingNotFound() {
        DummyCommunityDataAccessObject testDAO = new DummyCommunityDataAccessObject();
        final boolean[] failCalled = {false};
        StubUserRecipeDAO stubUserRecipeDAO = new StubUserRecipeDAO();

        CommunityOutputBoundary presenter = new CommunityOutputBoundary() {
            @Override
            public void prepareFailView(String error) {
                failCalled[0] = true;
                assertEquals("Rating not found", error);
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

            @Override
            public void prepareAddSucc(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }
        };

        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, presenter, stubUserRecipeDAO);
        // Use a non-existent rating ID
        CommunityLikeRecipeInputData inputData = new CommunityLikeRecipeInputData("Grace", 9999);
        interactor.likeRecipe(inputData);

        assertTrue(failCalled[0]);
    }

    @Test
    void likeRecipeFailsWhenDetailedRecipeUnavailable() {
        DummyCommunityDataAccessObject testDAO = new DummyCommunityDataAccessObject();
        // Ratings in DummyCommunityDataAccessObject don't have detailedRecipe set by default
        Rating ratingWithoutRecipe = testDAO.getCurrentRatings().get(0);
        // Ensure detailedRecipe is null (default)
        assertNull(ratingWithoutRecipe.getDetailedRecipe());

        final boolean[] failCalled = {false};
        StubUserRecipeDAO stubUserRecipeDAO = new StubUserRecipeDAO();

        CommunityOutputBoundary presenter = new CommunityOutputBoundary() {
            @Override
            public void prepareFailView(String error) {
                failCalled[0] = true;
                assertEquals("Detailed recipe unavailable for this rating yet.", error);
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

            @Override
            public void prepareAddSucc(CommunityRatingsOutputData response) {
                fail("Unexpected UI pathway");
            }
        };

        CommunityInputBoundary interactor = new CommunityMarketInteractor(testDAO, presenter, stubUserRecipeDAO);
        CommunityLikeRecipeInputData inputData = new CommunityLikeRecipeInputData("Grace", ratingWithoutRecipe.getRatingId());
        interactor.likeRecipe(inputData);

        assertTrue(failCalled[0]);
    }
}
