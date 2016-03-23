import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import java.net.*;
import java.io.*;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class JsonWorker {

	//starting the containers
	public static void startContainers () {
		
		try {
			URL url = new URL("http://localhost:2375/containers/json?all=1");

			InputStream is = url.openStream();
			
			//Reading Json from Docker
        		JsonReader jsonReader = Json.createReader(is); 
			JsonArray jsonArray = jsonReader.readArray();
			jsonReader.close();
        		is.close();
			
			for (int i = 0; i < jsonArray.size(); i++) {

				JsonObject container = jsonArray.getJsonObject(i);
				
				String containerId = container.getString("Id");
				String imageId = container.getString("ImageID");

				HttpClient   httpClient    = HttpClientBuilder.create().build();
				HttpPost     post          = new HttpPost("http://localhost:2375/containers/" + containerId  + "/start");
				post.setHeader("Content-type", "application/json");
				HttpResponse  response = httpClient.execute(post);

				System.out.println("Conteiner ID : " + containerId + " started");
				System.out.println();

			}

		} catch (MalformedURLException e) {
			System.out.println (e.getMessage());
		} catch (IOException e) {
			System.out.println (e.getMessage());
		}
	}


	//returning the list of active containers
	public static HashMap<String,String> getActiveContainers(){
		
		try {
			URL url = new URL("http://localhost:2375/containers/json");

			InputStream is = url.openStream();
			
			//Reading Json from Docker
        		JsonReader jsonReader = Json.createReader(is); 
			JsonArray jsonArray = jsonReader.readArray();
			jsonReader.close();
        		is.close();

			HashMap<String,String> hMap = new HashMap<String,String>();
			
			for (int i = 0; i < jsonArray.size(); i++) {

				JsonObject container = jsonArray.getJsonObject(i);
				
				String containerId = container.getString("Id");
				String imageId = container.getString("ImageID");

				hMap.put(containerId, imageId);
			}

			return hMap;

		} catch (MalformedURLException e) {
			System.out.println (e.getMessage());
		} catch (IOException e) {
			System.out.println (e.getMessage());
		}
	
		return new HashMap<String,String>();
	}
}
