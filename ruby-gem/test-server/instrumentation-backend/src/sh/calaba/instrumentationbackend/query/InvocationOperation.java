package sh.calaba.instrumentationbackend.query;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import sh.calaba.instrumentationbackend.InstrumentationBackend;

public class InvocationOperation implements Operation {

	private final String methodName;
	@SuppressWarnings("rawtypes")
	private final List arguments;
	private final Class<?>[] classes;
	private final Class<?>[] classesWithCharseq;

	@SuppressWarnings("rawtypes")
	public InvocationOperation(String methodName, List arguments) {
		this.methodName = methodName;
		this.arguments = arguments;
		this.classes = extractArgumentTypes(false);
		this.classesWithCharseq = extractArgumentTypes(true);
	}	

	@SuppressWarnings("rawtypes")
	@Override
	public Object apply(final Object o) throws Exception {
		final AtomicReference ref = new AtomicReference();
		final AtomicReference<Exception> refEx = new AtomicReference<Exception>();
		
		InstrumentationBackend.instrumentation.runOnMainSync(new Runnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					Method method = o.getClass().getMethod(InvocationOperation.this.methodName, InvocationOperation.this.classes);
					method.setAccessible(true);
					try {
						Object result;
						if( method.getReturnType().equals(Void.TYPE)){
							invokeMethod(o, method);
							result = "<VOID>";
			            }
						else {
							result = invokeMethod(o, method);							
						}
						ref.set(result);
						return;
					} catch (Exception e) {
						refEx.set(e);
						return;
					}
				} catch (NoSuchMethodException e) {
					try {
						Method method = o.getClass().getMethod(InvocationOperation.this.methodName, InvocationOperation.this.classesWithCharseq);
						method.setAccessible(true);
						try {
							Object result;
							if( method.getReturnType().equals(Void.TYPE)){
								invokeMethod(o, method);
								result = "<VOID>";
				            }
							else {
								result = invokeMethod(o, method);							
							}
							ref.set(result);
							return;						
						} catch (Exception ee) {
							refEx.set(e);
							return;
						}
					} catch (NoSuchMethodException ee) {
						System.out.println("Method not found with correct argument types. Trying to type convert.");
					}
				}
				//Warning: Slow path
				Method[] methods = o.getClass().getMethods();
				for (Method m : methods) {
					if (m.getName().equals(InvocationOperation.this.methodName)) {
						Class<?>[] parameterTypes = m.getParameterTypes();						
						if (parameterTypes.length == InvocationOperation.this.classes.length && areArgumentsConvertibleTo(parameterTypes)) {							
							try {
								Object result;
								if( m.getReturnType().equals(Void.TYPE)){
									invokeMethod(o, m);
									result = "<VOID>";
					            }
								else {
									result = invokeMethod(o, m);							
								}
								ref.set(result);
								return;
							} catch (Exception e) {
								e.printStackTrace();
								refEx.set(e);
								return;
							}
							
						}							
					}
				}
		
			}
			 
		 });
				
		 if (refEx.get() != null) {
			 throw refEx.get();
		 }
		 return ref.get();				
	}
	
	@SuppressWarnings("rawtypes")
	private Class[] extractArgumentTypes(boolean convertStringCharSeq) {
		Class[] types = new Class[arguments.size()];
		for (int i=0;i<arguments.size(); i++) {
			Object o = arguments.get(i);
			if (o != null) {
				Class<?> c = o.getClass();
				if (convertStringCharSeq && c.equals(String.class)) {
					c = CharSequence.class;//Android API specific optimization
				} 								
				types[i] = mapToPrimitiveClass(c); 	
			}
			else {
				types[i] = null;
			}			
		}
		return types;
	}
	
	private Class<?> mapToPrimitiveClass(Class<?> c) {
		if (c.equals(Integer.class)) {
			return int.class;
		}
		else if (c.equals(Float.class)) {
			return float.class;
		}
		else if (c.equals(Double.class)) {
			return double.class;
		}
		else if (c.equals(Boolean.class)) {
			return boolean.class;
		}		
		return c;
	}
	
	
	//parameterType.length == this.classes.length
	//Note: right now we don't do any clever mapping, we 
	//only use Java's sub type relation: isAssigableFrom
	private boolean areArgumentsConvertibleTo(Class<?>[] parameterTypes) {		
		for (int i=0; i < parameterTypes.length; i++) {
			Class<?> typei = parameterTypes[i];			
			if (this.arguments.get(i) == null) {
				if (typei.isPrimitive()) {
					//Can't pass null as primitive
					return false;
				}
				continue; //can always pass null (unless primitive)
			}
			if (typei.isPrimitive() && typei.equals(mapToPrimitiveClass(this.classes[i]))) {
				continue;
			}
			if (!typei.isAssignableFrom(this.classes[i])) {
				return false;
			}
		}
		return true;
	}
	
	private Object invokeMethod(Object o, Method m) throws Exception {
		List<?> a = this.arguments;
		int size = a.size();
		switch(size) {
			case 0:
				return m.invoke(o);
			case 1:
				return m.invoke(o,a.get(0));
			case 2:
				return m.invoke(o,a.get(0),a.get(1));
			case 3:
				return m.invoke(o,a.get(0),a.get(1),a.get(2));
			case 4:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3));
			case 5:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4));
			case 6:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5));
			case 7:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5),a.get(6));
			case 8:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5),a.get(6),a.get(7));
			case 9:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5),a.get(6),a.get(7),a.get(8));
			case 10:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5),a.get(6),a.get(7),a.get(8),a.get(9));
			case 11:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5),a.get(6),a.get(7),a.get(8),a.get(9),a.get(10));
			case 12:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5),a.get(6),a.get(7),a.get(8),a.get(9),a.get(10),a.get(11));
			case 13:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5),a.get(6),a.get(7),a.get(8),a.get(9),a.get(10),a.get(11),a.get(12));
			case 14:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5),a.get(6),a.get(7),a.get(8),a.get(9),a.get(10),a.get(11),a.get(12),a.get(13));				
			case 15:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5),a.get(6),a.get(7),a.get(8),a.get(9),a.get(10),a.get(11),a.get(12),a.get(13),a.get(14));				
			case 16:
				return m.invoke(o,a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5),a.get(6),a.get(7),a.get(8),a.get(9),a.get(10),a.get(11),a.get(12),a.get(13),a.get(14),a.get(15));				
		}
		throw new UnsupportedOperationException("Method with more than 16 arguments are not supported");
		
	}


	@Override
	public String getName() {
		return "InvocationOp["+this.methodName+", arguments = " + this.arguments + "]";
	}

}
