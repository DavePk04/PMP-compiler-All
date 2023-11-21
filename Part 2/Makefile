all:
	jflex src/LexicalAnalyzer.flex
	jflex src/GrammarReader.flex
	javac -d bin -cp src/ src/*.java
	mkdir -p dist/
	jar cfe dist/Part2.jar Main -C bin .
	jar cfe dist/ProcessGrammar.jar ProcessGrammar -C bin .

doc: $(wildcard src/*.java)
	javadoc -d doc/javadoc -cp bin src/*.java -Xdoclint:none 2> javadoc_warnings.log

clean:
	rm -f src/*.class
	rm -f bin/*.class
	rm -f dist/*.jar
	rm -f test/*.tex
	rm -f javadoc_warnings.log

.PHONY: test
test:
	java -jar dist/ProcessGrammar.jar -pat more/PMPGrammar.txt
	java -jar dist/ProcessGrammar.jar -wat more/Action-table.txt more/PMPGrammar.txt
	java -jar dist/Part2.jar test/printTest.pmp
	java -jar dist/Part2.jar -wt test/printTest.tex test/printTest.pmp
#	java -jar dist/Part2.jar test/euclid.pmp
#	java -jar dist/Part2.jar -wt test/euclid.tex test/euclid.pmp
