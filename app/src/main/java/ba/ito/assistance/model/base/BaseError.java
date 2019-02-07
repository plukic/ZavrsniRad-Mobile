package ba.ito.assistance.model.base;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class BaseError {


        @SerializedName("Message")
        private String message;

        @SerializedName("ModelState")
        private Map<String,List<String>> modelState;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Map<String, List<String>> getModelState() {
            return modelState;
        }

        public void setModelState(Map<String, List<String>> modelState) {
            this.modelState = modelState;
        }
}
