package vendor.mediatek.hardware.lbs.V1_0;

import android.hidl.base.V1_0.DebugInfo;
import android.hidl.base.V1_0.IBase;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public interface ILbs extends IBase {
    public static final String kInterfaceName = "vendor.mediatek.hardware.lbs@1.0::ILbs";

    @Override // android.hidl.base.V1_0.IBase
    IHwBinder asBinder();

    @Override // android.hidl.base.V1_0.IBase
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    ArrayList<byte[]> getHashChain() throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    String interfaceDescriptor() throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void ping() throws RemoteException;

    boolean sendToServer(ArrayList<Byte> arrayList) throws RemoteException;

    boolean sendToServerWithCallback(ILbsCallback iLbsCallback, ArrayList<Byte> arrayList) throws RemoteException;

    void setCallback(ILbsCallback iLbsCallback) throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    static default ILbs asInterface(IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof ILbs)) {
            return (ILbs) iface;
        }
        ILbs proxy = new Proxy(binder);
        try {
            Iterator<String> it = proxy.interfaceChain().iterator();
            while (it.hasNext()) {
                if (it.next().equals(kInterfaceName)) {
                    return proxy;
                }
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    @Override // android.hidl.base.V1_0.IBase
    static default ILbs castFrom(IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    @Override // android.hidl.base.V1_0.IBase
    static default ILbs getService(String serviceName, boolean retry) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: vendor.mediatek.hardware.lbs.V1_0.ILbs.getService(java.lang.String, boolean):vendor.mediatek.hardware.lbs.V1_0.ILbs
     arg types: [java.lang.String, boolean]
     candidates:
      android.hidl.base.V1_0.IBase.getService(java.lang.String, boolean):android.hidl.base.V1_0.IBase
      vendor.mediatek.hardware.lbs.V1_0.ILbs.getService(java.lang.String, boolean):vendor.mediatek.hardware.lbs.V1_0.ILbs */
    @Override // android.hidl.base.V1_0.IBase
    static default ILbs getService(boolean retry) throws RemoteException {
        return getService("default", retry);
    }

    @Override // android.hidl.base.V1_0.IBase
    static default ILbs getService(String serviceName) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName));
    }

    @Override // android.hidl.base.V1_0.IBase
    static default ILbs getService() throws RemoteException {
        return getService("default");
    }

    public static final class Proxy implements ILbs {
        private IHwBinder mRemote;

        public Proxy(IHwBinder remote) {
            this.mRemote = (IHwBinder) Objects.requireNonNull(remote);
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException e) {
                return "[class or subclass of vendor.mediatek.hardware.lbs@1.0::ILbs]@Proxy";
            }
        }

        public final boolean equals(Object other) {
            return HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs
        public void setCallback(ILbsCallback callback) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(ILbs.kInterfaceName);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs
        public boolean sendToServer(ArrayList<Byte> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(ILbs.kInterfaceName);
            _hidl_request.writeInt8Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readBool();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs
        public boolean sendToServerWithCallback(ILbsCallback callback, ArrayList<Byte> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(ILbs.kInterfaceName);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            _hidl_request.writeInt8Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readBool();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public ArrayList<String> interfaceChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256067662, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readStringVector();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public void debug(NativeHandle fd, ArrayList<String> options) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            _hidl_request.writeNativeHandle(fd);
            _hidl_request.writeStringVector(options);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256131655, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public String interfaceDescriptor() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256136003, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                return _hidl_reply.readString();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public ArrayList<byte[]> getHashChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256398152, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                ArrayList<byte[]> _hidl_out_hashchain = new ArrayList<>();
                HwBlob _hidl_blob = _hidl_reply.readBuffer(16);
                int _hidl_vec_size = _hidl_blob.getInt32(8);
                HwBlob childBlob = _hidl_reply.readEmbeddedBuffer((long) (_hidl_vec_size * 32), _hidl_blob.handle(), 0, true);
                _hidl_out_hashchain.clear();
                for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                    byte[] _hidl_vec_element = new byte[32];
                    childBlob.copyToInt8Array((long) (_hidl_index_0 * 32), _hidl_vec_element, 32);
                    _hidl_out_hashchain.add(_hidl_vec_element);
                }
                return _hidl_out_hashchain;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public void setHALInstrumentation() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256462420, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) throws RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public void ping() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256921159, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public DebugInfo getDebugInfo() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257049926, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                DebugInfo _hidl_out_info = new DebugInfo();
                _hidl_out_info.readFromParcel(_hidl_reply);
                return _hidl_out_info;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public void notifySyspropsChanged() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257120595, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends HwBinder implements ILbs {
        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(ILbs.kInterfaceName, IBase.kInterfaceName));
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public void debug(NativeHandle fd, ArrayList<String> arrayList) {
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public final String interfaceDescriptor() {
            return ILbs.kInterfaceName;
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{85, 62, 65, -65, 67, 94, 64, 108, 112, 12, 72, -79, -2, -98, 57, -26, -82, -112, -78, -126, -102, -83, 75, 93, -22, 100, 36, 50, 116, -75, -3, 76}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public final DebugInfo getDebugInfo() {
            DebugInfo info = new DebugInfo();
            info.pid = HidlSupport.getPidIfSharable();
            info.ptr = 0;
            info.arch = 0;
            return info;
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        @Override // vendor.mediatek.hardware.lbs.V1_0.ILbs, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public IHwInterface queryLocalInterface(String descriptor) {
            if (ILbs.kInterfaceName.equals(descriptor)) {
                return this;
            }
            return null;
        }

        public void registerAsService(String serviceName) throws RemoteException {
            registerService(serviceName);
        }

        public String toString() {
            return interfaceDescriptor() + "@Stub";
        }

        public void onTransact(int _hidl_code, HwParcel _hidl_request, HwParcel _hidl_reply, int _hidl_flags) throws RemoteException {
            boolean _hidl_is_oneway = false;
            boolean _hidl_is_oneway2 = true;
            if (_hidl_code == 1) {
                if ((_hidl_flags & 1) == 0) {
                    _hidl_is_oneway2 = false;
                }
                if (_hidl_is_oneway2) {
                    _hidl_reply.writeStatus(Integer.MIN_VALUE);
                    _hidl_reply.send();
                    return;
                }
                _hidl_request.enforceInterface(ILbs.kInterfaceName);
                setCallback(ILbsCallback.asInterface(_hidl_request.readStrongBinder()));
                _hidl_reply.writeStatus(0);
                _hidl_reply.send();
            } else if (_hidl_code == 2) {
                if (_hidl_flags == false || !true) {
                    _hidl_is_oneway2 = false;
                }
                if (_hidl_is_oneway2) {
                    _hidl_reply.writeStatus(Integer.MIN_VALUE);
                    _hidl_reply.send();
                    return;
                }
                _hidl_request.enforceInterface(ILbs.kInterfaceName);
                boolean _hidl_out_success = sendToServer(_hidl_request.readInt8Vector());
                _hidl_reply.writeStatus(0);
                _hidl_reply.writeBool(_hidl_out_success);
                _hidl_reply.send();
            } else if (_hidl_code != 3) {
                switch (_hidl_code) {
                    case 256067662:
                        if ((_hidl_flags & 1) == 0) {
                            _hidl_is_oneway2 = false;
                        }
                        if (_hidl_is_oneway2) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        ArrayList<String> _hidl_out_descriptors = interfaceChain();
                        _hidl_reply.writeStatus(0);
                        _hidl_reply.writeStringVector(_hidl_out_descriptors);
                        _hidl_reply.send();
                        return;
                    case 256131655:
                        if ((_hidl_flags & 1) == 0) {
                            _hidl_is_oneway2 = false;
                        }
                        if (_hidl_is_oneway2) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        debug(_hidl_request.readNativeHandle(), _hidl_request.readStringVector());
                        _hidl_reply.writeStatus(0);
                        _hidl_reply.send();
                        return;
                    case 256136003:
                        if ((_hidl_flags & 1) == 0) {
                            _hidl_is_oneway2 = false;
                        }
                        if (_hidl_is_oneway2) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        String _hidl_out_descriptor = interfaceDescriptor();
                        _hidl_reply.writeStatus(0);
                        _hidl_reply.writeString(_hidl_out_descriptor);
                        _hidl_reply.send();
                        return;
                    case 256398152:
                        if ((_hidl_flags & 1) == 0) {
                            _hidl_is_oneway2 = false;
                        }
                        if (_hidl_is_oneway2) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        ArrayList<byte[]> _hidl_out_hashchain = getHashChain();
                        _hidl_reply.writeStatus(0);
                        HwBlob _hidl_blob = new HwBlob(16);
                        int _hidl_vec_size = _hidl_out_hashchain.size();
                        _hidl_blob.putInt32(8, _hidl_vec_size);
                        _hidl_blob.putBool(12, false);
                        HwBlob childBlob = new HwBlob(_hidl_vec_size * 32);
                        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                            long _hidl_array_offset_1 = (long) (_hidl_index_0 * 32);
                            byte[] _hidl_array_item_1 = _hidl_out_hashchain.get(_hidl_index_0);
                            if (_hidl_array_item_1 == null || _hidl_array_item_1.length != 32) {
                                throw new IllegalArgumentException("Array element is not of the expected length");
                            }
                            childBlob.putInt8Array(_hidl_array_offset_1, _hidl_array_item_1);
                        }
                        _hidl_blob.putBlob(0, childBlob);
                        _hidl_reply.writeBuffer(_hidl_blob);
                        _hidl_reply.send();
                        return;
                    case 256462420:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_is_oneway = true;
                        }
                        if (!_hidl_is_oneway) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        setHALInstrumentation();
                        return;
                    case 256660548:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_is_oneway = true;
                        }
                        if (_hidl_is_oneway) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        return;
                    case 256921159:
                        if ((_hidl_flags & 1) == 0) {
                            _hidl_is_oneway2 = false;
                        }
                        if (_hidl_is_oneway2) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        ping();
                        _hidl_reply.writeStatus(0);
                        _hidl_reply.send();
                        return;
                    case 257049926:
                        if ((_hidl_flags & 1) == 0) {
                            _hidl_is_oneway2 = false;
                        }
                        if (_hidl_is_oneway2) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        DebugInfo _hidl_out_info = getDebugInfo();
                        _hidl_reply.writeStatus(0);
                        _hidl_out_info.writeToParcel(_hidl_reply);
                        _hidl_reply.send();
                        return;
                    case 257120595:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_is_oneway = true;
                        }
                        if (!_hidl_is_oneway) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        notifySyspropsChanged();
                        return;
                    case 257250372:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_is_oneway = true;
                        }
                        if (_hidl_is_oneway) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else {
                if ((_hidl_flags & 1) == 0) {
                    _hidl_is_oneway2 = false;
                }
                if (_hidl_is_oneway2) {
                    _hidl_reply.writeStatus(Integer.MIN_VALUE);
                    _hidl_reply.send();
                    return;
                }
                _hidl_request.enforceInterface(ILbs.kInterfaceName);
                boolean _hidl_out_success2 = sendToServerWithCallback(ILbsCallback.asInterface(_hidl_request.readStrongBinder()), _hidl_request.readInt8Vector());
                _hidl_reply.writeStatus(0);
                _hidl_reply.writeBool(_hidl_out_success2);
                _hidl_reply.send();
            }
        }
    }
}
