import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;

public class memreader {

	public static void main(String[] args) throws IOException {

		//starting the containers
		JsonWorker.startContainers();

		//getting the running container's id
		HashMap<String,String> hMap = JsonWorker.getActiveContainers();

		//getting the process IDs for the running conteiners
		File[] directories = new File("/proc").listFiles(File::isDirectory);

		for(Map.Entry<String,String> entry : hMap.entrySet()){

			for(File dir : directories) {

				if(NumberUtils.isNumber(dir.getName())){
				
					String filename = "/proc/" + dir.getName() + "/cgroup";
					
					try(Stream<String> stream = Files.lines(Paths.get(filename))){

						if(stream.filter(line -> line.indexOf(entry.getKey()) > 0 ).findFirst().isPresent())
							System.out.println(dir.getName());

					} catch (IOException e){
						System.out.println(e.getMessage());
					}
				}
			}
		}
	}
}
