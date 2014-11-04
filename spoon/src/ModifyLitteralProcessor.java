package processor;

import java.util.Random;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtLiteral;
import spoon.support.reflect.code.CtBinaryOperatorImpl;
import spoon.support.reflect.code.CtLiteralImpl;

public class ModifyLitteralProcessor extends AbstractProcessor<CtLiteral<?>> {

	@Override
	public void process(CtLiteral<?> literal) {
		
		boolean mustChange = true; // tells if a change occured

		CtLiteralImpl<Number> right = new CtLiteralImpl<Number>();
		CtLiteralImpl<Number> left = new CtLiteralImpl<Number>();

		CtBinaryOperatorImpl<Number> expr = new CtBinaryOperatorImpl<Number>();
		
		Number i; // random_value % CtLiteral value 
		Number j; 
		
		if (literal.getType().equals(getFactory().Type().INTEGER_PRIMITIVE)) {
			i = (Integer) ((CtLiteralImpl<?>) literal).getValue();
			j = new Random().nextInt(i.intValue());

			left.setValue(j.intValue());
			right.setValue(i.intValue() - j.intValue());
		} 
		else if (literal.getType().equals(getFactory().Type().LONG_PRIMITIVE)) {
			i = (Long) ((CtLiteralImpl<?>) literal).getValue();
			j = new Random().nextLong();

			left.setValue(j.longValue());
			right.setValue(i.longValue() - j.longValue());
		} 
		else {
			mustChange = false;
		}
		
		if (mustChange) {
			/* SET BINARY OPERATION */
			expr.setKind(BinaryOperatorKind.PLUS);
			expr.setLeftHandOperand(left);
			expr.setRightHandOperand(right);

			/* REPLACEMENT */
			literal.replace(expr);
		} 
		else {
			getFactory().getEnvironment().debugMessage("Impossible to transform");
		}
	}

}
