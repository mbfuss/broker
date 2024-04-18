package org.example.broker.component;

import org.example.broker.model.PrimeTask;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// PrimeNumberListener - listener-обработчик, который будет слушать сообщения из очереди
// Отвечает за обработку сообщений с числовыми диапазонами и вычисление простых чисел в этом диапазоне
@Component
public class PrimeNumberListener {

    // Cтатическое поле primeTaskMap для хранения задач на вычисление простых чисел
    // Используем HashMap для хранения ключ-значение, где ключ - уникальный идентификатор задачи, значение - объект PrimeTask
    public static Map<String, PrimeTask> primeTaskMap = new HashMap<>();

    // Аннотация @RabbitListener указывает на то, что метод receiveMessage будет обрабатывать сообщения из очереди queue1
    @RabbitListener(queues = "queue1")
    public void receiveMessage(String message) {
        // Разделяем сообщение по запятым для извлечения идентификатора задачи, начала и конца диапазона
        String[] parts = message.split(",");
        String taskGuid = parts[0];
        int start = Integer.parseInt(parts[1]);
        int end = Integer.parseInt(parts[2]);

        // Вычисляем список простых чисел в заданном диапазоне
        List<Integer> primeNumbers = calculatePrimeNumbers(start, end);

        // Если список простых чисел не пустой, создаем объект PrimeTask и сохраняем его в primeTaskMap
        if (!primeNumbers.isEmpty()) {
            PrimeTask primeTask = new PrimeTask(taskGuid, primeNumbers.get(0), primeNumbers.get(primeNumbers.size() - 1), primeNumbers.size());
            primeTaskMap.put(taskGuid, primeTask);
        }
    }

    // Метод для вычисления простых чисел в заданном диапазоне
    private List<Integer> calculatePrimeNumbers(int start, int end) {
        List<Integer> primeNumbers = new ArrayList<>();
        // Перебираем все числа от start до end и проверяем их на простоту
        for (int number = start; number <= end; number++) {
            if (isPrime(number)) {
                primeNumbers.add(number);
            }
        }
        return primeNumbers;
    }

    // Метод для проверки числа на простоту
    private boolean isPrime(int number) {
        // Проверяем, что число больше 1
        if (number <= 1) return false;

        // Перебираем числа от 2 до квадратного корня из number и проверяем делится ли number на них без остатка
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true; // Если число не делится нацело ни на одно число, значит оно простое
    }
}


