package com.company.web.wallet.controllers.MvcController;

import com.company.web.wallet.helpers.TransactionMapper;
import com.company.web.wallet.models.DTO.TransactionDto;
import com.company.web.wallet.models.Transaction;
import com.company.web.wallet.services.TransactionService;
import com.company.web.wallet.helpers.TransactionType;
import com.company.web.wallet.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transactions")
public class TransactionMVCController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper; // Assuming you have a mapper for mapping Transaction to TransactionDto

    @Autowired
    public TransactionMVCController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping()
    public String showTransactions(Model model) {
        List<Transaction> transactions = transactionService.getAllTransactions();

        // Map the transactions to TransactionDto objects using the TransactionMapper
        List<TransactionDto> transactionDtos = transactionMapper.toDtoList(transactions);

        model.addAttribute("transactionsDto", transactionDtos);
        return "transaction_list"; // Return the name of the view template
    }

    @PostMapping()
    public String filterTransactions(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) TransactionType direction,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection,
            Model model) {
        List<Transaction> filteredTransactions = transactionService.getTransactions(
                username, startDate, endDate, direction, sortBy, sortDirection);

        // Map the filtered transactions to TransactionDto objects using the TransactionMapper
        List<TransactionDto> filteredTransactionDtos = transactionMapper.toDtoList(filteredTransactions);

        model.addAttribute("transactions", filteredTransactionDtos);
        return "transaction_list"; // Return the name of the view template
    }


    // Other methods
}