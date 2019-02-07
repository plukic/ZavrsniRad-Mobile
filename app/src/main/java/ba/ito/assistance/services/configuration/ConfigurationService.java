package ba.ito.assistance.services.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.data.shared_prefs.SharedPrefsRepo;
import ba.ito.assistance.model.language.Language;
import ba.ito.assistance.model.user.ClientsDetailsModel;

public class ConfigurationService implements IConfigurationService {
    private SharedPrefsRepo sharedPrefsRepo;
    private List<Language> AvailableLanguage;

    @Inject
    public ConfigurationService(SharedPrefsRepo sharedPrefsRepo) {
        this.sharedPrefsRepo = sharedPrefsRepo;
        AvailableLanguage = new ArrayList<>();
        AvailableLanguage.add(new Language("English", "en_us", "en-US"));
        AvailableLanguage.add(new Language("Bosanski", "hr_HR", "ba"));
        AvailableLanguage.add(new Language("Hrvatski", "hr_HR", "hr"));
        AvailableLanguage.add(new Language("Srpski", "sr_RS", "sr-Latn-RS"));
    }

    @Override
    public Language GetSelectedLanguage() {
        Language language = sharedPrefsRepo.GetSelectedLanguage();
        if (language != null)
            return language;
        else {
            return AvailableLanguage.get(1);
        }
    }

    @Override
    public List<Language> GetAvailableLanguages() {
        return AvailableLanguage;
    }

    @Override
    public void setApplicationLanguage(Language selectedLanguage) {
        sharedPrefsRepo.SetSelectedLanguage(selectedLanguage);
    }

    @Override
    public int getTheme() {
        boolean isNightMode = sharedPrefsRepo.IsNightModeEnabled();
        return isNightMode ? R.style.NightTheme : getUsersTheme();
    }

    private int getUsersTheme() {
        ClientsDetailsModel clientInfo = sharedPrefsRepo.getClientInfo();
        if(clientInfo==null || clientInfo.ConfigurationGroup==null)
            return R.style.COTheme;
        switch (clientInfo.ConfigurationGroup) {
            case CO:
                return R.style.COTheme;
            case BMW:
                return R.style.BMWTheme;
        }
        return R.style.COTheme;
    }

    @Override
    public int getMapTheme() {
        boolean isNightMode = sharedPrefsRepo.IsNightModeEnabled();
        return isNightMode ? R.raw.style_json_dark : R.raw.style_json;
    }

    @Override
    public int getDialogTheme() {
        return R.style.AlertDialogTheme;
    }
}
