# Block Cipher Encryption

## Overview

A 4-bit substitution cipher to encrypt an array of bytes
(i.e. array of 4 bits). The substitution cipher maps each 4-bit input block to a different
4-bit output block.

## System Requirements

- Java JDK

```https://www.oracle.com/java/technologies/downloads/

```

- Visual Studio Code
- Get the 'Extension Pack for Java':

```
Name: Extension Pack for Java
Id: vscjava.vscode-java-pack
Description: Popular extensions for Java development that provides Java IntelliSense, debugging, testing, Maven/Gradle support, project management and more
Version: 0.29.0
Publisher: Microsoft
VS Marketplace Link: https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack
```

## Local Instance Instructions

1. Unzip this folder
2. Included a settings.json inside of block-cipher-encryption/.vscode to streamline the process of running Java files.
3. In block-cipher-encryption/src, run MainProgram.

## Test Cases

1.

```
Enter plaintext in hex: f3
Enter IV in hex (single hex digit): E
Plaintext: f3
ECB Ciphertext, in hex: b4
CTR Ciphertext, in hex: f8
CBC Ciphertext, in hex: 63
```

2.

```
Enter plaintext in hex: f310
Enter IV in hex (single hex digit): E
Plaintext: f310
ECB Ciphertext, in hex: b46c
CTR Ciphertext, in hex: f8d6
CBC Ciphertext, in hex: 639e
```

3.

```
Enter plaintext in hex: f310
Enter IV in hex (single hex digit): 6
Plaintext: f310
ECB Ciphertext, in hex: b46c
CTR Ciphertext, in hex: 52ce
CBC Ciphertext, in hex: e8e0
```

4.

```
Enter plaintext in hex: 123456789abcdefd456789aca32eb1fd456789aca32eb1fd456789aca32eb1f
Enter IV in hex (single hex digit): 6
Plaintext: 123456789abcdefd456789aca32eb1fd456789aca32eb1fd456789aca32eb1f
ECB Ciphertext, in hex: c694f3a1de75280b8f3a1de72749056b8f3a1de72749056b8f3a1de72749056b
CTR Ciphertext, in hex: a0fd304f826d591c75880db2c1f47fec75880db2c1f47fec75880db2c1f47fec
CBC Ciphertext, in hex: a5198801e1502b32bb0a8c3e94147245de54425b15ad4b7dcdd596b67851bc81
```
