package sample.AcoustID.AcoustIDJson;


import java.util.ArrayList;
import java.util.List;

public class Results {



        String status;
        List<Result> results = new ArrayList<>();

        public String getStatus() {
                return status;
        }

        public List<Result> getResults() {
                return results;
        }
}
