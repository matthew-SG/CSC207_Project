package data_access;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entities.Rating;
import entities.Recipe;
import entities.User;
import org.bson.Document;
import use_case.community.CommunityDataAccessInterface;
import use_case.community.input_data.CommunityPublishInputData;

import java.util.List;


public class DBCommunityDataAccessObject implements CommunityDataAccessInterface {
    public static void main(String[] args) {
        String connectionString =
                "mongodb+srv://hjtwillbeok_db_user:OlQZhUwFxD61dsyn@cluster0.mz2pdg0.mongodb.net/?appName=Cluster0";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // 1. Get the database (will be created if it doesn't exist)
                MongoDatabase database = mongoClient.getDatabase("hi");

// 2. Get the collection (will be created if it doesn't exist)
                MongoCollection<Document> collection = database.getCollection("users");

// 3. Create a Java object (Document) to store
                Document newUser = new Document("username", "jthu_test")
                        .append("email", "test@example.com")
                        .append("age", 21)
                        .append("isAdmin", true)
                        .append("joinDate", new java.util.Date());

// 4. ACTUALLY WRITE TO THE DATABASE
                collection.insertOne(newUser);

                System.out.println("Document inserted successfully!");
//                // Send a ping to confirm a successful connection
//                MongoDatabase database = mongoClient.getDatabase("hi");
//                database.runCommand(new Document("ping", 1));
//                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Recipe> getLikedRecipes(User user) {
        return List.of();
    }

    @Override
    public Recipe getSelectedRecipe(int recipeID) {
        return null;
    }

    @Override
    public List<Rating> getCurrentRatings() {
        return List.of();
    }

    @Override
    public List<Rating> publishReview(CommunityPublishInputData data) {
        return List.of();
    }
}
