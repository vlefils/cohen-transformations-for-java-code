import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtMethod;

public class MethodProcessor extends AbstractProcessor<CtMethod> {


    public void process(CtMethod element) {
	System.out.println("coucou");
    }

}