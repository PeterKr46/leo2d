package leo2d;

public class Debug {
	public static void log(Object data) {
		System.out.println("[LOG] " + (data != null ? data.toString() : "NULL"));
	}
}
