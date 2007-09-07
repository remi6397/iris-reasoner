<h1>IRIS Demo</h1>

<p>Enter a datalog program in the textarea below, choose a evaluation 
method and press "Submit Query" to test IRIS. For help on the IRIS datalog syntax, see <a href="/syntax">here</a>.
This Demo uses the latest stable iris release. You also can <a href="/demo?version=snapshot">run your queries against the current snapshot</a> (not guaranteed to work!). Please note that we placed a 30 seconds timeout per query to avoid overloading our server</p>
<form method="post" target="resultframe" action="/demoapp/?<?echo $_SERVER[QUERY_STRING]?>">
	<textarea style="border:solid black 1px; background-color:#eee; width:100%; height:200px" name="program">man('homer').
woman('marge').
hasSon('homer','bart').
isMale(?x) :- man(?x).
isFemale(?x) :- woman(?x).
isMale(?y) :- hasSon(?x,?y).

?-isMale(?x).</textarea><br/>
        <p style="margin-top:0px;margin-bottom:5px;">evaluation method: 
	<select name="method">
	 	<option value="1">naive</option>
		<option value="2">semi-naive</option>
		<option value="3">semi-naive with magic sets</option>
	</select>
	<input type="submit" value="Submit Query"></p>
</form>
<iframe style="border:solid white 1px; width:100%" name="resultframe" src="/demoapp/"></iframe>
