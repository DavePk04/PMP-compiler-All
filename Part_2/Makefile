# Define the base directory for the project
BASEDIR := ../Part_2

# Define the source and binary directories
SRCDIR := $(BASEDIR)/src
BINDIR := $(BASEDIR)/bin
MOREDIR := $(BASEDIR)/more
TESTDIR := $(BASEDIR)/test
DOCDIR := $(BASEDIR)/doc
DISTDIR := $(BASEDIR)/dist

# Define phony targets
.PHONY: all generatejavafromflex build processgrammar run javadoc removegeneratedfromflex clean dist

all: dist

generatejavafromflex:
	jflex -d $(SRCDIR) $(SRCDIR)/GrammarReader.flex
	jflex -d $(SRCDIR) $(SRCDIR)/LexicalAnalyzer.flex

build: generatejavafromflex
	javac -d $(BINDIR) $(SRCDIR)/*.java

processgrammar:
	java -cp $(BINDIR) ProcessGrammar $(MOREDIR)/PMPGrammar.txt

run: build processgrammar
	java -cp $(BINDIR) Main -wt $(TESTDIR)/euclid.tex $(TESTDIR)/euclid.pmp
	java -cp $(BINDIR) Main -wt $(TESTDIR)/printTest.tex $(TESTDIR)/printTest.pmp

javadoc: build
	javadoc -d $(DOCDIR) $(SRCDIR)/*.java

removegeneratedfromflex:
	rm -f $(SRCDIR)/GrammarReader.java
	rm -f $(SRCDIR)/LexicalAnalyzer.java

clean: removegeneratedfromflex
	rm -f $(BINDIR)/*.class
	rm -f $(TESTDIR)/*.tex
	rm -rf $(DOCDIR)/*

dist: build
	mkdir -p $(DISTDIR)
	jar cfe $(DISTDIR)/part2.jar Main -C $(BINDIR) .
