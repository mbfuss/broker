package org.example.broker.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

// Класс RabbitConfig содержит конфигурацию для подключения к брокеру сообщений RabbitMQ и создания необходимых элементов (Queue, Exchange, Binding)
@Configuration
public class RabbitConfig {

    // Метод connectionFactory() создает и конфигурирует подключение к RabbitMQ
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost"); // Устанавливаем адрес сервера RabbitMQ
        cachingConnectionFactory.setUsername("guest"); // Устанавливаем логин
        cachingConnectionFactory.setPassword("guest"); // Устанавливаем пароль
        cachingConnectionFactory.setVirtualHost("cpp"); // Устанавливаем виртуальный хост
        return cachingConnectionFactory; // Возвращаем сконфигурированное подключение
    }

    // Метод amqpAdmin() создает объект RabbitAdmin для управления элементами RabbitMQ (Queue, Exchange, Binding)
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory()); // Используем созданное подключение для создания RabbitAdmin
    }

    // Метод rabbitTemplate() создает объект RabbitTemplate для отправки и получения сообщений через RabbitMQ
    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory()); // Используем созданное подключение для создания RabbitTemplate
    }

    @Bean
    public Queue myQueue() {
        return new Queue("queue1");
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("testExchange", true, false);
    }

    // Метод binding() создает привязку (Binding) между Queue и Exchange с определенным routing key
    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("testRoutingKey"); // Привязываем Queue к Exchange с определенным routing key
    }
}

