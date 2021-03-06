package java.lang;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

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
public class ThreadLocal<T> {
    private static final int HASH_INCREMENT = 1640531527;
    private static AtomicInteger nextHashCode;
    private final int threadLocalHashCode;

    static class ThreadLocalMap {
        private static final int INITIAL_CAPACITY = 16;
        private int size;
        private Entry[] table;
        private int threshold;

        static class Entry extends WeakReference<ThreadLocal> {
            Object value;

            Entry(ThreadLocal k, Object v) {
                super(k);
                this.value = v;
            }
        }

        private void setThreshold(int len) {
            this.threshold = (len * 2) / 3;
        }

        private static int nextIndex(int i, int len) {
            return i + 1 < len ? i + 1 : 0;
        }

        private static int prevIndex(int i, int len) {
            return i + -1 >= 0 ? i - 1 : len - 1;
        }

        ThreadLocalMap(ThreadLocal firstKey, Object firstValue) {
            this.size = 0;
            this.table = new Entry[16];
            this.table[firstKey.threadLocalHashCode & 15] = new Entry(firstKey, firstValue);
            this.size = 1;
            setThreshold(16);
        }

        private ThreadLocalMap(ThreadLocalMap parentMap) {
            this.size = 0;
            setThreshold(len);
            this.table = new Entry[len];
            for (Entry e : parentMap.table) {
                if (e != null) {
                    ThreadLocal key = (ThreadLocal) e.get();
                    if (key != null) {
                        Entry c = new Entry(key, key.childValue(e.value));
                        int h = key.threadLocalHashCode & (len - 1);
                        while (this.table[h] != null) {
                            h = nextIndex(h, len);
                        }
                        this.table[h] = c;
                        this.size++;
                    }
                }
            }
        }

        private Entry getEntry(ThreadLocal key) {
            int i = key.threadLocalHashCode & (this.table.length - 1);
            Entry e = this.table[i];
            if (e == null || e.get() != key) {
                return getEntryAfterMiss(key, i, e);
            }
            return e;
        }

        private Entry getEntryAfterMiss(ThreadLocal key, int i, Entry e) {
            Entry[] tab = this.table;
            int len = tab.length;
            while (e != null) {
                ThreadLocal k = (ThreadLocal) e.get();
                if (k == key) {
                    return e;
                }
                if (k == null) {
                    expungeStaleEntry(i);
                } else {
                    i = nextIndex(i, len);
                }
                e = tab[i];
            }
            return null;
        }

        private void set(ThreadLocal key, Object value) {
            Entry[] tab = this.table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len - 1);
            Entry e = tab[i];
            while (e != null) {
                ThreadLocal k = (ThreadLocal) e.get();
                if (k == key) {
                    e.value = value;
                    return;
                } else if (k == null) {
                    replaceStaleEntry(key, value, i);
                    return;
                } else {
                    i = nextIndex(i, len);
                    e = tab[i];
                }
            }
            tab[i] = new Entry(key, value);
            int sz = this.size + 1;
            this.size = sz;
            if (!cleanSomeSlots(i, sz) && sz >= this.threshold) {
                rehash();
            }
        }

