package JLexPHP;
/**
 * ****************************
 * Questions: 2) How should I use the Java package system to make my tool more
 * modularized and coherent?
 *
 * Unimplemented: !) Fix BitSet issues -- expand only when necessary. 2)
 * Repeated accept rules. 6) Clean up the CAlloc class and use buffered
 * allocation. 9) Add to spec about extending character set. 11) m_verbose --
 * what should be done with it? 12) turn lexical analyzer into a coherent Java
 * package 13) turn lexical analyzer generator into a coherent Java package 16)
 * pretty up generated code 17) make it possible to have white space in regular
 * expressions 18) clean up all of the class files the lexer generator produces
 * when it is compiled, and reduce this number in some way. 24) character format
 * to and from file: writeup and implementation 25) Debug by testing all arcane
 * regular expression cases. 26) Look for and fix all UNDONE comments below. 27)
 * Fix package system. 28) Clean up unnecessary classes.
 * ***************************
 */

/*
 * SparseBitSet 25-Jul-1999.
 * C. Scott Ananian <cananian@alumni.princeton.edu>
 *
 * Re-implementation of the standard java.util.BitSet to support sparse
 * sets, which we need to efficiently support unicode character classes.
 */
/**
 * **********************************************************************
 * JLEX COPYRIGHT NOTICE, LICENSE AND DISCLAIMER.
 *
 * Copyright 1996 by Elliot Joel Berk
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose and without fee is hereby granted, provided
 * that the above copyright notice appear in all copies and that both the
 * copyright notice and this permission notice and warranty disclaimer appear in
 * supporting documentation, and that the name of Elliot Joel Berk not be used
 * in advertising or publicity pertaining to distribution of the software
 * without specific, written prior permission.
 *
 * Elliot Joel Berk disclaims all warranties with regard to this software,
 * including all implied warranties of merchantability and fitness. In no event
 * shall Elliot Joel Berk be liable for any special, indirect or consequential
 * damages or any damages whatsoever resulting from loss of use, data or
 * profits, whether in an action of contract, negligence or other tortious
 * action, arising out of or in connection with the use or performance of this
 * software.
 * *********************************************************************
 */
// set emacs indentation
// Local Variables:
// c-basic-offset:2
// End:

import java.util.Enumeration;

/**
 * A set of bits. The set automatically grows as more bits are needed.
 *
 * @author C. Scott Ananian
 * @version 1.00, 25 Jul 1999
 */
final class SparseBitSet implements Cloneable {

    /**
     * Sorted array of bit-block offsets.
     */
    int offs[];
    /**
     * Array of bit-blocks; each holding BITS bits.
     */
    long bits[];
    /**
     * Number of blocks currently in use.
     */
    int size;
    /**
     * log base 2 of BITS, for the identity: x/BITS == x >> LG_BITS
     */
    static final private int LG_BITS = 6;
    /**
     * Number of bits in a block.
     */
    static final private int BITS = 1 << LG_BITS;
    /**
     * BITS-1, using the identity: x % BITS == x & (BITS-1)
     */
    static final private int BITS_M1 = BITS - 1;

    /**
     * Creates an empty set.
     */
    public SparseBitSet() {
        bits = new long[4];
        offs = new int[4];
        size = 0;
    }

    /**
     * Creates an empty set with the specified size.
     *
     * @param nbits the size of the set
     */
    public SparseBitSet(int nbits) {
        this();
    }

    /**
     * Creates an empty set with the same size as the given set.
     */
    public SparseBitSet(JLexPHP.SparseBitSet set) {
        bits = new long[set.size];
        offs = new int[set.size];
        size = 0;
    }

    private void new_block(int bnum) {
        new_block(bsearch(bnum), bnum);
    }

    private void new_block(int idx, int bnum) {
        if (size == bits.length) { // resize
            long[] nbits = new long[size * 3];
            int[] noffs = new int[size * 3];
            System.arraycopy(bits, 0, nbits, 0, size);
            System.arraycopy(offs, 0, noffs, 0, size);
            bits = nbits;
            offs = noffs;
        }
        CUtility.ASSERT(size < bits.length);
        insert_block(idx, bnum);
    }

    private void insert_block(int idx, int bnum) {
        CUtility.ASSERT(idx <= size);
        CUtility.ASSERT(idx == size || offs[idx] != bnum);
        System.arraycopy(bits, idx, bits, idx + 1, size - idx);
        System.arraycopy(offs, idx, offs, idx + 1, size - idx);
        offs[idx] = bnum;
        bits[idx] = 0; //clear them bits.
        size++;
    }

    private int bsearch(int bnum) {
        int l = 0, r = size; // search interval is [l, r)
        while (l < r) {
            int p = (l + r) / 2;
            if (bnum < offs[p]) {
                r = p;
            } else if (bnum > offs[p]) {
                l = p + 1;
            } else {
                return p;
            }
        }
        CUtility.ASSERT(l == r);
        return l; // index at which the bnum *should* be, if it's not.
    }

