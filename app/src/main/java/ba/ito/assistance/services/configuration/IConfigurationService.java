package ba.ito.assistance.services.configuration;

import java.util.List;

import ba.ito.assistance.model.language.Language;
import io.reactivex.Observable;

public interface IConfigurationService {
    Language GetSelectedLanguage();
    List<Language>  GetAvailableLanguages();
    void setApplicationLanguage(Language selectedLanguage);

    int getTheme();

    int getMapTheme();

    int getDialogTheme();
}
