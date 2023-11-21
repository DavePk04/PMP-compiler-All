public class    Symbol{
	public static final int UNDEFINED_POSITION = -1;
	public static final Object NO_VALUE = null;
	private Terminal terminal = null;

	private NonTerminal nonTerminal = null;
	private final Object value;
	private final int line,column;

	public Symbol(Terminal term) {
		this.terminal = term;
		this.nonTerminal = null;

		this.line	= UNDEFINED_POSITION;
		this.column	= UNDEFINED_POSITION;
		this.value	= NO_VALUE;
	}

	public Symbol(NonTerminal nonTerm) {
		this.nonTerminal = nonTerm;
		this.terminal = null;

		this.line	= UNDEFINED_POSITION;
		this.column	= UNDEFINED_POSITION;
		this.value	= NO_VALUE;
	}


	public Symbol(Terminal unit,int line,int column,Object value){
		this.terminal = unit;
		this.line	= line+1;
		this.column	= column;
		this.value	= value;
	}

	public Symbol(Terminal unit,int line,int column){
		this(unit,line,column,NO_VALUE);
	}

	public Symbol(Terminal unit,int line){
		this(unit,line,UNDEFINED_POSITION,NO_VALUE);
	}

	public Symbol(Terminal unit,Object value){
		this(unit,UNDEFINED_POSITION,UNDEFINED_POSITION,value);
	}

	public boolean isTerminal(){
		return this.terminal != null;
	}

	public boolean isNonTerminal(){
		return this.terminal == null;
	}

	public Terminal getTerminal(){
		return this.terminal;
	}

	public Object getValue(){
		return this.value;
	}

	public int getLine(){
		return this.line;
	}

	public int getColumn(){
		return this.column;
	}
	public NonTerminal getNonTerminal() {
		return nonTerminal;
	}

	@Override
	public int hashCode(){
		final String value	= this.value != null? this.value.toString() : "null";
		final String type		= this.terminal != null? this.terminal.toString()  : "null";
		return new String(value+"_"+type).hashCode();
	}

	@Override
	public String toString(){
		if(this.isTerminal()){
			final String value	= this.value != null? this.value.toString() : "null";
			final String type		= this.terminal != null? this.terminal.toString()  : "null";
			return String.format("token: %-15slexical unit: %s", value, type);
		}
		return "Non-terminal symbol";
	}


	public boolean isEpsilon() {
		return terminal.equals(Terminal.EPSILON);
	}
}
