<h1>IRIS Reasoner</h1>
<p>
	IRIS - Integrated Rule Inference System is an extensible 
	reasoning engine for expressive rule-based languages. 
	Currently IRIS supports the following features:
	<ul>
		<li>Safe datalog with (locally) stratified 'negation as failure'</li>
		<li>Function symbols</li>
		<li>Detection of un-safe rules <a href="/saferules">(algorithm description here)</a></li>
		<li>Detection of non-stratified programs with negation <a href="/stratification">(algorithm descriptions here)</a></li>
		<li>Extended and extensible set of built-in predicates</li>
		<li>Support for all the XML schema data types</li>
	</ul>
</p>

<p>
	The following evaluation strategies are currently supported: 
	<ul>
		<li>Naive</li>
		<li>Semi-naive</li>
		<li>Semi-naive with magic sets and sideways information passing strategy (SIPS)</li>
	</ul>
</p>

<p>
	To learn more about the theoretical results that the reasoner
	is based upon have a look at the <a href="foundations">theoretical results</a>. 
</p>
	
<h2>IRIS Applications</h2>

<p>
	IRIS is a available under the GNU lesser general
	public licence (LGPL).
	It has been developed with the aim to support reasoning over
	<a href="http://www.wsmo.org/wsml/wsml-syntax">WSML</a> ontologies, 
	but can also be used in many other contexts. See below for the 
	use cases we know of:
</p>

<ul>
	<li><a href="wsml2reasoner">WSML Reasoner</a></li>
	<li><a href="rdfsreasoner">RDFS Reasoner</a></li>
</ul>

<p>
	If you are using IRIS in another context, please inform us and we will 
	be pleased to describe your application on this website. 
</p>

<h2>News</h2>

<table>
	<tr valign=top>
		<td width=150>
			<p align=left>
				13 Feb 2008
			</p>
		</td>
		<td>
			<p align=left>
				IRIS <a href="download#v0_5_6">version 0.56</a> released.<br />
				This is an interim release to fix an important bug in the rule compiler
				and also offer greater flexibility in rule format using:<br />
				<ul>
					<li>constructed terms with equality, inequality and assignment</li>
					<li>positioning of negated ordinary predicates</li>
				</ul>
				The magic sets optimisation is still disabled for this release,
				but will be included in version 0.6
			</p>
		</td>
	</tr>
	<tr valign=top>
		<td width=150>
			<p align=left>
				01 Feb 2008
			</p>
		</td>
		<td>
			<p align=left>
				IRIS <a href="download#v0_5_5">version 0.55</a> released.<br />
				This is an interim release for function symbols and an all new,
				faster evaluation implementation.<br />
				Also included is a GUI user environment (org.deri.iris.DemoW),
				new built-in predicates (including a regular expression matcher)
				and numerous bug fixes.<br />
				However, the magic sets optimisation is disabled for this release,
				but will be included in version 0.6
			</p>
		</td>
	</tr>
	<tr valign=top>
		<td width=150>
			<p align=left>
				08 Nov 2007
			</p>
		</td>
		<td>
			<p align=left>
				IRIS <a href="download#v0_5">version 0.5</a> released.<br />
				More built-in predicates, local stratification and query containment.
				Fixes for magic sets (conjunctive queries).
				New behaviour for built-ins and inconsistent data types.
			</p>
		</td>
	</tr>

	<tr valign=top>
		<td width=150>
			<p align=left>
				19 Sep 2007
			</p>
		</td>
		<td>
			<p align=left>
				IRIS <a href="download#v0_4">version 0.4</a> released.<br />
				Some database integration work started,
				lots of modifications to the date/time data types and many bug fixes.
			</p>
		</td>
	</tr>

	<tr valign=top>
		<td width=150>
			<p align=left>
				13 Jul 2007
			</p>
		</td>
		<td>
			<p align=left>
				We have created an initial draft of our web site and introduced a <a href="snapshot">daily build</a> system.
			</p>
		</td>
	</tr>
</table>

