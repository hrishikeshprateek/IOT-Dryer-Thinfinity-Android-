package thundersharp.thinkfinity.dryer.users.core;

public class RecipeModel {

    public String id;
    public String recipe_name;
    public String recipe_description;
    public String recipe_image;
    public float recipe_temperature;
    public float recipe_humidity;
    public float recipe_time;
    public String recipe_author;
    public String created_at;
    public String updated_at;

    public RecipeModel(String id, String recipe_name, String recipe_description, String recipe_image, float recipe_temperature, float recipe_humidity, float recipe_time, String recipe_author, String created_at, String updated_at) {
        this.id = id;
        this.recipe_name = recipe_name;
        this.recipe_description = recipe_description;
        this.recipe_image = recipe_image;
        this.recipe_temperature = recipe_temperature;
        this.recipe_humidity = recipe_humidity;
        this.recipe_time = recipe_time;
        this.recipe_author = recipe_author;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getRecipe_description() {
        return recipe_description;
    }

    public void setRecipe_description(String recipe_description) {
        this.recipe_description = recipe_description;
    }

    public String getRecipe_image() {
        return recipe_image;
    }

    public void setRecipe_image(String recipe_image) {
        this.recipe_image = recipe_image;
    }

    public float getRecipe_temperature() {
        return recipe_temperature;
    }

    public void setRecipe_temperature(float recipe_temperature) {
        this.recipe_temperature = recipe_temperature;
    }

    public float getRecipe_humidity() {
        return recipe_humidity;
    }

    public void setRecipe_humidity(float recipe_humidity) {
        this.recipe_humidity = recipe_humidity;
    }

    public float getRecipe_time() {
        return recipe_time;
    }

    public void setRecipe_time(float recipe_time) {
        this.recipe_time = recipe_time;
    }

    public String getRecipe_author() {
        return recipe_author;
    }

    public void setRecipe_author(String recipe_author) {
        this.recipe_author = recipe_author;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
