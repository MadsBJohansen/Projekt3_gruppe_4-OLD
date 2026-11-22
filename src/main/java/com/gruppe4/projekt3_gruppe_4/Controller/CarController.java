package com.gruppe4.projekt3_gruppe_4.Controller;

import com.gruppe4.projekt3_gruppe_4.Model.Car;
import com.gruppe4.projekt3_gruppe_4.Repo.CarModelRepo;
import com.gruppe4.projekt3_gruppe_4.Service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;

@Controller
public class CarController {

    private final CarModelRepo carModelRepo;
    private final CarService carService;

    public CarController(CarModelRepo carModelRepo, CarService carService) {
        this.carModelRepo = carModelRepo;
        this.carService = carService;
    }

    @GetMapping("/cars/create")
    public String showCreateCarForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("carModels", carModelRepo.findAllCarModels());

        return "create-car";
    }

    @PostMapping("/cars/create")
    public String createCar(
            @ModelAttribute("car") Car car,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Check that all numeric fields contain valid numbers.");
            model.addAttribute("carModels", carModelRepo.findAllCarModels());
            return "create-car";
        }

        try {
            carService.createCar(car);
            return "redirect:/cars/create";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("carModels", carModelRepo.findAllCarModels());
            return "create-car";
        }
    }
}