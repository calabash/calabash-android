package sh.calaba.instrumentationbackend.query;

import sh.calaba.org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class QueryResult {

    private List result;

    public QueryResult(List result) {

        this.result = result;
    }


    public String asJson() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(result);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert result to json", e);
        }
    }
}
