package ba.ito.assistance.util;

import android.content.res.Resources;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.model.base.BaseAuthError;
import ba.ito.assistance.model.base.BaseError;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;

public class BaseErrorFactory {
    private Gson gson;
    private Resources resources;

    @Inject
    public BaseErrorFactory(Gson gson, Resources resources) {
        this.gson = gson;
        this.resources = resources;
    }

    private String getJson(HttpException throwable) throws Exception {
        HttpException httpException = (HttpException) throwable;
        ResponseBody responseBody = httpException.response().errorBody();
        return responseBody.string();
    }

    private BaseError parseBaseError(String json) throws Exception {
        BaseError baseError = gson.fromJson(json, BaseError.class);
        if (baseError == null || baseError.getModelState() == null)
            return null;
        return baseError;
    }

    public String parseBaseAuthError(String json) {
        BaseAuthError baseAuthError = gson.fromJson(json, BaseAuthError.class);
        if (baseAuthError == null)
            return null;
        return baseAuthError.error_description;

    }

    public String parseSingleError(Throwable throwable, String errorKey) {
        if (!(throwable instanceof HttpException))
            return resources.getString(R.string.msg_unexpected_error);

        try {
            String json = getJson(((HttpException) throwable));

            String authError = parseBaseAuthError(json);
            if (authError != null)
                return authError;

            BaseError baseError = parseBaseError(json);
            if (baseError == null)
                return resources.getString(R.string.msg_unexpected_error);

            Map<String, List<String>> modelState = baseError.getModelState();
            if (!modelState.containsKey(errorKey) || modelState.get(errorKey).isEmpty())
                return resources.getString(R.string.msg_unexpected_error);
            return modelState.get(errorKey).get(0);
        } catch (Exception e) {
            return resources.getString(R.string.msg_unexpected_error);
        }
    }
}
