package org.deri.iris.rdb.cli;

import org.deri.iris.demo.Demo;

public class ConsoleTest {

	public static void main(String[] args) {
		args = new String[] {
				"--magic-sets",
				"--program-file=D:/Workspaces/SOA4All/iris/iris-impl/src/test/resources/transitive_closure.txt" };

		Console.main(args);

		args = new String[] {
				"magic-sets",
				"program-file=D:/Workspaces/SOA4All/iris/iris-impl/src/test/resources/transitive_closure.txt" };

		Demo.main(args);
	}

}
