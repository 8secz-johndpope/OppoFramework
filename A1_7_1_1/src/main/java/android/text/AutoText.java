package android.text;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.view.View;
import com.android.internal.R;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParserException;

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
public class AutoText {
    private static final int DEFAULT = 14337;
    private static final int INCREMENT = 1024;
    private static final int RIGHT = 9300;
    private static final int TRIE_C = 0;
    private static final int TRIE_CHILD = 2;
    private static final int TRIE_NEXT = 3;
    private static final char TRIE_NULL = '￿';
    private static final int TRIE_OFF = 1;
    private static final int TRIE_ROOT = 0;
    private static final int TRIE_SIZEOF = 4;
    private static AutoText sInstance;
    private static Object sLock;
    private Locale mLocale;
    private int mSize;
    private String mText;
    private char[] mTrie;
    private char mTrieUsed;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 0073 in method: android.text.AutoText.<clinit>():void, dex: 
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
        // Can't load method instructions: Load method exception: bogus opcode: 0073 in method: android.text.AutoText.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.AutoText.<clinit>():void");
    }

    private AutoText(Resources resources) {
        this.mLocale = resources.getConfiguration().locale;
        init(resources);
    }

    private static AutoText getInstance(View view) {
        AutoText instance;
        Resources res = view.getContext().getResources();
        Locale locale = res.getConfiguration().locale;
        synchronized (sLock) {
            instance = sInstance;
            if (!locale.equals(instance.mLocale)) {
                instance = new AutoText(res);
                sInstance = instance;
            }
        }
        return instance;
    }

    public static String get(CharSequence src, int start, int end, View view) {
        return getInstance(view).lookup(src, start, end);
    }

    public static int getSize(View view) {
        return getInstance(view).getSize();
    }

    private int getSize() {
        return this.mSize;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x004e A:{LOOP_END, LOOP:0: B:1:0x000a->B:16:0x004e} */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0046 A:{SYNTHETIC} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String lookup(CharSequence src, int start, int end) {
        int here = this.mTrie[0];
        int i = start;
        while (i < end) {
            char c = src.charAt(i);
            while (here != 65535) {
                if (c != this.mTrie[here + 0]) {
                    here = this.mTrie[here + 3];
                } else if (i != end - 1 || this.mTrie[here + 1] == TRIE_NULL) {
                    here = this.mTrie[here + 2];
                    if (here != 65535) {
                        return null;
                    }
                    i++;
                } else {
                    int off = this.mTrie[here + 1];
                    return this.mText.substring(off + 1, (off + 1) + this.mText.charAt(off));
                }
            }
            if (here != 65535) {
            }
        }
        return null;
    }

    /* JADX WARNING: Missing block: B:28:?, code:
            r13.flushLayoutCache();
     */
    /* JADX WARNING: Missing block: B:29:0x007f, code:
            r6.close();
            r12.mText = r7.toString();
     */
    /* JADX WARNING: Missing block: B:30:0x0088, code:
            return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void init(Resources r) {
        XmlResourceParser parser = r.getXml(R.xml.autotext);
        StringBuilder right = new StringBuilder(RIGHT);
        this.mTrie = new char[DEFAULT];
        this.mTrie[0] = TRIE_NULL;
        this.mTrieUsed = 1;
        try {
            XmlUtils.beginDocument(parser, "words");
            String odest = PhoneConstants.MVNO_TYPE_NONE;
            while (true) {
                XmlUtils.nextElement(parser);
                String element = parser.getName();
                if (element == null || !element.equals("word")) {
                    break;
                }
                String src = parser.getAttributeValue(null, "src");
                if (parser.next() == 4) {
                    char off;
                    String dest = parser.getText();
                    if (dest.equals(odest)) {
                        off = 0;
                    } else {
                        off = (char) right.length();
                        right.append((char) dest.length());
                        right.append(dest);
                    }
                    add(src, off);
                }
            }
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        } catch (Throwable th) {
            parser.close();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x008c A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0040  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void add(String src, char off) {
        int slen = src.length();
        int herep = 0;
        this.mSize++;
        for (int i = 0; i < slen; i++) {
            char c = src.charAt(i);
            boolean found = false;
            while (this.mTrie[herep] != TRIE_NULL) {
                if (c != this.mTrie[this.mTrie[herep] + 0]) {
                    herep = this.mTrie[herep] + 3;
                } else if (i == slen - 1) {
                    this.mTrie[this.mTrie[herep] + 1] = off;
                    return;
                } else {
                    herep = this.mTrie[herep] + 2;
                    found = true;
                    if (found) {
                        this.mTrie[herep] = newTrieNode();
                        this.mTrie[this.mTrie[herep] + 0] = c;
                        this.mTrie[this.mTrie[herep] + 1] = TRIE_NULL;
                        this.mTrie[this.mTrie[herep] + 3] = TRIE_NULL;
                        this.mTrie[this.mTrie[herep] + 2] = TRIE_NULL;
                        if (i == slen - 1) {
                            this.mTrie[this.mTrie[herep] + 1] = off;
                            return;
                        }
                        herep = this.mTrie[herep] + 2;
                    }
                }
            }
            if (found) {
            }
        }
    }

    private char newTrieNode() {
        if (this.mTrieUsed + 4 > this.mTrie.length) {
            char[] copy = new char[(this.mTrie.length + 1024)];
            System.arraycopy(this.mTrie, 0, copy, 0, this.mTrie.length);
            this.mTrie = copy;
        }
        char ret = this.mTrieUsed;
        this.mTrieUsed = (char) (this.mTrieUsed + 4);
        return ret;
    }
}