package util;

/**
 * InputParser — utility class to parse and validate user input from the GUI.
 *
 * Used by all GUI panels to convert text field strings into int arrays.
 */
public class InputParser {

    /**
     * Parses a comma-separated or space-separated string of integers.
     * Example: "7, 0, 1, 2, 0, 3" → [7, 0, 1, 2, 0, 3]
     *
     * @throws IllegalArgumentException if the input is empty or contains non-integers
     */
    public static int[] parseIntArray(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }

        String[] tokens = input.trim().split("[,\\s]+");
        int[] result = new int[tokens.length];

        for (int i = 0; i < tokens.length; i++) {
            try {
                result[i] = Integer.parseInt(tokens[i].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number: '" + tokens[i] + "'");
            }
        }

        return result;
    }

    /**
     * Parses a single integer from a text field.
     * @throws IllegalArgumentException if not a valid positive integer
     */
    public static int parsePositiveInt(String input, String fieldName) {
        try {
            int value = Integer.parseInt(input.trim());
            if (value <= 0) throw new IllegalArgumentException(fieldName + " must be greater than 0.");
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid integer.");
        }
    }
}
