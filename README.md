# SOLID Recipe Manager 🍳

Group: TUT0401-09 | Team Name: SOLID

SOLID Recipe Manager is a comprehensive Java-based application designed to streamline the cooking experience. From generating meal plans based on strict dietary restrictions to providing hands-free, text-to-speech cooking instructions, this application leverages Clean Architecture principles to assist users in maintaining a healthy and organized culinary lifestyle.

## 📖 Table of Contents

- [Features](#-features)
- [Architecture & Design](#-architecture--design)
- [Data Persistence](#-data-persistence)
- [Installation & Setup](#-installation--setup)
- [Usage Guide](#-usage-guide)
- [Contributors](#-contributors)

## 🚀 Features

### 🥗 Smart Recipe Discovery

- **Recipe Generator:** Input dietary restrictions (Vegan, Gluten-free, etc.), cuisine preferences, and nutritional goals to generate random, tailored recipes.
- **Ingredient Search:** Have leftovers? Search for recipes based strictly on ingredients you already have at home.
- **Swipe & Save:** Like or dislike recipes based on images and descriptions. Approved recipes are saved to your personal collection.
- **Nutritional Info:** View detailed caloric and macronutrient information for every recipe.

### 📝 Planning & Organization

- **Meal Plan Generator:** Generates a 3-meal daily plan based on your daily caloric goals using recipes from your liked list.
- **Grocery List Manager:** Automatically adds ingredients from recipes to your grocery list.
  - Edit quantities and remove items.
  - **Unit Conversion:** Standardize measurement units for easier shopping.
  - Manual entry for custom items.

### 🍳 Cooking Companion

- **Step-by-Step Mode:** A focused view for cooking.
- **Hands-Free Instructions:** Utilizes the Java Speech API to read instructions aloud.
- **Navigation:** Voice or button-controlled navigation through steps to keep your screen clean while cooking.

### 🤝 Community & Social

- **Review System:** Rate recipes (1-5 stars) and leave comments.
- **Gamification:** Earn status/points through engagement (implementation pending).
- **Cloud Sync:** Community reviews are stored via Firebase Firestore to share insights across all users.

## 🏗 Architecture & Design

This project strictly follows Clean Architecture principles to ensure separation of concerns, scalability, and testability.

### The Layers

1. **Entities:** Core business logic (User, Recipe, GroceryList, Ingredient, MealPlan, Rating).
2. **Use Cases (Interactors):** Specific application logic (e.g., RecipeGenerator, GroceryListEditor, HandsFreeInstruction).
3. **Interface Adapters:**
   - **Controllers:** Process user input (userController, recipeController).
   - **Presenters:** Format data for the view.
   - **State & ViewModels:** Manage the state of the GUI.
   - **Gateways (DAOs):** Interfaces for data access (ReviewDA, UserDA).
4. **Frameworks & Drivers:**
   - **View:** Java Swing UI (Login, Recipe View, Meal Plan, etc.).
   - **Data Access:** Implementations for File I/O and Firebase.
   - **Main:** The entry point AppBuilder.

## 💾 Data Persistence

The application uses a hybrid data approach to balance performance and connectivity:

### 1. Local Storage (JSON & CSV)

User-specific private data is stored locally for fast access and offline capability.

- **User Profiles:** `data/users.csv` (Stores username and password)
- **User Data Directory:** `data/<username>/`
  - **Saved Recipes:** `liked_recipes.json`
  - **Grocery Lists:** `grocery_list.json`
  - **Meal Plans:** `meal_plans.json`

### 2. Cloud Storage (Firebase Firestore)

Shared community data is stored in the cloud.

- **Community Reviews:** Ratings and comments accessible by all users.
- **User Authentication Data:** Secure login handling.

## 🛠 Installation & Setup

### Prerequisites

- **Java Development Kit (JDK):** Version 17 or higher.
- **Build Tool:** Maven or Gradle.
- **Internet Connection:** Required for the Recipe API and Firebase connection.

### Configuration

1. **Clone the repository:**

   ```bash
   git clone git@github.com:matthew-SG/CSC207_Project.git
   ```

2. **API Keys:**
   **Important:** We cannot store the API key in an environment file because the Firebase configuration contains specific data that is tied to our API key. If we try to transmit our api via github to the TA, that defeats the purpose, so we have just kept it hardcoded instead.


### Running the App

**Using Maven:**

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="app.App"
```

**Using an IDE:**
Open the project in IntelliJ IDEA or Eclipse, let it resolve dependencies via `pom.xml`, and run `src/main/java/app/App.java`.

## 📱 Usage Guide

### 1. Getting Started

- **Sign Up/Login:** Create an account to initialize your local JSON files.
- **Set Preferences:** On first login, input your dietary restrictions (e.g., Vegetarian, Keto) and allergies.

### 2. Finding Food

- Navigate to the **Recipe Generator**.
- Click "Generate" to see recipes matching your criteria.
- Click "Like" to save a recipe to your Liked Recipes view.

### 3. Cooking

- Go to **Liked Recipes** and select a dish.
- Click **Start Cooking** to enter Step-by-Step mode.
- Toggle **Text-to-Speech** for audio guidance.

### 4. Shopping

- In the Recipe view, click **Add to Grocery List**.
- Navigate to the **Grocery List** tab to view ingredients.
- Use the **Convert Units** button to switch between metric/imperial.

## 👥 Contributors

- **Taaha:** Input processing & Recipe Generation logic.
- **Rayyan:** Approval/Dislike logic & Recipe List management.
- **Jinzi:** Grocery List editor, unit conversion, and list clearing.
- **Jingtian:** Community features, upload system, and gamification.
- **Abdullah:** Ingredient search implementation.
- **Matthew:** Daily Meal Plan generator logic.
- **Adish:** Step-by-step cooking view & Text-to-Speech integration.
