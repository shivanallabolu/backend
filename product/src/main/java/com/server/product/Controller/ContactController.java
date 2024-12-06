package com.server.product.Controller;

import com.server.product.Model.Entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://budget-manager-1cyf.onrender.com", allowedHeaders = "*", methods = {RequestMethod.POST})
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private com.server.product.Repository.ContactRepository contactRepository;

    @PostMapping("/save")
    public ResponseEntity<Contact> saveContact(@RequestBody Contact contact) {
        Contact savedContact = contactRepository.save(contact);
        return ResponseEntity.ok(savedContact);
    }
}
