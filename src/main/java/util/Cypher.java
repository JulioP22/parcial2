package util;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

public class Cypher {
    private static Cypher cypher;
    private StrongPasswordEncryptor passwordEncryptor;
    private StrongTextEncryptor textEncryptor;


    private Cypher(){
        passwordEncryptor = new StrongPasswordEncryptor();
        textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword("123456");
    }

    public static Cypher getInstance(){
        if (cypher == null){
            cypher = new Cypher();
        }

        return cypher;
    }


    public String encrypt(String password){
        return passwordEncryptor.encryptPassword(password);
    }


    public boolean checkPassword(String inputPassword,String encryptedPassword){
        return passwordEncryptor.checkPassword(inputPassword,encryptedPassword);
    }

    public String encryptCookie(String param){
        System.out.println(param);
        return textEncryptor.encrypt(param);
    }

    public String decryptCookieParam(String param){
        return textEncryptor.decrypt(param);
    }
}
