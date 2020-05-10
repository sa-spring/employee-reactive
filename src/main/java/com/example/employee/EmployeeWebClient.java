package com.example.employee;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.web.reactive.function.client.WebClient;

public class EmployeeWebClient {

    WebClient client = WebClient.create("http://localhost:8080");

    // public void consume() {

    // // Mono<Employee> employeeMono = client.get().uri("/employees/{id}",
    // "1").retrieve().bodyToMono(Employee.class);

    // // employeeMono.subscribe(System.out::println);

    // client.get()
    // .uri("/employees")
    // .retrieve()
    // .bodyToFlux(Employee.class)
    // .map(this::doSomeSlowWork)
    // .subscribe(employee -> {
    // System.out.println("Client subscribes: "+ employee);
    // });
    // }

    public void consume() {

        // Mono<Employee> employeeMono = client.get().uri("/employees/{id}",
        // "1").retrieve().bodyToMono(Employee.class);

        // employeeMono.subscribe(System.out::println);

        client.get().uri("/employees").retrieve().bodyToFlux(Employee.class).map(this::doSomeSlowWork)
                .subscribe(new Subscriber<Employee>() {
                    private Subscription subscription;
                    private Integer count = 0;

                    @Override
                    public void onNext(Employee t) {
                        count++;
                        if (count >= 2) {
                            count = 0;
                            subscription.request(2);
                            System.out.println("Client requested 2 ");

                        }
                        System.out.println("Client subscribes: " + t);
                    }

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        subscription.request(2);
                        System.out.println("Client requested 2 ");

                    }

                    @Override
                    public void onError(Throwable t) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Client completed");
                    }
                });
    }

    private Employee doSomeSlowWork(Employee employee) {
        try {
            Thread.sleep(90);
        } catch (InterruptedException e) {
        }
        return employee;
    }
}