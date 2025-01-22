package com.acon.weatherapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/map")
public class MapController {
	
	@GetMapping("/weather")
	public String weather() {
		return "map/weather";
	}
	
	@GetMapping("/search")
	public String mapSearch() {
		return "map/geolocation";
	}
	
	@GetMapping("/localWeather")
	public String localWeather() {
		return "map/weather";
	}
}
