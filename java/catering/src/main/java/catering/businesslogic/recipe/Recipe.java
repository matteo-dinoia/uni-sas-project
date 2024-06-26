package catering.businesslogic.recipe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import catering.persistence.PersistenceManager;
import catering.persistence.handler.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Recipe {
    private static Map<Integer, Recipe> all = new HashMap<>();

    private int id;
    private String name;

    private Recipe() {

    }

    public Recipe(String name) {
        id = 0;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return name;
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<Recipe> loadAllRecipes() {
        String query = "SELECT * FROM Recipes";
        PersistenceManager.executeQuery(query, rs -> {
            int id = rs.getInt("id");
            if (all.containsKey(id)) {
                Recipe rec = all.get(id);
                rec.name = rs.getString("name");
            } else {
                Recipe rec = new Recipe(rs.getString("name"));
                rec.id = id;
                all.put(rec.id, rec);
            }
        });
        ObservableList<Recipe> ret =  FXCollections.observableArrayList(all.values());
        ret.sort((o1, o2) -> (o1.getName().compareTo(o2.getName())));
        return ret;
    }

    public static ObservableList<Recipe> getAllRecipes() {
        return FXCollections.observableArrayList(all.values());
    }

    public static Recipe loadRecipeById(int id) {
        if (all.containsKey(id)) return all.get(id);
        Recipe rec = new Recipe();
        String query = "SELECT * FROM Recipes WHERE id = " + id;
        PersistenceManager.executeQuery(query, rs -> {
                rec.name = rs.getString("name");
                rec.id = id;
                all.put(rec.id, rec);
        });
        return rec;
    }


}
