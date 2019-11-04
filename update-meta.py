#!/usr/bin/env python3
import sys
import pathlib
from csv import DictReader, excel_tab
from collections import namedtuple

from lxml import etree

Person = namedtuple("Person", ["name", "mail", "org", "url"])

IDS_MAIL = "{}@ids-mannheim.de"
IDS = "Leibniz-Institut für Deutsche Sprache"
IDS_URL = "http://www.ids-mannheim.de"

PEOPLE = (
    Person("Bernhard Fisseni", IDS_MAIL.format("fisseni"), IDS, IDS_URL),
    Person("Bernhard Fisseni", IDS_MAIL.format("thomas.schmidt"), IDS, IDS_URL)
)

XML_PARSER = etree.XMLParser(
    # dtd_validation=False,
    # load_dtd=False,
)

UTILITIES_DIR = pathlib.Path("..", "utilities", "src", "main", "resources")
LANGUAGES_639_1 = UTILITIES_DIR.joinpath("language-codes-two-letters.txt")
LANGUAGES_639_2 = UTILITIES_DIR.joinpath("language-codes-three-letters.txt")
LANGUAGES_639_3 = UTILITIES_DIR.joinpath("sil", "iso-639-3.tab")

NAMESPACE = "http://www.clarin.eu/cmd/1/profiles/clarin.eu:cr1:p_1320657629644"
NS_MAP = {"ns": NAMESPACE}

LANGUAGE_TAGS = set()
for fil in (LANGUAGES_639_1, LANGUAGES_639_2):
    LANGUAGE_TAGS.update(fil.read_text("UTF_8").split())
with LANGUAGES_639_3.open(encoding="UTF-8") as fil:
    LANGUAGE_TAGS.update(r["Id"] for r in DictReader(fil, dialect=excel_tab))
print(LANGUAGE_TAGS)


def create_lang(code):
    root = etree.Element(f"{{{NAMESPACE}}}ParameterValue")
    etree.SubElement(root, f"{{{NAMESPACE}}}Value").text = code
    etree.SubElement(root, f"{{{NAMESPACE}}}WebServiceArgValue").text = code
    return root


def get_values_empty(root):
    vals = root.find("ns:Values", namespaces=NS_MAP)
    if vals is None:
        vals = etree.SubElement(root, f"{{{NAMESPACE}}}Values")
    else:
        for el in vals:
            vals.remove(el)
    return vals


def fix_langs(root):
    lang_par = root.xpath(".//ns:Parameter[./ns:Name[string() = 'lang']]",
                          namespaces=NS_MAP)
    if not lang_par:
        sys.stderr.write("Language parameter missing, can't fix.\n")
        exit(1)
    else:
        lang_par = lang_par[0]
    vals = get_values_empty(lang_par)
    vals.extend(create_lang(c) for c in LANGUAGE_TAGS)


def create_creator(person):
    root = etree.Element(f"{{{NAMESPACE}}}Creator")
    contact = etree.SubElement(root, f"{{{NAMESPACE}}}Contact")
    etree.SubElement(contact, f"{{{NAMESPACE}}}Person").text = person.name
    etree.SubElement(contact, f"{{{NAMESPACE}}}Email").text = person.mail
    etree.SubElement(contact, f"{{{NAMESPACE}}}Organisation").text = person.org
    etree.SubElement(contact, f"{{{NAMESPACE}}}Url").text = person.url
    return root


def fix_creators(root):
    creators = root.xpath(".//ns:Creators", namespaces=NS_MAP)[0]
    for el in creators:
        creators.remove(el)
    creators.extend(create_creator(p) for p in PEOPLE)


def make_new_name(old_name):
    old_path = pathlib.Path(old_name)
    new_fn = old_path.with_name(old_path.stem + ".new.xml")
    return new_fn


for xml_file in sys.argv[1:]:
    with open(xml_file) as in_file:
        meta_tree = etree.parse(in_file, parser=XML_PARSER)
        fix_langs(meta_tree)
        fix_creators(meta_tree)
        with open(make_new_name(xml_file), "w", encoding="UTF-8") as out_file:
            out_file.write(etree.tounicode(meta_tree))
        print(etree.tounicode(meta_tree))
