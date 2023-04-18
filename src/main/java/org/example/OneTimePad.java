package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Описание программы:
 * Эта программа использует метод шифрования "одноразовый блокнот" (one-time pad) для шифрования и расшифрования сообщений.
 * Подробное описание того, как работает программа:
 *
 * Считывание данных: Программа считывает содержимое файла input.txt, который содержит исходное сообщение,
 * и сохраняет его в массив байтов inputBytes.
 *
 * Генерация ключа: Для генерации случайного ключа одноразового блокнота, программа создает массив байтов keyBytes такой же длины,
 * как и inputBytes. Затем она использует класс SecureRandom для генерации случайных значений байтов и сохраняет их в keyBytes.
 * Этот ключ записывается в файл key.txt.
 *
 * Шифрование: Шифрование выполняется с помощью операции XOR (исключающее ИЛИ) между исходным сообщением и ключом.
 * Программа вызывает функцию xor, которая принимает на вход два массива байтов (inputBytes и keyBytes)
 * и возвращает новый массив байтов, представляющий зашифрованный текст. Зашифрованный текст кодируется в Base64
 * и сохраняется в файл encrypted.txt.
 *
 * Расшифрование: Расшифрование выполняется снова с помощью операции XOR между зашифрованным текстом и ключом.
 * Программа вызывает функцию xor с аргументами encryptedBytes (результат шифрования) и keyBytes.
 * Возвращенный массив байтов представляет собой расшифрованное сообщение.
 * Расшифрованный текст сохраняется в файл decrypted.txt.
 *
 * Вывод информации: Программа выводит сообщения о завершении процессов шифрования и расшифрования, указывая,
 * что результаты сохранены в файлах encrypted.txt и decrypted.txt соответственно.
 *
 * Операция XOR обеспечивает симметричное шифрование и расшифрование, так как выполнение операции XOR дважды
 * с тем же ключом восстанавливает исходное сообщение. Теоретически, одноразовый блокнот является непроницаемой криптосистемой,
 * если ключ генерируется случайным образом, имеет длину, равную длине сообщения, и используется только однажды.
 * Однако на практике возникают сложности с генерацией, хранением и передачей длинных случайных ключей,
 * что может снизить уровень безопасности системы.
 */
public class OneTimePad {

  public static void main(String[] args) throws IOException {
    String inputFilePath = "input.txt";
    String keyFilePath = "key.txt";
    String encryptedFilePath = "encrypted.txt";
    String decryptedFilePath = "decrypted.txt";

    byte[] inputBytes = Files.readAllBytes(Paths.get(inputFilePath));

    // Генерируем случайны ключ, равной длины исходному посланию
    byte[] keyBytes = new byte[inputBytes.length];
    SecureRandom random = new SecureRandom();
    random.nextBytes(keyBytes);
    Files.write(Paths.get(keyFilePath), keyBytes);

    // Шифруем
    byte[] encryptedBytes = xor(inputBytes, keyBytes);
    String encrypted = Base64.getEncoder().encodeToString(encryptedBytes);
    Files.write(Paths.get(encryptedFilePath), encrypted.getBytes());

    // Дешифруем
    byte[] decryptedBytes = xor(encryptedBytes, keyBytes);
    String decrypted = new String(decryptedBytes);
    Files.write(Paths.get(decryptedFilePath), decrypted.getBytes());

    System.out.println("Зифрование послание прошло успешно. Зашифрованное послание сохранено в файле - " + encryptedFilePath);
    System.out.println("Дешифровка полсания прошла успешна. Послание записано в файл - " + decryptedFilePath);
  }

  private static byte[] xor(byte[] input, byte[] key) {
    byte[] output = new byte[input.length];
    for (int i = 0; i < input.length; i++) {
      output[i] = (byte) (input[i] ^ key[i]);
    }
    return output;
  }
}
