package com.habitify.login.Auth;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import de.taimos.totp.TOTP;

public class OneTimeAuth {
    public String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public boolean validate(String secretKey, String otp){
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.validate(hexKey,otp);
    }

    public void authenticate(String secretKey ) {
        String lastCode = null;
        while (true) {
            String code = getTOTPCode(secretKey);
            if (!code.equals(lastCode)) {
                System.out.println(code);
            }
            lastCode = code;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            ;
        }
    }


    public void createQRCode(String barCodeData, String filePath, int
            height, int width) throws WriterException, IOException {
        BitMatrix matrix =
                new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width,
                        height);
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        }
    }

    public String getGoogleAuthenticatorBarCode(String secretKey, String
            account, String issuer) {
        try {
            return "otpauth://totp/" +
                    URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20") +
                    "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20") +
                    "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
        } catch
        (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getQrURL(String barcode){
        String url = "https://www.google.com/chart?chs=200x200&chld=M|0&cht=qr&chl=";
        url += barcode;
        return url;
    }


}