package com.dashingdanstrains.dcstester;

public class SpeckCipher {

    public static String authenticate(String challenge){
        long[] plaintext = new long[2];
        long[] ciphertext = new long[2];
        long result = 0;
        result = Long.parseLong(challenge, 16);
        plaintext[0] = 65535 & result;
        plaintext[1] = result >> 16;
        long[][] res = encrypt(plaintext, ciphertext, key);
        long[] ciphertext2 = res[1];
        long out = (ciphertext2[1] << 16) | ciphertext2[0];
        return Long.toHexString(out);
    }

    private static long[][] encrypt(long[] pt, long[] ct, char[] k) {
        int i;
        char[] kr = new char[SPECK_M];
        for (int i2 = 0; i2 < SPECK_N; i2++) {
            ct[i2] = pt[i2];
        }
        for (int i3 = 0; i3 < 4; i3++) {
            kr[i3] = k[kIndex];
            int i4 = kIndex;
            kIndex = i4 + 1;
            if (i4 >= k.length - 1) {
                i = 0;
            } else {
                i = kIndex;
                kIndex = i + 1;
            }
            kIndex = i;
        }
        for (int i5 = 0; i5 < SPECK_ROUNDS; i5++) {
            long[] rnd1 = rnd(ct[1], ct[0], kr[0]);
            ct[1] = (char) rnd1[0];
            ct[0] = (char) rnd1[1];
            long[] rnd2 = rnd(kr[1], kr[0], i5);
            kr[1] = (char) rnd2[0];
            kr[0] = (char) rnd2[1];
            char tmp = kr[1];
            kr[1] = kr[2];
            kr[2] = kr[3];
            kr[3] = tmp;
        }
        return new long[][]{pt, ct};
    }
    private static final int SPECK_ALPHA = 7;
    private static final int SPECK_BETA = 2;
    private static final int SPECK_M = 4;
    private static final int SPECK_N = 2;
    private static final int SPECK_ROUNDS = 22;
    static int kIndex = 0;
    static char[] key = {5196, 46084, 38013, 32838};

    private static long ROR16(long x, int r) {
        return (x >> r) | (x << (16 - r));
    }

    private static long ROL16(long x, int r) {
        return (x << r) | (x >> (16 - r));
    }

    private static long[] rnd(long x, long y, int k) {
        long _x = (ROR16(x, SPECK_ALPHA) + y) ^ k;
        long _y = ROL16(y, SPECK_BETA) ^ _x;
        return new long[]{_x, _y};
    }
}