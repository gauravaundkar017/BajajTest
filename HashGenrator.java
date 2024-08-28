import org.json.*;
import java.io.FileReader;
import java.security.MessageDigest;
import java.util.Random;
import java.io.FileNotFoundException;

public class HashGenrator {

    public static void main(String[] args) {
        if (args.length != 2) {
           
            return;
        }

        String prnNumber = args[0].toLowerCase();
        String jsonFilePath = args[1];

        try {
            // Step 2: Read and Parse the JSON File
            FileReader reader = new FileReader(jsonFilePath);
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

            // Step 3: Traverse JSON to find the first "destination"
            String destinationValue = findDestinationValue(jsonObject);

            if (destinationValue == null) {
                System.out.println("No 'destination' key found in the JSON file.");
                return;
            }

            // Step 4: Generate a Random Alphanumeric String
            String randomString = generateRandomString(8);

            // Step 5: Concatenate PRN Number, Destination Value, and Random String, then generate MD5 Hash
            String concatenatedString = prnNumber + destinationValue + randomString;
            String md5Hash = generateMD5Hash(concatenatedString);

            // Step 6: Print the result
            System.out.println(md5Hash + ";" + randomString);

        } catch (FileNotFoundException e) {
            System.out.println("JSON file not found: " + jsonFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String findDestinationValue(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);

            if (key.equals("destination")) {
                return value.toString();
            } else if (value instanceof JSONObject) {
                String result = findDestinationValue((JSONObject) value);
                if (result != null) {
                    return result;
                }
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    if (array.get(i) instanceof JSONObject) {
                        String result = findDestinationValue(array.getJSONObject(i));
                        if (result != null) {
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }

    private static String generateMD5Hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
