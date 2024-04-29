# method: `open(file: String): boolean`

## BVA Step 1
Input: The name of the FXML file to open from the `resources/presentation` directory

Output: A yes or no answer if the file was opened successfully and an internal file pointer that is set to the file, or an exception if the file can't be opened

## BVA Step 2
Input: String

Output: Boolean, Pointer

## BVA Step 3
Input:
- A FXML file that exists in the `resources/presentation` directory
- A FXML file that does not exist in the `resources/presentation` directory
- A non-FXML file that exists in the `resources/presentation` directory
- A directory

Output:
- Boolean:
  - 1 (true)
  - 0 (false, can't set)
  - NullPointerException
  - IllegalArgumentException
- Pointer:
  - Null pointer (will not test according to Martin's rules)
  - Valid file pointer

## BVA Step 4
### Test value 1
Input: `start_screen.fxml`

Output: 1, pointer to `start_screen.fxml`
### Test value 2
Input: `missing_file.fxml`

Output: NullPointerException "The requested file does not exist"
### Test value 3
Input: `styles.css`

Output: IllegalArgumentException "The requested file is not an FXML file"
### Test value 4
Input: `presentation`

Output: NullPointerException "The requested file does not exist"
