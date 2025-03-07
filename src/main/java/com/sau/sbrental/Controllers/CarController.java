package com.sau.sbrental.Controllers;

import com.sau.sbrental.Models.Car;
import com.sau.sbrental.Repositories.CarRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // 🚗 **Tüm araçları listele**
    @GetMapping
    public String listCars(Model model) {
        List<Car> cars = carRepository.findAll();

        // Resimleri Base64 formatına çevirerek modele ekle
        for (Car car : cars) {
            if (car.getImage() != null) {
                car.setImageBase64(Base64.getEncoder().encodeToString(car.getImage()));
            }
        }

        model.addAttribute("cars", cars);
        return "cars"; // templates/cars.html
    }

    // ➕ **Yeni araç ekleme formunu aç**
    @GetMapping("/add")
    public String addCarForm(Model model) {
        model.addAttribute("car", new Car());
        return "car-add-form"; // templates/car-add-form.html
    }

    // ✅ **Yeni araç kaydet (Resim Dahil)**
    @PostMapping("/add")
    public String saveCar(@ModelAttribute Car car, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (!imageFile.isEmpty()) {
                car.setImage(imageFile.getBytes()); // Resmi byte dizisi olarak kaydet
            }
            carRepository.save(car);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/cars";
    }

    // ✏️ **Düzenleme sayfasını aç**
    @GetMapping("/edit/{id}")
    public String editCarForm(@PathVariable("id") Long id, Model model) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz Araç ID: " + id));

        // Resmi Base64 olarak hazırla
        if (car.getImage() != null) {
            car.setImageBase64(Base64.getEncoder().encodeToString(car.getImage()));
        }

        model.addAttribute("car", car);
        return "car-edit-form"; // templates/car-edit-form.html
    }

    // ✅ **Araç güncelleme işlemi (Resim Dahil)**
    @PostMapping("/edit/{id}")
    public String updateCar(@PathVariable("id") Long id, @ModelAttribute Car car, @RequestParam("imageFile") MultipartFile imageFile) {
        Car existingCar = carRepository.findById(id).orElse(null);
        if (existingCar == null) {
            return "redirect:/cars";
        }

        // Güncellenmiş bilgileri mevcut nesneye ata
        existingCar.setBrand(car.getBrand());
        existingCar.setModel(car.getModel());
        existingCar.setPrice(car.getPrice());
        existingCar.setYear(car.getYear());
        existingCar.setPlate(car.getPlate());
        existingCar.setAvailable(car.isAvailable());

        try {
            if (!imageFile.isEmpty()) {
                existingCar.setImage(imageFile.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        carRepository.save(existingCar);
        return "redirect:/cars";
    }

    // ❌ **Araç silme işlemi**
    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable("id") Long id) {
        carRepository.deleteById(id);
        return "redirect:/cars";
    }
}
