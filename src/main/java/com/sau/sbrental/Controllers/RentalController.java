package com.sau.sbrental.Controllers;

import com.sau.sbrental.Models.Car;
import com.sau.sbrental.Models.Customer;
import com.sau.sbrental.Models.Rental;
import com.sau.sbrental.Repositories.CarRepository;
import com.sau.sbrental.Repositories.CustomerRepository;
import com.sau.sbrental.Repositories.RentalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rentals")
public class RentalController {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    public RentalController(RentalRepository rentalRepository, CarRepository carRepository, CustomerRepository customerRepository) {
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
    }

    // Kiralama Kayıtlarını Listele
    @GetMapping
    public String listRentals(Model model) {
        List<Rental> rentals = rentalRepository.findAll();
        model.addAttribute("rentals", rentals);
        return "rentals"; // templates/rentals.html
    }

    // Yeni Kiralama Kaydı Formunu Göster
    @GetMapping("/add")
    public String showAddRentalForm(Model model) {
        List<Car> availableCars = carRepository.findByAvailableTrue();
        List<Customer> customers = customerRepository.findAll();

        model.addAttribute("rental", new Rental());
        model.addAttribute("availableCars", availableCars);
        model.addAttribute("customers", customers);
        return "rental-add-form"; // templates/rental-add-form.html
    }

    // Yeni Kiralama Kaydı Oluştur
    @PostMapping("/add")
    public String saveRental(@ModelAttribute Rental rental,
                             @RequestParam("carId") Long carId,
                             @RequestParam("customerId") Integer customerId) {

        Car car = carRepository.findById(carId).orElse(null);
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (car != null && customer != null) {
            rental.setCar(car);
            rental.setCustomer(customer);
            rentalRepository.save(rental);

            // Aracı kiralanmış olarak işaretle
            car.setAvailable(false);
            carRepository.save(car);
        }

        return "redirect:/rentals";
    }

    // Kiralama Kaydını Sil
    @GetMapping("/delete/{id}")
    public String deleteRental(@PathVariable("id") int id) {
        Rental rental = rentalRepository.findById(id).orElse(null);
        if(rental != null){
            Car car = rental.getCar();
            if(car != null){
                car.setAvailable(true);
                carRepository.save(car);
            }
            rentalRepository.deleteById(id);
        }

        return "redirect:/rentals";
    }

    // Kiralama Kaydını Düzenleme Sayfasını Göster
    @GetMapping("/edit/{id}")
    public String editRentalForm(@PathVariable("id") int id, Model model) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz Kiralama ID: " + id));

        List<Car> availableCars = carRepository.findByAvailableTrue();
        List<Customer> customers = customerRepository.findAll();

        model.addAttribute("rental", rental);
        model.addAttribute("availableCars", availableCars);
        model.addAttribute("customers", customers);
        return "rental-edit-form"; // templates/rental-edit-form.html
    }

    // Kiralama Kaydını Güncelle
    @PostMapping("/edit/{id}")
    public String updateRental(@PathVariable("id") int id,
                               @ModelAttribute Rental rental,
                               @RequestParam("carId") Long carId,
                               @RequestParam("customerId") Integer customerId) {

        Rental existingRental = rentalRepository.findById(id).orElse(null);
        if (existingRental == null) {
            return "redirect:/rentals";
        }

        Car car = carRepository.findById(carId).orElse(null);
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (car != null && customer != null) {
            existingRental.setCar(car);
            existingRental.setCustomer(customer);
            existingRental.setRentDate(rental.getRentDate());
            existingRental.setReturnDate(rental.getReturnDate());
            existingRental.setStartDate(rental.getStartDate());
            existingRental.setEndDate(rental.getEndDate());
            existingRental.setPackageType(rental.getPackageType());
            existingRental.setPrice(rental.getPrice());

            rentalRepository.save(existingRental);
        }

        return "redirect:/rentals";
    }
}