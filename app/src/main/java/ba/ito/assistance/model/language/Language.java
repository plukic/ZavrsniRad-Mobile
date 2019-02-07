package ba.ito.assistance.model.language;

public class Language {
    public String Language;
    public String AndroidLanguageLocale;
    public String ServerLanguageLocale;

    public Language(String language,String languageLocale,String serverLanguageLocale) {
        Language = language;
        AndroidLanguageLocale = languageLocale;
        this.ServerLanguageLocale=serverLanguageLocale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        if (Language != null ? !Language.equals(language.Language) : language.Language != null)
            return false;
        if (AndroidLanguageLocale != null ? !AndroidLanguageLocale.equals(language.AndroidLanguageLocale) : language.AndroidLanguageLocale != null)
            return false;
        return ServerLanguageLocale != null ? ServerLanguageLocale.equals(language.ServerLanguageLocale) : language.ServerLanguageLocale == null;
    }

    @Override
    public int hashCode() {
        int result = Language != null ? Language.hashCode() : 0;
        result = 31 * result + (AndroidLanguageLocale != null ? AndroidLanguageLocale.hashCode() : 0);
        result = 31 * result + (ServerLanguageLocale != null ? ServerLanguageLocale.hashCode() : 0);
        return result;
    }
}
