package grp4.gcash.mini.walletservice.repository;

import grp4.gcash.mini.walletservice.model.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, String> {
    boolean existsByUserId(String userId);
}
