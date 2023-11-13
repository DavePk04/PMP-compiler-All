all: run

build: src/Main.class

src/Main.class: src/Main.java
	javac src/Main.java

generate: src/GrammarReader.java

src/GrammarReader.java: src/GrammarReader.flex
	cd src && jflex GrammarReader.flex

run: build generate
	java -classpath src Main ../more/PMPGrammar.txt

clean:
	rm -f src/GrammarReader.java
	rm -f src/*.class
