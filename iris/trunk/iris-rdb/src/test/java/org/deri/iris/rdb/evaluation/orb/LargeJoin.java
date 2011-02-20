/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2011 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.rdb.evaluation.orb;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.storage.IRelation;

/**
 * Executes the LargeJoin tests of the OpenRuleBench.
 */
public abstract class LargeJoin {

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

	public void join2() throws Exception {
		ClassLoader loader = getClass().getClassLoader();

		URL join1Url = loader.getResource("openrulebench/join2/join2.iris");

		File program = new File(join1Url.toURI());
		execute(program);
	}

	private void execute(File program) throws Exception {
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

		IKnowledgeBase kb = createKnowledgeBase(rawFacts, rules);

		for (IQuery query : queries) {
			start = System.currentTimeMillis();

			// Execute the query.
			kb.execute(query);

			end = System.currentTimeMillis();
			duration = (end - start) / 1000.0;

			System.out.println(query + " took " + duration + "s");
		}
	}

	protected abstract IKnowledgeBase createKnowledgeBase(
			Map<IPredicate, IRelation> rawFacts, List<IRule> rules)
			throws EvaluationException;

	public static enum Join1Data {

		DATA0("openrulebench/join1/join1_50000.iris"),

		DATA1("openrulebench/join1/join1_250000.iris"),

		DATA2("openrulebench/join1/join1_1250000.iris");

		private String fileName;

		private Join1Data(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return fileName;
		}

	}

	public static enum Join1Query {

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
