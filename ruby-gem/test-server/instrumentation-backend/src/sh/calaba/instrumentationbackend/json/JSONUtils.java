package sh.calaba.instrumentationbackend.json;

import java.io.IOException;
import java.util.Map;

import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import sh.calaba.org.codehaus.jackson.map.SerializationConfig;

public class JSONUtils {

	public static String asJson(Map<?,?> map) {
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		try {
			return mapper.writeValueAsString(map);
		} catch (IOException e) {
			throw new RuntimeException("Could not convert result to json: "+map, e);
		}
	}

    public static ObjectMapper calabashObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new CustomAndroidModule());

        return objectMapper;
    }
}
