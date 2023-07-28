package dev.crevan.l2j.c1.loginserver;

import java.io.*;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import dev.crevan.l2j.c1.loginserver.exception.HackingException;
import dev.crevan.l2j.c1.util.Base64;

public class Logins {

    private static final String SHA = "SHA";
    public static final Logger log = Logger.getLogger(Logins.class.getName());

    private final Map<String, byte[]> logPass;
    private final Map<String, Integer> accessLevels;
    private final Map<String, Integer> hackProtection;
    private final boolean autoCreate;

    public Logins(final boolean autoCreate) {
        logPass = new HashMap<>();
        accessLevels = new HashMap<>();
        hackProtection = new HashMap<>();
        this.autoCreate = autoCreate;
        log.config("Auto creating new accounts: " + autoCreate);
        File loginFile = new File("data/logins.txt");
        if (loginFile.exists()) {
            readAccountsFromDisk(loginFile);
        }
    }

    public int getAccessLevel(final String user) {
        return accessLevels.get(user);
    }

    public boolean loginValid(final String user, final String password, final InetAddress address) throws HackingException {
        boolean isOk;
        Integer failedConnects = hackProtection.get(address.getHostAddress());
        if (failedConnects != null && failedConnects > 2) {
            log.warning("Hacking detected from ip: " + address.getHostAddress() + " ... adding IP to banList");
            throw new HackingException(address.getHostAddress());
        }

        try {
            MessageDigest md = MessageDigest.getInstance(SHA);
            byte[] raw = password.getBytes(StandardCharsets.UTF_8);
            byte[] hash = md.digest(raw);

            byte[] expected = logPass.get(user);

            if (expected == null) {
                if (autoCreate) {
                    logPass.put(user, hash);
                    accessLevels.put(user, 0);
                    log.info("Created new account for user: " + user);
                    saveAccountsToDisk();
                    return true;
                } else {
                    log.warning("Account missing for user: " + user);
                    return false;
                }
            }
            isOk = true;
            for (int i = 0; i < expected.length; i++) {
                if (hash[i] != expected[i]) {
                    isOk = false;
                    break;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            log.warning("No such algorithm");
            isOk = false;
            e.printStackTrace();
        }

        if (!isOk) {
            int failedCount = 1;
            if (failedConnects != null) {
                failedCount = failedConnects + 1;
            }
            hackProtection.put(address.getHostAddress(), failedCount);
        } else {
            hackProtection.remove(address.getHostAddress());
        }

        return isOk;
    }

    public void saveAccountsToDisk() {
        File login = new File("data/logins.txt");
        try (FileWriter writer = new FileWriter(login)) {
            for (String name : logPass.keySet()) {
                byte[] pass = logPass.get(name);
                writer.write(name);
                writer.write("\t");
                writer.write(Base64.encodeBytes(pass));
                writer.write("\t");
                writer.write(accessLevels.get(name));
                writer.write("\r\n");
            }
        } catch (IOException ioe) {
            log.warning("Error while saving accounts to file");
            ioe.printStackTrace();
        }
    }

    private void readAccountsFromDisk(final File file) {
        logPass.clear();
        try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file))) {
            int i = 0;
            String line;
            while ( (line = lineNumberReader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, "\t\n\r");
                if (tokenizer.hasMoreTokens()) {
                    String name = tokenizer.nextToken().toLowerCase();
                    String password = tokenizer.nextToken();
                    logPass.put(name, Base64.decode(password));

                    if (tokenizer.hasMoreTokens()) {
                        String access = tokenizer.nextToken();
                        Integer level = Integer.parseInt(access);
                        accessLevels.put(access, level);
                    } else {
                        accessLevels.put(name, 0);
                    }
                    i++;
                }
            }
            log.config("Found " + i + " logins on disk");
        } catch (IOException ioe) {
            log.warning("Error while logins file reading");
            ioe.printStackTrace();
        }
    }
}
