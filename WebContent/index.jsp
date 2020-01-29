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

tt, code, pre, kbd, var, a.local_link {
	font-family: "Fira Mono", monospace;
}

.attention b {
	color: darkred;
}

.info b, .info a, a.doc {
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
	margin-bottom: 10em
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
          <b>Caveat:</b> The links to the services are not useful and
          are only present for ease of copying and pasting! <b>You
            must use the HTTP POST method to make use of the web
            services.</b>
        </p>
        <ul class="service">
          <li><b class="title">text2iso</b> (local URL: <a
            href="services/text2iso" class="local_link">services/text2iso</a>)
            <a class="doc"
            href="https://github.com/Exmaralda-Org/teispeechtools#user-content-plain-text-to-iso-tei-annotated-texts-cli-command-text2iso">[library
              documentation]</a> – converting <a
            href="https://github.com/Exmaralda-Org/teispeechtools/blob/master/doc/Simple-EXMARaLDA.md">Plain
              Text</a> to ISO-TEI-annotated texts</li>
          <li><b class="title">segmentize</b> (local URL: <a
            href="services/segmentize" class="local_link">services/segmentize</a>)
            <a class="doc"
            href="https://github.com/Exmaralda-Org/teispeechtools#user-content-segmentation-according-to-transcription-convention-cli-command-segmentize">[library
              documentation]</a> – segmentation according to transcription
            conventions</li>
          <li><b class="title">guess</b> (local URL: <a
            href="services/guess" class="local_link">services/guess</a>)
            <a class="doc"
            href="https://github.com/Exmaralda-Org/teispeechtools#user-content-language-detection-cli-command-guess">[library
              documentation]</a> – language-detection</li>
          <li><b class="title">normalize</b> (local URL: <a
            href="services/normalize" class="local_link">services/normalize</a>)
            <a class="doc"
            href="https://github.com/Exmaralda-Org/teispeechtools#user-content-orthonormal-like-normalization-command-normalize">[library
              documentation]</a> – OrthoNormal-like Normalization</li>
          <li><b class="title">pos</b> (local URL: <a
            href="services/pos" class="local_link">services/pos</a>) <a
            class="doc"
            href="https://github.com/Exmaralda-Org/teispeechtools#user-content-pos-tagging-with-the-treetagger-command-pos">[library
              documentation]</a> – POS-Tagging with the TreeTagger</li>
          <li><b class="title">align</b> (local URL: <a
            href="services/align" class="local_link">services/align</a>)
            <a class="doc"
            href="https://github.com/Exmaralda-Org/teispeechtools#user-content-pseudo-alignment-using-phonetic-transcription-or-orthographic-information-command-align">[library
              documentation]</a> – Pseudo-alignment using Phonetic
            Transcription or Orthographic Information</li>
          <li><b class="title">identify</b> (local URL: <a
            href="services/identify" class="local_link">services/identify</a>)
            [no documentation] and <b class="title">unidentify</b>
            (local URL: <a href="services/unidentify" class="local_link">services/unidentify</a>)
            [no documentation] – adding and removing XML IDs</li>
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
