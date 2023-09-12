package restful.utils;

public class Utils {

	public static void delay(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}

}
