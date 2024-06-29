package thundersharp.thinkfinity.dryer.users.core.model;

public class LogModel {
    public String timestamp;
    public String log_level;
    public String message;

    public LogModel() {
    }

    public LogModel(String timestamp, String log_level, String message) {
        this.timestamp = timestamp;
        this.log_level = log_level;
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLog_level() {
        return log_level;
    }

    public void setLog_level(String log_level) {
        this.log_level = log_level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
