<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% String title="IDS Web Services for TEI-encoded Transcriptions of Spoken Language"; %>
<html>
<head>
<meta charset="UTF-8">
<title><%= title %></title>
</head>
<body>
<h1><%= title %></h1>
<p>This page contains the <a href="https://en.wikipedia.org/wiki/Representational_state_transfer">RESTful webservices</a> for
transcriptions of spoken data following the <a href="http://www.tei-c.org/release/doc/tei-p5-doc/en/html/TS.html">TEI guidelines</a>.  Currently, we have:</p>
<ul>
  <li>a service for normalizing TEI input at <a href="services/normalize"><code>services/normalize</code></a></li>
</ul>
<p>(Caveat: Links to services are not clickable and are only present for ease of copying!)</p>
</body>
</html>