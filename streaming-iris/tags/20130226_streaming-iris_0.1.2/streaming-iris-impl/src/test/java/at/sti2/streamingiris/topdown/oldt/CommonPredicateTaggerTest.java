package at.sti2.streamingiris.topdown.oldt;

import java.util.HashSet;

import junit.framework.TestCase;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;
import at.sti2.streamingiris.evaluation.topdown.CommonPredicateTagger;
import at.sti2.streamingiris.factory.Factory;

public class CommonPredicateTaggerTest extends TestCase {

	public void testImmediateRecursion() throws ParserException {
		String program = "s(?X, ?Y) :- s(?X, ?Z), r(?Z, ?Y)."
				+ "s(?X, ?Y) :- r(?X, ?Y)." +

				"?- s(1, ?Y).";

		HashSet<IPredicate> expectedSet = new HashSet<IPredicate>();
		expectedSet.add(Factory.BASIC.createPredicate("r", 2));

		Parser parser = new Parser();
		parser.parse(program);

		CommonPredicateTagger tagger = new CommonPredicateTagger(
				parser.getRules(), parser.getQueries().get(0));
		System.out.println(expectedSet + " " + tagger.getMemoPredicates());
		assertEquals(true, expectedSet.containsAll(tagger.getMemoPredicates()));
		assertEquals(expectedSet.size(), tagger.getMemoPredicates().size());
	}

	public void testCommonPredicates() throws ParserException {
		String program = "p(?X) :- a(?X), b(?X), a(?X), b(?X), c(?X), a(?X)." +

		"?- p(1).";

		HashSet<IPredicate> expectedSet = new HashSet<IPredicate>();
		expectedSet.add(Factory.BASIC.createPredicate("a", 1));

		Parser parser = new Parser();
		parser.parse(program);

		CommonPredicateTagger tagger = new CommonPredicateTagger(
				parser.getRules(), parser.getQueries().get(0));
		System.out.println(expectedSet + " " + tagger.getMemoPredicates());
		assertEquals(true, expectedSet.containsAll(tagger.getMemoPredicates()));
		assertEquals(expectedSet.size(), tagger.getMemoPredicates().size());
	}

	public void testSiblingExample() throws Exception {
		String program = "parent( '1a', '2a' )."
				+ "parent( '2a', '3a' )."
				+

				"parent( '1b', '2b' )."
				+ "parent( '1b', '2c' )."
				+

				"parent( '2b', '3b' )."
				+ "parent( '2b', '3c' )."
				+ "parent( '2c', '3d' )."
				+ "parent( '2c', '3e' )."
				+

				"parent( '3b', '4b' )."
				+ "parent( '3e', '4e' )."
				+

				"sibling(?X,?Y) :- parent(?Z,?X), parent(?Z,?Y), ?X != ?Y."
				+ "cousin(?X,?Y) :- parent(?XP,?X), parent(?YP,?Y), sibling(?XP,?YP).\n"
				+ "cousin(?X,?Y) :- parent(?XP,?X), parent(?YP,?Y), cousin(?XP,?YP).\n"
				+

				"?- cousin(?X,?Y).\n";

		HashSet<IPredicate> expectedSet = new HashSet<IPredicate>();
		expectedSet.add(Factory.BASIC.createPredicate("parent", 2));

		Parser parser = new Parser();
		parser.parse(program);

		CommonPredicateTagger tagger = new CommonPredicateTagger(
				parser.getRules(), parser.getQueries().get(0));
		System.out.println(expectedSet + " " + tagger.getMemoPredicates());
		assertEquals(true, expectedSet.containsAll(tagger.getMemoPredicates()));
		assertEquals(expectedSet.size(), tagger.getMemoPredicates().size());
	}

}
