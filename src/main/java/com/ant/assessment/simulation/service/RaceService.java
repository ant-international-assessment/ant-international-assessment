package com.ant.assessment.simulation.service;

import com.ant.assessment.simulation.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class RaceService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void startRace(List<Car> carList) throws InterruptedException {
        int numberOfCars = carList.size();

        CountDownLatch readyLatch = new CountDownLatch(numberOfCars);
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(numberOfCars);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfCars);

        for (Car originalCar : carList) {
            executor.submit(() -> {
                Car car = new Car(originalCar.getId(), originalCar.getName());
                readyLatch.countDown();

                try {
                    startSignal.await();

                    car.setStatus("RUNNING");
                    broadcast(car);

                    for (int pos = 0; pos < 1000; pos += getRandomNumber(1, 25)) {
                        Thread.sleep(100);
                        car.setPosition(pos);
                        broadcast(car);
                    }

                    System.out.println("Car " + car.getName() + " has finished!");
                    car.setPosition(1000);
                    car.setStatus("FINISHED");
                    broadcast(car);

                    finishLatch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        readyLatch.await();
        startSignal.countDown();
        finishLatch.await();
        executor.shutdown();
    }

    private void broadcast(Car car) {
        messagingTemplate.convertAndSend("/topic/car-progress", car);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
