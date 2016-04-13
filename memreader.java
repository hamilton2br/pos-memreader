import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.Collections;
import java.util.Enumeration;

import org.apache.commons.lang3.math.NumberUtils;

public class memreader {

	public static void main(String[] args) throws IOException {

		//starting the containers
		ContainerUtil.startContainers();

		//getting the running container's id
		HashMap<String,String> hMap = ContainerUtil.getActiveContainers();

		//getting the process IDs for the running conteiners
		File[] directories = new File("/proc").listFiles(File::isDirectory);

		for(Map.Entry<String,String> entry : hMap.entrySet()){

			//pausing the container to get its memory
			ContainerUtil.pauseContainer( entry.getKey() );

			for(File dir : directories) {

				if(NumberUtils.isNumber(dir.getName())){
				
					String filename = "/proc/" + dir.getName() + "/cgroup";

					//finding the cgroup file for the containerId					
					try(Stream<String> stream = Files.lines(Paths.get(filename))){

						if(stream.filter(line -> line.indexOf(entry.getKey()) > 0 ).findFirst().isPresent()){
							String outFile = "./" + getMacAddress() + "-" + 
										entry.getKey() + "-" + 
										entry.getValue() + "-" + 
										dir.getName() + ".mem";

							String fileSource = "/proc/" + dir.getName() + "/numa_maps";

							//writing memory to file
							writeMemToFile(outFile, fileSource);
						}

					} catch (IOException e){
						System.out.println(e.getMessage());
					}
				}
			}

			//unpausing the container after memory collection
			ContainerUtil.unpauseContainer( entry.getKey() );
		}
	}

	//Getting the machine mac address
	private static String getMacAddress(){
		try {
			//getting hardware address from inside a virtual machine from NetworkInterface object returns null, so need a gambiarra.
			//if this issue is ever solved please replace for the appropriate code
			String command = "ifconfig";
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader inn = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = inn.readLine();

			while( line != null ){
				
				if(line.indexOf("eth0") >= 0){
					String[] split = line.split(" ");
					return split[split.length-1];
				}

				line = inn.readLine();
			}

			inn.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "error";
	}


	//Writing the memory content to a file
	private static void writeMemToFile(String outFile, String fileSource){

		FileInputStream instream = null;
		FileOutputStream outstream = null;

		try{
			File infile =new File(fileSource);
			File outfile =new File(outFile);

			instream = new FileInputStream(infile);
			outstream = new FileOutputStream(outfile);

			byte[] buffer = new byte[1024];

			int length;
		
			while ((length = instream.read(buffer)) > 0){
				outstream.write(buffer, 0, length);
			}

			//Closing the input/output file streams
			instream.close();
			outstream.close();

		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}
