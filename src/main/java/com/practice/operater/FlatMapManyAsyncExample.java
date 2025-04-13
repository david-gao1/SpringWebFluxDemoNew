package com.practice.operater;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class FlatMapManyAsyncExample {
    public static void main(String[] args) {
        // 模拟从数据库中查询一个用户信息
        Mono<User> userMono = Mono.just(new User(1, "Alice"));

        // 使用 flatMapMany 将用户信息转换为该用户的订单信息 Flux
        Flux<Order> orderFlux = userMono.flatMapMany(user -> {
            // 模拟根据用户 ID 查询订单信息
            List<Order> orders = Arrays.asList(
                    new Order(101, user.getId(), "Product A"),
                    new Order(102, user.getId(), "Product B")
            );
            return Flux.fromIterable(orders);
        });

        // 订阅 Flux 并打印结果
        orderFlux.subscribe(System.out::println);
    }

    static class User {
        private int id;
        private String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }
    }

    static class Order {
        private int orderId;
        private int userId;
        private String productName;

        public Order(int orderId, int userId, String productName) {
            this.orderId = orderId;
            this.userId = userId;
            this.productName = productName;
        }

        @Override
        public String toString() {
            return "Order{orderId=" + orderId + ", userId=" + userId + ", productName='" + productName + "'}";
        }
    }
}