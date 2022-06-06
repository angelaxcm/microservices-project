package com.grp4.gcash.mini.transactionservice.controller;

import com.grp4.gcash.mini.transactionservice.payload.CashInRequest;
import com.grp4.gcash.mini.transactionservice.payload.CashInResponse;
import com.grp4.gcash.mini.transactionservice.payload.CashOutRequest;
import com.grp4.gcash.mini.transactionservice.payload.CashOutResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("transaction")
public class TransactionController {


    @PostMapping("cash-in")
    public CashInResponse cashIn(@Valid @RequestBody CashInRequest request) throws UserNotFoundException {
//        User user = userRepository.findByNumber(request.getMobileNumber());
        CashInResponse cashInResponse = new CashInResponse();
        Double Balance = 1000.0;
        Balance += request.getCashInAmount();

//        if (!userRepository.existsByNumber(request.getMobileNumber())) {
//            throw new UserNotFoundException();
//        }

        CashInResponse response = new CashInResponse(request.getMobileNumber(), request.getCashInAmount(), Balance);
        return response;
    }

    @PostMapping("cash-out")
    public CashOutResponse cashOut(@Valid @RequestBody CashOutRequest request) throws InsufficientBalanceException, UserNotFoundException{

//        User user = userRepository.findByNumber(request.getMobileNumber());
        CashOutResponse cashOutResponse = new CashOutResponse();
        Double Balance = 1000.0;
        Balance -= request.getCashOutAmount();

//        if (!userRepository.existsByNumber(request.getMobileNumber())) {
//            throw new UserNotFoundException();
//        }

        CashOutResponse response = new CashOutResponse(request.getMobileNumber(), request.getCashOutAmount(), Balance);
        return response;
    }
}