    /**
     * Sets a bit.
     *
     * @param bit the bit to be set
     */
    public void set(int bit) {
        int bnum = bit >> LG_BITS;
        int idx = bsearch(bnum);
        if (idx >= size || offs[idx] != bnum) {
            new_block(idx, bnum);
        }
        bits[idx] |= (1L << (bit & BITS_M1));
    }

    /**
     * Clears a bit.
     *
     * @param bit the bit to be cleared
     */
    public void clear(int bit) {
        int bnum = bit >> LG_BITS;
        int idx = bsearch(bnum);
        if (idx >= size || offs[idx] != bnum) {
            new_block(idx, bnum);
        }
        bits[idx] &= ~(1L << (bit & BITS_M1));
    }

    /**
     * Clears all bits.
     */
    public void clearAll() {
        size = 0;
    }

    /**
     * Gets a bit.
     *
     * @param bit the bit to be gotten
     */
    public boolean get(int bit) {
        int bnum = bit >> LG_BITS;
        int idx = bsearch(bnum);
        if (idx >= size || offs[idx] != bnum) {
            return false;
        }
        return 0 != (bits[idx] & (1L << (bit & BITS_M1)));
    }

    /**
     * Logically ANDs this bit set with the specified set of bits.
     *
     * @param set the bit set to be ANDed with
     */
    public void and(JLexPHP.SparseBitSet set) {
        binop(this, set, AND);
    }

    /**
     * Logically ORs this bit set with the specified set of bits.
     *
     * @param set the bit set to be ORed with
     */
    public void or(JLexPHP.SparseBitSet set) {
        binop(this, set, OR);
    }

    /**
     * Logically XORs this bit set with the specified set of bits.
     *
     * @param set the bit set to be XORed with
     */
    public void xor(JLexPHP.SparseBitSet set) {
        binop(this, set, XOR);
    }

    // BINARY OPERATION MACHINERY
    private static interface BinOp {

        public long op(long a, long b);
    }

    private static final JLexPHP.SparseBitSet.BinOp AND = new JLexPHP.SparseBitSet.BinOp() {
        public final long op(long a, long b) {
            return a & b;
        }
    };
    private static final JLexPHP.SparseBitSet.BinOp OR = new JLexPHP.SparseBitSet.BinOp() {
        public final long op(long a, long b) {
            return a | b;
        }
    };
    private static final JLexPHP.SparseBitSet.BinOp XOR = new JLexPHP.SparseBitSet.BinOp() {
        public final long op(long a, long b) {
            return a ^ b;
        }
    };

    private static final void binop(JLexPHP.SparseBitSet a, JLexPHP.SparseBitSet b, JLexPHP.SparseBitSet.BinOp op) {
        int nsize = a.size + b.size;
        long[] nbits;
        int[] noffs;
        int a_zero, a_size;
        // be very clever and avoid allocating more memory if we can.
        if (a.bits.length < nsize) { // oh well, have to make working space.
            nbits = new long[nsize];
            noffs = new int[nsize];
            a_zero = 0;
            a_size = a.size;
        } else { // reduce, reuse, recycle!
            nbits = a.bits;
            noffs = a.offs;
            a_zero = a.bits.length - a.size;
            a_size = a.bits.length;
            System.arraycopy(a.bits, 0, a.bits, a_zero, a.size);
            System.arraycopy(a.offs, 0, a.offs, a_zero, a.size);
        }
        // ok, crunch through and binop those sets!
        nsize = 0;
        for (int i = a_zero, j = 0; i < a_size || j < b.size;) {
            long nb;
            int no;
            if (i < a_size && (j >= b.size || a.offs[i] < b.offs[j])) {
                nb = op.op(a.bits[i], 0);
                no = a.offs[i];
                i++;
            } else if (j < b.size && (i >= a_size || a.offs[i] > b.offs[j])) {
                nb = op.op(0, b.bits[j]);
                no = b.offs[j];
                j++;
            } else { // equal keys; merge.
                nb = op.op(a.bits[i], b.bits[j]);
                no = a.offs[i];
                i++;
                j++;
            }
            if (nb != 0) {
                nbits[nsize] = nb;
                noffs[nsize] = no;
                nsize++;
            }
        }
        a.bits = nbits;
        a.offs = noffs;
        a.size = nsize;
    }

    /**
     * Gets the hashcode.
     */
    public int hashCode() {
        long h = 1234;
        for (int i = 0; i < size; i++) {
            h ^= bits[i] * offs[i];
        }
        return (int) ((h >> 32) ^ h);
    }

    /**
     * Calculates and returns the set's size
     */
    public int size() {
        return (size == 0) ? 0 : ((1 + offs[size - 1]) << LG_BITS);
    }

    /**
     * Compares this object against the specified object.
     *
     * @param obj the object to commpare with
     * @return true if the objects are the same; false otherwise.
     */
    public boolean equals(Object obj) {
        if ((obj != null) && (obj instanceof JLexPHP.SparseBitSet)) {
            return equals(this, (JLexPHP.SparseBitSet) obj);
        }
        return false;
    }

