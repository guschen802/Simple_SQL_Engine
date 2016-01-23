package edu.buffalo.cse562;

import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.schema.Column;

public interface SqlIterator {
	public LeafValue[] readNextTuple();
	public void reset();
	public Column[] getInputSchema();
}
