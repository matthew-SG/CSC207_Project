package use_case.meal_plan;

/**
 * Input Boundary for the Meal Plan Use Case.
 */
public interface MealPlanInputBoundary {

    /**
     * Executes the meal plan use case.
     * @param mealPlanInputData the input data
     */
    void execute(MealPlanInputData mealPlanInputData);

    /**
     * Sets the generation strategy for the meal plan generator.
     * @param generationStrategy the strategy to be used when generating the meal plans
     */
    void setStrategy(MealPlanGeneratorStrategy generationStrategy);
}
