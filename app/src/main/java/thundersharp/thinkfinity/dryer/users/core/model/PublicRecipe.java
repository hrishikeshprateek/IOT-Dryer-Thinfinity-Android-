package thundersharp.thinkfinity.dryer.users.core.model;

public class PublicRecipe {

    private String recipe_id;
    private String recipe_name;
    private String recipe_description;
    private String recipe_image;
    private double recipe_temperature;
    private double recipe_humidity;
    private double recipe_time;
    private String recipe_author;
    private String created_at;
    private String updated_at;

    public PublicRecipe(String recipe_id, String recipe_name, String recipe_description, String recipe_image, double recipe_temperature, double recipe_humidity, double recipe_time, String recipe_author, String created_at, String updated_at) {
        this.recipe_id = recipe_id;
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

    public PublicRecipe() {
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
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

    public double getRecipe_temperature() {
        return recipe_temperature;
    }

    public void setRecipe_temperature(double recipe_temperature) {
        this.recipe_temperature = recipe_temperature;
    }

    public double getRecipe_humidity() {
        return recipe_humidity;
    }

    public void setRecipe_humidity(double recipe_humidity) {
        this.recipe_humidity = recipe_humidity;
    }

    public double getRecipe_time() {
        return recipe_time;
    }

    public void setRecipe_time(double recipe_time) {
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