        private void remove(ThreadLocal key) {
            Entry[] tab = this.table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len - 1);
            Entry e = tab[i];
            while (e != null) {
                if (e.get() == key) {
                    e.clear();
                    expungeStaleEntry(i);
                    return;
                }
                i = nextIndex(i, len);
                e = tab[i];
            }
        }

        private void replaceStaleEntry(ThreadLocal key, Object value, int staleSlot) {
            Entry e;
            Entry[] tab = this.table;
            int len = tab.length;
            int slotToExpunge = staleSlot;
            int i = prevIndex(staleSlot, len);
            while (true) {
                e = tab[i];
                if (e == null) {
                    break;
                }
                if (e.get() == null) {
                    slotToExpunge = i;
                }
                i = prevIndex(i, len);
            }
            i = nextIndex(staleSlot, len);
            while (true) {
                e = tab[i];
                if (e != null) {
                    ThreadLocal k = (ThreadLocal) e.get();
                    if (k == key) {
                        e.value = value;
                        tab[i] = tab[staleSlot];
                        tab[staleSlot] = e;
                        if (slotToExpunge == staleSlot) {
                            slotToExpunge = i;
                        }
                        cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
                        return;
                    }
                    if (k == null && slotToExpunge == staleSlot) {
                        slotToExpunge = i;
                    }
                    i = nextIndex(i, len);
                } else {
                    tab[staleSlot].value = null;
                    tab[staleSlot] = new Entry(key, value);
                    if (slotToExpunge != staleSlot) {
                        cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
                    }
                    return;
                }
            }
        }

        private int expungeStaleEntry(int staleSlot) {
            Entry[] tab = this.table;
            int len = tab.length;
            tab[staleSlot].value = null;
            tab[staleSlot] = null;
            this.size--;
            int i = nextIndex(staleSlot, len);
            while (true) {
                Entry e = tab[i];
                if (e == null) {
                    return i;
                }
                ThreadLocal k = (ThreadLocal) e.get();
                if (k == null) {
                    e.value = null;
                    tab[i] = null;
                    this.size--;
                } else {
                    int h = k.threadLocalHashCode & (len - 1);
                    if (h != i) {
                        tab[i] = null;
                        while (tab[h] != null) {
                            h = nextIndex(h, len);
                        }
                        tab[h] = e;
                    }
                }
                i = nextIndex(i, len);
            }
        }

        private boolean cleanSomeSlots(int i, int n) {
            boolean removed = false;
            Entry[] tab = this.table;
            int len = tab.length;
            do {
                i = nextIndex(i, len);
                Entry e = tab[i];
                if (e != null && e.get() == null) {
                    n = len;
                    removed = true;
                    i = expungeStaleEntry(i);
                }
                n >>>= 1;
            } while (n != 0);
            return removed;
        }

        private void rehash() {
            expungeStaleEntries();
            if (this.size >= this.threshold - (this.threshold / 4)) {
                resize();
            }
        }

        private void resize() {
            int newLen = oldLen * 2;
            Entry[] newTab = new Entry[newLen];
            int count = 0;
            for (Entry e : this.table) {
                if (e != null) {
                    ThreadLocal k = (ThreadLocal) e.get();
                    if (k == null) {
                        e.value = null;
                    } else {
                        int h = k.threadLocalHashCode & (newLen - 1);
                        while (newTab[h] != null) {
                            h = nextIndex(h, newLen);
                        }
                        newTab[h] = e;
                        count++;
                    }
                }
            }
            setThreshold(newLen);
            this.size = count;
            this.table = newTab;
        }

        private void expungeStaleEntries() {
            Entry[] tab = this.table;
            int len = tab.length;
            for (int j = 0; j < len; j++) {
                Entry e = tab[j];
                if (e != null && e.get() == null) {
                    expungeStaleEntry(j);
                }
            }
        }
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 0073 in method: java.lang.ThreadLocal.<clinit>():void, dex: 
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
        // Can't load method instructions: Load method exception: bogus opcode: 0073 in method: java.lang.ThreadLocal.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.ThreadLocal.<clinit>():void");
    }

    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    protected T initialValue() {
        return null;
    }

    public ThreadLocal() {
        this.threadLocalHashCode = nextHashCode();
    }

    public T get() {
        ThreadLocalMap map = getMap(Thread.currentThread());
        if (map != null) {
            Entry e = map.getEntry(this);
            if (e != null) {
                return e.value;
            }
        }
        return setInitialValue();
    }

    private T setInitialValue() {
        T value = initialValue();
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            map.set(this, value);
        } else {
            createMap(t, value);
        }
        return value;
    }

    public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            map.set(this, value);
        } else {
            createMap(t, value);
        }
    }

    public void remove() {
        ThreadLocalMap m = getMap(Thread.currentThread());
        if (m != null) {
            m.remove(this);
        }
    }

    ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;
    }

    void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, (Object) firstValue);
    }

    static ThreadLocalMap createInheritedMap(ThreadLocalMap parentMap) {
        return new ThreadLocalMap(parentMap, null);
    }

    T childValue(T t) {
        throw new UnsupportedOperationException();
    }
}
