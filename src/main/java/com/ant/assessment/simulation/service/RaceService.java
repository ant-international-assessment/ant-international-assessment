package com.ant.assessment.simulation.service;

import com.ant.assessment.simulation.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class RaceService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ScoreService scoreService;

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

                    car.setPosition(1000);
                    car.setStatus("FINISHED");
                    broadcast(car);

                    originalCar.setPosition(car.getPosition());
                    originalCar.setStatus("FINISHED");

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
        afterRaceProcess(carList);
    }

    private void afterRaceProcess(List<Car> carList) {
        // 1. Sort by position to determine race results
        List<Car> sortedCars = carList.stream()
                .sorted(Comparator.comparingInt(Car::getPosition).reversed())
                .collect(Collectors.toList());

        List<String> finishOrder = sortedCars.stream()
                .map(Car::getName)
                .collect(Collectors.toList());

        // 2. Calculate scores for all cars
        Map<String, Integer> scoreMap = scoreService.calculateScores(finishOrder);

        // 3. Find the user car
        Car userCar = carList.stream()
                .filter(r -> !r.getUsername().isEmpty())
                .findFirst()
                .orElse(null);

        // 4. Save score only for the real user
        if (userCar != null) {
            String userId = userCar.getName();
            int earnedScore = scoreMap.getOrDefault(userId, 0);
            try {
                scoreService.saveResult(userId, finishOrder, earnedScore);
            } catch (Exception e) {
                System.err.println("Error saving user score: " + e.getMessage());
            }
        }
    }


    private void broadcast(Car car) {
        messagingTemplate.convertAndSend("/topic/car-progress", car);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
