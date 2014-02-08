package sh.calaba.instrumentationbackend.query;

import java.io.IOException;
import java.util.List;

import sh.calaba.org.codehaus.jackson.map.ObjectMapper;

public class QueryResult {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private List<?> result;

    public QueryResult(List<?> result) {

        this.result = result;
    }

    public boolean isEmpty() {
        return result.isEmpty();
    }

    public String asJson() {
        try {
            return MAPPER.writeValueAsString(result);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert result to json", e);
        }
    }

    public List getResult() {
        return result;
    }


    public List asList() {
        return ViewMapper.mapViews(result);
    }
}
