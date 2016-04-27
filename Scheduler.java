import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.Date;

public class Scheduler {

	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static void startScheduleTask() {

		final ScheduledFuture<?> taskHandle = scheduler.scheduleAtFixedRate(
			new Runnable() {
				public void run() {
					//verifica se há shots maiores que 3 horas
						//se tiver remove-os
						System.out.println(new Date());
					//roda o leitor de memória
				}
			}
		, 5, 5, TimeUnit.MINUTES);
	}

	public static void main(String[] args) {
	
		//starting the containers
		ContainerUtil.startContainers();
		startScheduleTask();
	}
}
