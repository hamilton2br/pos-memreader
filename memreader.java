import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.math.NumberUtils;

public class memreader {

	public static void main(String[] args) throws IOException {

		//getting the running container's id
		HashMap<String,String> hMap = JsonWorker.getActiveContainers();

		//getting the process IDs for the running conteiners
		File[] directories = new File("/proc").listFiles(File::isDirectory);

		for(File dir : directories) {

			if(NumberUtils.isNumber(dir.getName())){
				
				Scanner scanner = new Scanner("/proc/" + dir.getName() + "/cgroup");
				boolean found = false;				

				while( scanner.hasNextLine() && !found ){
				
					String line = scanner.nextLine();

					//System.out.println(line);
	
					for(Map.Entry<String,String> entry : hMap.entrySet()){

						if(line.indexOf(entry.getKey()) > 0){
							System.out.println(dir.getName());
							found = true;
						}
					}
				}

				scanner.close();
			}
		}
	}
}
