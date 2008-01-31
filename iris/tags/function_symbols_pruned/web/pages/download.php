<style type="text/css">ul{margin-top:-20px;}</style>


<h1>Download</h1>

<p>IRIS releases are available at <a href="http://sourceforge.net/project/platformdownload.php?group_id=167309">sourceforge</a>.</p>

<h2>Release History</h2>

<p><b><a id="v5" href="http://sourceforge.net/project/showfiles.php?group_id=167309&package_id=190271&release_id=552735">v0.5</a></b> 2007-11-08</p> 
<p>New features</p>
<ul>
	<li>1798276 - Magic Sets evaluation allows conjunctive queries</li>
	<li>1810724 - Built-in predicates must support all data types.</li>
	<li>1810725 - Negated built-ins must behave as NAF</li>
	<li>1803612 - Improve unsafe rule detection and throw specific exception.</li>
	<li>1803623 - Throw specific exception for non-stratified program.</li>
	<li>1803626 - IRIS parser should have better failure semantics.</li>
	<li>1811460 - Create MODULUS built-in predicate</li>
	<li>1811468 - Create missing unary built-in predicates.</li>
	<li>1812263 - Create new SAME_TYPE built-in.</li>
	<li>1822569 - Implement Local Stratification algorithm</li>
	<li>1773196 - Query Containment stage 1</li>
</ul>
	
<p>Bug fixes</p>
<ul>
	<li>1822055 - Magic Sets gives different results to Naive and Semi-Naive</li>
	<li>1814410 - Some term types accept invalid values.</li>
	<li>1810126 - Queries of an adorned program are not adorned</li>
	<li>1808896 - Numeric type coercion error</li>
	<li>1808292 - Floating point round-off error</li>
	<li>1807797 - Negated built-ins do not evaluate correctly.</li>
	<li>1804145 - Infinite loop with unary built-in (unsafe rule problem)</li>
	<li>1794766 - Incorrect results with (positive) built-in predicate</li>
</ul>

<p>Maintenance updates</p>
<ul>
	<li>1815907 - ILiteral extends Atom, but this is a has-a relationship</li>
	<li>1815904 - Query uses Body instead of a List</li>
	<li>1814958 - tuple, head, body and query? should be lists</li>
	<li>1814955 - All objects up to Rule and AdornedRule should be immutable</li>
	<li>1817654 - Re-implement Program</li>
	<li>1814955 - All objects up to Rule and AdornedRule should be immutable</li>
	<li>1815048 - Allow predicate names to have underscores and digits.</li>
	<li>1814418 - toString() for some terms types is too verbose.</li>
	<li>1812121 - Revise safe-rule checks.</li>
	<li>1811653 - Rename UNEQUAL to NOT_EQUAL</li>
	<li>1811425 - Refactor built-in predicates</li>
	<li>1775365 - Create IRIS API guide</li>
	<li>1775368 - Create IRIS 'datalog' support guide.</li>
</ul>


<p><b><a id="v4" href="http://sourceforge.net/project/showfiles.php?group_id=167309&package_id=190271&release_id=540609">v0.4</a></b> 2007-09-19</p>
<p>New features</p>
<ul>
	<li>1773193 - IRIS is now able to persist facts in a relational database</li>
	<li>1773182 - Add subtraction builtin for dateTime</li>
</ul>
	
<p>Bug fixes</p>
<ul>
	<li>1716628 - MiscOps.statify(...) method is broken</li>
	<li>1749168 - satisfiability checks broken</li>
	<li>1778705 - it is possible to specify inconsistent timezones</li>
	<li>1780055 - Duration should hold the timespan in millis</li>
	<li>1792385 - DateTime datatype handles months incorrectly</li>
	<li>1792822 - Exceptionally long evaluation time</li>
</ul>
	
<p>Known problems</p>
<ul>
    <li>1798276 - Magic Sets evaluation does not allow conjunctive queries. (This will be fixed for the next release.)</li>
</ul>

<p><b><a id="v3" href="http://sourceforge.net/project/showfiles.php?group_id=167309&package_id=190271&release_id=516287">v0.3</a></b> 2007-06-15</p>  
<ul>
	<li>enabled multiple programs per vm</li>
</ul>

<p><b><a id="v2" href="http://sourceforge.net/project/showfiles.php?group_id=167309&package_id=190271&release_id=485199">v0.2</a></b> 2007-02-09</p> 
<ul>
	<li>support for all data types supported by WSML</li>
	<li>a number of built-ins implemented</li>
	<li>added the IndexingOnTheFlyRelation and corresponding implementations</li>
	<li>added the IRelationFactory interface</li>
	<li>modified the IBuiltinInterface</li>
	<li>reimplemented the Complementor class</li>
	<li>added the possibility to make custom built-ins</li>
	<li>bugfixes</li>
</ul>

<p><b><a id="v1" href="http://sourceforge.net/project/showfiles.php?group_id=167309&package_id=190271&release_id=484835">v0.1</a></b> 2007-02-08</p>
<ul><li>initial release</li></ul>

<h2>Daily Snapshot</h2>
<p>We also offer a <a href="snapshot">daily snapshot</a>.
</p>