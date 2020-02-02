---
author: Bernhard Fisseni
title: Normalization services for TEI XML files
---

# Purpose

This is a WEB service for applying orthographic normalization, mainly to transcripts 
of spoken data in [TEI](http://www.tei-c.org/release/doc/tei-p5-doc/en/html/TS.html)

It uses the functionality provided by the
[TEI Speech Tools library](https://github.com/Exmaralda-Org/teispeechtools);
for the documentation of services and parameters, see [there](https://github.com/Exmaralda-Org/teispeechtools).


## Using TEILicht on-line

A [running instance](https://clarin.ids-mannheim.de/teilicht) is provided by the
[Leibniz Institute for the German Language](https://www.ids-mannheim.de).


# Compilation and Hosting TEILicht Yourself

## Dependencies

Besides the dependencies available via
[Maven](https://maven.apache.org/), needs [some utility
functions](https://github.com/teoric/java-utilities). These can be
locally [`mvn
install`ed](https://maven.apache.org/plugins/maven-install-plugin/usage.html).


## Compilation

    mvn clean install

and then deploy the `war` in `target/` to your servlet container of choice.


# Calling the service

The normalization service will be at `<root>/normalize`, and expects data via `POST`, 
accepting/returning the MIME types: 

- `application/tei+xml;format-variant=tei-iso-spoken;tokenized=1`
- `application/tei+xml;format-variant=tei-dta;tokenized=1`
- `application/tei+xml`
- `application/xml`


# WADL

The
[WADL](https://en.wikipedia.org/wiki/Web_Application_Description_Language)
file is at
`SERVICE_NAME/services/application.wadl`, e.g. at IDS:
<https://clarin.ids-mannheim.de/teilicht/services/application.wadl>.
