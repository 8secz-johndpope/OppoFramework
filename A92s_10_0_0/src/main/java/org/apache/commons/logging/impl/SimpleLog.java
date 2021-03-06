package org.apache.commons.logging.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;

@Deprecated
public class SimpleLog implements Log, Serializable {
    protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
    public static final int LOG_LEVEL_ALL = 0;
    public static final int LOG_LEVEL_DEBUG = 2;
    public static final int LOG_LEVEL_ERROR = 5;
    public static final int LOG_LEVEL_FATAL = 6;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_OFF = 7;
    public static final int LOG_LEVEL_TRACE = 1;
    public static final int LOG_LEVEL_WARN = 4;
    protected static DateFormat dateFormatter = null;
    protected static String dateTimeFormat = null;
    protected static boolean showDateTime = false;
    protected static boolean showLogName = false;
    protected static boolean showShortName = false;
    protected static final Properties simpleLogProps = new Properties();
    protected static final String systemPrefix = "org.apache.commons.logging.simplelog.";
    protected int currentLogLevel;
    protected String logName = null;
    private String shortLogName = null;

    static {
        showLogName = false;
        showShortName = true;
        showDateTime = false;
        dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;
        dateFormatter = null;
        InputStream in = getResourceAsStream("simplelog.properties");
        if (in != null) {
            try {
                simpleLogProps.load(in);
                in.close();
            } catch (IOException e) {
            }
        }
        showLogName = getBooleanProperty("org.apache.commons.logging.simplelog.showlogname", showLogName);
        showShortName = getBooleanProperty("org.apache.commons.logging.simplelog.showShortLogname", showShortName);
        showDateTime = getBooleanProperty("org.apache.commons.logging.simplelog.showdatetime", showDateTime);
        if (showDateTime == 1) {
            dateTimeFormat = getStringProperty("org.apache.commons.logging.simplelog.dateTimeFormat", dateTimeFormat);
            try {
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            } catch (IllegalArgumentException e2) {
                dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            }
        }
    }

