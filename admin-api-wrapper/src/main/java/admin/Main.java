package admin;

import java.lang.reflect.Constructor;

public class Main {
	public static void main(String[] args) throws Exception {
		Class c=Class.forName("admin.Setup");
		Constructor con = c.getDeclaredConstructor();
		ISetup setup = (ISetup) con.newInstance();
		setup.prepare("https://localhost:9999/pf-admin-api/v1");
		setup.setup();
	}

}
