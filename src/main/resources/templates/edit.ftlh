<#include "_head.ftl">

  <div class="row">
    <div class="col-xl-2 col-md-3 col-sm-4">
		<nav class="m-t-2 nav nav-pills nav-stacked" id="input-file-links"></nav>

	    <button type="button" class="m-y-2 btn btn-outline-primary btn-block" id="action-compile">Compile</button>
    	
		<nav class="nav nav-pills nav-stacked" id="output-file-links"></nav>
    </div>
    <div class="col-xl-10 col-md-9 col-sm-8" style="padding-left:1em">
      <textarea id="code" name="code" style="height:auto"></textarea>
      <img id="file-img" class="hidden" />
      <p id="file-desc" class="text-muted"></p>
    </div>
  </div>
  <div class="text-xs-center">

  </div>

<link rel="stylesheet" href="/static/codemirror/lib/codemirror.css">
<script src="/static/codemirror/lib/codemirror.js"></script>
<script src="/static/codemirror/addon/fold/foldcode.js"></script>
<script src="/static/codemirror/addon/fold/foldgutter.js"></script>
<script src="/static/codemirror/addon/fold/xml-fold.js"></script>
<script src="/static/codemirror/addon/fold/indent-fold.js"></script>
<link rel="stylesheet" href="/static/codemirror/addon/fold/foldgutter.css">

<script src="/static/codemirror/addon/display/fullscreen.js"></script>
<link rel="stylesheet" href="/static/codemirror/addon/display/fullscreen.css">

<script src="/static/codemirror/mode/turtle/turtle.js"></script>
<script src="/static/codemirror/mode/xml/xml.js"></script>

<link rel="stylesheet" href="/static/pcdemo.css">

<script>
	var scenario = ${filesJson};
</script>

<script src="/static/pcdemo.js"></script>

<#include "_tail.ftl">
