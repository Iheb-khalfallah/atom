package net.vpc.gaming.atomgames.pong.main.shared.dal.corba.generated;


/**
 * tn/edu/eniso/pong/main/shared/dal/corba/generated/_ClientOperationsCorbaStub.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from DalCorba.idl
 * Tuesday, December 20, 2011 11:10:02 PM CET
 */

public class _ClientOperationsCorbaStub extends org.omg.CORBA.portable.ObjectImpl implements net.vpc.gaming.atomgames.pong.main.shared.dal.corba.generated.ClientOperationsCorba {

    public void modelChanged(net.vpc.gaming.atomgames.pong.main.shared.dal.corba.generated.ModelCorba data) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("modelChanged", false);
            net.vpc.gaming.atomgames.pong.main.shared.dal.corba.generated.ModelCorbaHelper.write($out, data);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            modelChanged(data);
        } finally {
            _releaseReply($in);
        }
    } // modelChanged

    // Type-specific CORBA::Object operations
    private static String[] __ids = {
            "IDL:ConnectorCorba/ClientOperationsCorba:1.0"};

    public String[] _ids() {
        return (String[]) __ids.clone();
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException {
        String str = s.readUTF();
        String[] args = null;
        java.util.Properties props = null;
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
        try {
            org.omg.CORBA.Object obj = orb.string_to_object(str);
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
            _set_delegate(delegate);
        } finally {
            orb.destroy();
        }
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        String[] args = null;
        java.util.Properties props = null;
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
        try {
            String str = orb.object_to_string(this);
            s.writeUTF(str);
        } finally {
            orb.destroy();
        }
    }
} // class _ClientOperationsCorbaStub