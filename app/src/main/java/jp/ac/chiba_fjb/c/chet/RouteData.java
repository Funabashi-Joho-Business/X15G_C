package jp.ac.chiba_fjb.c.chet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//リターンデータのサンプル
//https://maps.googleapis.com/maps/api/directions/json?language=ja&origin=%E5%8D%83%E8%91%89&destination=%E6%9D%B1%E4%BA%AC&mode=driving&sensor=false

@JsonIgnoreProperties(ignoreUnknown=true)
public class RouteData{
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Location{
		public double lat;
		public double lng;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Distance{
		public String text;
		public String value;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Duration{
		public String text;
		public String value;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Polyline{
		public String points;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Steps{
		public Location end_location;
		public Location start_location;
		public Polyline polyline;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Legs{
		public Distance distance;
		public Duration duration;
		public String start_address;
		public Location start_location;
		public String end_address;
		public Location end_location;
		public Steps[] steps;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Routes{
		public Legs[] legs;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Routes[] routes;
}
