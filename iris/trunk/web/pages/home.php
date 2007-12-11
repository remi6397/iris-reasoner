<h1>IRIS Reasoner</h1>
<p>
	IRIS - Integrated Rule Inference System is an extensible 
	reasoning engine for expressive rule-based languages. 
	Currently IRIS supports the following features:
	<ul>
		<li>Safe datalog with (locally) stratified 'negation as failure'</li>
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
<p>
<tt>2007-07-13</tt> We have created an initial draft of our web site and introduced a <a href="snapshot">daily build</a> system
</p>
