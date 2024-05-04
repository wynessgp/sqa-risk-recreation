# method: `open(file: String): boolean`

## BVA Step 1
Input: The name of the language bundle to open

Output: A yes or no answer if the file was opened successfully and an internal ResourceBundle pointer that is set to the file, or an exception if the file can't be opened

## BVA Step 2
Input: String

Output: Boolean, Pointer

## BVA Step 3
Input:
- The `English` language bundle
- The `Bruh` language bundle
- A language bundle that does not exist

Output:
- Boolean:
    - 1 (true)
    - 0 (false, can't set)
    - IllegalArgumentException
- Pointer:
    - Null pointer (will not test according to Martin's rules)
    - Valid ResourceBundle pointer

## BVA Step 4
### Test value 1
Input: `English`

Output: 1, pointer to `strings_English.properties`
### Test value 2
Input: `Bruh`

Output: 1, pointer to `strings_Bruh.properties`
### Test value 3
Input: `Nonexistent`

Output: IllegalArgumentException "The requested bundle does not exist"
