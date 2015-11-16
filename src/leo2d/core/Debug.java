package leo2d.core;

public class Debug {
	public static void log(Object data) {
		System.out.println("[LOG] " + (data != null ? data.toString() : "NULL"));
	}
}
