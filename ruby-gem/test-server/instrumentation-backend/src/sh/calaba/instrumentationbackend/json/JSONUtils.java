package sh.calaba.instrumentationbackend.json;

import java.io.IOException;
import java.util.Map;

import sh.calaba.org.codehaus.jackson.map.ObjectMapper;

public class JSONUtils {

	public static String asJson(Map<?,?> map) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(map);
		} catch (IOException e) {
			throw new RuntimeException("Could not convert result to json: "+map, e);
		}
	}
}
