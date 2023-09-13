package restful.utils;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

public class Utils {

	public static void delay(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}
	
	public static void showInfoJob(String name, JobExecutionContext ctx) {
		
		JobDetail jobDetail = ctx.getJobDetail();
		System.out.println("\n------------------------------------------------------ ");
		
		System.out.println(name + DateFormatUtils.format(new Date(), "HH:mm:ss"));
		System.out.println("	>> jobDetail : " + jobDetail);
		System.out.println("	>> jobDetail > key > name : " + jobDetail.getKey().getName());
		System.out.println("	>> jobDetail > key > group : " + jobDetail.getKey().getGroup());
		System.out.println("	>> job : " + ctx.getJobInstance());
		System.out.println("	>> jobDataMap : " + ctx.getMergedJobDataMap());

		System.out.println("	>> Thread Pool : " + Thread.currentThread().getName());
	}

	public static void showInfoJob2(String name, JobExecutionContext ctx) {

		JobDetail jobDetail = ctx.getJobDetail();
		System.out.println("\n--------Job2-7s Detail Info --------------------------------- ");

		System.out.println(name + DateFormatUtils.format(new Date(), "HH:mm:ss"));
		System.out.println("	>> jobDetail : " + jobDetail);
		System.out.println("	>> jobDetail > key > name : " + jobDetail.getKey().getName());
		System.out.println("	>> jobDetail > key > group : " + jobDetail.getKey().getGroup());
		System.out.println("	>> job : " + ctx.getJobInstance());
		System.out.println("	>> jobDataMap : " + ctx.getMergedJobDataMap());

		System.out.println("	>> Thread Pool : " + Thread.currentThread().getName());
	}

}
