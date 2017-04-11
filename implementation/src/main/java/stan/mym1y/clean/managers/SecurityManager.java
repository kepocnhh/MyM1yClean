package stan.mym1y.clean.managers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

import stan.mym1y.clean.components.Security;

public class SecurityManager
    implements Security
{
    private final Random random = new Random();
    private final MessageDigest messageDigestSha512;

    public SecurityManager()
    {
        try
        {
            messageDigestSha512 = MessageDigest.getInstance("SHA-512");
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String sha512(String data)
    {
        try
        {
            byte[] bytes = messageDigestSha512.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(byte aByte : bytes)
            {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }
        catch(UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }
    public int newUniqueId()
    {
        return random.nextInt(Integer.MAX_VALUE-2)+1;
    }
    public String newUUID()
    {
        return UUID.randomUUID().toString();
    }
}