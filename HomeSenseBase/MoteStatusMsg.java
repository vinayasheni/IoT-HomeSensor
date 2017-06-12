/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'MoteStatusMsg'
 * message type.
 */

public class MoteStatusMsg extends net.tinyos.message.Message {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 2;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 137;

    /** Create a new MoteStatusMsg of size 2. */
    public MoteStatusMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new MoteStatusMsg of the given data_length. */
    public MoteStatusMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new MoteStatusMsg with the given data_length
     * and base offset.
     */
    public MoteStatusMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new MoteStatusMsg using the given byte array
     * as backing store.
     */
    public MoteStatusMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new MoteStatusMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public MoteStatusMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new MoteStatusMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public MoteStatusMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new MoteStatusMsg embedded in the given message
     * at the given base offset.
     */
    public MoteStatusMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new MoteStatusMsg embedded in the given message
     * at the given base offset and length.
     */
    public MoteStatusMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <MoteStatusMsg> \n";
      try {
        s += "  [status=0x"+Long.toHexString(get_status())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: status
    //   Field type: int, unsigned
    //   Offset (bits): 0
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'status' is signed (false).
     */
    public static boolean isSigned_status() {
        return false;
    }

    /**
     * Return whether the field 'status' is an array (false).
     */
    public static boolean isArray_status() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'status'
     */
    public static int offset_status() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'status'
     */
    public static int offsetBits_status() {
        return 0;
    }

    /**
     * Return the value (as a int) of the field 'status'
     */
    public int get_status() {
        return (int)getUIntBEElement(offsetBits_status(), 16);
    }

    /**
     * Set the value of the field 'status'
     */
    public void set_status(int value) {
        setUIntBEElement(offsetBits_status(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'status'
     */
    public static int size_status() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'status'
     */
    public static int sizeBits_status() {
        return 16;
    }

}