    /**
     * Compares two SparseBitSets for equality.
     *
     * @return true if the objects are the same; false otherwise.
     */
    public static boolean equals(JLexPHP.SparseBitSet a, JLexPHP.SparseBitSet b) {
        for (int i = 0, j = 0; i < a.size || j < b.size;) {
            if (i < a.size && (j >= b.size || a.offs[i] < b.offs[j])) {
                if (a.bits[i++] != 0) {
                    return false;
                }
            } else if (j < b.size && (i >= a.size || a.offs[i] > b.offs[j])) {
                if (b.bits[j++] != 0) {
                    return false;
                }
            } else // equal keys
             if (a.bits[i++] != b.bits[j++]) {
                    return false;
                }
        }
        return true;
    }

    /**
     * Clones the SparseBitSet.
     */
    public Object clone() {
        try {
            JLexPHP.SparseBitSet set = (JLexPHP.SparseBitSet) super.clone();
            set.bits = (long[]) bits.clone();
            set.offs = (int[]) offs.clone();
            return set;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * Return an <code>Enumeration</code> of <code>Integer</code>s which
     * represent set bit indices in this SparseBitSet.
     */
    public Enumeration elements() {
        return new Enumeration() {
            int idx = -1, bit = BITS;

            {
                advance();
            }

            public boolean hasMoreElements() {
                return (idx < size);
            }

            public Object nextElement() {
                int r = bit + (offs[idx] << LG_BITS);
                advance();
                return new Integer(r);
            }

            private void advance() {
                while (idx < size) {
                    while (++bit < BITS) {
                        if (0 != (bits[idx] & (1L << bit))) {
                            return;
                        }
                    }
                    idx++;
                    bit = -1;
                }
            }
        };
    }

    /**
     * Converts the SparseBitSet to a String.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('{');
        for (Enumeration e = elements(); e.hasMoreElements();) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(e.nextElement());
        }
        sb.append('}');
        return sb.toString();
    }

    /**
     * Check validity.
     */
    private boolean isValid() {
        if (bits.length != offs.length) {
            return false;
        }
        if (size > bits.length) {
            return false;
        }
        if (size != 0 && 0 <= offs[0]) {
            return false;
        }
        for (int i = 1; i < size; i++) {
            if (offs[i] < offs[i - 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Self-test.
     */
    public static void main(String[] args) {
        final int ITER = 500;
        final int RANGE = 65536;
        JLexPHP.SparseBitSet a = new JLexPHP.SparseBitSet();
        CUtility.ASSERT(!a.get(0) && !a.get(1));
        CUtility.ASSERT(!a.get(123329));
        a.set(0);
        CUtility.ASSERT(a.get(0) && !a.get(1));
        a.set(1);
        CUtility.ASSERT(a.get(0) && a.get(1));
        a.clearAll();
        CUtility.ASSERT(!a.get(0) && !a.get(1));
        java.util.Random r = new java.util.Random();
        java.util.Vector v = new java.util.Vector();
        for (int n = 0; n < ITER; n++) {
            int rr = ((r.nextInt() >>> 1) % RANGE) << 1;
            a.set(rr);
            v.addElement(new Integer(rr));
            // check that all the numbers are there.
            CUtility.ASSERT(a.get(rr) && !a.get(rr + 1) && !a.get(rr - 1));
            for (int i = 0; i < v.size(); i++) {
                CUtility.ASSERT(a.get(((Integer) v.elementAt(i)).intValue()));
            }
        }
        JLexPHP.SparseBitSet b = (JLexPHP.SparseBitSet) a.clone();
        CUtility.ASSERT(a.equals(b) && b.equals(a));
        for (int n = 0; n < ITER / 2; n++) {
            int rr = (r.nextInt() >>> 1) % v.size();
            int m = ((Integer) v.elementAt(rr)).intValue();
            b.clear(m);
            v.removeElementAt(rr);
            // check that numbers are removed properly.
            CUtility.ASSERT(!b.get(m));
        }
        CUtility.ASSERT(!a.equals(b));
        JLexPHP.SparseBitSet c = (JLexPHP.SparseBitSet) a.clone();
        JLexPHP.SparseBitSet d = (JLexPHP.SparseBitSet) a.clone();
        c.and(a);
        CUtility.ASSERT(c.equals(a) && a.equals(c));
        c.xor(a);
        CUtility.ASSERT(!c.equals(a) && c.size() == 0);
        d.or(b);
        CUtility.ASSERT(d.equals(a) && !b.equals(d));
        d.and(b);
        CUtility.ASSERT(!d.equals(a) && b.equals(d));
        d.xor(a);
        CUtility.ASSERT(!d.equals(a) && !b.equals(d));
        c.or(d);
        c.or(b);
        CUtility.ASSERT(c.equals(a) && a.equals(c));
        c = (JLexPHP.SparseBitSet) d.clone();
        c.and(b);
        CUtility.ASSERT(c.size() == 0);
        System.out.println("Success.");
    }
}
