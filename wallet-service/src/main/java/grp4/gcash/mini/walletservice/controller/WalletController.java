package grp4.gcash.mini.walletservice.controller;

import grp4.gcash.mini.walletservice.model.Wallet;
import grp4.gcash.mini.walletservice.repository.WalletRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("wallet")
public class WalletController {
    private final WalletRepository walletRepository;

    public WalletController(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @PostMapping
    public void createWallet (@Valid @RequestBody CreateWallet request) throws CreateWalletException{
        if(walletRepository.existsByUserId(request.getUserId())){
            throw new CreateWalletException("Wallet with id " + request.getUserId() + " already exist!");
        }
        Wallet wallet = new Wallet(request.getUserId());
        walletRepository.save(wallet);
    }

    @PutMapping("{id}")
    public void updateWallet(@PathVariable String id, @Valid @RequestBody UpdateWalletRequest request) throws WalletNotFoundException {
        Wallet wallet = getThisWallet(id);
        wallet.setBalance(request.getBalance());
        walletRepository.save(wallet);
    }

    @GetMapping("{id}")
    public GetWalletResponse getWallet(@PathVariable String id) throws WalletNotFoundException {
        Wallet wallet = getThisWallet(id);
        return new GetWalletResponse(wallet.getUserId(), wallet.getBalance());
    }

    /*
    @GetMapping
    public GetWalletResponse getWallet(@RequestBody GetWalletRequest request) throws WalletNotFoundException {
        Wallet wallet = getThisWallet(request.getUserId());
        return new GetWalletResponse(wallet.getUserId(), wallet.getBalance());
    }
     */

    public Wallet getThisWallet(String userId) throws WalletNotFoundException {
        Optional<Wallet> wallet = walletRepository.findById(userId);
        if(wallet.isEmpty()){
            throw new WalletNotFoundException("Wallet with id " + userId + " not found!");
        }
        return wallet.get();
    }

    public Double getBalance(String userId) throws WalletNotFoundException {
        return getThisWallet(userId).getBalance();
    }

    @ExceptionHandler(CreateWalletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleCreateWalletException(CreateWalletException e) {
        return Map.of("error", e.getMessage());
    }
    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleWalletNotFoundException(WalletNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

}
