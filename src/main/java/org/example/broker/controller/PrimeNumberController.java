package org.example.broker.controller;

import org.example.broker.component.PrimeNumberListener;
import org.example.broker.model.PrimeTask;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/calc")
// Класс PrimeNumberController отвечает за обработку запросов связанных с поиском простых чисел и управлением задачами поиска

public class PrimeNumberController {

    @Autowired
    private RabbitTemplate rabbitTemplate; // Инжектируем RabbitTemplate для отправки сообщений в RabbitMQ

    // Метод calculatePrimeNumbers() обрабатывает POST запрос на расчет простых чисел в заданном диапазоне
    @PostMapping("/prime")
    public ResponseEntity<String> calculatePrimeNumbers(@RequestBody Map<String, Integer> request) {
        UUID taskGuid = UUID.randomUUID(); // Генерируем UUID для идентификации задачи
        int start = request.get("start"); // Получаем начало диапазона из запроса
        int end = request.get("end"); // Получаем конец диапазона из запроса

        String message = taskGuid.toString() + "," + start + "," + end; // Формируем сообщение для отправки в очередь RabbitMQ
        rabbitTemplate.convertAndSend("queue1", message); // Отправляем сообщение в очередь "queue1" в RabbitMQ

        return ResponseEntity.ok(taskGuid.toString()); // Возвращаем ответ с UUID задачи
    }

    // Метод getPrimeNumbersTask() обрабатывает GET запрос для получения информации о задаче поиска простых чисел по заданному UUID
    @GetMapping("/prime/{guid}")
    public ResponseEntity<Object> getPrimeNumbersTask(@PathVariable String guid) {
        if (PrimeNumberListener.primeTaskMap.containsKey(guid)) { // Проверяем, содержит ли карта задач указанный UUID
            PrimeTask primeTask = PrimeNumberListener.primeTaskMap.get(guid); // Получаем объект PrimeTask из карты по UUID
            return ResponseEntity.ok(Map.of("first", primeTask.getStartNumber(), "last", primeTask.getEndNumber(), "count", primeTask.getPrimeCount())); // Возвращаем информацию о задаче
        } else {
            return ResponseEntity.notFound().build(); // Если задачи с указанным UUID не существует, возвращаем 404 Not Found
        }
    }
}