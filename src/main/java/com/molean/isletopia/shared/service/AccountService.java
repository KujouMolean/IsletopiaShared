package com.molean.isletopia.shared.service;

import com.molean.isletopia.shared.database.AccountDao;
import com.molean.isletopia.shared.model.Account;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Random;

public class AccountService {
    public static boolean register(String playerName, String address, String rawPassword) {
        try {
            Account tAccount = AccountDao.getAccount(playerName.toLowerCase(Locale.ROOT));
            if (tAccount != null) {
                return false;
            }
            Account account = new Account();
            account.setUsername(playerName.toLowerCase(Locale.ROOT));
            account.setRealname(playerName);
            account.setIp(address);
            account.setRegip(address);
            account.setRegdate(System.currentTimeMillis());
            account.setPassword(getSaltedSha256(rawPassword, (new Random().nextLong()) + ""));
            AccountDao.register(account);
            tAccount = AccountDao.getAccount(playerName.toLowerCase(Locale.ROOT));
            return tAccount != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean login(String playerName, String rawPassword) {
        try {
            Account account = AccountDao.getAccount(playerName.toLowerCase(Locale.ROOT));
            if (account == null) {
                return false;
            }
            String password = account.getPassword();
            return comparePassword(rawPassword, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean changePassword(String playerName, String rawPassword) {
        try {
            Account account = AccountDao.getAccount(playerName.toLowerCase(Locale.ROOT));
            if (account == null) {
                return false;
            }
            account.setPassword(getSaltedSha256(rawPassword, (new Random().nextLong()) + ""));
            AccountDao.updateAccount(account);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean comparePassword(String rawPassword, String encryptPassword) {
        String[] split = encryptPassword.split("\\$");
        String salt = split[2];
        return getSaltedSha256(rawPassword, salt).equals(encryptPassword);
    }

    public static String sha256(String message) {
        String hash;
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            algorithm.reset();
            algorithm.update(message.getBytes());
            byte[] digest = algorithm.digest();
            hash = String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return hash;
    }

    public static String getSaltedSha256(String password, String salt) {
        return "$SHA$" + salt + "$" + sha256(sha256(password) + salt);
    }
}
