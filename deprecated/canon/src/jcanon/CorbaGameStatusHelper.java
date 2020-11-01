package jcanon;


/**
* jcanon/CorbaGameStatusHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /home/vpc/xprojects/games/canon/src/tn/rnu/enit/ateliercorba/jcanon/jcanon.idl
* lundi 15 octobre 2007 19 h 43 CEST
*/

abstract public class CorbaGameStatusHelper
{
  private static String  _id = "IDL:tn.rnu.enit.ateliercorba/jcanon/CorbaGameStatus:1.0";

  public static void insert (org.omg.CORBA.Any a, jcanon.CorbaGameStatus that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static jcanon.CorbaGameStatus extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_enum_tc (jcanon.CorbaGameStatusHelper.id (), "CorbaGameStatus", new String[] { "GAME_INIT", "GAME_STARTED", "GAME_OVER"} );
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static jcanon.CorbaGameStatus read (org.omg.CORBA.portable.InputStream istream)
  {
    return jcanon.CorbaGameStatus.from_int (istream.read_long ());
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, jcanon.CorbaGameStatus value)
  {
    ostream.write_long (value.value ());
  }

}