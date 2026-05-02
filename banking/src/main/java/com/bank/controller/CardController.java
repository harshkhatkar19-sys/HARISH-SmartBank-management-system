package com.bank.controller;

import com.bank.model.Card;
import com.bank.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/issue")
    public String issueCard(@RequestBody Map<String, String> payload) {
        String accNo = payload.get("accountNumber");
        String holderName = payload.get("holderName");
        String cardType = payload.get("cardType");
        
        // We could also call a check method here, but let's just use the service's result for now.
        // To be more precise, let's update the service to throw an exception or return a status code.
        // For simplicity, let's just assume "FAILED" might mean already exists if we don't have other errors.
        // Actually, let's make it better.
        
        if (cardService.issueCard(accNo, holderName, cardType)) {
            return "SUCCESS";
        }
        return "ALREADY_EXISTS_OR_FAILED";
    }

    @GetMapping("/{accNo}")
    public List<Card> getCards(@PathVariable String accNo) {
        return cardService.getCardsByAccount(accNo);
    }

    @PutMapping("/status")
    public String updateStatus(@RequestBody Map<String, String> payload) {
        String cardNumber = payload.get("cardNumber");
        String status = payload.get("status");
        if (cardService.updateCardStatus(cardNumber, status)) {
            return "SUCCESS";
        }
        return "FAILED";
    }

    @PutMapping("/pin")
    public String updatePin(@RequestBody Map<String, String> payload) {
        String cardNumber = payload.get("cardNumber");
        String newPin = payload.get("pin");
        if (cardService.updatePin(cardNumber, newPin)) {
            return "SUCCESS";
        }
        return "FAILED";
    }

    @DeleteMapping("/{accNo}")
    public String deleteCards(@PathVariable String accNo) {
        if (cardService.deleteCardsByAccount(accNo)) {
            return "SUCCESS";
        }
        return "FAILED";
    }
}
