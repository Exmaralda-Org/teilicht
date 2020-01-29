<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.jar.Manifest,java.io.IOException"%>
<!DOCTYPE html>
<%
    String title = "TEILicht: IDS Web Services for TEI-encoded Transcriptions of Spoken Language";
    InputStream inputStream = getServletConfig().getServletContext()
            .getResourceAsStream("/META-INF/MANIFEST.MF");
    Manifest manifest = new Manifest(inputStream);
    String version = manifest.getMainAttributes()
            .getValue("Implementation-Version");
%>
<html>
<head>
<style type="text/css">
html, body {
	font-family: sans-serif;
}

.attention b {
	color: darkred;
}

.info b, .info a {
	color: darkgreen;
	font-weight: bold;
}

.service a.title {
	color: darkblue;
	font-weight: bold;
}

p.info {
	border-color: darkgreen;
}

p.attention {
	border-color: darkred;
}

p.attention, p.info {
	border-width: 3px;
	background: lightgrey;
	padding: 1ex;
}

div.footer {
	position: fixed;
	left: 0;
	bottom: 0;
	width: 100%;
	text-align: center;
	display: block;
	font-size: 87.5%;
	letter-spacing: 0.1ex;
	background-color: darkgrey;
}

div.outer_container {
	text-align: center;
	width: 100%;
}

div.mid_container {
	max-width: 61em;
}

div.mid_container {
	text-align: left;
	margin: 0 auto;
	display: block;
}

div.inner_footer {
	display: inline-block;
	max-width: 55em;
}

h1.title {
	text-align: center;
}
</style>
<meta charset="UTF-8">
<title><%=title%></title>
</head>
<body>
  <div class="outer_container">
    <div class="mid_container">
      <div class="inner_container">
        <h1 class="title"><%=title%></h1>
        <p>
          This page contains the <a
            href="https://en.wikipedia.org/wiki/Representational_state_transfer">RESTful
            webservices</a> for transcriptions of spoken data following the
          <a
            href="http://www.tei-c.org/release/doc/tei-p5-doc/en/html/TS.html">TEI
            guidelines</a>. In principle, target documents are those
          conforming to the ISO standard <a
            href="https://www.iso.org/standard/37338.html">ISO
            24624:2016(E)</a> ‘Language resource management – Transcription
          of spoken language’. Currently, we offer:
        </p>
        <p class="attention">
          (<b>Caveat:</b> The links to the services are not useful and
          are only present for ease of copying! <b>You must use the
            HTTP POST method to make use of the web services.</b>)
        </p>
        <ul class="service">
          <li><a class="title" href="services/text2iso">text2iso</a>
            – converting <a
            href="https://github.com/Exmaralda-Org/teispeechtools/blob/master/doc/Simple-EXMARaLDA.md">Plain
              Text</a> to ISO-TEI-annotated texts</li>
          <li><a class="title" href="services/segmentize">segmentize</a>
            – segmentation according to transcription conventions</li>
          <li><a class="title" href="services/guess">guess</a> –
            language-detection</li>
          <li><a class="title" href="services/normalize">normalize</a>
            – OrthoNormal-like Normalization</li>
          <li><a class="title" href="services/pos">pos</a> –
            POS-Tagging with the TreeTagger</li>
          <li><a class="title" href="services/align">align</a> –
            Pseudo-alignment using Phonetic Transcription or
            Orthographic Information</li>
          <li><a class="title" href="services/identify">identify</a>
            and <a class="title" href="services/unidentify">unidentify</a>
            – adding and removing XML IDs</li>
        </ul>
        <p class="info">
          Further information on the function of the services and their
          parameters can be found in <a
            href="https://github.com/Exmaralda-Org/teispeechtools">the
            documentation of the underlying library</a>.
        </p>
        <!-- Adjust if running locally: -->
      </div>
    </div>
    <div class="footer">
      <div class="inner_footer">
        <p>
          TEILicht is hosted by the <a href="http://www.ids-mannheim.de">Leibniz
            Institute for the German Language</a> (IDS) in Mannheim,
          Germany. For further information, please contact <a
            href="mailto:fisseni@ids-mannheim.de?subject=TEILicht">Bernhard
            Fisseni</a> (services) or <a
            href="mailto:thomas.schmidt@ids-mannheim.de?subject=TEILicht">Thomas
            Schmidt</a> (transcription, AGD/DGD curation workflow).
        </p>
        <p>
          TEILicht version <b><%=version%></b>
        </p>
      </div>
    </div>
  </div>
</body>
</html>
