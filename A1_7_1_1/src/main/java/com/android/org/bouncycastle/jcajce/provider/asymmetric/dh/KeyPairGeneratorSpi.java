package com.android.org.bouncycastle.jcajce.provider.asymmetric.dh;

import com.android.org.bouncycastle.crypto.generators.DHBasicKeyPairGenerator;
import com.android.org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Hashtable;

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
public class KeyPairGeneratorSpi extends KeyPairGenerator {
    private static Object lock;
    private static Hashtable params;
    int certainty;
    DHBasicKeyPairGenerator engine;
    boolean initialised;
    DHKeyGenerationParameters param;
    SecureRandom random;
    int strength;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 0073 in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.<clinit>():void, dex: 
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
        // Can't load method instructions: Load method exception: bogus opcode: 0073 in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.<clinit>():void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 00e8 in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.<init>():void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        Caused by: java.lang.IllegalArgumentException: bogus opcode: 00e8
        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1227)
        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1234)
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:581)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:74)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
        	... 5 more
        */
    public KeyPairGeneratorSpi() {
        /*
        // Can't load method instructions: Load method exception: bogus opcode: 00e8 in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.<init>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.<init>():void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 00ef in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.generateKeyPair():java.security.KeyPair, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        Caused by: java.lang.IllegalArgumentException: bogus opcode: 00ef
        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1227)
        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1234)
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:581)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:74)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
        	... 5 more
        */
    public java.security.KeyPair generateKeyPair() {
        /*
        // Can't load method instructions: Load method exception: bogus opcode: 00ef in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.generateKeyPair():java.security.KeyPair, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.generateKeyPair():java.security.KeyPair");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: null in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(int, java.security.SecureRandom):void, dex:  in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(int, java.security.SecureRandom):void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        Caused by: jadx.core.utils.exceptions.DecodeException: null in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(int, java.security.SecureRandom):void, dex: 
        	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:51)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:103)
        	... 5 more
        Caused by: java.io.EOFException
        	at com.android.dx.io.instructions.ShortArrayCodeInput.read(ShortArrayCodeInput.java:54)
        	at com.android.dx.io.instructions.ShortArrayCodeInput.readLong(ShortArrayCodeInput.java:71)
        	at com.android.dx.io.instructions.InstructionCodec$31.decode(InstructionCodec.java:652)
        	at jadx.core.dex.instructions.InsnDecoder.decodeRawInsn(InsnDecoder.java:66)
        	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:48)
        	... 6 more
        */
    public void initialize(int r1, java.security.SecureRandom r2) {
        /*
        // Can't load method instructions: Load method exception: null in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(int, java.security.SecureRandom):void, dex:  in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(int, java.security.SecureRandom):void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(int, java.security.SecureRandom):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: null in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(java.security.spec.AlgorithmParameterSpec, java.security.SecureRandom):void, dex:  in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(java.security.spec.AlgorithmParameterSpec, java.security.SecureRandom):void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        Caused by: jadx.core.utils.exceptions.DecodeException: null in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(java.security.spec.AlgorithmParameterSpec, java.security.SecureRandom):void, dex: 
        	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:51)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:103)
        	... 5 more
        Caused by: java.io.EOFException
        	at com.android.dx.io.instructions.ShortArrayCodeInput.read(ShortArrayCodeInput.java:54)
        	at com.android.dx.io.instructions.InstructionCodec.decodeRegisterList(InstructionCodec.java:920)
        	at com.android.dx.io.instructions.InstructionCodec.access$900(InstructionCodec.java:31)
        	at com.android.dx.io.instructions.InstructionCodec$25.decode(InstructionCodec.java:572)
        	at jadx.core.dex.instructions.InsnDecoder.decodeRawInsn(InsnDecoder.java:66)
        	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:48)
        	... 6 more
        */
    public void initialize(java.security.spec.AlgorithmParameterSpec r1, java.security.SecureRandom r2) throws java.security.InvalidAlgorithmParameterException {
        /*
        // Can't load method instructions: Load method exception: null in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(java.security.spec.AlgorithmParameterSpec, java.security.SecureRandom):void, dex:  in method: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(java.security.spec.AlgorithmParameterSpec, java.security.SecureRandom):void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.org.bouncycastle.jcajce.provider.asymmetric.dh.KeyPairGeneratorSpi.initialize(java.security.spec.AlgorithmParameterSpec, java.security.SecureRandom):void");
    }
}
