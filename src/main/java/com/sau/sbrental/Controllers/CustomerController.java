package com.sau.sbrental.Controllers;

import com.sau.sbrental.Models.Customer;
import com.sau.sbrental.Repositories.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public String listCustomers(Model model) {
        List<Customer> customers = customerRepository.findAll();
        model.addAttribute("customers", customers);
        return "customers";
    }

    @GetMapping("/add")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer-form";
    }

    @PostMapping("/add")
    public String saveCustomer(@ModelAttribute Customer customer) {
        customerRepository.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String editCustomerForm(@PathVariable("id") Integer id, Model model) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz Müşteri ID: " + id));
        model.addAttribute("customer", customer);
        return "customer-edit-form";
    }

    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable("id") Integer id, @ModelAttribute Customer customer) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer == null) {
            return "redirect:/customers";
        }

        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setAddress(customer.getAddress());

        customerRepository.save(existingCustomer);
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Integer id) {
        customerRepository.deleteById(id);
        return "redirect:/customers";
    }
}