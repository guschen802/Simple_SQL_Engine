package edu.buffalo.cse562;

import net.sf.jsqlparser.expression.LeafValue;
import net.sf.jsqlparser.schema.Column;

public class SimpleJoinIterator implements SqlIterator {
	private SqlIterator mLeft;
	private SqlIterator mRight;
	private LeafValue[] mLeftTuple;
	private Column[] mInputSchema;
	public SimpleJoinIterator(SqlIterator left,SqlIterator right) {
		super();
		this.mLeft = left;
		this.mRight = right;
		this.mLeftTuple = mLeft.readNextTuple();
		
		Column[] leftColumn = mLeft.getInputSchema();
		Column[] rightColumn = mRight.getInputSchema();
		mInputSchema = new Column[leftColumn.length + rightColumn.length];
		int index1 = 0;
		int index2 = 0;
		for(int i =0; i< mInputSchema.length;i++){
			if (index1 < mLeftTuple.length) {
				mInputSchema[i] = leftColumn[index1];
				index1++;
			}
			else {
				mInputSchema[i] = rightColumn[index2];
				index2++;
			}
		}
	}

	@Override
	public LeafValue[] readNextTuple() {
		
		LeafValue[] rightTuple = mRight.readNextTuple();
		if (rightTuple == null) {
			mLeftTuple = mLeft.readNextTuple();
			if (mLeftTuple == null) {
				return null;
			}
			else {
				mRight.reset();
				rightTuple = mRight.readNextTuple();
			}
		}
		
		LeafValue[] joinTuple = new LeafValue[mLeftTuple.length+rightTuple.length];
		int index1 = 0;
		int index2 = 0;
		for(int i =0; i< joinTuple.length;i++){
			if (index1 < mLeftTuple.length) {
				joinTuple[i] = mLeftTuple[index1];
				index1++;
			}
			else {
				joinTuple[i] = rightTuple[index2];
				index2++;
			}
		}
		
		return joinTuple;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		System.err.println("PANIC! Can not reset join iterator!");
	}

	@Override
	public Column[] getInputSchema() {
		return mInputSchema;
	}

}
