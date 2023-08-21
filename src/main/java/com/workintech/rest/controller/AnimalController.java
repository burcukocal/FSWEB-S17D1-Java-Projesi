package com.workintech.rest.controller;

import com.workintech.rest.entity.Animal;
import com.workintech.rest.mapping.AnimalResponse;
import com.workintech.rest.validation.AnimalValidation;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workintech/animal")
public class AnimalController {
    @Value("${instructor.name}")
    private  String name;
    @Value("${instructor.surname}")
    private  String surname;

    private Map<Integer, Animal> animals;

//    public AnimalController() {
//        animals = new HashMap<>();
//    }

    @PostConstruct
    public void init(){
        animals = new HashMap<>();
    }
    @GetMapping("/welcome")
    public String welcome(){
        return name + " - " + surname;
    }

    @GetMapping("/")
    public List<Animal> get(){
        return animals.values().stream().toList();
    }
    @GetMapping("/{id}")
    public AnimalResponse get(@PathVariable int id){
        if (!AnimalValidation.isIdValid(id)){
            return new AnimalResponse(null, "Id is not valid", 400);
        }
        if (!AnimalValidation.isMapContainsKey(animals,id)){
            return new AnimalResponse(null, "Animal is not exist", 400);
        }
        //return animals.get(id);
        return new AnimalResponse(animals.get(id), "Success", 200);
    }
    @PostMapping("/")
    public AnimalResponse save(@RequestBody Animal animal){
        if (AnimalValidation.isMapContainsKey(animals, animal.getId())){
            return new AnimalResponse(null, "Animal is already exist", 400);
        }
        if (!AnimalValidation.isAnimalCredentialsValid(animal)){
            return new AnimalResponse(null, "Animal credentials are not valid", 400);
        }
        animals.put(animal.getId(), animal);
        return new AnimalResponse(animals.get(animal.getId()), "success", 201);
    }

    @PutMapping("/{id}")
    public AnimalResponse update(@PathVariable int id, @RequestBody Animal animal){
        if (!AnimalValidation.isIdValid(id)){
            return new AnimalResponse(null, "Animal is already exist", 400);
        }
        if (!AnimalValidation.isAnimalCredentialsValid(animal)){
            return new AnimalResponse(null, "Animal credentials are not valid", 400);
        }
        animals.put(id, new Animal(id,animal.getName()));
        return new AnimalResponse(animals.get(animal.getId()), "success", 201);
    }

    @DeleteMapping("/{id}")
    public AnimalResponse deleteAnimal(@PathVariable int id){
        if (!AnimalValidation.isMapContainsKey(animals, id)){
            return new AnimalResponse(null, "Animal is already exist", 400);
        }
        Animal animal = animals.get(id);
        animals.remove(id);
        return new AnimalResponse(animal, "success",200);
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Animal Controller has been destroyed");
    }
}
