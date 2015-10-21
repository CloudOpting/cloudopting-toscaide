package eu.cloudopting.web.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CytoscapeController {
	private final Logger log = LoggerFactory.getLogger(CytoscapeController.class);

	@RequestMapping(value = "/nodes", method = RequestMethod.GET)
	@ResponseBody
	public String getNodes() {
		log.debug("in getNodes");
		JSONObject jret = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject props = new JSONObject();
		try {
			props.put("cpu", 2);
			props.put("ram", 3);
			data.put("shape", "rectangle");
			data.put("color", "#992222");
			data.put("props", props);
			jret.put("container", data);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String ret = jret.toString();
		return ret;

	}

}