    private static String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        } catch (SecurityException e) {
        }
        return prop == null ? simpleLogProps.getProperty(name) : prop;
    }

    private static String getStringProperty(String name, String dephault) {
        String prop = getStringProperty(name);
        return prop == null ? dephault : prop;
    }

    private static boolean getBooleanProperty(String name, boolean dephault) {
        String prop = getStringProperty(name);
        return prop == null ? dephault : "true".equalsIgnoreCase(prop);
    }

    public SimpleLog(String name) {
        this.logName = name;
        setLevel(3);
        String lvl = getStringProperty("org.apache.commons.logging.simplelog.log." + this.logName);
        int i = String.valueOf(name).lastIndexOf(".");
        while (lvl == null && i > -1) {
            name = name.substring(0, i);
            lvl = getStringProperty("org.apache.commons.logging.simplelog.log." + name);
            i = String.valueOf(name).lastIndexOf(".");
        }
        lvl = lvl == null ? getStringProperty("org.apache.commons.logging.simplelog.defaultlog") : lvl;
        if ("all".equalsIgnoreCase(lvl) == 1) {
            setLevel(0);
        } else if ("trace".equalsIgnoreCase(lvl) == 1) {
            setLevel(1);
        } else if ("debug".equalsIgnoreCase(lvl) == 1) {
            setLevel(2);
        } else if ("info".equalsIgnoreCase(lvl) == 1) {
            setLevel(3);
        } else if ("warn".equalsIgnoreCase(lvl) == 1) {
            setLevel(4);
        } else if ("error".equalsIgnoreCase(lvl) == 1) {
            setLevel(5);
        } else if ("fatal".equalsIgnoreCase(lvl) == 1) {
            setLevel(6);
        } else if ("off".equalsIgnoreCase(lvl) == 1) {
            setLevel(7);
        }
    }

    public void setLevel(int currentLogLevel2) {
        this.currentLogLevel = currentLogLevel2;
    }

    public int getLevel() {
        return this.currentLogLevel;
    }

    /* access modifiers changed from: protected */
    public void log(int type, Object message, Throwable t) {
        StringBuffer buf = new StringBuffer();
        if (showDateTime == 1) {
            buf.append(dateFormatter.format(new Date()));
            buf.append(" ");
        }
        switch (type) {
            case 1:
                buf.append("[TRACE] ");
                break;
            case 2:
                buf.append("[DEBUG] ");
                break;
            case 3:
                buf.append("[INFO] ");
                break;
            case 4:
                buf.append("[WARN] ");
                break;
            case 5:
                buf.append("[ERROR] ");
                break;
            case LOG_LEVEL_FATAL /*{ENCODED_INT: 6}*/:
                buf.append("[FATAL] ");
                break;
        }
        if (showShortName == 1) {
            if (this.shortLogName == null) {
                String str = this.logName;
                this.shortLogName = str.substring(str.lastIndexOf(".") + 1);
                String str2 = this.shortLogName;
                this.shortLogName = str2.substring(str2.lastIndexOf("/") + 1);
            }
            buf.append(String.valueOf(this.shortLogName));
            buf.append(" - ");
        } else if (showLogName == 1) {
            buf.append(String.valueOf(this.logName));
            buf.append(" - ");
        }
        buf.append(String.valueOf(message));
        if (t != null) {
            buf.append(" <");
            buf.append(t.toString());
            buf.append(">");
            StringWriter sw = new StringWriter(1024);
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            buf.append(sw.toString());
        }
        write(buf);
    }

    /* access modifiers changed from: protected */
    public void write(StringBuffer buffer) {
        System.err.println(buffer.toString());
    }

    /* access modifiers changed from: protected */
    public boolean isLevelEnabled(int logLevel) {
        return logLevel >= this.currentLogLevel ? true : false;
    }

    @Override // org.apache.commons.logging.Log
    public final void debug(Object message) {
        if (isLevelEnabled(2) == 1) {
            log(2, message, null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final void debug(Object message, Throwable t) {
        if (isLevelEnabled(2) == 1) {
            log(2, message, t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final void trace(Object message) {
        if (isLevelEnabled(1) == 1) {
            log(1, message, null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final void trace(Object message, Throwable t) {
        if (isLevelEnabled(1) == 1) {
            log(1, message, t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final void info(Object message) {
        if (isLevelEnabled(3) == 1) {
            log(3, message, null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final void info(Object message, Throwable t) {
        if (isLevelEnabled(3) == 1) {
            log(3, message, t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final void warn(Object message) {
        if (isLevelEnabled(4) == 1) {
            log(4, message, null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final void warn(Object message, Throwable t) {
        if (isLevelEnabled(4) == 1) {
            log(4, message, t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final void error(Object message) {
        if (isLevelEnabled(5) == 1) {
            log(5, message, null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final void error(Object message, Throwable t) {
        if (isLevelEnabled(5) == 1) {
            log(5, message, t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final void fatal(Object message) {
        if (isLevelEnabled(6) == 1) {
            log(6, message, null);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final void fatal(Object message, Throwable t) {
        if (isLevelEnabled(6) == 1) {
            log(6, message, t);
        }
    }

    @Override // org.apache.commons.logging.Log
    public final boolean isDebugEnabled() {
        return isLevelEnabled(2);
    }

    @Override // org.apache.commons.logging.Log
    public final boolean isErrorEnabled() {
        return isLevelEnabled(5);
    }

    @Override // org.apache.commons.logging.Log
    public final boolean isFatalEnabled() {
        return isLevelEnabled(6);
    }

    @Override // org.apache.commons.logging.Log
    public final boolean isInfoEnabled() {
        return isLevelEnabled(3);
    }

    @Override // org.apache.commons.logging.Log
    public final boolean isTraceEnabled() {
        return isLevelEnabled(1);
    }

    @Override // org.apache.commons.logging.Log
    public final boolean isWarnEnabled() {
        return isLevelEnabled(4);
    }

    /* access modifiers changed from: private */
    public static ClassLoader getContextClassLoader() {
        ClassLoader classLoader = null;
        if (0 == 0) {
            try {
                try {
                    classLoader = (ClassLoader) Thread.class.getMethod("getContextClassLoader", null).invoke(Thread.currentThread(), null);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e2) {
                    if (!(e2.getTargetException() instanceof SecurityException)) {
                        throw new LogConfigurationException("Unexpected InvocationTargetException", e2.getTargetException());
                    }
                }
            } catch (NoSuchMethodException e3) {
            }
        }
        if (classLoader == null) {
            return SimpleLog.class.getClassLoader();
        }
        return classLoader;
    }

    private static InputStream getResourceAsStream(final String name) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction() {
            /* class org.apache.commons.logging.impl.SimpleLog.AnonymousClass1 */

            @Override // java.security.PrivilegedAction
            public Object run() {
                ClassLoader threadCL = SimpleLog.getContextClassLoader();
                if (threadCL != null) {
                    return threadCL.getResourceAsStream(name);
                }
                return ClassLoader.getSystemResourceAsStream(name);
            }
        });
    }
}
