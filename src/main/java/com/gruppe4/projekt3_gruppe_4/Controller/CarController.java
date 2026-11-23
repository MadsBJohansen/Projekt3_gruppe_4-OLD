package com.gruppe4.projekt3_gruppe_4.Controller;

import com.gruppe4.projekt3_gruppe_4.Model.Car;
import com.gruppe4.projekt3_gruppe_4.Repo.CarModelRepo;
import com.gruppe4.projekt3_gruppe_4.Repo.LocationRepo;
import com.gruppe4.projekt3_gruppe_4.Service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CarController {
    private final CarModelRepo carModelRepo;
    private final CarService carService;
    private final LocationRepo locationRepo;

    public CarController(CarModelRepo carModelRepo, CarService carService, LocationRepo locationRepo) {
        this.carModelRepo = carModelRepo;
        this.carService = carService;
        this.locationRepo = locationRepo;
    }

    @ModelAttribute
    public void addFormOptions(Model model) {
        model.addAttribute("carModels", carModelRepo.findAllCarModels());
        model.addAttribute("locations", locationRepo.findAllLocations());
    }

    @GetMapping("/cars/create")
    public String showCreateCarForm(Model model) {
        model.addAttribute("car", new Car());
        return "create-car";
    }

    @PostMapping("/cars/create")
    public String createCar(@ModelAttribute("car") Car car, BindingResult bindingResult,
                            Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Check that all numeric fields contain valid numbers. Your other entries have been kept.");
            return "create-car";
        }
        if (!locationRepo.existsById(car.getCurrentLocationId())) {
            bindingResult.rejectValue("currentLocationId", "invalidLocation", "Choose an existing location.");
            model.addAttribute("error", "Choose an existing location. Your other entries have been kept.");
            return "create-car";
        }
        try {
            carService.createCar(car);
            redirectAttributes.addFlashAttribute("success", "Car " + car.getLicensePlate()
                    + " was created with status Available.");
            return "redirect:/cars/create";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "create-car";
        }
    }
}
