import java.util.concurrent.TimeUnit.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import java.util.Date;
import java.util.Calendar;
import java.util.stream.Stream;
import java.util.List;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class Scheduler {

	//some constants
	private static final int startInterval = 0;
	private static final int interval = 1;
	private static final int purgeTime = 5;
	
	//scheduler
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	//scheduler function
	public static void startScheduleTask() {

		final ScheduledFuture<?> taskHandle = scheduler.scheduleAtFixedRate(
			new Runnable() {
				public void run() {
					System.out.println(new Date());

					//roda o leitor de memória
					Memreader.readMemory();

					//delete old mem files
					deleteOldMemFiles();
				}
			}
		, startInterval, interval, TimeUnit.MINUTES);
	}

	//deleting old mem files
	public static void deleteOldMemFiles(){

		try {
		
			//current directory
			File f = new File(".");

			//creating the filter - refactor, get this somewhere else
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String lowercaseName = name.toLowerCase();
					
					if( lowercaseName.endsWith(".mem") ){
						return true;
					} else { 
						return false;
					}
				}
			};

			//listing files
			File[] files = f.listFiles(filter);

			//set up threshold
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, purgeTime * -1);
			long timeLimit = cal.getTimeInMillis();

			//delete old files
			for(File file : files){
				if( file.lastModified() < timeLimit  ) {
					file.delete(); 
				}
			}
			
		} catch (SecurityException se) {
			System.out.println(se.getMessage());
		}
	}

	public static void main(String[] args) {
	
		//starting the containers
		ContainerUtil.startContainers();
		startScheduleTask();
	}
}
