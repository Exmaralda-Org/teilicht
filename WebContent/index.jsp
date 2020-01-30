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
<link rel="stylesheet" type="text/css"
  href="https://code.cdn.mozilla.net/fonts/fira.css">
<style type="text/css">
html, body {
	font-family: "Fira Sans", sans-serif;
}

body {
	display: flex;
	min-height: 100vh;
	margin: 0 auto;
	flex-direction: column;
	justify-content: center;
	align-items: center; /*NEW*/
}

main {
	min-height: 5em;
	flex: 1;
}

main, footer {
	max-width: 55em;
}

tt, code, pre, kbd, var, a.local_link {
	font-family: "Fira Mono", monospace;
}

.attention b {
	color: darkred;
}

.info b, .info a, a.doc, b.library {
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

footer {
	margin-top: 2em;
	text-align: center;
	font-size: 87.5%;
	letter-spacing: 0.1ex;
	background-color: darkgrey;
}

h1.title {
	text-align: center;
}
</style>
<meta charset="UTF-8">
<title><%=title%></title>
</head>
<body>
  <main>
    <h1 class="title"><%=title%></h1>
    <p>
      This page contains the <a
        href="https://en.wikipedia.org/wiki/Representational_state_transfer">RESTful
        webservices</a> for transcriptions of spoken data following the <a
        href="http://www.tei-c.org/release/doc/tei-p5-doc/en/html/TS.html">TEI
        guidelines</a>. In principle, target documents are those conforming
      to the ISO standard <a
        href="https://www.iso.org/standard/37338.html">ISO
        24624:2016(E)</a> <em>Language resource management –
        Transcription of spoken language</em>. The services are built on the
      <a href="https://github.com/Exmaralda-Org/teispeechtools">library
        <b class="library">teispeechtools</b>
      </a>. Currently, we offer:
    </p>
    <p class="attention">
      <b>Caveat:</b> The links to the services are not usable in the
      browser and are only present for ease of copying and pasting! <b>You
        must use the HTTP POST method to make use of the web services.</b>
    </p>
    <ul class="service">
      <li><b class="title">text2iso</b>: converting <a
        href="https://github.com/Exmaralda-Org/teispeechtools/blob/master/doc/Simple-EXMARaLDA.md">Plain
          Text</a> to ISO-TEI-annotated texts (local URL: <a
        href="services/text2iso" class="local_link">services/text2iso</a>)
        <a class="doc"
        href="https://github.com/Exmaralda-Org/teispeechtools#user-content-plain-text-to-iso-tei-annotated-texts-cli-command-text2iso">[library
          documentation]</a></li>
      <li><b class="title">segmentize</b>: segmentation according
        to transcription conventions (local URL: <a
        href="services/segmentize" class="local_link">services/segmentize</a>)
        <a class="doc"
        href="https://github.com/Exmaralda-Org/teispeechtools#user-content-segmentation-according-to-transcription-convention-cli-command-segmentize">[library
          documentation]</a></li>
      <li><b class="title">guess</b>: language-detection (local
        URL: <a href="services/guess" class="local_link">services/guess</a>)
        <a class="doc"
        href="https://github.com/Exmaralda-Org/teispeechtools#user-content-language-detection-cli-command-guess">[library
          documentation]</a></li>
      <li><b class="title">normalize</b>: OrthoNormal-like
        Normalization (local URL: <a href="services/normalize"
        class="local_link">services/normalize</a>) <a class="doc"
        href="https://github.com/Exmaralda-Org/teispeechtools#user-content-orthonormal-like-normalization-command-normalize">[library
          documentation]</a></li>
      <li><b class="title">pos</b>: POS-Tagging with the TreeTagger
        (local URL: <a href="services/pos" class="local_link">services/pos</a>)
        <a class="doc"
        href="https://github.com/Exmaralda-Org/teispeechtools#user-content-pos-tagging-with-the-treetagger-command-pos">[library
          documentation]</a></li>
      <li><b class="title">align</b>: Pseudo-alignment using
        Phonetic Transcription or Orthographic Information (local URL: <a
        href="services/align" class="local_link">services/align</a>) <a
        class="doc"
        href="https://github.com/Exmaralda-Org/teispeechtools#user-content-pseudo-alignment-using-phonetic-transcription-or-orthographic-information-command-align">[library
          documentation]</a></li>
      <li><b class="title">identify</b> and <b class="title">unidentify</b>:
        adding and removing XML IDs (local URL: <a
        href="services/identify" class="local_link">services/identify</a>)
        [<a
        class="doc"
        href="https://github.com/Exmaralda-Org/teispeechtools#user-content-adding-and-removing-xmlid-command-identify-and-unidentify">library documentation</a>] and (local URL: <a href="services/unidentify"
        class="local_link">services/unidentify</a>) [<a
        class="doc"
        href="https://github.com/Exmaralda-Org/teispeechtools#user-content-adding-and-removing-xmlid-command-identify-and-unidentify">library documentation</a>]</li>
    </ul>
    <p class="info">
      Further information on the function of the services and their
      parameters can be found in <a
        href="https://github.com/Exmaralda-Org/teispeechtools">the
        documentation of the underlying library</a>.
    </p>
  </main>
  <!-- Adjust if running locally: -->
  <footer>
    <p>
      TEILicht is hosted by the <a href="http://www.ids-mannheim.de">Leibniz
        Institute for the German Language</a> (IDS) in Mannheim, Germany.
      For further information, please contact <a
        href="mailto:fisseni@ids-mannheim.de?subject=TEILicht">Bernhard
        Fisseni</a> (services) or <a
        href="mailto:thomas.schmidt@ids-mannheim.de?subject=TEILicht">Thomas
        Schmidt</a> (transcription, AGD/DGD curation workflow).
    </p>
    <p>
      TEILicht version <b><%=version%></b>
    </p>
  </footer>
</body>
</html>
