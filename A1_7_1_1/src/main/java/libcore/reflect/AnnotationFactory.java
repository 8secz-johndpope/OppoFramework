package libcore.reflect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*  JADX ERROR: NullPointerException in pass: ReSugarCode
    java.lang.NullPointerException
    	at jadx.core.dex.visitors.ReSugarCode.initClsEnumMap(ReSugarCode.java:159)
    	at jadx.core.dex.visitors.ReSugarCode.visit(ReSugarCode.java:44)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:12)
    	at jadx.core.ProcessClass.process(ProcessClass.java:32)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
    */
/*  JADX ERROR: NullPointerException in pass: ExtractFieldInit
    java.lang.NullPointerException
    	at jadx.core.dex.visitors.ExtractFieldInit.checkStaticFieldsInit(ExtractFieldInit.java:58)
    	at jadx.core.dex.visitors.ExtractFieldInit.visit(ExtractFieldInit.java:44)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:12)
    	at jadx.core.ProcessClass.process(ProcessClass.java:32)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
    */
public final class AnnotationFactory implements InvocationHandler, Serializable {
    private static final transient Map<Class<? extends Annotation>, AnnotationMember[]> cache = null;
    private AnnotationMember[] elements;
    private final Class<? extends Annotation> klazz;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 0073 in method: libcore.reflect.AnnotationFactory.<clinit>():void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1227)
        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1234)
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:581)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:74)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
        	... 5 more
        */
    static {
        /*
        // Can't load method instructions: Load method exception: bogus opcode: 0073 in method: libcore.reflect.AnnotationFactory.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: libcore.reflect.AnnotationFactory.<clinit>():void");
    }

    /* JADX WARNING: Missing block: B:9:0x0014, code:
            if (r10.isAnnotation() != false) goto L_0x0037;
     */
    /* JADX WARNING: Missing block: B:11:0x0033, code:
            throw new java.lang.IllegalArgumentException("Type is not annotation: " + r10.getName());
     */
    /* JADX WARNING: Missing block: B:15:0x0037, code:
            r0 = r10.getDeclaredMethods();
            r1 = new libcore.reflect.AnnotationMember[r0.length];
            r3 = 0;
     */
    /* JADX WARNING: Missing block: B:17:0x0040, code:
            if (r3 >= r0.length) goto L_0x0063;
     */
    /* JADX WARNING: Missing block: B:18:0x0042, code:
            r2 = r0[r3];
            r4 = r2.getName();
            r6 = r2.getReturnType();
     */
    /* JADX WARNING: Missing block: B:20:?, code:
            r1[r3] = new libcore.reflect.AnnotationMember(r4, r2.getDefaultValue(), r6, r2);
     */
    /* JADX WARNING: Missing block: B:22:0x005a, code:
            r5 = move-exception;
     */
    /* JADX WARNING: Missing block: B:23:0x005b, code:
            r1[r3] = new libcore.reflect.AnnotationMember(r4, r5, r6, r2);
     */
    /* JADX WARNING: Missing block: B:24:0x0063, code:
            r8 = cache;
     */
    /* JADX WARNING: Missing block: B:25:0x0065, code:
            monitor-enter(r8);
     */
    /* JADX WARNING: Missing block: B:27:?, code:
            cache.put(r10, r1);
     */
    /* JADX WARNING: Missing block: B:28:0x006b, code:
            monitor-exit(r8);
     */
    /* JADX WARNING: Missing block: B:29:0x006c, code:
            return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static AnnotationMember[] getElementsDescription(Class<? extends Annotation> annotationType) {
        synchronized (cache) {
            AnnotationMember[] desc = (AnnotationMember[]) cache.get(annotationType);
            if (desc != null) {
                return desc;
            }
        }
        int i++;
    }

    public static <A extends Annotation> A createAnnotation(Class<? extends Annotation> annotationType, AnnotationMember[] elements) {
        AnnotationFactory factory = new AnnotationFactory(annotationType, elements);
        ClassLoader classLoader = annotationType.getClassLoader();
        Class[] clsArr = new Class[1];
        clsArr[0] = annotationType;
        return (Annotation) Proxy.newProxyInstance(classLoader, clsArr, factory);
    }

    private AnnotationFactory(Class<? extends Annotation> klzz, AnnotationMember[] values) {
        this.klazz = klzz;
        AnnotationMember[] defs = getElementsDescription(this.klazz);
        if (values == null) {
            this.elements = defs;
            return;
        }
        this.elements = new AnnotationMember[defs.length];
        for (int i = this.elements.length - 1; i >= 0; i--) {
            for (AnnotationMember val : values) {
                if (val.name.equals(defs[i].name)) {
                    this.elements[i] = val.setDefinition(defs[i]);
                    break;
                }
            }
            this.elements[i] = defs[i];
        }
    }

    private void readObject(ObjectInputStream os) throws IOException, ClassNotFoundException {
        os.defaultReadObject();
        AnnotationMember[] defs = getElementsDescription(this.klazz);
        AnnotationMember[] old = this.elements;
        List<AnnotationMember> merged = new ArrayList(defs.length + old.length);
        for (AnnotationMember el1 : old) {
            for (AnnotationMember el2 : defs) {
                if (el2.name.equals(el1.name)) {
                    break;
                }
            }
            merged.add(el1);
        }
        for (AnnotationMember def : defs) {
            for (AnnotationMember val : old) {
                if (val.name.equals(def.name)) {
                    merged.add(val.setDefinition(def));
                    break;
                }
            }
            merged.add(def);
        }
        this.elements = (AnnotationMember[]) merged.toArray(new AnnotationMember[merged.size()]);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!this.klazz.isInstance(obj)) {
            return false;
        }
        int i;
        if (Proxy.isProxyClass(obj.getClass())) {
            AnnotationFactory handler = Proxy.getInvocationHandler(obj);
            if (handler instanceof AnnotationFactory) {
                AnnotationFactory other = handler;
                if (this.elements.length != other.elements.length) {
                    return false;
                }
                AnnotationMember[] annotationMemberArr = this.elements;
                int length = annotationMemberArr.length;
                int i2 = 0;
                while (i2 < length) {
                    AnnotationMember el1 = annotationMemberArr[i2];
                    AnnotationMember[] annotationMemberArr2 = other.elements;
                    i = 0;
                    int length2 = annotationMemberArr2.length;
                    while (i < length2) {
                        if (el1.equals(annotationMemberArr2[i])) {
                            i2++;
                        } else {
                            i++;
                        }
                    }
                    return false;
                }
                return true;
            }
        }
        AnnotationMember[] annotationMemberArr3 = this.elements;
        i = 0;
        int length3 = annotationMemberArr3.length;
        while (i < length3) {
            AnnotationMember el = annotationMemberArr3[i];
            if (el.tag == '!') {
                return false;
            }
            try {
                if (!el.definingMethod.isAccessible()) {
                    el.definingMethod.setAccessible(true);
                }
                Object otherValue = el.definingMethod.invoke(obj, new Object[0]);
                if (otherValue != null) {
                    if (el.tag == '[') {
                        if (!el.equalArrayValue(otherValue)) {
                            return false;
                        }
                    } else if (!el.value.equals(otherValue)) {
                        return false;
                    }
                } else if (el.value != AnnotationMember.NO_VALUE) {
                    return false;
                }
                i++;
            } catch (Throwable th) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hash = 0;
        for (AnnotationMember element : this.elements) {
            hash += element.hashCode();
        }
        return hash;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('@');
        result.append(this.klazz.getName());
        result.append('(');
        for (int i = 0; i < this.elements.length; i++) {
            if (i != 0) {
                result.append(", ");
            }
            result.append(this.elements[i]);
        }
        result.append(')');
        return result.toString();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int i = 0;
        String name = method.getName();
        Class[] params = method.getParameterTypes();
        if (params.length == 0) {
            if ("annotationType".equals(name)) {
                return this.klazz;
            }
            if ("toString".equals(name)) {
                return toString();
            }
            if ("hashCode".equals(name)) {
                return Integer.valueOf(hashCode());
            }
            AnnotationMember element = null;
            AnnotationMember[] annotationMemberArr = this.elements;
            int length = annotationMemberArr.length;
            while (i < length) {
                AnnotationMember el = annotationMemberArr[i];
                if (name.equals(el.name)) {
                    element = el;
                    break;
                }
                i++;
            }
            if (element == null || !method.equals(element.definingMethod)) {
                throw new IllegalArgumentException(method.toString());
            }
            Object value = element.validateValue();
            if (value != null) {
                return value;
            }
            throw new IncompleteAnnotationException(this.klazz, name);
        } else if (params.length == 1 && params[0] == Object.class && "equals".equals(name)) {
            return Boolean.valueOf(equals(args[0]));
        } else {
            throw new IllegalArgumentException("Invalid method for annotation type: " + method);
        }
    }
}
