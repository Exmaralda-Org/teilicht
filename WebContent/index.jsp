<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    String title = "TEILicht: IDS Web Services for TEI-encoded Transcriptions of Spoken Language";
%>
<html>
<head>
<meta charset="UTF-8">
<title><%=title%></title>
</head>
<body>
  <h1><%=title%></h1>
  <p>This page contains the <a href="https://en.wikipedia.org/wiki/Representational_state_transfer">RESTful webservices</a> for transcriptions of spoken data following the <a href="http://www.tei-c.org/release/doc/tei-p5-doc/en/html/TS.html">TEI guidelines</a>. In principle, target documents are those conforming to the ISO standard <a href="https://www.iso.org/standard/37338.html">ISO 24624:2016(E)</a> ‘Language resource management – Transcription of spoken language’. Currently, we offer:</p>
  <p>(<b>Caveat:</b> The links to the services are not useful and are only present for ease of copying! <b>You must use the HTTP POST method to make use of the web services.</b>)</p>
  <ul>
    <li><a href="services/text2iso">text2iso</a> – converting <a href="https://github.com/Exmaralda-Org/teispeechtools/blob/master/doc/Simple-EXMARaLDA.md">Plain Text</a> to ISO-TEI-annotated texts</li>
    <li><a href="services/segmentize">segmentize</a> – segmentation according to transcription conventions</li>
    <li><a href="services/guess">guess</a> – language-detection</li>
    <li><a href="services/normalize">normalize</a> – OrthoNormal-like Normalization</li>
    <li><a href="services/pos">pos</a> – POS-Tagging with the TreeTagger</li>
    <li><a href="services/align">align</a> – Pseudo-alignment using Phonetic Transcription or Orthographic Information</li>
    <li><a href="services/identify">identify</a> and <a href="services/unidentify">unidentify</a> – adding and removing XML IDs</li>
  </ul>
  <p>Further information on the function of the services and their
      parameters can be found in <a
          href="https://github.com/Exmaralda-Org/teispeechtools">the
          documentation of the underlying library</a>.</p>
</body>
</html>
