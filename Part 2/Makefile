# Makefile for building and running the project

# Compiler
JC = javac

# Flags
JFLAGS = -g

# Source directory
SRC_DIR = src

# Binary directory
BIN_DIR = bin

# More directory
MORE_DIR = more

# Test directory
TEST_DIR = test

# Main class
PROCESS_CLASS = ProcessGrammar
MAIN_CLASS = Main

# JFlex files
JFLEX_FILES = GrammarReader.flex LexicalAnalyzer.flex

# ProcessGrammar file
PROCESS_GRAMMAR_FILE = $(SRC_DIR)/$(MAIN_CLASS).java

# Source files
SOURCE_FILES = $(SRC_DIR)/GrammarReader.java $(SRC_DIR)/Grammar.java $(PROCESS_GRAMMAR_FILE)

# Target files
TARGET_FILES = $(BIN_DIR)/$(MAIN_CLASS).class

all: lexer compile grammar parser parseTree

lexer:
	jflex $(SRC_DIR)/GrammarReader.flex
	jflex $(SRC_DIR)/LexicalAnalyzer.flex

compile:
	$(JC) $(JFLAGS) -d $(BIN_DIR) $(SRC_DIR)/*.java

grammar:
	java -cp $(BIN_DIR) $(PROCESS_CLASS) $(MORE_DIR)/PMPGrammar.txt

parser:
	java -cp $(BIN_DIR) $(MAIN_CLASS) $(TEST_DIR)/printTest.pmp

parseTree:
	java -cp $(BIN_DIR) $(MAIN_CLASS) -wt $(TEST_DIR)/printTest.tex $(TEST_DIR)/printTest.pmp

clean:
	rm -rf $(BIN_DIR)/*.class
	rm -rf $(TEST_DIR)/*.tex
