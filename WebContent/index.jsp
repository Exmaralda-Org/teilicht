<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.jar.Manifest"%>
<!DOCTYPE html>
<%
    String title = "TEILicht: IDS Web Services for TEI-encoded Transcriptions of Spoken Language";
			InputStream inputStream = getServletConfig().getServletContext()
					.getResourceAsStream("/META-INF/MANIFEST.MF");
			Manifest manifest = new Manifest(inputStream);
			String version = manifest.getMainAttributes().getValue("Implementation-Version");
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

section {
  padding-top: 1.5em;
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

.footer_item {
  display: table-cell;
  margin: auto;
  vertical-align: middle;
  font-size: 87.5%;
}

footer {
  display: table-row;
  margin-top: 2em;
  text-align: center;
  letter-spacing: 0.1ex;
  /* background-color: darkgrey; */
  border-top-style: solid;
  padding: 1em;
}

p.bib {
  margin-left: 2em;
  text-indent: -2em;
}

h1.title {
  text-align: center;
}
</style>
<script type="text/javascript">
    function toClipboard(event, el) {
        event.preventDefault();
        navigator.clipboard
                .writeText(el.href)
                .then(
                        function () {
                            alert("The full link to the service is '"
                                    + el.href
                                    + "'. It has been copied to the clipboard. Use it with the http POST method.");
                        },
                        function () {
                            alert("The full link to the service is '" + el.href
                                    + "'.  Use it with the http POST method.");
                        });
    }
</script>
<meta charset="UTF-8">
<title><%=title%></title>
</head>
<body>
  <main>
    <h1 class="title"><%=title%></h1>
    <section>
      <p>
        This page contains the <a
          href="https://en.wikipedia.org/wiki/Representational_state_transfer">RESTful
          webservices</a> for transcriptions of spoken data following the <a
          href="http://www.tei-c.org/release/doc/tei-p5-doc/en/html/TS.html">TEI
          guidelines</a>. In principle, target documents are those
        conforming to the ISO standard <a
          href="https://www.iso.org/standard/37338.html">ISO
          24624:2016(E)</a> <em>Language resource management –
          Transcription of spoken language</em>. The services are built on
        the <a href="https://github.com/Exmaralda-Org/teispeechtools">library
          <b class="library">teispeechtools</b>
        </a>; the <a href="https://github.com/Exmaralda-Org/teilicht">source
          code of the services</a> is available on GitHub. Currently, we
        offer:
      </p>
      <p class="attention">
        <b>Caveat:</b> The links to the services are not usable in the
        browser and are only present for ease of copying and pasting! <b>You
          must use the HTTP POST method to make use of the web services.</b>
        Given a <a
          href="https://developer.mozilla.org/en-US/docs/Web/API/Clipboard/writeText">modern
          browser</a>, clicking on the links copies the URL to the
        clipboard.
      </p>
      <ul class="service">
        <li><b class="title">text2iso</b>: converting plain text in
          <a
          href="https://github.com/Exmaralda-Org/teispeechtools/blob/master/doc/Simple-EXMARaLDA.md">Simple
            EXMARaLDA format</a> to ISO-TEI-annotated texts (local URL: <a
          href="services/text2iso" class="local_link"
          onclick="toClipboard(event, this)">services/text2iso</a>) <a
          class="doc"
          href="https://github.com/Exmaralda-Org/teispeechtools#user-content-plain-text-to-iso-tei-annotated-texts-cli-command-text2iso">[library
            documentation]</a></li>
        <li><b class="title">segmentize</b>: segmentation according
          to transcription conventions (local URL: <a
          href="services/segmentize" class="local_link"
          onclick="toClipboard(event, this)">services/segmentize</a>) <a
          class="doc"
          href="https://github.com/Exmaralda-Org/teispeechtools#user-content-segmentation-according-to-transcription-convention-cli-command-segmentize">[library
            documentation]</a></li>
        <li><b class="title">text2seg</b>: Combines the <b
          class="title">text2iso</b> and <b class="title">segmentize</b>.
          (local URL: <a href="services/segmentize" class="local_link"
          onclick="toClipboard(event, this)">services/segmentize</a>).
          See <a class="doc"
          href="https://github.com/Exmaralda-Org/teispeechtools#user-content-plain-text-to-iso-tei-annotated-texts-cli-command-text2iso">[documentation
            of <kbd>text2iso</kbd>]
        </a> and <a class="doc"
          href="https://github.com/Exmaralda-Org/teispeechtools#user-content-segmentation-according-to-transcription-convention-cli-command-segmentize">[documentation
          of <kbd>segmentize</kbd>]</a> for parameters.</li>
        <li><b class="title">guess</b>: language-detection (local
          URL: <a href="services/guess" class="local_link"
          onclick="toClipboard(event, this)">services/guess</a>) <a
          class="doc"
          href="https://github.com/Exmaralda-Org/teispeechtools#user-content-language-detection-cli-command-guess">[library
            documentation]</a></li>
        <li><b class="title">normalize</b>: OrthoNormal-like
          Normalization (local URL: <a href="services/normalize"
          class="local_link" onclick="toClipboard(event, this)">services/normalize</a>)
          <a class="doc"
          href="https://github.com/Exmaralda-Org/teispeechtools#user-content-orthonormal-like-normalization-command-normalize">[library
            documentation]</a></li>
        <li><b class="title">pos</b>: POS-Tagging with the
          TreeTagger (local URL: <a href="services/pos"
          class="local_link" onclick="toClipboard(event, this)">services/pos</a>)
          <a class="doc"
          href="https://github.com/Exmaralda-Org/teispeechtools#user-content-pos-tagging-with-the-treetagger-command-pos">[library
            documentation]</a></li>
        <li><b class="title">align</b>: Pseudo-alignment using
          Phonetic Transcription or Orthographic Information (local URL:
          <a href="services/align" class="local_link"
          onclick="toClipboard(event, this)">services/align</a>) <a
          class="doc"
          href="https://github.com/Exmaralda-Org/teispeechtools#user-content-pseudo-alignment-using-phonetic-transcription-or-orthographic-information-command-align">[library
            documentation]</a></li>
        <li><b class="title">identify</b> and <b class="title">unidentify</b>:
          adding and removing XML IDs (local URL: <a
          href="services/identify" class="local_link"
          onclick="toClipboard(event, this)">services/identify</a>) [<a
          class="doc"
          href="https://github.com/Exmaralda-Org/teispeechtools#user-content-adding-and-removing-xmlid-command-identify-and-unidentify">library
            documentation</a>] and (local URL: <a href="services/unidentify"
          class="local_link" onclick="toClipboard(event, this)">services/unidentify</a>)
          [<a class="doc"
          href="https://github.com/Exmaralda-Org/teispeechtools#user-content-adding-and-removing-xmlid-command-identify-and-unidentify">library
            documentation</a>]</li>
      </ul>
      <p class="info">
        Further information on the function of the services and their
        parameters can be found in <a
          href="https://github.com/Exmaralda-Org/teispeechtools">the
          documentation of the underlying library</a>.
      </p>
    </section>
    <section>
      <h2>Background</h2>
      <p>
        TEILicht was developed within the CLARIN activities of the <a
          href="http://www.ids-mannheim.de">Leibniz Institute for
          the German Language</a> (IDS).
      </p>
      <p class="bib">
        <a href="https://www.uni-due.de/~hg0132">Bernhard Fisseni</a>, <a
          href="https://www1.ids-mannheim.de/prag/personal/schmidtthomas.html">Thomas
          Schmidt</a> (2019): “CLARIN Web Services for TEI-annotated
        Transcripts of Spoken Language”. In Kiril Simov, Maria Eskevich
        (eds.): <a
          href="https://office.clarin.eu/v/CE-2019-1512_CLARIN2019_ConferenceProceedings.pdf#page=43"><em>Proceedings
            of the CLARIN Annual Conference 2019</em></a>. <a
          href="http://www.clarin.eu">CLARIN ERIC</a>: <a
          href="https://www.leipzig.de/">Leipzig</a>, pp. 36–39.
      </p>
    </section>
  </main>
  <!-- Adjust if running locally: -->
  <footer>
    <div class="footer_item">
      <a href="https://www.clarin.eu"><img width="100px"
        src="images/clarin-eu-logo.png" alt="CLARIN-EU" border="0" /></a>
    </div>
    <div class="footer_item">
      <p>
        TEILicht is hosted by the <a href="http://www.ids-mannheim.de">Leibniz
          Institute for the German Language</a> (IDS) in <a
          href="https://www.mannheim.de">Mannheim</a>, <a
          href="https://en.wikipedia.org/wiki/Germany">Germany</a>. For
        further information, please contact <a
          href="mailto:fisseni@ids-mannheim.de?subject=TEILicht">Bernhard
          Fisseni</a> (services) or <a
          href="mailto:thomas.schmidt@ids-mannheim.de?subject=TEILicht">Thomas
          Schmidt</a> (transcription, AGD/DGD curation workflow).
      </p>
      <p>
        TEILicht version <b><%=version%></b>
      </p>
    </div>
    <div class="footer_item">
      <a href="https://www.clarin-d.net/"><img width="120px"
        src="images/clarin-d-logo.png" alt="CLARIN-D" border="0" /></a>
    </div>
  </footer>
</body>
</html>
