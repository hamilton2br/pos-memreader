import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;


import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonWorker {

	public static void main(String[] args){
		URL url = new URL("http://localhost:2375/containers/json?all=1");

		try ( InputStream is = url.openStream(); 
		      JSonParser parser = Json.createParser(is)  ) {

			while ( parser.hasNext() ) {
				Event e = 
			}
		} 
	}
}
