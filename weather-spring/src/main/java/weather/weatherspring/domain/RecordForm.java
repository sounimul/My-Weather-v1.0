package weather.weatherspring.domain;

public class RecordForm {
    private String saveTempComment;
    private String saveHumidComment;
    private String saveRainComment;

    public String getSaveTempComment() {
        return saveTempComment;
    }

    public void setSaveTempComment(String saveTempComment) {
        this.saveTempComment = saveTempComment;
    }

    public String getSaveHumidComment() {
        return saveHumidComment;
    }

    public void setSaveHumidComment(String saveHumidComment) {
        this.saveHumidComment = saveHumidComment;
    }

    public String getSaveRainComment() {
        return saveRainComment;
    }

    public void setSaveRainComment(String saveRainComment) {
        this.saveRainComment = saveRainComment;
    }
}
