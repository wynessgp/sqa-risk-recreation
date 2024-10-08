# method: `open(file: String): boolean`

## BVA Step 1
Input: The name of the CSS file to open from the `resources/presentation` directory

Output: A yes or no answer if the file was opened successfully and an internal file pointer that is set to the file, or an exception if the file can't be opened

## BVA Step 2
Input: String

Output: Boolean, Pointer

## BVA Step 3
Input:
- A CSS file that exists in the `resources/presentation` directory
- A CSS file that does not exist in the `resources/presentation` directory
- A non-CSS file that exists in the `resources/presentation` directory
- A directory

Output:
- Boolean:
    - 1 (true)
    - 0 (false, can't set)
    - NullPointerException
    - IllegalArgumentException
- Pointer:
    - Null pointer (will not test according to Martin's rules)
    - Valid file pointer URL

## BVA Step 4
### Test value 1
Input: `styles.css`

Output: 1, pointer to `styles.css`
### Test value 2
Input: `missing_file.css`

Output: NullPointerException "The requested file does not exist"
### Test value 3
Input: `start_screen.fxml`

Output: IllegalArgumentException "The requested file is not a CSS file"
### Test value 4
Input: `images`

Output: IllegalArgumentException "The requested file is not a CSS file"
