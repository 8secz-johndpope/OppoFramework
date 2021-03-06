package junit.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.NumberFormat;
import java.util.Properties;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import junit.framework.TestSuite;

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
public abstract class BaseTestRunner implements TestListener {
    public static final String SUITE_METHODNAME = "suite";
    private static Properties fPreferences;
    static boolean fgFilterStack;
    static int fgMaxMessageLength;
    boolean fLoading;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 0073 in method: junit.runner.BaseTestRunner.<clinit>():void, dex: 
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
        // Can't load method instructions: Load method exception: bogus opcode: 0073 in method: junit.runner.BaseTestRunner.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: junit.runner.BaseTestRunner.<clinit>():void");
    }

    protected abstract void runFailed(String str);

    public abstract void testEnded(String str);

    public abstract void testFailed(int i, Test test, Throwable th);

    public abstract void testStarted(String str);

    public BaseTestRunner() {
        this.fLoading = true;
    }

    public synchronized void startTest(Test test) {
        testStarted(test.toString());
    }

    protected static void setPreferences(Properties preferences) {
        fPreferences = preferences;
    }

    protected static Properties getPreferences() {
        if (fPreferences == null) {
            fPreferences = new Properties();
            fPreferences.put("loading", "true");
            fPreferences.put("filterstack", "true");
            readPreferences();
        }
        return fPreferences;
    }

    public static void savePreferences() throws IOException {
        FileOutputStream fos = new FileOutputStream(getPreferencesFile());
        try {
            getPreferences().store(fos, "");
        } finally {
            fos.close();
        }
    }

    public void setPreference(String key, String value) {
        getPreferences().put(key, value);
    }

    public synchronized void endTest(Test test) {
        testEnded(test.toString());
    }

    public synchronized void addError(Test test, Throwable t) {
        testFailed(1, test, t);
    }

    public synchronized void addFailure(Test test, AssertionFailedError t) {
        testFailed(2, test, t);
    }

    public Test getTest(String suiteClassName) {
        if (suiteClassName.length() <= 0) {
            clearStatus();
            return null;
        }
        try {
            Class<?> testClass = loadSuiteClass(suiteClassName);
            try {
                Method suiteMethod = testClass.getMethod(SUITE_METHODNAME, new Class[0]);
                if (Modifier.isStatic(suiteMethod.getModifiers())) {
                    try {
                        Test test = (Test) suiteMethod.invoke(null, new Class[0]);
                        if (test == null) {
                            return test;
                        }
                        clearStatus();
                        return test;
                    } catch (InvocationTargetException e) {
                        runFailed("Failed to invoke suite():" + e.getTargetException().toString());
                        return null;
                    } catch (IllegalAccessException e2) {
                        runFailed("Failed to invoke suite():" + e2.toString());
                        return null;
                    }
                }
                runFailed("Suite() method must be static");
                return null;
            } catch (Exception e3) {
                clearStatus();
                return new TestSuite(testClass);
            }
        } catch (ClassNotFoundException e4) {
            String clazz = e4.getMessage();
            if (clazz == null) {
                clazz = suiteClassName;
            }
            runFailed("Class not found \"" + clazz + "\"");
            return null;
        } catch (Exception e5) {
            runFailed("Error: " + e5.toString());
            return null;
        }
    }

    public String elapsedTimeAsString(long runTime) {
        return NumberFormat.getInstance().format(((double) runTime) / 1000.0d);
    }

    protected String processArguments(String[] args) {
        String suiteName = null;
        int i = 0;
        while (i < args.length) {
            if (args[i].equals("-noloading")) {
                setLoading(false);
            } else if (args[i].equals("-nofilterstack")) {
                fgFilterStack = false;
            } else if (args[i].equals("-c")) {
                if (args.length > i + 1) {
                    suiteName = extractClassName(args[i + 1]);
                } else {
                    System.out.println("Missing Test class name");
                }
                i++;
            } else {
                suiteName = args[i];
            }
            i++;
        }
        return suiteName;
    }

    public void setLoading(boolean enable) {
        this.fLoading = enable;
    }

    public String extractClassName(String className) {
        if (className.startsWith("Default package for")) {
            return className.substring(className.lastIndexOf(".") + 1);
        }
        return className;
    }

    public static String truncate(String s) {
        if (fgMaxMessageLength == -1 || s.length() <= fgMaxMessageLength) {
            return s;
        }
        return s.substring(0, fgMaxMessageLength) + "...";
    }

    public TestSuiteLoader getLoader() {
        return new StandardTestSuiteLoader();
    }

    protected Class<?> loadSuiteClass(String suiteClassName) throws ClassNotFoundException {
        return Class.forName(suiteClassName);
    }

    protected void clearStatus() {
    }

    protected boolean useReloadingTestSuiteLoader() {
        return getPreference("loading").equals("true") ? this.fLoading : false;
    }

    private static File getPreferencesFile() {
        return new File(System.getProperty("user.home"), "junit.properties");
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0022 A:{SYNTHETIC, Splitter: B:8:0x0022} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void readPreferences() {
        InputStream is = null;
        try {
            InputStream is2 = new FileInputStream(getPreferencesFile());
            try {
                setPreferences(new Properties(getPreferences()));
                getPreferences().load(is2);
                is = is2;
            } catch (IOException e) {
                is = is2;
                if (is == null) {
                    try {
                        is.close();
                    } catch (IOException e2) {
                    }
                }
            }
        } catch (IOException e3) {
            if (is == null) {
            }
        }
    }

    public static String getPreference(String key) {
        return getPreferences().getProperty(key);
    }

    public static int getPreference(String key, int dflt) {
        String value = getPreference(key);
        int intValue = dflt;
        if (value == null) {
            return dflt;
        }
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
        }
        return intValue;
    }

    public static String getFilteredTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter));
        return getFilteredTrace(stringWriter.getBuffer().toString());
    }

    public static boolean inVAJava() {
        return false;
    }

    public static String getFilteredTrace(String stack) {
        if (showStackRaw()) {
            return stack;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        BufferedReader br = new BufferedReader(new StringReader(stack), 1000);
        while (true) {
            try {
                String line = br.readLine();
                if (line == null) {
                    return sw.toString();
                }
                if (!filterLine(line)) {
                    pw.println(line);
                }
            } catch (Exception e) {
                return stack;
            }
        }
    }

    protected static boolean showStackRaw() {
        return (getPreference("filterstack").equals("true") && fgFilterStack) ? false : true;
    }

    static boolean filterLine(String line) {
        String[] patterns = new String[8];
        patterns[0] = "junit.framework.TestCase";
        patterns[1] = "junit.framework.TestResult";
        patterns[2] = "junit.framework.TestSuite";
        patterns[3] = "junit.framework.Assert.";
        patterns[4] = "junit.swingui.TestRunner";
        patterns[5] = "junit.awtui.TestRunner";
        patterns[6] = "junit.textui.TestRunner";
        patterns[7] = "java.lang.reflect.Method.invoke(";
        for (String indexOf : patterns) {
            if (line.indexOf(indexOf) > 0) {
                return true;
            }
        }
        return false;
    }
}
