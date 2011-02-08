package org.deri.iris.rdb.evaluation.orb;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.deri.iris.Configuration;
import org.deri.iris.KnowledgeBase;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.optimisations.magicsets.MagicSets;
import org.deri.iris.optimisations.rulefilter.RuleFilter;
import org.deri.iris.rdb.RdbKnowledgeBase;
import org.deri.iris.storage.IRelation;

public class OpenRuleBench {

	private IrisSystem system;

	private OpenRuleBench(IrisSystem system) {
		this.system = system;
	}

	public void join1(Join1Query query, Join1Data data) throws Exception {
		File program = null;

		ClassLoader loader = getClass().getClassLoader();

		URL join1DataUrl = loader.getResource(data.getFileName());
		URL join1QueryUrl = loader.getResource(query.getFileName());

		try {
			program = File.createTempFile("join1program", ".iris");
			program.createNewFile();

			FileUtils.copyURLToFile(join1DataUrl, program);
			File queryFile = new File(join1QueryUrl.toURI());

			List<String> lines = FileUtils.readLines(queryFile);

			FileWriter writer = new FileWriter(program, true);
			for (String line : lines) {
				writer.write(line);
				writer.write(System.getProperty("line.separator"));
			}

			writer.flush();
			writer.close();

			execute(program);
		} finally {
			if (program != null) {
				program.delete();
			}
		}
	}

	// public void dblp() throws Exception {
	// File program = null;
	//
	// ClassLoader loader = getClass().getClassLoader();
	//
	// URL dataUrl = loader
	// .getResource("openrulebench/dblp_test/dblp_data.iris");
	// URL programUrl = loader
	// .getResource("openrulebench/dblp_test/dblp_program.iris");
	//
	// try {
	// program = File.createTempFile("dbplProgram", ".iris");
	// program.createNewFile();
	//
	// FileUtils.copyURLToFile(dataUrl, program);
	// File queryFile = new File(programUrl.toURI());
	//
	// List<String> lines = FileUtils.readLines(queryFile);
	//
	// FileWriter writer = new FileWriter(program, true);
	// for (String line : lines) {
	// writer.write(line);
	// writer.write(System.getProperty("line.separator"));
	// }
	//
	// writer.flush();
	// writer.close();
	//
	// execute(program);
	// } finally {
	// if (program != null) {
	// program.delete();
	// }
	// }
	// }

	public void join2() throws Exception {
		ClassLoader loader = getClass().getClassLoader();

		URL join1Url = loader.getResource("openrulebench/join2/join2.iris");

		File program = new File(join1Url.toURI());
		execute(program);
	}

	private void execute(File program) throws Exception {
		Configuration config = new Configuration();

		config.programOptmimisers.add(new RuleFilter());
		config.programOptmimisers.add(new MagicSets());

		FileReader reader = new FileReader(program);

		Parser parser = new Parser();

		long start = System.currentTimeMillis();
		parser.parse(reader);
		long end = System.currentTimeMillis();
		double duration = (end - start) / 1000.0;

		System.out.println("Parsing program took " + duration + "s");

		Map<IPredicate, IRelation> rawFacts = parser.getFacts();
		List<IRule> rules = parser.getRules();
		List<IQuery> queries = parser.getQueries();

		IKnowledgeBase kb = null;

		if (system == IrisSystem.ORIGINAL) {
			kb = new KnowledgeBase(rawFacts, rules, config);
		} else if (system == IrisSystem.RDB) {
			IFacts facts = new Facts(parser.getFacts(), config.relationFactory);
			kb = new RdbKnowledgeBase(facts, rules, config);
		}

		for (IQuery query : queries) {
			start = System.currentTimeMillis();

			// Execute the query.
			kb.execute(query);

			end = System.currentTimeMillis();
			duration = (end - start) / 1000.0;

			System.out.println(query + " took " + duration + "s");
		}
	}

	public static void main(String[] args) throws Exception {
		OpenRuleBench bench = new OpenRuleBench(IrisSystem.RDB);

		bench.join1(Join1Query.BF_B1, Join1Data.DATA0);
		// bench.join2();
	}

	private static enum IrisSystem {

		ORIGINAL,

		RDB;

	}

	private static enum Join1Data {

		DATA0("openrulebench/data/join1_50000.iris"),

		DATA1("openrulebench/data/join1_250000.iris"),

		DATA2("openrulebench/data/join1_1250000.iris");

		private String fileName;

		private Join1Data(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return fileName;
		}

	}

	private static enum Join1Query {

		A("openrulebench/join1/join_a.iris"),

		B1("openrulebench/join1/join_b1.iris"),

		B2("openrulebench/join1/join_b2.iris"),

		BF_A("openrulebench/join1/join_bf_a.iris"),

		BF_B1("openrulebench/join1/join_bf_b1.iris"),

		BF_B2("openrulebench/join1/join_bf_b2.iris"),

		DUPLICATE_A("openrulebench/join1/join_duplicate_a.iris"),

		DUPLICATE_B1("openrulebench/join1/join_duplicate_b1.iris"),

		DUPLICATE_B2("openrulebench/join1/join_duplicate_b2.iris"),

		FB_A("openrulebench/join1/join_fb_a.iris"),

		FB_B1("openrulebench/join1/join_fb_b1.iris"),

		FB_B2("openrulebench/join1/join_fb_b2.iris");

		private String fileName;

		private Join1Query(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return fileName;
		}

	}

}
