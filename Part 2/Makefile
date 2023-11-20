all:
	jflex src/LexicalAnalyzer.flex
	jflex src/GrammarReader.flex
	javac -d bin -cp src/ src/*.java
	mkdir -p dist/
	jar cfe dist/Part2.jar Main -C bin .
	jar cfe dist/ProcessGrammar.jar ProcessGrammar -C bin .

clean:
	rm -f src/*.class
	rm -f bin/*.class
	rm -f dist/*.jar
	rm -f test/*.tex

.PHONY: test
test:
	java -jar dist/ProcessGrammar.jar more/PMPGrammar.txt
	java -jar dist/Part2.jar test/printTest.pmp
	java -jar dist/Part2.jar -wt test/printTest.tex test/printTest.pmp