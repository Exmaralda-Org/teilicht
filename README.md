---
author: Bernhard Fisseni
title: Normalization services for TEI XML files
---

# Purpose

This is a WEB service for applying orthographic normalization, mainly to transcripts 
of spoken data in [TEI](http://www.tei-c.org/release/doc/tei-p5-doc/en/html/TS.html)

It basically searches for `<w>` elements and applies normalization to their
[text content](https://www.w3schools.com/xml/prop_element_textcontent.asp). 


# Compilation

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


# Metadata

CMDI-Metadata are in `MetaData/OrthoNormal-metadata.xml`
