package ba.ito.assistance.model.settings;

public class ProfileDetailItem {
    private String hint;
    private String content;

    public ProfileDetailItem(String hint, String content) {
        this.hint = hint;
        this.content = content;
    }

    public String getHint() {
        return hint;
    }

    public String getContent() {
        return content;
    }
}
