package com.marqusm.demobackendrestjavaspringboot.repository;

import com.marqusm.demobackendrestjavaspringboot.model.database.AccountRecord;
import com.marqusm.demobackendrestjavaspringboot.repository.base.SqlRepository;
import org.springframework.stereotype.Repository;

/** @author : Marko Mišković */
@Repository
public class AccountRepository extends SqlRepository<AccountRecord> {}
