package com.sky;

import java.util.HashMap;
import java.util.Map;


public class ParentalControlService {

	private MovieService movieService;

	public static Map<String, Integer> parentalControlLevelMap = new HashMap<String, Integer>();

	static {
		parentalControlLevelMap.put("U",1);
		parentalControlLevelMap.put("PG",2);
		parentalControlLevelMap.put("12",3);
		parentalControlLevelMap.put("15",4);
		parentalControlLevelMap.put("18",5);
	}

	public boolean movieAccessAllowed(String parentalControlPref, String movieId) throws TitleNotFoundException, ControlLevelNotFoundException {
    	try {
			String movieParentalControlLevel = movieService.getParentalControlLevel(movieId);
			return isAccessAllowed(movieParentalControlLevel, parentalControlPref);
		} catch (TechnicalFailureException e) {
			return false;
		}
	}

	private boolean isAccessAllowed(String movieParentalControlLevel, String parentalControlPref) throws ControlLevelNotFoundException {
		if(parentalControlLevelMap.get(parentalControlPref) != null){
			return parentalControlLevelMap.get(parentalControlPref) >= parentalControlLevelMap.get(movieParentalControlLevel);
		} else {
			throw new ControlLevelNotFoundException();
		}
	}

}
