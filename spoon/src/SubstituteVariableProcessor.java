
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.visitor.filter.TypeFilter;

public class SubstituteVariableProcessor extends AbstractProcessor<CtVariable<?>>{

	Map<String,String> association;
	List<String> variablesName;
	private static String PREFIX_GEN_VAR = "$";
	static final String VAR_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_";
	private Random rnd = new Random();

	@Override
	public void init() {
		super.init();
		association= new HashMap<String,String>();
		variablesName = new LinkedList<String>();
	}
	
	
	@Override
	public void process(CtVariable<?> var) {	
			variablesName.add(var.getSimpleName());
	}

	@Override
	public void processingDone() {
		super.processingDone();
		
		for(String s : variablesName){
			association.put(s, genUniqName());
		}
		
		for (CtPackage p : getFactory().Package().getAllRoots()){
			for(CtElement element : p.getElements(new TypeFilter<CtVariable<?>>(CtVariable.class))){
				String varName = ((CtVariable<?>)element).getSimpleName();
				((CtVariable<?>)element).setSimpleName(association.get(varName));
			}
			
			for(CtElement element : p.getElements(new TypeFilter<CtVariableAccess<?>>(CtVariableAccess.class))){
				String varName = ((CtVariableAccess<?>)element).getVariable().getSimpleName();
				((CtVariableAccess<?>)element).getVariable().setSimpleName(association.get(varName));
			}
			
		}
	}
	
	
	public String genUniqName(){
		StringBuilder sb;
		
		do{
			int length = this.rnd.nextInt(25) + 5;
			sb = new StringBuilder(length);
			
			for(int i = 0 ; i < length ; i++) {
				sb.append(VAR_ALPHABET.charAt(rnd.nextInt(VAR_ALPHABET.length())));
			}
		}
		while(association.containsValue(sb.toString()) && variablesName.contains(sb.toString())); // check if name is not already generated and not a default variable name
		
		return PREFIX_GEN_VAR + sb.toString();
	}
	
}

